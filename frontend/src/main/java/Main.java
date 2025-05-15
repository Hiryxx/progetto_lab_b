import classes.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame app = new MainFrame();
            app.setLocationRelativeTo(null); // Center the window
            app.setVisible(true);
        });
    }
}