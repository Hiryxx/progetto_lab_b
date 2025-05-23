import classes.MainFrame;
import connection.SocketConnection;

import javax.swing.SwingUtilities;
import java.net.Socket;

public class Main {
    static int PORT = 9000;
    public static void main(String[] args) {
        Socket clientSocket = new Socket();
        try {
            clientSocket.connect(new java.net.InetSocketAddress("localhost", PORT));
        } catch (Exception e) {
            System.out.println("Error connecting to server: " + e.getMessage());
            return;
        }
        SocketConnection socket = new SocketConnection(clientSocket);
        try {
            socket.send("PING");
            //String response = socket.receive();
            socket.receiveUntilStop(); // todo fix this

            String userJson = "{\"cf\":\"12345678901234567890123456789013\",\"name\":\"John Doe\",\"email\":\"franco.raossi@gmail.com\",\"password\":\"password\"}";
            socket.send("CREATE_USER;" + userJson);
            socket.receiveUntilStop();
            System.out.println("User created successfully");

            socket.send("GET_USERS");
            socket.receiveUntilStop();
            System.out.println("Users retrieved successfully");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }


        SwingUtilities.invokeLater(() -> {
            MainFrame app = new MainFrame(socket);
            app.setLocationRelativeTo(null); // Center the window
            app.setVisible(true);

        });
    }
}