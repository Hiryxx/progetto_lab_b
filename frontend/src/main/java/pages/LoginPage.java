package pages;

import classes.MainFrame;
import classes.Page;

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

    public LoginPage(MainFrame mainFrame) {
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

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("SF Pro Display", Font.BOLD, 32));
        welcomeLabel.setForeground(textPrimary);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel subtitleLabel = new JLabel("Sign in to your BookHub account");
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

        // Email field
        JPanel emailPanel = createFormField("Email", "email");
        emailField = (JTextField) findTextComponent(emailPanel);
        form.add(emailPanel);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password field
        JPanel passwordPanel = createPasswordField("Password");
        passwordField = (JPasswordField) findTextComponent(passwordPanel);
        form.add(passwordPanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // Remember me and forgot password
        JPanel optionsPanel = createOptionsPanel();
        form.add(optionsPanel);
        form.add(Box.createRigidArea(new Dimension(0, 30)));

        // Login button
        JButton loginButton = createModernButton("Sign In", primaryColor, Color.WHITE, true);
        loginButton.addActionListener(e -> handleLogin());
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(loginButton);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        // Or divider
        JPanel dividerPanel = createDivider("or");
        form.add(dividerPanel);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

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
        inputContainer.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        inputContainer.setPreferredSize(new Dimension(0, 50));

        // Text field
        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        textField.setBorder(null);
        textField.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        textField.setForeground(textPrimary);
        textField.setCaretColor(primaryColor);

        // Icon
        JLabel iconLabel = new JLabel(getFieldIcon(type));
        iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        iconLabel.setForeground(textSecondary);

        inputContainer.add(iconLabel, BorderLayout.WEST);
        inputContainer.add(Box.createHorizontalStrut(10));
        inputContainer.add(textField, BorderLayout.CENTER);

        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        fieldPanel.add(inputContainer);

        return fieldPanel;
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
        passwordField.setEchoChar('•'); // Default hidden

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

    private JPanel createOptionsPanel() {
        JPanel options = new JPanel(new BorderLayout());
        options.setOpaque(false);

        // Remember me checkbox
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        showPasswordCheck = new JCheckBox("Show password");
        showPasswordCheck.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        showPasswordCheck.setForeground(textSecondary);
        showPasswordCheck.setOpaque(false);
        showPasswordCheck.setFocusPainted(false);

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

        JLabel forgotLabel = new JLabel("Forgot password?");
        forgotLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        forgotLabel.setForeground(primaryColor);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleForgotPassword();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                forgotLabel.setForeground(primaryHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgotLabel.setForeground(primaryColor);
            }
        });

        rightPanel.add(forgotLabel);

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

    private JPanel createSocialLoginPanel() {
        JPanel social = new JPanel(new GridLayout(1, 2, 15, 0));
        social.setOpaque(false);

        JButton googleButton = createModernButton("🌐 Google", new Color(66, 133, 244), Color.WHITE, false);
        JButton appleButton = createModernButton("🍎 Apple", new Color(0, 0, 0), Color.WHITE, false);

        googleButton.addActionListener(e -> handleSocialLogin("google"));
        appleButton.addActionListener(e -> handleSocialLogin("apple"));

        social.add(googleButton);
        social.add(appleButton);

        return social;
    }

    private JPanel createLoginFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel signupText = new JLabel("Don't have an account? ");
        signupText.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        signupText.setForeground(textSecondary);

        JLabel signupLink = new JLabel("Sign up");
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

        button.setFont(new Font("SF Pro Text", Font.BOLD, 16));
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 50));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        return button;
    }

    private String getFieldIcon(String type) {
        switch (type) {
            case "email": return "✉️";
            case "user": return "👤";
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

        // Here you would implement actual login logic
        // For demo purposes, just navigate to home
        changePage("home");
    }

    private void handleForgotPassword() {
        JOptionPane.showMessageDialog(this,
                "Password reset functionality would be implemented here",
                "Forgot Password",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleSocialLogin(String provider) {
        JOptionPane.showMessageDialog(this,
                "Social login with " + provider + " would be implemented here",
                "Social Login",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleSignUp() {
        // Navigate to sign up page
        JOptionPane.showMessageDialog(this,
                "Sign up page would be implemented here",
                "Sign Up",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createModernBottomNavigationPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(cardColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Top border with shadow
                g2d.setColor(borderColor);
                g2d.fillRect(0, 0, getWidth(), 1);

                // Subtle shadow
                GradientPaint shadow = new GradientPaint(0, 1, new Color(0, 0, 0, 8), 0, 10, new Color(0, 0, 0, 0));
                g2d.setPaint(shadow);
                g2d.fillRect(0, 1, getWidth(), 10);
                g2d.dispose();
            }
        };

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton homeButton = createModernNavButton("🏠", "Home", textSecondary, false);
        JButton libraryButton = createModernNavButton("📚", "Library", textSecondary, false);
        JButton recommendButton = createModernNavButton("⭐", "Discover", textSecondary, false);
        JButton profileButton = createModernNavButton("👤", "Profile", textSecondary, false);

        // Add click handlers for navigation
        homeButton.addActionListener(e -> changePage("home"));
        libraryButton.addActionListener(e -> changePage("library"));
        recommendButton.addActionListener(e -> changePage("recommendations"));
        profileButton.addActionListener(e -> changePage("profile"));

        panel.add(homeButton);
        panel.add(libraryButton);
        panel.add(recommendButton);
        panel.add(profileButton);

        return panel;
    }

    private JButton createModernNavButton(String icon, String text, Color color, boolean active) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (active) {
                    g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 20));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setLayout(new BorderLayout(0, 5));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 20));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        textLabel.setForeground(color);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        button.add(iconLabel, BorderLayout.CENTER);
        button.add(textLabel, BorderLayout.SOUTH);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    textLabel.setForeground(primaryColor);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) {
                    textLabel.setForeground(color);
                }
            }
        });

        return button;
    }
}