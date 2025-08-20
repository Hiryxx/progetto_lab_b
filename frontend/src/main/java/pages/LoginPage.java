package pages;

import classes.MainFrame;
import classes.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import components.buttons.AuthButton;
import components.checks.PasswordCheck;
import components.inputs.FormField;
import components.inputs.PasswordFormField;
import components.inputs.TextFormField;
import components.panels.AuthOptionPanel;
import connection.Response;
import json.JsonObject;
import state.UserState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import static classes.styles.Colors.*;

public class LoginPage extends Page {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheck;

    public LoginPage() {
        super();
        this.render();
    }

    @Override
    public void render() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout());

        // Background with gradient
        JPanel backgroundPanel = createGradientBackground();

        // Main content
        JPanel contentPanel = createMainContent();
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        this.add(backgroundPanel, BorderLayout.CENTER);
        this.emailField.setText("franco@gmail.com");
        this.passwordField.setText("franco");

    }

    @Override
    public void refresh() {

    }

    private JPanel createGradientBackground() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Main gradient background
                GradientPaint gradient = new GradientPaint(0, 0, gradientStart, getWidth(), getHeight(), gradientEnd);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Overlay pattern for texture
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int i = 0; i < getWidth(); i += 40) {
                    for (int j = 0; j < getHeight(); j += 40) {
                        g2d.fillOval(i, j, 2, 2);
                    }
                }

                g2d.dispose();
            }
        };
        return panel;
    }

    private JPanel createMainContent() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        // Login card
        JPanel loginCard = createLoginCard();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 50, 50, 50);

        contentPanel.add(loginCard, gbc);
        return contentPanel;
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card shadow - multiple layers for depth
                g2d.setColor(new Color(0, 0, 0, 5));
                g2d.fillRoundRect(8, 8, getWidth()-8, getHeight()-8, 24, 24);
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(4, 4, getWidth()-4, getHeight()-4, 24, 24);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(2, 2, getWidth()-2, getHeight()-2, 24, 24);

                // Card background
                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth()-8, getHeight()-8, 24, 24);

                // Subtle inner glow
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(1, 1, getWidth()-10, getHeight()-10, 22, 22);

                g2d.dispose();
            }
        };

        card.setLayout(new BorderLayout(0, 0));
        card.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        card.setPreferredSize(new Dimension(450, 600));

        // Header
        JPanel headerPanel = createLoginHeader();
        card.add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = createLoginForm();
        card.add(formPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createLoginFooter();
        card.add(footerPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createLoginHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoPanel.setOpaque(false);

        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(createModernBookIcon(50, 50));
        logoPanel.add(logoLabel);

        JLabel welcomeLabel = new JLabel("Bentornato!");
        welcomeLabel.setFont(new Font("SF Pro Display", Font.BOLD, 32));
        welcomeLabel.setForeground(textPrimary);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel subtitleLabel = new JLabel("Accedi al tuo account BookHub");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        subtitleLabel.setForeground(textSecondary);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        header.add(logoPanel);
        header.add(welcomeLabel);
        header.add(subtitleLabel);

        return header;
    }

    private JPanel createLoginForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        FormField emailPanel = new TextFormField("Email", "email");
        emailField = emailPanel.getField();
        form.add(emailPanel);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password field
        FormField passwordPanel = new PasswordFormField("Password");
        passwordField = (JPasswordField) passwordPanel.getField();
        form.add(passwordPanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // Remember me and forgot password
        JPanel optionsPanel = new AuthOptionPanel(passwordField);
        form.add(optionsPanel);
        form.add(Box.createRigidArea(new Dimension(0, 30)));

        // Login button
        JButton loginButton = new AuthButton("Accedi", primaryColor, Color.WHITE, true);
        loginButton.addActionListener(e -> handleLogin());
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(loginButton);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        // Or divider
        JPanel dividerPanel = createDivider("or");
        form.add(dividerPanel);
        form.add(Box.createRigidArea(new Dimension(0, 20)));


        return form;
    }

    private JPanel createPasswordField(String label) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);

        // Label
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        fieldLabel.setForeground(textPrimary);
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Input field container
        JPanel inputContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);

                g2d.dispose();
            }
        };
        inputContainer.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        inputContainer.setPreferredSize(new Dimension(0, 50));

        // Password field
        JPasswordField passwordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        passwordField.setBorder(null);
        passwordField.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        passwordField.setForeground(textPrimary);
        passwordField.setCaretColor(primaryColor);


        // Icon
        JLabel iconLabel = new JLabel("🔒");
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 16));

        inputContainer.add(iconLabel, BorderLayout.WEST);
        inputContainer.add(Box.createHorizontalStrut(10));
        inputContainer.add(passwordField, BorderLayout.CENTER);

        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        fieldPanel.add(inputContainer);

        return fieldPanel;
    }
    // todo remove later
    private JPanel createOptionsPanel() {
        JPanel options = new JPanel(new BorderLayout());
        options.setOpaque(false);

        // Remember me checkbox
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        showPasswordCheck = new PasswordCheck("Mostra password");

        showPasswordCheck.addActionListener(e -> {
            boolean isSelected = showPasswordCheck.isSelected();
            if (isSelected) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        leftPanel.add(showPasswordCheck);

        // Forgot password link
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);

        options.add(leftPanel, BorderLayout.WEST);
        options.add(rightPanel, BorderLayout.EAST);

        return options;
    }

    private JPanel createDivider(String text) {
        JPanel divider = new JPanel(new BorderLayout());
        divider.setOpaque(false);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        // Left line
        JPanel leftLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(borderColor);
                g.fillRect(0, getHeight()/2-1, getWidth(), 1);
            }
        };
        leftLine.setOpaque(false);

        // Right line
        JPanel rightLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(borderColor);
                g.fillRect(0, getHeight()/2-1, getWidth(), 1);
            }
        };
        rightLine.setOpaque(false);

        // Text
        JLabel textLabel = new JLabel(" " + text + " ");
        textLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        textLabel.setForeground(textSecondary);
        textLabel.setOpaque(true);
        textLabel.setBackground(cardColor);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        divider.add(leftLine, BorderLayout.WEST);
        divider.add(textLabel, BorderLayout.CENTER);
        divider.add(rightLine, BorderLayout.EAST);

        return divider;
    }


    private JPanel createLoginFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel signupText = new JLabel("Non hai un account? ");
        signupText.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        signupText.setForeground(textSecondary);

        JLabel signupLink = new JLabel("Registrati");
        signupLink.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        signupLink.setForeground(primaryColor);
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        signupLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changePage("register");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                signupLink.setForeground(primaryHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signupLink.setForeground(primaryColor);
            }
        });


        footer.add(signupText);
        footer.add(signupLink);

        return footer;
    }


    private Icon createModernBookIcon(int width, int height) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Modern book design with gradients
                GradientPaint bookGradient = new GradientPaint(x, y, primaryColor, x + width, y + height, accentColor);
                g2d.setPaint(bookGradient);
                g2d.fillRoundRect(x, y + height/6, width*4/5, height*4/5, 8, 8);

                // Book spine with accent color
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(x + width*4/5, y + height/6, width/5, height*4/5, 6, 6);

                // Modern book details
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.setStroke(new BasicStroke(1.5f));
                for (int i = 1; i < 3; i++) {
                    int lineY = y + height/6 + i * height/4;
                    g2d.drawLine(x + 5, lineY, x + width*3/5, lineY);
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() { return width; }

            @Override
            public int getIconHeight() { return height; }
        };
    }

    // Event handlers
    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JsonObject userJson = new JsonObject();
        userJson.put("email", email);
        userJson.put("password", password);
        MainFrame.getSocketConnection().send("LOGIN", userJson);

        Response response = MainFrame.getSocketConnection().receive();
        if (!response.isError()) {
            String user = response.getResponseText();
            System.out.println("Login successful for user: " + user);
            try {
                UserState.login(user);
            } catch (JsonProcessingException e) {
                JOptionPane.showMessageDialog(this,
                        "Errore durante il login:",
                        "Errore di registrazione",
                        JOptionPane.ERROR_MESSAGE);
            }
            changePage("home");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il login: " + response.getResponseText(),
                    "Errore di registrazione",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}