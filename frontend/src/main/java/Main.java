import classes.MainFrame;
import connection.SocketConnection;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.io.*;
import java.net.Socket;
import java.net.URL;

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

        URL icon = Main.class.getResource("/icon.png");

        try {
            assert icon != null;
            MainFrame.mainFrame.setIconImage(ImageIO.read(icon));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            try {
                System.out.println("Setting dock icon for macOS");
                if (icon != null && java.awt.Taskbar.isTaskbarSupported()) {
                    java.awt.Taskbar taskbar = java.awt.Taskbar.getTaskbar();
                    if (taskbar.isSupported(java.awt.Taskbar.Feature.ICON_IMAGE)) {
                        taskbar.setIconImage(ImageIO.read(icon));
                    }
                }
            } catch (Exception e) {
                System.err.println("Could not set dock icon: " + e.getMessage());
            }
        } else if (os.contains("win")) {
            System.setProperty("java.awt.Window.locationByPlatform", "true");
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame.init(socket);
            MainFrame.mainFrame.setLocationRelativeTo(null);

            MainFrame.mainFrame.setVisible(true);

        });
    }
}