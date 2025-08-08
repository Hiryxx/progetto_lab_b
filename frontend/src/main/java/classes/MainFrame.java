package classes;

import connection.SocketConnection;
import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private SocketConnection socketConnection;

    public MainFrame() {
        this.socketConnection = null;
        // Set up the frame
        setTitle("Book Recommender");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main container with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Create and add pages
        HomePage homePage = new HomePage(this);
        LoginPage loginPage = new LoginPage(this);
        RegisterPage registerPage = new RegisterPage(this);
        contentPanel.add(homePage, "home");
        contentPanel.add(loginPage, "login");
        contentPanel.add(registerPage,"register");
        // Show the first page
        cardLayout.show(contentPanel, "home");

        // Add content panel to frame
        add(contentPanel);
    }

    public void addPage(String name, JPanel page) {
        contentPanel.add(page, name);
    }

    public void showPage(String name) {
        cardLayout.show(contentPanel, name);
    }
}