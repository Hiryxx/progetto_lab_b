package pages;

import classes.MainFrame;
import classes.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPage extends Page {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox termsCheck;

    // Modern color palette (same as LoginPage)
    private Color primaryColor = new Color(99, 102, 241);      // Modern indigo
    private Color primaryHover = new Color(79, 70, 229);       // Darker indigo
    private Color accentColor = new Color(248, 113, 113);      // Modern coral
    private Color backgroundColor = new Color(248, 250, 252);   // Very light blue-gray
    private Color cardColor = new Color(255, 255, 255);        // Pure white
    private Color textPrimary = new Color(15, 23, 42);         // Dark slate
    private Color textSecondary = new Color(100, 116, 139);    // Medium slate
    private Color borderColor = new Color(226, 232, 240);      // Light slate
    private Color errorColor = new Color(239, 68, 68);         // Red for errors
    private Color successColor = new Color(34, 197, 94);       // Green for success

    // Modern gradients
    private Color gradientStart = new Color(139, 92, 246);     // Purple
    private Color gradientEnd = new Color(59, 130, 246);       // Blue

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

        // Modern bottom navigation (same as LoginPage)
        JPanel bottomPanel = createModernBottomNavigationPanel();
        this.add(bottomPanel, BorderLayout.SOUTH);
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

        // Register card
        JPanel registerCard = createRegisterCard();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 50, 50, 50);

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
        card.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
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
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoPanel.setOpaque(false);

        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(createModernBookIcon(50, 50));
        logoPanel.add(logoLabel);

        // Welcome text
        JLabel welcomeLabel = new JLabel("Join BookHub!");
        welcomeLabel.setFont(new Font("SF Pro Display", Font.BOLD, 32));
        welcomeLabel.setForeground(textPrimary);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel subtitleLabel = new JLabel("Create your BookHub account");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        subtitleLabel.setForeground(textSecondary);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

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
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        // Email field
        JPanel emailPanel = createFormField("Email Address", "email");
        emailField = (JTextField) findTextComponent(emailPanel);
        form.add(emailPanel);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password field
        JPanel passwordPanel = createPasswordField("Password");
        passwordField = (JPasswordField) findTextComponent(passwordPanel);
        form.add(passwordPanel);
        form.add(Box.createRigidArea(new Dimension(0, 20)));

        // Confirm password field
        JPanel confirmPasswordPanel = createPasswordField("Confirm Password");
        confirmPasswordField = (JPasswordField) findTextComponent(confirmPasswordPanel);
        form.add(confirmPasswordPanel);
        form.add(Box.createRigidArea(new Dimension(0, 15)));

        // Terms and conditions
        JPanel termsPanel = createTermsPanel();
        form.add(termsPanel);
        form.add(Box.createRigidArea(new Dimension(0, 30)));

        // Register button
        JButton registerButton = createModernButton("Create Account", primaryColor, Color.WHITE, true);
        registerButton.addActionListener(e -> handleRegister());
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(registerButton);
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

    private JPanel createTermsPanel() {
        JPanel terms = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        terms.setOpaque(false);

        termsCheck = new JCheckBox();
        termsCheck.setOpaque(false);
        termsCheck.setFocusPainted(false);

        JLabel termsText = new JLabel("I agree to the ");
        termsText.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        termsText.setForeground(textSecondary);

        JLabel termsLink = new JLabel("Terms of Service");
        termsLink.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        termsLink.setForeground(primaryColor);
        termsLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        termsLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTermsOfService();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                termsLink.setForeground(primaryHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                termsLink.setForeground(primaryColor);
            }
        });

        JLabel andText = new JLabel(" and ");
        andText.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        andText.setForeground(textSecondary);

        JLabel privacyLink = new JLabel("Privacy Policy");
        privacyLink.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        privacyLink.setForeground(primaryColor);
        privacyLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        privacyLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handlePrivacyPolicy();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                privacyLink.setForeground(primaryHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                privacyLink.setForeground(primaryColor);
            }
        });

        terms.add(termsCheck);
        terms.add(termsText);
        terms.add(termsLink);
        terms.add(andText);
        terms.add(privacyLink);

        return terms;
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

        googleButton.addActionListener(e -> handleSocialRegister("google"));
        appleButton.addActionListener(e -> handleSocialRegister("apple"));

        social.add(googleButton);
        social.add(appleButton);

        return social;
    }

    private JPanel createRegisterFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel loginText = new JLabel("Already have an account? ");
        loginText.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        loginText.setForeground(textSecondary);

        JLabel loginLink = new JLabel("Log in");
        loginLink.setFont(new Font("SF Pro Text", Font.BOLD, 14));
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
    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Basic validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
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