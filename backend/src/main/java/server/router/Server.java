package server.router;

import database.models.Book;
import database.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
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
            } catch (IllegalAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }, User.class);

        router.register("CREATE_BOOK", (Book book) -> {
            try {
                book.create();
            } catch (IllegalAccessException | SQLException e) {
                throw new RuntimeException(e);
            }
        }, Book.class);

        router.register("PING", () -> {
            System.out.println("Ping received");
        });


    }

    /**
     * Starts the server and listens for incoming connections.
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(9000);
        System.out.println("Server started on port 9000");

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
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
     * @param clientSocket The socket for the client connection.
     */
    private void handleClient(Socket clientSocket) {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from " + clientSocket.getInetAddress() + ": " + inputLine);

                try {
                    // Parses command and args
                    String[] parts = inputLine.split(";", 2);
                    if (parts.length < 2) {
                        out.println("Error: Invalid command format. Use 'command;jsonData'");
                        continue;
                    }

                    // Execute the command
                    router.execute(parts[0], parts[1].describeConstable());
                    // todo send result back to client
                    out.println("Success: Command executed");
                } catch (Exception e) {
                    System.err.println("Error processing command: " + e.getMessage());
                    out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
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
