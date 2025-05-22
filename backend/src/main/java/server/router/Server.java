package server.router;

import database.models.Book;
import database.models.User;
import database.query.QueryResult;
import server.router.connection.SocketConnection;
import server.router.connection.response.MultiResponse;
import server.router.connection.response.Sendable;
import server.router.connection.response.SingleResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class Server  implements AutoCloseable {
    private ServerSocket serverSocket;
    private final Router router;
    private volatile boolean running = true;

    public Server() {
        router = new Router();
    }

    /**
     * Registers the commands and their corresponding actions.
     */
    public void setup() {
        router.register("CREATE_USER", (User user) -> {
            try {
                user.create();
                return new SingleResponse("User created successfully");
            } catch (IllegalAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }, User.class);

        router.register("CREATE_BOOK", (Book book) -> {
            try {
                book.create();
                return new SingleResponse("Book created successfully");
            } catch (IllegalAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }, Book.class);

        router.register("GET_USERS", () -> {
            try {
                QueryResult query = User.selectBy("*").build().execute();
                return new MultiResponse(query.stream());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        router.register("PING", () -> new SingleResponse("PONG"));

        router.register("TRY", SingleResponse::new);


    }

    /**
     * Starts the server and listens for incoming connections.
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (running) {
            try {
                SocketConnection clientSocket = new SocketConnection(serverSocket.accept());
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
                        connection.getOut().println("Error: Invalid command format.");
                        continue;
                    }

                    String command = parts[0].toUpperCase();
                    Optional<String> args = parts.length > 1 ? Optional.of(parts[1]) : Optional.empty();

                    // Execute the command
                    Sendable result = router.execute(command, args);
                    System.out.println("SENDING DATA");
                    connection.send(result);
                } catch (Exception e) {
                    System.err.println("Error processing command: " + e.getMessage());
                    connection.getOut().println("Error: " + e.getMessage());
                    // TODO SEND STOP MESSAGE?
                }
                System.out.println("SENDING STOP MESSAGE");
                connection.send(new SingleResponse("STOP"));
            }
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        } finally {
            try {
                connection.close();
                System.out.println("Client disconnected: " + connection.getInetAddress());
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public Router getRouter() {
        return router;
    }

    /**
     * Stops the server and closes the server socket.
     */
    @Override
    public void close() throws Exception {
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

    private void trySendStream(Socket socket) {
        ArrayList<String> a = new ArrayList<>();
        a.add("Hello");
        a.add("World");
        Stream<String> targetStream = a.stream();

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            targetStream.forEach(out::println);
        } catch (IOException e) {
            System.err.println("Error sending stream: " + e.getMessage());
        }
    }

}
