package server.router;

import database.models.Book;
import database.models.User;
import database.query.QueryResult;
import server.router.connection.SocketConnection;
import server.router.connection.response.MultiResponse;
import server.router.connection.response.Sendable;
import server.router.connection.response.SingleResponse;
import utils.DbUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Optional;

/**
 * The Server class handles incoming client connections and processes commands.
 * It uses a Router to route commands to their corresponding actions.
 */
public class Server  implements AutoCloseable {
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
        commandRegister.register("CREATE_USER", (User user) -> {
            try {
                user.create();
                return new SingleResponse("User created successfully");
            } catch (IllegalAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }, User.class);

        commandRegister.register("CREATE_BOOK", (Book book) -> {
            try {
                book.create();
                return new SingleResponse("Book created successfully");
            } catch (IllegalAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }, Book.class);

        commandRegister.register("GET_USERS", () -> {
            try {
                QueryResult query = User.selectBy("*").build().execute();
                return new MultiResponse(query);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        commandRegister.register("PING", () -> new SingleResponse("PONG"));

        commandRegister.register("TRY", SingleResponse::new);
    }

    private void createInitialEntities() {
        // todo
    }

    /**
     * Initializes the database connection and sets up the necessary tables.
     */
    private void setupDb() {
        try {
            DbUtil.init(User.class);
            DbUtil.init(Book.class);
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Starts the server and listens for incoming connections.
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
                    String[] parts = inputLine.split(";", 2);
                    if (parts.length < 1) {
                        connection.getOut().println("Error: Invalid command format. You need to provide a command.");
                        continue;
                    }

                    String command = parts[0].toUpperCase();
                    // Check if it has a json argument
                    Optional<String> args = parts.length > 1 ? Optional.of(parts[1]) : Optional.empty();

                    Sendable result = commandRegister.execute(command, args);
                    System.out.println("SENDING DATA");
                    connection.send(result);
                } catch (Exception e) {
                    System.err.println("Error processing command: " + e.getMessage());
                    connection.getOut().println("Error: " + e.getMessage());
                    e.printStackTrace();
                }
                System.out.println("SENDING STOP MESSAGE");
                connection.sendStopMessage();
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
     *
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
