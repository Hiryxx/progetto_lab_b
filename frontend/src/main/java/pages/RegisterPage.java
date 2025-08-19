package pages;

import classes.MainFrame;
import classes.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.regex.Pattern;

import static classes.styles.Colors.*;

public class RegisterPage extends Page {
    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField fiscalCodeField;
    private JTextField emailField;
    private JPasswordField passwordField;

    // Regex per la validazione del Codice Fiscale
    private static final String FISCAL_CODE_REGEX = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$";

    public RegisterPage() {
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
        nameField.setText("Franco");
        lastNameField.setText("Rossi");
        fiscalCodeField.setText("RSSFNC80A01F205E");
        emailField.setText("franco@gmail.com");
        passwordField.setText("franco");

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

        // Register card - ridotti i margini
        JPanel registerCard = createRegisterCard();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        contentPanel.add(registerCard, gbc);
        return contentPanel;
    }

    private JPanel createRegisterCard() {
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
        card.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
        card.setPreferredSize(new Dimension(450, 700));

        // Header
        JPanel headerPanel = createRegisterHeader();
        card.add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = createRegisterForm();
        card.add(formPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createRegisterFooter();
        card.add(footerPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createRegisterHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoPanel.setOpaque(false);

        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(createModernBookIcon(40, 40));
        logoPanel.add(logoLabel);

        // Welcome text
        JLabel welcomeLabel = new JLabel("Unisciti a BookHub!");
        welcomeLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        welcomeLabel.setForeground(textPrimary);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel subtitleLabel = new JLabel("Crea il tuo account BookHub");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        subtitleLabel.setForeground(textSecondary);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        header.add(logoPanel);
        header.add(welcomeLabel);
        header.add(subtitleLabel);

        return header;
    }

    private JPanel createRegisterForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        // Name field
        FormField namePanel = new TextFormField("Nome", "user");
        nameField = namePanel.getField();
        form.add(namePanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // User field
        FormField userPanel = new TextFormField("Cognome", "lastname");
        lastNameField = userPanel.getField();
        form.add(userPanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // Fiscal Code field
        FormField fiscalCodePanel = new TextFormField("Codice fiscale", "fiscal");
        fiscalCodeField = fiscalCodePanel.getField();
        form.add(fiscalCodePanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // Email field
        FormField emailPanel = new TextFormField("Email", "email");
        emailField = emailPanel.getField();
        form.add(emailPanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password field
        FormField passwordPanel = new PasswordFormField("Password");
        passwordField = (JPasswordField) passwordPanel.getField();
        form.add(passwordPanel);
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel optionsPanel = new AuthOptionPanel(passwordField);
        form.add(optionsPanel);
        form.add(Box.createRigidArea(new Dimension(0, 30)));

        // Register button
        JButton loginButton = new AuthButton("Registrati", primaryColor, Color.WHITE, true);
        loginButton.addActionListener(e -> handleRegister());
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(loginButton);
        form.add(Box.createRigidArea(new Dimension(0, 18)));


        JPanel dividerPanel = createDivider("or");
        form.add(dividerPanel);
        form.add(Box.createRigidArea(new Dimension(0, 18)));


        return form;
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
        textLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        textLabel.setForeground(textSecondary);
        textLabel.setOpaque(true);
        textLabel.setBackground(cardColor);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        divider.add(leftLine, BorderLayout.WEST);
        divider.add(textLabel, BorderLayout.CENTER);
        divider.add(rightLine, BorderLayout.EAST);

        return divider;
    }

    private JPanel createRegisterFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel loginText = new JLabel("Hai gia un account? ");
        loginText.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        loginText.setForeground(textSecondary);

        JLabel loginLink = new JLabel("Accedi");
        loginLink.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        loginLink.setForeground(primaryColor);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleSignIn();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setForeground(primaryHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setForeground(primaryColor);
            }
        });

        footer.add(loginText);
        footer.add(loginLink);

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
    private void handleRegister() {
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String fiscalCode = fiscalCodeField.getText().toUpperCase();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        System.out.println("Registering user: " + name + ", " + lastName + ", " + fiscalCode + ", " + email);

        if (name.isEmpty() || lastName.isEmpty() || fiscalCode.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Per favore inserisci tutti i campi",
                    "Errore di registrazione",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Pattern.matches(FISCAL_CODE_REGEX, fiscalCode)) {
            JOptionPane.showMessageDialog(this,
                    "Codice fiscale non valido.",
                    "Errore di registrazione",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JsonObject userJson = new JsonObject();
        userJson.put("cf", fiscalCode);
        userJson.put("name", name);
        userJson.put("lastname", lastName);
        userJson.put("email", email);
        userJson.put("password", password);

        MainFrame.getSocketConnection().send("REGISTER", userJson);


        Response response = MainFrame.getSocketConnection().receive();
        if (!response.isError()) {
            UserState.login(userJson.toString());
            System.out.println("User registered successfully: " + fiscalCode);
            changePage("home");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Errore durante la registrazione: " + response.getResponse(),
                    "Errore di registrazione",
                    JOptionPane.ERROR_MESSAGE);
        }


    }

    private void handleSignIn() {
        // Navigate to sign in page
        changePage("login");
    }

}