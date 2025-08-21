package pages;

import classes.Page;
import components.cards.BookCard;
import components.ModernScrollBarUI;
import data.BookData;
import state.BooksState;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static classes.styles.Colors.*;

public class HomePage extends Page {
    private JTextField searchField;
    private JComboBox<String> yearFilter;
    private JComboBox<String> authorFilter;
    private JComboBox<String> categoryFilter;
    private List<BookData> filteredBooks;

    public HomePage() {
        super();
        this.render();
    }

    @Override
    public void render() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BorderLayout(0, 0));
        topContainer.setBackground(backgroundColor);

        JPanel topPanel = createTopPanel();
        topContainer.add(topPanel, BorderLayout.NORTH);

        JPanel filterBar = createFilterBar();
        topContainer.add(filterBar, BorderLayout.SOUTH);

        this.add(topContainer, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(backgroundColor);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        try {
            BooksState.fetchBooks();
            filteredBooks = new ArrayList<>(BooksState.books);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore durante il caricamento dei libri: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }

        contentPanel.add(createHeroSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        contentPanel.add(createFeaturedBooksPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));


        JScrollPane scrollPane = createScrollPane(contentPanel);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
    }

    private JPanel createFilterBar() {
        JPanel filterBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(cardColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(borderColor);
                g2d.drawLine(0, 0, getWidth(), 0);

                GradientPaint shadowGradient = new GradientPaint(
                        0, getHeight() - 5, new Color(0, 0, 0, 10),
                        0, getHeight(), new Color(0, 0, 0, 0)
                );
                g2d.setPaint(shadowGradient);
                g2d.fillRect(0, getHeight() - 5, getWidth(), 5);

                g2d.dispose();
            }
        };

        filterBar.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        filterBar.setPreferredSize(new Dimension(0, 70));
        filterBar.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel filterLabel = new JLabel("Filtri:");
        filterLabel.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        filterLabel.setForeground(textPrimary);
        filterBar.add(filterLabel);

        yearFilter = createStyledComboBox(getYearOptions());
        filterBar.add(createFilterContainer("📅 Anno", yearFilter));

        authorFilter = createStyledComboBox(getAuthorOptions());
        filterBar.add(createFilterContainer("✍️ Autore", authorFilter));

        categoryFilter = createStyledComboBox(getCategoryOptions());
        filterBar.add(createFilterContainer("📚 Categoria", categoryFilter));

        JButton resetButton = createResetButton();
        filterBar.add(resetButton);

        JButton applyButton = createApplyButton();
        filterBar.add(applyButton);

        return filterBar;
    }

    private JPanel createFilterContainer(String label, JComboBox<String> comboBox) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        container.setOpaque(false);

        JLabel filterLabel = new JLabel(label);
        filterLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        filterLabel.setForeground(textSecondary);

        container.add(filterLabel);
        container.add(comboBox);

        return container;
    }

    private JComboBox<String> createStyledComboBox(String[] options) {
        JComboBox<String> comboBox = new JComboBox<>(options) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
                g2d.dispose();
            }
        };

        comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        comboBox.setForeground(textPrimary);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        comboBox.setPreferredSize(new Dimension(150, 35));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                label.setFont(new Font("SF Pro Text", Font.PLAIN, 14));

                if (isSelected) {
                    label.setBackground(primaryColor);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(textPrimary);
                }

                return label;
            }
        });

        return comboBox;
    }

    private JButton createResetButton() {
        JButton button = new JButton("🔄 Reset") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(240, 240, 240));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(245, 245, 245));
                } else {
                    g2d.setColor(Color.WHITE);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        button.setForeground(textSecondary);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));

        button.addActionListener(e -> resetFilters());

        return button;
    }

    private JButton createApplyButton() {
        JButton button = new JButton("Applica Filtri") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(primaryHover);
                } else if (getModel().isRollover()) {
                    GradientPaint gradient = new GradientPaint(
                            0, 0, gradientStart,
                            getWidth(), 0, gradientEnd
                    );
                    g2d.setPaint(gradient);
                } else {
                    g2d.setColor(primaryColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 35));

        button.addActionListener(e -> applyFilters());

        return button;
    }

    private String[] getYearOptions() {
        if (BooksState.books == null || BooksState.books.isEmpty()) {
            return new String[]{"Tutti gli anni"};
        }

        Set<String> years = new TreeSet<>(Collections.reverseOrder());
        years.add("Tutti gli anni");

        for (BookData book : BooksState.books) {
            if (book.getYear() > 0) {
                years.add(String.valueOf(book.getYear()));
            }
        }

        return years.toArray(new String[0]);
    }

    private String[] getAuthorOptions() {
        if (BooksState.books == null || BooksState.books.isEmpty()) {
            return new String[]{"Tutti gli autori"};
        }

        Set<String> authors = new TreeSet<>();
        authors.add("Tutti gli autori");

        for (BookData book : BooksState.books) {
            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                // Gestisce autori multipli separati da virgola
                String[] bookAuthors = book.getAuthors().split(",");
                for (String author : bookAuthors) {
                    authors.add(author.trim());
                }
            }
        }

        return authors.toArray(new String[0]);
    }

    private String[] getCategoryOptions() {
        if (BooksState.books == null || BooksState.books.isEmpty()) {
            return new String[]{"Tutte le categorie"};
        }

        Set<String> categories = new TreeSet<>();
        categories.add("Tutte le categorie");

        for (BookData book : BooksState.books) {
            if (book.getCategories() != null && !book.getCategories().isEmpty()) {
                // Gestisce categorie multiple separate da virgola
                String[] bookCategories = book.getCategories().split(",");
                for (String category : bookCategories) {
                    categories.add(category.trim());
                }
            }
        }

        return categories.toArray(new String[0]);
    }

    private void resetFilters() {
        yearFilter.setSelectedIndex(0);
        authorFilter.setSelectedIndex(0);
        categoryFilter.setSelectedIndex(0);
        filteredBooks = new ArrayList<>(BooksState.books);
        refresh();
    }

    private void applyFilters() {
        filteredBooks = BooksState.books.stream()
                .filter(book -> filterByYear(book))
                .filter(book -> filterByAuthor(book))
                .filter(book -> filterByCategory(book))
                .collect(Collectors.toList());

        refresh();

        JOptionPane.showMessageDialog(this,
                "Trovati " + filteredBooks.size() + " libri con i filtri selezionati",
                "Filtri Applicati",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean filterByYear(BookData book) {
        String selectedYear = (String) yearFilter.getSelectedItem();
        if ("Tutti gli anni".equals(selectedYear)) {
            return true;
        }
        return String.valueOf(book.getYear()).equals(selectedYear);
    }

    private boolean filterByAuthor(BookData book) {
        String selectedAuthor = (String) authorFilter.getSelectedItem();
        if ("Tutti gli autori".equals(selectedAuthor)) {
            return true;
        }
        return book.getAuthors() != null && book.getAuthors().contains(selectedAuthor);
    }

    private boolean filterByCategory(BookData book) {
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        if ("Tutte le categorie".equals(selectedCategory)) {
            return true;
        }
        return book.getCategories() != null && book.getCategories().contains(selectedCategory);
    }


    private JPanel createTopPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

        JPanel searchPanel = createSearchPanel();
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(0, 0));
        searchPanel.setOpaque(false);

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
        searchField.addActionListener( e ->{

            if (searchField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Per favore inserisci un termine di ricerca valido.",
                        "Errore di ricerca", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "Invio ricercato: " + searchField.getText(),
                    "AAA",
                    JOptionPane.INFORMATION_MESSAGE);
                } );
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

                GradientPaint gradient = new GradientPaint(0, 0, new Color(67, 56, 202, 20),
                        getWidth(), getHeight(), new Color(147, 51, 234, 20));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

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

            // TODO Usa i libri filtrati invece di tutti i libri
            List<BookData> booksToShow = filteredBooks != null ? filteredBooks : BooksState.books;

            for (BookData book : booksToShow) {
                JPanel bookWrapper = new BookCard(book, 0f);
                booksPanel.add(bookWrapper);
            }
            return booksPanel;
        });
    }



    private JPanel createSectionPanel(String title, String subtitle, Supplier<JPanel> contentSupplier) {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        panel.setOpaque(false);

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

                GradientPaint gradient = new GradientPaint(0, 0, color, getWidth(), getHeight(),
                        new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

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

        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        return scrollPane;
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

                GradientPaint bookGradient = new GradientPaint(x, y, Color.WHITE, x, y + height, new Color(240, 240, 255));
                g2d.setPaint(bookGradient);
                g2d.fillRoundRect(x, y + height / 6, width * 4 / 5, height * 4 / 5, 8, 8);

                g2d.setColor(accentColor);
                g2d.fillRoundRect(x + width * 4 / 5, y + height / 6, width / 5, height * 4 / 5, 6, 6);

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