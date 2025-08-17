package pages;

import classes.MainFrame;
import classes.Page;
import components.buttons.AuthButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;

import static classes.styles.Colors.*;

public class RegisterPage extends Page {
    private JTextField nameField;
    private JTextField userField;
    private JTextField fiscalCodeField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox termsCheck;


    // Regex per la validazione del Codice Fiscale
    private static final String FISCAL_CODE_REGEX = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$";

    public RegisterPage(MainFrame mainFrame) {
        super(mainFrame);
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
        JLabel welcomeLabel = new JLabel("Join BookHub!");
        welcomeLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        welcomeLabel.setForeground(textPrimary);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel subtitleLabel = new JLabel("Create your BookHub account");
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
        JPanel namePanel = createFormField("Full Name", "user");
        nameField = (JTextField) findTextComponent(namePanel);
        form.add(namePanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // User field
        JPanel userPanel = createFormField("Username", "username");
        userField = (JTextField) findTextComponent(userPanel);
        form.add(userPanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // Fiscal Code field
        JPanel fiscalCodePanel = createFormField("Fiscal Code", "fiscal");
        fiscalCodeField = (JTextField) findTextComponent(fiscalCodePanel);
        form.add(fiscalCodePanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // Email field
        JPanel emailPanel = createFormField("Email Address", "email");
        emailField = findTextComponent(emailPanel);
        form.add(emailPanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password field
        JPanel passwordPanel = createPasswordField("Password");
        passwordField = (JPasswordField) findTextComponent(passwordPanel);
        form.add(passwordPanel);
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        // Register button
        JButton loginButton = new AuthButton("Register", primaryColor, Color.WHITE, true);
        loginButton.addActionListener(e -> handleRegister());
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(loginButton);
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        // todo add show password

        // Or divider
        JPanel dividerPanel = createDivider("or");
        form.add(dividerPanel);
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        // Social login buttons
        JPanel socialPanel = createSocialLoginPanel();
        form.add(socialPanel);

        return form;
    }

    private JPanel createFormField(String label, String type) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);

        // Label
        JLabel fieldLabel = new JLabel(label, SwingConstants.CENTER); // Modifica qui
        fieldLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        fieldLabel.setForeground(textPrimary);
        fieldLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Modifica qui

        // Input field container
        JPanel inputContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Field background
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Border
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);

                g2d.dispose();
            }
        };
        inputContainer.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        inputContainer.setPreferredSize(new Dimension(0, 44));

        // Text field
        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        textField.setBorder(null);
        textField.setFont(new Font("SF Pro Text", Font.PLAIN, 15));
        textField.setForeground(textPrimary);
        textField.setCaretColor(primaryColor);

        // Icon
        JLabel iconLabel = new JLabel(getFieldIcon(type));
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 15));
        iconLabel.setForeground(textSecondary);

        inputContainer.add(iconLabel, BorderLayout.WEST);
        inputContainer.add(Box.createHorizontalStrut(8));
        inputContainer.add(textField, BorderLayout.CENTER);

        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        fieldPanel.add(inputContainer);

        return fieldPanel;
    }

    private JPanel createPasswordField(String label) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);

        // Label
        JLabel fieldLabel = new JLabel(label, SwingConstants.CENTER); // Modifica qui
        fieldLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        fieldLabel.setForeground(textPrimary);
        fieldLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Modifica qui

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
        inputContainer.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        inputContainer.setPreferredSize(new Dimension(0, 44));

        // Password field
        JPasswordField passwordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        passwordField.setBorder(null);
        passwordField.setFont(new Font("SF Pro Text", Font.PLAIN, 15));
        passwordField.setForeground(textPrimary);
        passwordField.setCaretColor(primaryColor);

        // Icon
        JLabel iconLabel = new JLabel("🔒");
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 15));

        inputContainer.add(iconLabel, BorderLayout.WEST);
        inputContainer.add(Box.createHorizontalStrut(8));
        inputContainer.add(passwordField, BorderLayout.CENTER);

        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        fieldPanel.add(inputContainer);

        return fieldPanel;
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

    private JPanel createSocialLoginPanel() {
        JPanel social = new JPanel(new GridLayout(1, 2, 12, 0));
        social.setOpaque(false);

        JButton googleButton = createModernButton("🌐 Google", new Color(66, 133, 244), Color.WHITE, false);
        JButton appleButton = createModernButton("🍎 Apple", new Color(0, 0, 0), Color.WHITE, false);

        googleButton.addActionListener(e -> handleSocialRegister("google"));
        appleButton.addActionListener(e -> handleSocialRegister("apple"));

        social.add(googleButton);
        social.add(appleButton);

        return social;
    }

    private JPanel createRegisterFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel loginText = new JLabel("Already have an account? ");
        loginText.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        loginText.setForeground(textSecondary);

        JLabel loginLink = new JLabel("Log in");
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

    private JButton createModernButton(String text, Color bgColor, Color textColor, boolean isPrimary) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color currentBg = bgColor;
                if (getModel().isPressed()) {
                    currentBg = isPrimary ? primaryHover : bgColor.darker();
                } else if (getModel().isRollover()) {
                    currentBg = isPrimary ? primaryHover : bgColor.brighter();
                }

                if (isPrimary) {
                    // Gradient for primary button
                    GradientPaint gradient = new GradientPaint(0, 0, currentBg, getWidth(), getHeight(),
                            currentBg.darker());
                    g2d.setPaint(gradient);
                } else {
                    g2d.setColor(currentBg);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Border for secondary buttons
                if (!isPrimary) {
                    g2d.setColor(borderColor);
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("SF Pro Text", Font.BOLD, 15));
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 44));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        return button;
    }

    private String getFieldIcon(String type) {
        switch (type) {
            case "email": return "✉️";
            case "user": return "👤";
            case "username": return "👤";
            case "fiscal": return "🇮🇹";
            default: return "📝";
        }
    }

    private JTextField findTextComponent(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component innerComp : ((JPanel) comp).getComponents()) {
                    if (innerComp instanceof JTextField || innerComp instanceof JPasswordField) {
                        return (JTextField) innerComp;
                    }
                    if (innerComp instanceof JPanel) {
                        for (Component deepComp : ((JPanel) innerComp).getComponents()) {
                            if (deepComp instanceof JTextField || deepComp instanceof JPasswordField) {
                                return (JTextField) deepComp;
                            }
                        }
                    }
                }
            }
        }
        return null;
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
        String username = userField.getText();
        String fiscalCode = fiscalCodeField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Basic validation
        if (name.isEmpty() || username.isEmpty() || fiscalCode.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate fiscal code format
        if (!Pattern.matches(FISCAL_CODE_REGEX, fiscalCode.toUpperCase())) {
            JOptionPane.showMessageDialog(this,
                    "Invalid fiscal code format. It must be 16 alphanumeric characters.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!termsCheck.isSelected()) {
            JOptionPane.showMessageDialog(this,
                    "Please accept the Terms of Service and Privacy Policy",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Here you would implement actual registration logic
        // For demo purposes, show success message and navigate to login
        JOptionPane.showMessageDialog(this,
                "Account created successfully! Please sign in.",
                "Registration Success",
                JOptionPane.INFORMATION_MESSAGE);
        changePage("login");
    }

    private void handleSocialRegister(String provider) {
        JOptionPane.showMessageDialog(this,
                "Social registration with " + provider + " would be implemented here",
                "Social Registration",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleTermsOfService() {
        JOptionPane.showMessageDialog(this,
                "Terms of Service would be displayed here",
                "Terms of Service",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handlePrivacyPolicy() {
        JOptionPane.showMessageDialog(this,
                "Privacy Policy would be displayed here",
                "Privacy Policy",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleSignIn() {
        // Navigate to sign in page
        changePage("login");
    }

}