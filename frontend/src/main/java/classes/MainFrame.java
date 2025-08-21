package classes;

import components.thiss.NavButton;
import connection.SocketConnection;
import pages.*;
import state.UserState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import static classes.styles.Colors.*;

public class MainFrame extends JFrame {
    public static JFrame mainFrame = new JFrame();
    private static JPanel contentPanel;
    private static CardLayout cardLayout;
    private static SocketConnection socketConnection;
    private static String currentPage;
    private static Map<String, NavButton> navButtons = new HashMap<>();
    private static Map<String, Page> pages = new HashMap<>();

    private static JPanel bottomNavigationPanel;


    public static void init(SocketConnection socketConnection) {
        MainFrame.socketConnection = socketConnection;

        mainFrame.setTitle("Book Recommender");
        mainFrame.setSize(1300, 900);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        HomePage homePage = new HomePage();
        LoginPage loginPage = new LoginPage();
        RegisterPage registerPage = new RegisterPage();
        ProfilePage profilePage = new ProfilePage();
        BookDetailsPage bookDetailsPage = new BookDetailsPage();
        LibraryPage libraryPage = new LibraryPage();
        LibraryDetailPage libraryDetailPage = new LibraryDetailPage();

        addPage(homePage, "home");
        addPage(loginPage, "login");
        addPage(registerPage, "register");
        addPage(profilePage, "profile");
        addPage(bookDetailsPage, "bookDetails");
        addPage(libraryPage, "library");
        addPage(libraryDetailPage, "libraryDetail");

        showPage("home");

        mainFrame.add(contentPanel);

        render();
    }

    public static void render() {
        createBottomNavigationPanel();
        mainFrame.add(bottomNavigationPanel, BorderLayout.SOUTH);
    }


    public static void addPage(Page page, String name) {
        contentPanel.add(page, name);
        pages.put(name, page);
    }

    public static void showPage(String name) {
        pages.get(name).refresh();
        //todo maybe found a better way
        if (name.equals("profile") || name.equals("login")){
            currentPage = "auth";
        } else
            currentPage = name;

        cardLayout.show(contentPanel, name);
        // todo add better repaint handling with revalidate
        contentPanel.repaint();
        // System.out.println("Showing page: " + name);
        System.out.println("TRYING TO SHOW PAGE: " + name);
        updateNavButtonStates();
    }

    private static void updateNavButtonStates() {
        NavButton authButton = navButtons.get("auth");
        if (authButton != null) {
            if (UserState.isLoggedIn) {
                System.out.println("User is logged in, updating auth button to profile");
                authButton.setActionListener(e -> showPage("profile"));
                authButton.setTextLabel("Profilo");
            } else if (authButton.getText().equals("Profilo")) {
                authButton.setActionListener(e -> showPage("login"));
                authButton.setTextLabel("Login");
            }
            authButton.repaint();
        }
        navButtons.forEach((pageName, button) -> {
            boolean isActive = currentPage.equals(pageName);
            JLabel textLabel = button.getTextLabel();
            if (isActive) {
                textLabel.setForeground(primaryColor);
                button.setSelected(true);
            } else {
                textLabel.setForeground(textSecondary);
                button.setSelected(false);
            }

            if (UserState.isLoggedIn && pageName.equals("library")){
                bottomNavigationPanel.add(button);
            } else if (!UserState.isLoggedIn && pageName.equals("library")){
                bottomNavigationPanel.remove(button);
            }

            button.repaint();
        });
    }

    private static void createBottomNavigationPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(cardColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(borderColor);
                g2d.fillRect(0, 0, getWidth(), 1);

                GradientPaint shadow = new GradientPaint(0, 1, new Color(0, 0, 0, 8), 0, 10, new Color(0, 0, 0, 0));
                g2d.setPaint(shadow);
                g2d.fillRect(0, 1, getWidth(), 10);
                g2d.dispose();
            }
        };

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        NavButton homeButton = new NavButton("🏠", "Home", "home", e -> showPage("home"));
        NavButton libraryButton = new NavButton("📚", "Libreria", "library", e -> showPage("library"));
        NavButton authButton = new NavButton("👤", "Login", "auth", e -> showPage("login"));

        navButtons.put("home", homeButton);
        navButtons.put("library", libraryButton);
        navButtons.put("auth", authButton);

        panel.add(homeButton);
        panel.add(authButton);
        homeButton.setSelected(true);

        bottomNavigationPanel = panel;
    }

    private JButton createNavButton(String icon, String text, String pageName) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                if (currentPage != null && currentPage.equals(pageName)) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 20));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2d.dispose();
                }
                super.paintComponent(g);
            }
        };

        button.setLayout(new BorderLayout(0, 5));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 20));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        // Color is set in updateNavButtonStates
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        button.add(iconLabel, BorderLayout.CENTER);
        button.add(textLabel, BorderLayout.SOUTH);

        button.addActionListener(e -> showPage(pageName));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!currentPage.equals(pageName)) {
                    textLabel.setForeground(primaryColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!currentPage.equals(pageName)) {
                    textLabel.setForeground(textSecondary);
                }
            }
        });

        return button;
    }

    public static SocketConnection getSocketConnection() {
        return socketConnection;
    }
}