package server;

import database.models.Author;
import database.models.Category;
import database.models.book.*;
import database.models.Library;
import database.models.User;
import database.query.PrepareQuery;
import database.query.Query;
import database.query.QueryResult;
import server.connection.SocketConnection;
import server.connection.response.*;
import server.router.CommandRegister;
import utils.DbUtil;
import utils.HashUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Server class handles incoming client connections and processes commands.
 * It uses a Router to route commands to their corresponding actions.
 */
public class Server implements AutoCloseable {
    private ServerSocket serverSocket;
    private final CommandRegister commandRegister;
    private volatile boolean running = true;

    public Server() {
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
     * The scripts are defined in a manifest file located in the resources directory.
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
            System.out.println("Executing SQL script: " + scriptFileName);
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
                    System.out.println("Table " + tableName + " already exists, skipping creation.");
                    result.close();
                    continue;
                }
                DbUtil.executeSqlFromStream(scriptStream);
                System.out.println("Executed SQL file: " + scriptFileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("All SQL files executed successfully.");
    }

    /**
     * Register the server commands
     */
    private void registerCommands() {
        commandRegister.register("REGISTER", (User user) -> {
            try {
                String unhashedPassword = user.getPassword();
                String hashedPassword = HashUtils.hash(unhashedPassword);

                user.setPassword(hashedPassword);
                user.create();
                return new SingleResponse("User created successfully");
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Error creating user: " + e.getMessage());
            }
        }, User.class);

        commandRegister.register("LOGIN", (User user) -> {
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

        commandRegister.register("CREATE_LIBRARY", (Library library) -> {
            try {
                library.create();
                return new SingleResponse("Library created successfully");
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Error creating user: " + e.getMessage());
            }
        }, Library.class);


        commandRegister.register("GET_LIBRARIES", (String userCf) -> {
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


        commandRegister.register("GET_LIBRARY_BOOKS", (Library library) -> {
            try {
                PrepareQuery pq = LibraryBook.selectBy("*")
                        .where("libraryId = ?")
                        .join(Book.class, "Book.id = LibraryBooks.bookId")
                        .prepare(library.getId());

                QueryResult result = pq.executeResult();
                return new MultiResponse(result);
            } catch (SQLException e) {
                return new ErrorResponse("Error listing books: " + e.getMessage());
            }
        }, Library.class);


        commandRegister.register("ADD_BOOK", (LibraryBook libraryBook) -> {
            try {
                libraryBook.create();
                return new SingleResponse("Book added successfully");
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Error creating book: " + e.getMessage());
            }
        }, LibraryBook.class);


        commandRegister.register("REMOVE_BOOK", (LibraryBook libraryBook) -> {
            try {
                libraryBook.delete();
                return new SingleResponse("Book removed successfully");
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Error removing book: " + e.getMessage());
            }
        }, LibraryBook.class);


        commandRegister.register("BOOK_INFO", (Book book) -> {
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

        // TODO add useful fields
        commandRegister.register("ADD_RATING", (BookRating bookRating) -> {
            try {
                bookRating.create();
                return new SingleResponse("Book rating added successfully");
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Error creating book rating: " + e.getMessage());
            }
        }, BookRating.class);


        commandRegister.register("ADD_SUGGESTION", (BookSuggestion bookSuggestion) -> {
            try {
                bookSuggestion.create();
                return new SingleResponse("Book suggestion added successfully");
            } catch (IllegalAccessException | SQLException e) {
                return new ErrorResponse("Error creating book suggestion: " + e.getMessage());
            }
        }, BookSuggestion.class);


        commandRegister.register("PING", () -> new SingleResponse("PONG"));

        commandRegister.register("TRY", SingleResponse::new);

        commandRegister.setFreeCommand("BOOK_INFO");
        commandRegister.setFreeCommand("REGISTER");
        commandRegister.setFreeCommand("LOGIN");
    }

    /**
     * Starts the server and listens for incoming connections.
     *
     * @param port The port number where the server is located
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (running) {
            try {
                Socket socket = serverSocket.accept();
                SocketConnection clientSocket = new SocketConnection(socket);
                System.out.println("New client connected: " + clientSocket.getInetAddress());

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
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from " + connection.getInetAddress() + ": " + inputLine);

                try {
                    // parts are command;?json;?userId
                    String[] parts = inputLine.split(";", 3);
                    if (parts.length < 1) {
                        // TODO specific method for sending error message
                        connection.getOut().println("Error: Invalid command format. You need to provide a command.");
                        continue;
                    }

                    String command = parts[0].toUpperCase();
                    // Check if it has a json argument
                    Optional<String> args = parts.length > 1 ? Optional.of(parts[1]) : Optional.empty();

                    String userId = parts.length > 2 ? parts[2] : null;

                    // Simple way to "authenticate" the user
                    if (!commandRegister.isFreeCommand(command) && userId == null) {
                        Sendable errorResponse = new ErrorResponse("You need to provide a userId for this command.");
                        connection.send(errorResponse);
                        continue;
                    }
                    System.out.println("Processing command: " + command + " with args: " + args.orElse("None"));

                    Sendable result = commandRegister.execute(command, args);
                    System.out.println("SENDING DATA");
                    connection.send(result);
                } catch (Exception e) {
                    System.err.println("Error processing command: " + e.getMessage());
                    Sendable errorResponse = new ErrorResponse("Error processing command: " + e.getMessage());
                    connection.send(errorResponse);
                }
                /*System.out.println("SENDING STOP MESSAGE");
                connection.sendStopMessage();*/
            }
        } catch (IOException e) {
            if (connection.getSocket().isClosed()) {
                System.out.println("Connection closed by client: " + connection.getInetAddress());
            } else {
                System.err.println("Client connection error (" + connection.getInetAddress() + "): " + e.getMessage());
            }
        } finally {
            try {
                connection.close();
                System.out.println("Client disconnected: " + connection.getInetAddress());
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    /**
     * Returns the CommandRegister instance.
     */
    public CommandRegister getRouter() {
        return commandRegister;
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
            System.out.println("Server stopped");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }
}
