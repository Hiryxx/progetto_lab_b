package server;

import data.BookFilterRequest;
import database.models.Author;
import database.models.Category;
import database.models.book.*;
import database.models.Library;
import database.models.User;
import database.query.PrepareQuery;
import database.query.Query;
import database.query.QueryResult;
import database.query.SelectBuilder;
import server.connection.SocketConnection;
import server.connection.request.EntityRequest;
import server.connection.request.Request;
import server.connection.request.StringRequest;
import server.connection.response.*;
import server.router.CommandRegister;
import utils.DbUtil;
import utils.HashUtils;
import utils.JSONUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

/**
 * The Server class handles incoming client connections and processes commands.
 * It uses a Router to route commands to their corresponding actions.
 */
public class Server implements AutoCloseable {
    private ServerSocket serverSocket;
    private final CommandRegister commandRegister;
    private volatile boolean running = true;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public Server() {
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.INFO);

        ConsoleHandler console = new ConsoleHandler();
        console.setLevel(Level.INFO);
        console.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(console);
        commandRegister = new CommandRegister();
    }

    /**
     * Registers the commands and their corresponding actions.
     */
    public void setup() {
        setupDb();
        createInitialEntities();
        registerCommands();
    }


    /**
     * Initializes the database connection and sets up the necessary tables.
     */
    private void setupDb() {
        try {
            DbUtil.init(User.class);
            DbUtil.init(Book.class);
            DbUtil.init(Library.class);
            DbUtil.init(LibraryBook.class);
            DbUtil.init(BookRating.class);
            DbUtil.init(BookSuggestion.class);

            DbUtil.init(Author.class);
            DbUtil.init(BookAuthor.class);

            DbUtil.init(Category.class);
            DbUtil.init(BookCategory.class);

        } catch (SQLException | IllegalAccessException e) {
            // this has to block the server execution
            throw new RuntimeException("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Creates initial entities in the database by executing SQL scripts.
     * The scripts are defined in a manifest file located in the resources' directory.
     * If any of the scripts fail to execute, an exception is thrown and the server is stopped.
     */
    private void createInitialEntities() {
        List<String> scriptFiles = new ArrayList<>();

        try (InputStream manifestStream = Server.class.getResourceAsStream("/manifest.txt")) {
            assert manifestStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(manifestStream))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        scriptFiles.add(line.trim());
                    }
                }
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Could not read SQL manifest file.", e);
        }

        for (String scriptFileName : scriptFiles) {
            LOGGER.log(Level.INFO,"Executing SQL script: " + scriptFileName);
            String resourcePath = "/sql_inserts/" + scriptFileName;
            try (InputStream scriptStream = Server.class.getResourceAsStream(resourcePath)) {
                if (scriptStream == null) {
                    throw new RuntimeException("Could not find script file in resources: " + resourcePath);
                }
                String tableName = scriptFileName.replace(".sql", "");
                String checkQuery = "SELECT 1 FROM " + tableName + " LIMIT 1;";

                PrepareQuery prepareCheck = new PrepareQuery(new Query(checkQuery));
                QueryResult result = prepareCheck.executeResult();

                if (result.iterator().hasNext()) {
                    LOGGER.log(Level.INFO,"Table " + tableName + " already exists, skipping creation.");
                    result.close();
                    continue;
                }
                DbUtil.executeSqlFromStream(scriptStream);
                LOGGER.log(Level.INFO,"Executed SQL file: " + scriptFileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        LOGGER.log(Level.INFO,"All SQL files executed successfully.");
    }

    /**
     * Register the server commands
     */
    private void registerCommands() {
        commandRegister.register("REGISTER", (EntityRequest<User> request) -> {
            User user = request.getEntity();
            try {
                String unhashedPassword = user.getPassword();
                String hashedPassword = HashUtils.hash(unhashedPassword);

                user.setPassword(hashedPassword);
                user.create();
                return new SingleResponse("User created successfully");
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Utente già esistente o errore nella creazione");
            }
        }, User.class);

        commandRegister.register("LOGIN", (EntityRequest<User> request) -> {

            User user = request.getEntity();

            String hashedPassword = HashUtils.hash(user.getPassword());
            user.setPassword(hashedPassword);

            PrepareQuery pq = User.selectBy("*")
                    .where("email = ? AND password = ?")
                    .prepare(user.getEmail(), user.getPassword());

            try (QueryResult result = pq.executeResult()) {
                var iterator = result.iterator();
                if (!iterator.hasNext()) {
                    return new ErrorResponse("Credenziali non valide");
                }
                ResultSet rs = iterator.next();

                return new JsonResponse(rs);
            } catch (Exception e) {
                return new ErrorResponse("Error executing login query: " + e.getMessage());
            }


        }, User.class);

        commandRegister.register("CREATE_LIBRARY", (EntityRequest<Library> request) -> {
            Library library = request.getEntity();
            try {
                library.create();

                try (QueryResult queryResult = Library.selectBy("*")
                        .where("name = ? AND userCf = ?")
                        .prepare(library.getName(), library.getUserCf()).executeResult()) {

                    var iterator = queryResult.iterator();

                    iterator.hasNext();

                    ResultSet rs = iterator.next();
                    return new JsonResponse(rs);
                }
            } catch (Exception e) {
                return new ErrorResponse("Error creating user: " + e.getMessage());
            }
        }, Library.class);

        commandRegister.register("GET_LIBRARIES", (StringRequest request) -> {
            String userCf = request.getUserCf();
            try {
                PrepareQuery pq = Library.selectBy("*")
                        .where("userCf = ?")
                        .prepare(userCf);

                QueryResult result = pq.executeResult();
                return new MultiResponse(result);
            } catch (SQLException e) {
                return new ErrorResponse("Error creating user: " + e.getMessage());
            }
        });


        commandRegister.register("GET_LIBRARY_BOOKS", (EntityRequest<Library> request) -> {
            Library library = request.getEntity();
            try {
                PrepareQuery pq = LibraryBook.selectBy("books.*, " +
                                "STRING_AGG(DISTINCT authors.name, ', ') as authors, " +
                                "STRING_AGG(DISTINCT categories.name, ', ') as categories")
                        .join(Book.class, "books.id = librarybooks.bookId")
                        .join(BookAuthor.class, "bookauthors.bookId = books.id")
                        .join(Author.class, "authors.id = bookauthors.authorId")
                        .join(BookCategory.class, "bookcategories.bookId = books.id")
                        .join(Category.class, "categories.id = bookcategories.categoryId")
                        .where("librarybooks.libraryId = ?")
                        .groupBy("books.id")
                        .limit(10)
                        .prepare(library.getId());

                QueryResult result = pq.executeResult();
                return new MultiResponse(result);
            } catch (SQLException e) {
                return new ErrorResponse("Error listing books: " + e.getMessage());
            }
        }, Library.class);

        commandRegister.register("GET_BOOKS", (StringRequest request) -> {
            String limitInput = request.getArgument();
            try {
                int limit = limitInput != null ? Integer.parseInt(limitInput) : 20;
                PrepareQuery pq = Book.selectBy("books.*, " +
                                "STRING_AGG(DISTINCT authors.name, ', ') as authors, " +
                                "STRING_AGG(DISTINCT categories.name, ', ') as categories")
                        .join(BookCategory.class, "bookcategories.bookid = books.id")
                        .join(BookAuthor.class, "bookauthors.bookid = books.id")
                        .join(Author.class, "authors.id = bookauthors.authorid")
                        .join(Category.class, "categories.id = bookcategories.categoryid")
                        .groupBy("books.id")
                        .limit(limit)
                        .prepare();

                QueryResult result = pq.executeResult();
                return new MultiResponse(result);
            } catch (SQLException e) {
                return new ErrorResponse("Error getting books: " + e.getMessage());
            }
        });

        commandRegister.register("GET_FILTERED_BOOKS", (StringRequest request) -> {
            String filterJson = request.getArgument();
            try {
                BookFilterRequest filters = JSONUtil.getMAPPER().readValue(filterJson, BookFilterRequest.class);

                SelectBuilder queryBuilder = Book.selectBy("books.*, " +
                                "STRING_AGG(DISTINCT authors.name, ', ') as authors, " +
                                "STRING_AGG(DISTINCT categories.name, ', ') as categories")
                        .join(BookCategory.class, "bookcategories.bookid = books.id")
                        .join(BookAuthor.class, "bookauthors.bookid = books.id")
                        .join(Author.class, "authors.id = bookauthors.authorid")
                        .join(Category.class, "categories.id = bookcategories.categoryid");

                List<Object> parameters = new ArrayList<>();
                List<String> conditions = new ArrayList<>();

                if (filters.getTitle() != null && !filters.getTitle().isEmpty()) {
                    conditions.add("LOWER(books.title) LIKE LOWER(?)");
                    parameters.add("%" + filters.getTitle() + "%");
                }

                if (filters.getYear() != null && filters.getYear() > 0) {
                    conditions.add("books.year = ?");
                    parameters.add(filters.getYear());
                }

                if (filters.getAuthorId() >= 0) {
                    conditions.add("bookauthors.authorid = ?");
                    parameters.add(filters.getAuthorId());
                }

                if (filters.getCategoryId() >= 0) {
                    conditions.add("bookcategories.categoryid = ?");
                    parameters.add(filters.getCategoryId());
                }

                if (!conditions.isEmpty()) {
                    String whereClause = String.join(" AND ", conditions);
                    queryBuilder.where(whereClause);
                }

                queryBuilder.groupBy("books.id");

                int limit = filters.getLimit() != null && filters.getLimit() > 0 ? filters.getLimit() : 20;
                queryBuilder.limit(limit);

                PrepareQuery pq;
                if (!parameters.isEmpty()) {
                    pq = queryBuilder.prepare(parameters.toArray());
                } else {
                    pq = queryBuilder.prepare();
                }

                QueryResult result = pq.executeResult();
                return new MultiResponse(result);

            } catch (SQLException e) {
                return new ErrorResponse("Error getting filtered books: " + e.getMessage());
            } catch (Exception e) {
                return new ErrorResponse("Error parsing filter request: " + e.getMessage());
            }
        });


        commandRegister.register("IS_BOOK_IN_LIBRARY", (StringRequest request) -> {
            try {
                int bookId = Integer.parseInt(request.getArgument());
                String userCf = request.getUserCf();

                PrepareQuery pq = LibraryBook.selectBy("1")
                        .join(Library.class, "libraries.id = librarybooks.libraryId")
                        .where("librarybooks.bookId = ? AND libraries.usercf = ?")
                        .prepare(bookId, userCf);

                try (QueryResult result = pq.executeResult()) {
                    if (result.iterator().hasNext()) {
                        return new SingleResponse("true");
                    } else {
                        return new SingleResponse("false");
                    }
                }
            } catch (NumberFormatException e) {
                return new ErrorResponse("Invalid bookId format. Must be a number.");
            } catch (Exception e) {
                return new ErrorResponse("Error checking book in library: " + e.getMessage());
            }
        });

        commandRegister.register("ADD_BOOK", (EntityRequest<LibraryBook> request) -> {
            LibraryBook libraryBook = request.getEntity();
            try {
                libraryBook.create();
                return new SingleResponse("Book added successfully");
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Error creating book: " + e.getMessage());
            }
        }, LibraryBook.class);

        commandRegister.register("BOOK_INFO", (EntityRequest<Book> request) -> {
            Book book = request.getEntity();
            try {
                PrepareQuery pq = Book.selectBy("*")
                        .where("id = ?")
                        .prepare(book.getId());

                QueryResult result = pq.executeResult();

                return new MultiResponse(result);
            } catch (SQLException e) {
                return new ErrorResponse("Error checking book info: " + e.getMessage());
            }
        }, Book.class);

        commandRegister.register("GET_CATEGORIES", () -> {
            try {
                PrepareQuery pq = Category.selectBy("*")
                        .orderBy("name").prepare();

                QueryResult result = pq.executeResult();
                return new MultiResponse(result);
            } catch (SQLException e) {
                return new ErrorResponse("Error creating book suggestion: " + e.getMessage());
            }
        });

        commandRegister.register("GET_AUTHORS", () -> {
            try {
                PrepareQuery pq = Author.selectBy("*")
                        .orderBy("name").prepare();

                QueryResult result = pq.executeResult();
                return new MultiResponse(result);
            } catch (SQLException e) {
                return new ErrorResponse("Error creating book suggestion: " + e.getMessage());
            }
        });

        commandRegister.register("GET_BOOK_SUGGESTIONS", (EntityRequest<Book> request) -> {
            Book book = request.getEntity();
            try {
                PrepareQuery pq = Book.selectBy("books.*, " +
                                "STRING_AGG(DISTINCT authors.name, ', ') as authors, " +
                                "STRING_AGG(DISTINCT categories.name, ', ') as categories, " +
                                "COUNT(DISTINCT booksuggestions.usercf) as suggestioncount")
                        .join(BookSuggestion.class, "booksuggestions.targetbookid = books.id")
                        .join(BookAuthor.class, "bookauthors.bookid = books.id")
                        .join(Author.class, "authors.id = bookauthors.authorid")
                        .join(BookCategory.class, "bookcategories.bookid = books.id")
                        .join(Category.class, "categories.id = bookcategories.categoryid")
                        .where("booksuggestions.sourcebookid = ?")
                        .groupBy("books.id")
                        .orderBy("suggestioncount DESC")
                        .limit(5)
                        .prepare(book.getId());

                QueryResult result = pq.executeResult();
                return new MultiResponse(result);
            } catch (SQLException e) {
                return new ErrorResponse("Error getting book suggestions: " + e.getMessage());
            }
        }, Book.class);

        commandRegister.register("GET_BOOK_RATINGS", (EntityRequest<BookRating> request) -> {
            BookRating book = request.getEntity();
            try {
                PrepareQuery pq = BookRating.selectBy("AVG(bookratings.stile) as stile, " +
                                "AVG(bookratings.contenuto) as contenuto, " +
                                "AVG(bookratings.gradevolezza) as gradevolezza, " +
                                "AVG(bookratings.originalita) as originalita, " +
                                "AVG(bookratings.edizione) as edizione, " +
                                "AVG(bookratings.votofinale) as votofinale, " +
                                "COUNT(*) as rating_count")
                        .where("bookratings.bookid = ?")
                        .prepare(book.getBookid());

                QueryResult result = pq.executeResult();
                var iterator = result.iterator();

                if (iterator.hasNext()) {
                    ResultSet rs = iterator.next();
                    return new JsonResponse(rs);
                } else {
                    return new ErrorResponse("No ratings found for this book");
                }
            } catch (SQLException e) {
                return new ErrorResponse("Error getting book ratings: " + e.getMessage());
            }
        }, BookRating.class);

        commandRegister.register("SAVE_BOOK_RATING", (EntityRequest<BookRating> request) -> {
            BookRating bookRating = request.getEntity();
            try {
                int stile = bookRating.getStile();
                int contenuto = bookRating.getContenuto();
                int gradevolezza = bookRating.getGradevolezza();
                int originalita = bookRating.getOriginalita();
                int edizione = bookRating.getEdizione();

                double finalScore = (stile + contenuto + gradevolezza + originalita + edizione) / 5.0;
                bookRating.setVotofinale((int) Math.round(finalScore));

                bookRating.create();
                return new SingleResponse("Book rating saved successfully with final score: " + bookRating.getVotofinale());
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Error saving book rating: " + e.getMessage());
            }
        }, BookRating.class);

        commandRegister.register("IS_BOOK_RATED", (StringRequest request) -> {
            try {
                int bookId = Integer.parseInt(request.getArgument());
                String userCf = request.getUserCf();

                PrepareQuery pq = BookRating.selectBy("1")
                        .where("bookid = ? AND usercf = ?")
                        .prepare(bookId, userCf);

                try (QueryResult result = pq.executeResult()) {
                    if (result.iterator().hasNext()) {
                        return new SingleResponse("true");
                    } else {
                        return new SingleResponse("false");
                    }
                }
            } catch (NumberFormatException e) {
                return new ErrorResponse("Invalid bookId format. Must be a number.");
            } catch (Exception e) {
                return new ErrorResponse("Error checking book rating: " + e.getMessage());
            }
        });

        commandRegister.register("IS_BOOK_SUGGESTED", (StringRequest request) -> {
            try {
                int bookId = Integer.parseInt(request.getArgument());
                String userCf = request.getUserCf();

                PrepareQuery pq = BookSuggestion.selectBy("1")
                        .where("sourcebookid = ? AND usercf = ?")
                        .prepare(bookId, userCf);

                try (QueryResult result = pq.executeResult()) {
                    if (result.iterator().hasNext()) {
                        return new SingleResponse("true");
                    } else {
                        return new SingleResponse("false");
                    }
                }
            } catch (NumberFormatException e) {
                return new ErrorResponse("Invalid bookId format. Must be a number.");
            } catch (Exception e) {
                return new ErrorResponse("Error checking book suggestion: " + e.getMessage());
            }
        });

        commandRegister.register("GET_RECOMMENDABLE_BOOKS", (EntityRequest<Book> request) -> {
            Book book = request.getEntity();
            String userCf = request.getUserCf();

            try {
                PrepareQuery pq = Book.selectBy("DISTINCT books.*, " +
                                "STRING_AGG(DISTINCT authors.name, ', ') as authors, " +
                                "STRING_AGG(DISTINCT categories.name, ', ') as categories")
                        .join(LibraryBook.class, "librarybooks.bookid = books.id")
                        .join(Library.class, "libraries.id = librarybooks.libraryid")
                        .join(BookAuthor.class, "bookauthors.bookid = books.id")
                        .join(Author.class, "authors.id = bookauthors.authorid")
                        .join(BookCategory.class, "bookcategories.bookid = books.id")
                        .join(Category.class, "categories.id = bookcategories.categoryid")
                        .where("LOWER(libraries.usercf) = LOWER(?) AND books.id != ?")
                        .groupBy("books.id")
                        .orderBy("books.title")
                        .prepare(userCf, book.getId());

                QueryResult result = pq.executeResult();
                return new MultiResponse(result);
            } catch (SQLException e) {
                return new ErrorResponse("Error getting recommendable books: " + e.getMessage());
            }
        }, Book.class);


        commandRegister.register("SUGGEST_BOOK", (StringRequest request) -> {
            try {
                String argument = request.getArgument();
                String userCf = request.getUserCf();

                // "targetBookId,sourceBookId1,sourceBookId2,..."
                String[] parts = argument.split(",");
                if (parts.length < 2) {
                    return new ErrorResponse("Invalid input format. Expected: targetBookId,sourceBookId1,sourceBookId2,...");
                }

                int targetBookId = Integer.parseInt(parts[0].trim());

                for (int i = 1; i < parts.length; i++) {
                    int sourceBookId = Integer.parseInt(parts[i].trim());
                    BookSuggestion suggestion = new BookSuggestion(targetBookId, sourceBookId, userCf);

                    suggestion.create();
                }

                int suggestionCount = parts.length - 1;
                return new SingleResponse("Successfully created " + suggestionCount + " book suggestions");

            } catch (NumberFormatException e) {
                return new ErrorResponse("Invalid book ID format. All IDs must be numbers.");
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Error creating book suggestions: " + e.getMessage());
            }
        });

        commandRegister.register("GET_PROFILE_BOOKS", (StringRequest request) -> {
            String userCf = request.getUserCf();
            try {
                // find the most popular category across all user's libraries
                PrepareQuery categoryQuery = Category.selectBy("categories.*, COUNT(*) as book_count")
                        .join(BookCategory.class, "bookcategories.categoryid = categories.id")
                        .join(Book.class, "books.id = bookcategories.bookid")
                        .join(LibraryBook.class, "librarybooks.bookid = books.id")
                        .join(Library.class, "libraries.id = librarybooks.libraryid")
                        .where("libraries.usercf = ?")
                        .groupBy("categories.id, categories.name")
                        .orderBy("book_count DESC")
                        .limit(1)
                        .prepare(userCf);

                QueryResult categoryResult = categoryQuery.executeResult();
                var categoryIterator = categoryResult.iterator();

                if (!categoryIterator.hasNext()) {
                    return new ErrorResponse("No books found in user's libraries");
                }

                ResultSet categoryRs = categoryIterator.next();
                int favoriteCategoryId = categoryRs.getInt("id");
                String favoriteCategoryName = categoryRs.getString("name");
                int bookCount = categoryRs.getInt("book_count");

                categoryResult.close();

                PrepareQuery booksQuery = Book.selectBy("DISTINCT books.*, " +
                                "STRING_AGG(DISTINCT authors.name, ', ') as authors, " +
                                "'" + favoriteCategoryName + "' as favorite_category, " +
                                favoriteCategoryId + " as favorite_category_id, " +
                                bookCount + " as category_book_count, " +
                                "STRING_AGG(DISTINCT categories.name, ', ') as categories")
                        .join(BookCategory.class, "bookcategories.bookid = books.id")
                        .join(Category.class, "categories.id = bookcategories.categoryid")
                        .join(BookAuthor.class, "bookauthors.bookid = books.id")
                        .join(Author.class, "authors.id = bookauthors.authorid")
                        .where("bookcategories.categoryid = ? AND books.id NOT IN (" +
                                "SELECT DISTINCT librarybooks.bookid " +
                                "FROM librarybooks " +
                                "JOIN libraries ON libraries.id = librarybooks.libraryid " +
                                "WHERE libraries.usercf = ?)")
                        .groupBy("books.id")
                        .orderBy("books.title")
                        .limit(16)
                        .prepare(favoriteCategoryId, userCf);

                QueryResult booksResult = booksQuery.executeResult();
                return new MultiResponse(booksResult);

            } catch (Exception e) {
                return new ErrorResponse("Error getting profile books: " + e.getMessage());
            }
        });


        commandRegister.setFreeCommand("GET_BOOKS");
        commandRegister.setFreeCommand("GET_FILTERED_BOOKS");
        commandRegister.setFreeCommand("BOOK_INFO");
        commandRegister.setFreeCommand("REGISTER");
        commandRegister.setFreeCommand("LOGIN");
        commandRegister.setFreeCommand("GET_CATEGORIES");
        commandRegister.setFreeCommand("GET_AUTHORS");

        commandRegister.setFreeCommand("GET_BOOK_SUGGESTIONS");
        commandRegister.setFreeCommand("GET_BOOK_RATINGS");
    }

    /**
     * Starts the server and listens for incoming connections.
     *
     * @param port The port number where the server is located
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        LOGGER.log(Level.INFO,"Server started on port " + port);

        while (running) {
            try {
                Socket socket = serverSocket.accept();
                SocketConnection clientSocket = new SocketConnection(socket);
                LOGGER.log(Level.INFO,"New client connected: " + clientSocket.getInetAddress());

                Thread.startVirtualThread(() -> handleClient(clientSocket));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Handles the client connection.
     *
     * @param connection The socket connection for a single client.
     */
    private void handleClient(SocketConnection connection) {
        try {
            String inputLine;
            BufferedReader in = connection.getIn();
            while ((inputLine = readLineSafe(in)) != null) {
                if (inputLine.isEmpty()) {
                    break; // Client disconnected
                }
                LOGGER.log(Level.INFO, "Received from " + connection.getInetAddress() + ": " + inputLine);

                try {
                    // parts are command;json;userId
                    String[] parts = inputLine.split(";", 3);
                    Request request;
                    try {
                        request = commandRegister.parseRequest(parts);
                    } catch (Exception e) {
                        LOGGER.log(Level.INFO, "Error parsing request: " + e.getMessage());
                        Sendable errorResponse = new ErrorResponse("Error parsing request: " + e.getMessage());
                        connection.send(errorResponse);
                        continue;
                    }
                    Sendable result = commandRegister.execute(request);
                    connection.send(result);
                } catch (Exception e) {
                    System.err.println("Error processing command: " + e.getMessage());
                    Sendable errorResponse = new ErrorResponse("Error processing command: " + e.getMessage());
                    connection.send(errorResponse);
                }
            }
        } catch (IOException e) {
            if (connection.getSocket().isClosed()) {
                LOGGER.log(Level.INFO,"Connection closed by client: " + connection.getInetAddress());
            } else {
                LOGGER.log(Level.INFO,"Client connection error (" + connection.getInetAddress() + "): " + e.getMessage());
            }
        } finally {
            try {
                connection.close();
                LOGGER.log(Level.INFO,"Client disconnected: " + connection.getInetAddress());
            } catch (IOException e) {
                LOGGER.log(Level.INFO,"Error closing client socket: " + e.getMessage());
            }
        }
    }

    /**
     * Stops the server and closes the server socket.
     */
    @Override
    public void close() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            LOGGER.log(Level.INFO,"Server stopped");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    private static String readLineSafe(BufferedReader in) {
        try {
            StringBuilder line = new StringBuilder();
            int ch;

            while ((ch = in.read()) != -1) {
                char c = (char) ch;

                if (c == '\\') {
                    int next = in.read();
                    if (next == 'n') {
                        break;
                    } else {
                        line.append(c);
                        if (next != -1) {
                            line.append((char) next);
                        }
                    }
                } else {
                    line.append(c);
                }
            }

            return line.toString();
        } catch (IOException e) {
            System.err.println("Error reading from socket: " + e.getMessage());
            return null;
        }
    }
}
