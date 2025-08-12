package pages;

import classes.MainFrame;
import classes.Page;
import components.ModernScrollBarUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

public class HomePage extends Page {
    private JTextField searchField;

    // Colori
    private Color primaryColor = new Color(99, 102, 241);      // Indaco
    private Color primaryHover = new Color(79, 70, 229);       // Indaco  scuro
    private Color accentColor = new Color(248, 113, 113);      // Corallo
    private Color backgroundColor = new Color(248, 250, 252);   // Azzurro-grigio  chiaro
    private Color cardColor = new Color(255, 255, 255);        // Bianco puro
    private Color textPrimary = new Color(15, 23, 42);         // Ardesia scura
    private Color textSecondary = new Color(100, 116, 139);    // Ardesia media
    private Color borderColor = new Color(226, 232, 240);      // Ardesia chiara

    // Sfumature
    private Color gradientStart = new Color(139, 92, 246);     // Viola
    private Color gradientEnd = new Color(59, 130, 246);       // Blu

    public HomePage(MainFrame mainFrame) {
        super(mainFrame);
        this.render();
    }

    @Override
    public void render() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));

        // Barra superiore con ricerca
        JPanel topPanel = createTopPanel();
        this.add(topPanel, BorderLayout.NORTH);

        // Contenuto principale
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(backgroundColor);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        // Prima sezione
        contentPanel.add(createHeroSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Libri in evidenza
        contentPanel.add(createFeaturedBooksPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Browser categorie
        contentPanel.add(createCategoriesPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Libri recenti
        contentPanel.add(createRecentBooksPanel());

        // Colonna per scorrere
        JScrollPane scrollPane = createScrollPane(contentPanel);
        this.add(scrollPane, BorderLayout.CENTER);

        // Menù di navigazione
        JPanel bottomPanel = createBottomNavigationPanel();
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sfondo
                GradientPaint gradient = new GradientPaint(0, 0, gradientStart, getWidth(), 0, gradientEnd);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };

        panel.setLayout(new BorderLayout(20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        panel.setPreferredSize(new Dimension(0, 90));

        // Sezione del titolo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);

        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(createBookIcon(45, 45));
        titlePanel.add(logoLabel);

        titlePanel.add(Box.createHorizontalStrut(15));

        JLabel titleLabel = new JLabel("BookHub");
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Barra di ricerca
        JPanel searchPanel = createSearchPanel();
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(0, 0));
        searchPanel.setOpaque(false);

        // Spazione per la ricerca
        JPanel searchContainer = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
            }
        };
        searchContainer.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 15));

        searchField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        searchField.setBorder(null);
        searchField.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        searchField.setForeground(textPrimary);
        searchField.setText("Cerca libri, autori...");
        searchField.setForeground(textSecondary);

        // Placeholder barra di ricerca
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Cerca libri, autori...")) {
                    searchField.setText("");
                    searchField.setForeground(textPrimary);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(textSecondary);
                    searchField.setText("Cerca libri, autori...");
                }
            }
        });

        JButton searchButton = createIconButton("🔍");

        searchContainer.add(searchField, BorderLayout.CENTER);
        searchContainer.add(searchButton, BorderLayout.EAST);

        searchPanel.add(searchContainer, BorderLayout.CENTER);
        return searchPanel;
    }

    private JPanel createHeroSection() {
        JPanel hero = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sfondo
                GradientPaint gradient = new GradientPaint(0, 0, new Color(67, 56, 202, 20),
                        getWidth(), getHeight(), new Color(147, 51, 234, 20));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Bordo
                g2d.setColor(new Color(139, 92, 246, 30));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };

        hero.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Scopri il tuo prossimo");
        welcomeLabel.setFont(new Font("SF Pro Display", Font.BOLD, 36));
        welcomeLabel.setForeground(textPrimary);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel favoriteLabel = new JLabel("Libro Preferito");
        favoriteLabel.setFont(new Font("SF Pro Display", Font.BOLD, 36));
        favoriteLabel.setForeground(primaryColor);
        favoriteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Esplora migliaia di libri con suggerimenti personalizzati");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 18));
        subtitleLabel.setForeground(textSecondary);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        textPanel.add(welcomeLabel);
        textPanel.add(favoriteLabel);
        textPanel.add(subtitleLabel);

        hero.add(textPanel, BorderLayout.WEST);
        return hero;
    }

    private JPanel createFeaturedBooksPanel() {
        return createSectionPanel("✨ Libri in Evidenza", "Le migliori scelte per te", () -> {
            JPanel booksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            booksPanel.setOpaque(false);

            for (int i = 1; i <= 4; i++) {
                JPanel bookWrapper = createBookCard("Libro in Evidenza " + i, "Autore " + i, "Genere " + i, 4.5f - (i * 0.3f) % 2);
                booksPanel.add(bookWrapper);
            }
            return booksPanel;
        });
    }

    private JPanel createCategoriesPanel() {
        return createSectionPanel("📚 Sfoglia per Categoria", "Trova libri nei tuoi generi preferiti", () -> {
            String[] categories = {"Narrativa", "Saggistica", "Scienza", "Storia",
                    "Biografia", "Fantasy", "Mistero", "Self-Help"};

            Color[] categoryColors = {
                    new Color(236, 72, 153),  // Rosa
                    new Color(168, 85, 247),  // Viola
                    new Color(59, 130, 246),  // Blu
                    new Color(16, 185, 129),  // Smeraldo
                    new Color(245, 158, 11),  // Ambra
                    new Color(139, 92, 246),  // Violetto
                    new Color(34, 197, 94),   // Verde
                    new Color(239, 68, 68)    // Rosso
            };

            JPanel categoriesPanel = new JPanel(new GridLayout(2, 4, 20, 20));
            categoriesPanel.setOpaque(false);

            for (int i = 0; i < categories.length; i++) {
                JPanel categoryCard = createCategoryCard(categories[i], categoryColors[i]);
                categoriesPanel.add(categoryCard);
            }
            return categoriesPanel;
        });
    }

    private JPanel createRecentBooksPanel() {
        return createSectionPanel("🕐 Aggiunti di Recente", "Nuovi arrivi nella nostra collezione", () -> {
            JPanel booksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            booksPanel.setOpaque(false);

            for (int i = 1; i <= 4; i++) {
                JPanel bookWrapper = createBookCard("Libro Recente " + i, "Autore " + i, "Genere " + ((i + 2) % 5 + 1), 4.0f + (i * 0.2f) % 1);
                booksPanel.add(bookWrapper);
            }
            return booksPanel;
        });
    }

    private JPanel createSectionPanel(String title, String subtitle, Supplier<JPanel> contentSupplier) {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        panel.setOpaque(false);

        // Intestazione
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        titleLabel.setForeground(textPrimary);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        subtitleLabel.setForeground(textSecondary);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        headerPanel.add(titlePanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentSupplier.get(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCategoryCard(String category, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sfondo
                GradientPaint gradient = new GradientPaint(0, 0, color, getWidth(), getHeight(),
                        new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                // Effetto ombra
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth(), getHeight(), 16, 16);

                g2d.dispose();
            }
        };

        card.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        card.setPreferredSize(new Dimension(0, 120));

        JLabel label = new JLabel(category);
        label.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(label, BorderLayout.CENTER);

        // Effetti hover
        card.addMouseListener(new MouseAdapter() {
            private Timer hoverTimer;

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));

                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(10, evt -> {
                    card.repaint();
                });
                hoverTimer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                card.repaint();
            }
        });

        return card;
    }

    private JScrollPane createScrollPane(JPanel contentPanel) {
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(backgroundColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Barra di scorrimento
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        return scrollPane;
    }

    private JPanel createBottomNavigationPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(cardColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Bordo superiore
                g2d.setColor(borderColor);
                g2d.fillRect(0, 0, getWidth(), 1);

                // Ombra
                GradientPaint shadow = new GradientPaint(0, 1, new Color(0, 0, 0, 8), 0, 10, new Color(0, 0, 0, 0));
                g2d.setPaint(shadow);
                g2d.fillRect(0, 1, getWidth(), 10);
                g2d.dispose();
            }
        };

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton homeButton = createNavButton("🏠", "Home", primaryColor, true);
        JButton libraryButton = createNavButton("📚", "Libreria", textSecondary, false);
        JButton recommendButton = createNavButton("⭐", "Scopri", textSecondary, false);
        JButton profileButton = createNavButton("👤", "Profilo", textSecondary, false);
        JButton profileTryButton = createNavButton("👤", "Profilo1", textSecondary, false);
        profileButton.addActionListener(e -> changePage("login"));
        profileTryButton.addActionListener(e -> changePage("profile"));


        panel.add(homeButton);
        panel.add(libraryButton);
        panel.add(recommendButton);
        panel.add(profileButton);
        panel.add(profileTryButton);

        return panel;
    }

    private JButton createNavButton(String icon, String text, Color color, boolean active) {
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

    private JPanel createBookCard(String title, String author, String genre, float rating) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombra card
                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 16, 16);

                // Sfondo card
                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);

                // Bordo
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);

                g2d.dispose();
            }
        };

        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(180, 260));
        card.setMaximumSize(new Dimension(180, 260));
        card.setMinimumSize(new Dimension(180, 260));

        // Segnaposto copertina libro
        JPanel coverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Copertina
                GradientPaint gradient = new GradientPaint(0, 0, primaryColor, getWidth(), getHeight(), accentColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Icona libro
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("SF Pro Text", Font.BOLD, 24));
                FontMetrics fm = g2d.getFontMetrics();
                String bookIcon = "📖";
                int x = (getWidth() - fm.stringWidth(bookIcon)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(bookIcon, x, y);

                g2d.dispose();
            }
        };
        coverPanel.setPreferredSize(new Dimension(0, 140));

        // Spazio informazioni libro
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        titleLabel.setForeground(textPrimary);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel authorLabel = new JLabel("di " + author);
        authorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        authorLabel.setForeground(textSecondary);
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        authorLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        JLabel genreLabel = new JLabel(genre);
        genreLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 11));
        genreLabel.setForeground(primaryColor);
        genreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        genreLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        // Valutazione
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ratingPanel.setOpaque(false);
        ratingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= rating ? "⭐" : "☆");
            star.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
            ratingPanel.add(star);
        }

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(genreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(ratingPanel);

        card.add(coverPanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        // Effetto hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        return card;
    }

    private JButton createIconButton(String icon) {
        JButton button = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(primaryHover);
                } else if (getModel().isRollover()) {
                    g2d.setColor(primaryColor);
                } else {
                    g2d.setColor(primaryColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private Icon createBookIcon(int width, int height) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Design libro
                GradientPaint bookGradient = new GradientPaint(x, y, Color.WHITE, x, y + height, new Color(240, 240, 255));
                g2d.setPaint(bookGradient);
                g2d.fillRoundRect(x, y + height / 6, width * 4 / 5, height * 4 / 5, 8, 8);

                // Dorso libro
                g2d.setColor(accentColor);
                g2d.fillRoundRect(x + width * 4 / 5, y + height / 6, width / 5, height * 4 / 5, 6, 6);

                // Dettagli libro
                g2d.setColor(new Color(200, 200, 220));
                g2d.setStroke(new BasicStroke(1.5f));
                for (int i = 1; i < 3; i++) {
                    int lineY = y + height / 6 + i * height / 4;
                    g2d.drawLine(x + 5, lineY, x + width * 3 / 5, lineY);
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return width;
            }

            @Override
            public int getIconHeight() {
                return height;
            }
        };
    }
}