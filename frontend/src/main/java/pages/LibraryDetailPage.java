package pages;

import classes.Page;
import components.ModernScrollBarUI;
import components.cards.BookCard;
import components.panels.StatItem;
import state.LibraryDetail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static classes.styles.Colors.*;

public class LibraryDetailPage extends Page {
    private JLabel titleLabel = new JLabel();
    private List<BookData> libraryBooks;
    private JPanel booksContainer;
    private JTextField searchField;
    private String currentFilter = "all";
    private JLabel subtitleLabel = new JLabel();
    private int reviewCount = 0;
    private int ratedCount = 0;

    private StatItem ratedStat;
    private StatItem reviewStat;

    // Data class for books
    public static class BookData {
        String title;
        String author;
        String genre;
        float userRating; // User's personal rating (0 if not rated)
        float avgRating;  // Average rating from all users
        boolean hasReview;
        boolean hasSuggestions;
        String isbn;
        int year;

        public BookData(String title, String author, String genre, float userRating, float avgRating, boolean hasReview, boolean hasSuggestions, String isbn, int year) {
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.userRating = userRating;
            this.avgRating = avgRating;
            this.hasReview = hasReview;
            this.hasSuggestions = hasSuggestions;
            this.isbn = isbn;
            this.year = year;
        }
    }

    public LibraryDetailPage() {
        super();
        this.render();
    }


    private void initializeBooks() {
        libraryBooks = new ArrayList<>();
        String libraryName = LibraryDetail.libraryName;
        if (libraryName.equals("Da Leggere")) {
            libraryBooks.add(new BookData("Il nome del vento", "Patrick Rothfuss", "Fantasy", 0, 4.5f, false, false, "978-8804668695", 2007));
            libraryBooks.add(new BookData("Fondazione", "Isaac Asimov", "Fantascienza", 5.0f, 4.3f, true, true, "978-8804719", 1951));
            libraryBooks.add(new BookData("Neuromante", "William Gibson", "Cyberpunk", 4.0f, 4.2f, true, false, "978-8804718", 1984));
            libraryBooks.add(new BookData("Dune", "Frank Herbert", "Fantascienza", 0, 4.6f, false, false, "978-8804717", 1965));
            libraryBooks.add(new BookData("Il problema dei tre corpi", "Liu Cixin", "Fantascienza", 3.5f, 4.1f, true, true, "978-8804716", 2008));
            libraryBooks.add(new BookData("L'anello di Re Salomone", "Konrad Lorenz", "Saggistica", 0, 4.0f, false, false, "978-8804715", 1949));
        } else if (libraryName.equals("Fantascienza")) {
            libraryBooks.add(new BookData("Hyperion", "Dan Simmons", "Fantascienza", 4.5f, 4.4f, true, true, "978-8804714", 1989));
            libraryBooks.add(new BookData("Ubik", "Philip K. Dick", "Fantascienza", 4.0f, 4.1f, true, false, "978-8804713", 1969));
            libraryBooks.add(new BookData("1984", "George Orwell", "Distopia", 5.0f, 4.5f, true, true, "978-8804712", 1949));
            libraryBooks.add(new BookData("Fahrenheit 451", "Ray Bradbury", "Distopia", 0, 4.3f, false, false, "978-8804711", 1953));
        }

       subtitleLabel.setText( libraryBooks.size() + " libri nella libreria");
    }

    // TODO PROBABLY I DO NOT NEED TOP NAVBAR
    @Override
    public void render() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));

        // Header
        JPanel headerPanel = createHeader();
        this.add(headerPanel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(backgroundColor);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));


        booksContainer = new JPanel();
        booksContainer.setOpaque(false);
        booksContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        refreshBooksGrid();

        // Scroll pane for books
        JScrollPane scrollPane = createScrollPane(booksContainer);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        this.add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        titleLabel.setText("📚 " + LibraryDetail.libraryName);
        initializeBooks();

        refreshBooksGrid();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel() {
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

        header.setLayout(new BorderLayout(20, 0));
        header.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        header.setPreferredSize(new Dimension(0, 150));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setOpaque(false);

        JButton backButton = createBackButton();
        leftPanel.add(backButton);

        JPanel titleSection = new JPanel();
        titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
        titleSection.setOpaque(false);

        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        titleSection.add(titleLabel);
        titleSection.add(subtitleLabel);

        leftPanel.add(titleSection);
        header.add(leftPanel, BorderLayout.WEST);

        JPanel statsPanel = createStatsPanel();
        header.add(statsPanel, BorderLayout.EAST);

        return header;
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("← Indietro") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(255, 255, 255, 40));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 30));
                } else {
                    g2d.setColor(new Color(255, 255, 255, 20));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        backButton.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(e -> changePage("library"));

        return backButton;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        statsPanel.setOpaque(false);

        ratedStat = new StatItem("", "Valutati");
        statsPanel.add(ratedStat);


        reviewStat = new StatItem("", "Recensiti");
        statsPanel.add(reviewStat);

        return statsPanel;
    }





    private void filterBooks() {
        refreshBooksGrid();
    }

    private void refreshBooksGrid() {
        booksContainer.removeAll();

        booksContainer.setLayout(new GridLayout(0, 1, 15, 15));

        booksContainer.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = booksContainer.getWidth();
                int columns = 1;

                if (width > 1200) {
                    columns = 4;
                } else if (width > 900) {
                    columns = 3;
                } else if (width > 600) {
                    columns = 2;
                }

                booksContainer.setLayout(new GridLayout(0, columns, 15, 15));
                booksContainer.revalidate();
            }
        });
        if (libraryBooks != null){

        // Filter books based on current filter
        List<BookData> filteredBooks = new ArrayList<>();

        for (BookData book : libraryBooks) {
            boolean include = false;

            switch (currentFilter) {
                case "all":
                    include = true;
                    break;
                case "rated":
                    include = book.userRating > 0;
                    break;
                case "not_rated":
                    include = book.userRating == 0;
                    break;
                case "with_review":
                    include = book.hasReview;
                    break;
            }

            // Apply search filter if needed
            if (include && searchField != null && !searchField.getText().isEmpty()
                    && !searchField.getText().equals("Cerca nella libreria...")) {
                String searchText = searchField.getText().toLowerCase();
                include = book.title.toLowerCase().contains(searchText) ||
                        book.author.toLowerCase().contains(searchText);
            }

            if (include) {
                filteredBooks.add(book);
            }
        }

        // Add book cards
        for (BookData book : filteredBooks) {
            JPanel bookCard = new BookCard(book.title, book.author, book.genre, book.userRating > 0 ? book.userRating : book.avgRating);
            booksContainer.add(bookCard);
        }
        // TODO THIS PROBABLY DOES NOT WORK
        if (filteredBooks.isEmpty()) {
            JLabel emptyLabel = new JLabel("Nessun libro trovato");
            emptyLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 18));
            emptyLabel.setForeground(textSecondary);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            booksContainer.add(emptyLabel);
        }
            ratedCount = (int) libraryBooks.stream().filter(b -> b.userRating > 0).count();
            ratedStat.setLabelText(String.valueOf(ratedCount));


            reviewCount = (int) libraryBooks.stream().filter(b -> b.hasReview).count();
            reviewStat.setLabelText(String.valueOf(reviewCount));
        }


        booksContainer.revalidate();
        booksContainer.repaint();
    }

    private JPanel createEnhancedBookCard(BookData book) {
        JPanel card = createBookCard(book.title, book.author, book.genre,
                book.userRating > 0 ? book.userRating : book.avgRating);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        if (book.userRating > 0) {
            JLabel ratedIcon = new JLabel("✓");
            ratedIcon.setFont(new Font("SF Pro Text", Font.BOLD, 12));
            ratedIcon.setForeground(new Color(34, 197, 94)); // Green
            ratedIcon.setToolTipText("Hai valutato questo libro");
            statusPanel.add(ratedIcon);
        }

        if (book.hasReview) {
            JLabel reviewIcon = new JLabel("💬");
            reviewIcon.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
            reviewIcon.setToolTipText("Hai recensito questo libro");
            statusPanel.add(reviewIcon);
        }

        if (book.hasSuggestions) {
            JLabel suggestionIcon = new JLabel("💡");
            suggestionIcon.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
            suggestionIcon.setToolTipText("Hai suggerito libri correlati");
            statusPanel.add(suggestionIcon);
        }

        JButton actionButton = new JButton(book.userRating > 0 ? "Modifica valutazione" : "Valuta libro") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(primaryHover);
                } else if (getModel().isRollover()) {
                    g2d.setColor(primaryHover);
                } else {
                    g2d.setColor(primaryColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        actionButton.setFont(new Font("SF Pro Text", Font.BOLD, 12));
        actionButton.setForeground(Color.WHITE);
        actionButton.setPreferredSize(new Dimension(150, 30));
        actionButton.setBorder(null);
        actionButton.setFocusPainted(false);
        actionButton.setContentAreaFilled(false);
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        actionButton.addActionListener(e -> {
            changePage("bookDetail");
        });

        // Add components to card
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(statusPanel, BorderLayout.NORTH);
        bottomPanel.add(actionButton, BorderLayout.SOUTH);

        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    // TODO EXTRACT CLASS
    private JPanel createBookCard(String title, String author, String genre, float rating) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);

                g2d.dispose();
            }
        };

        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(220, 320));
        card.setMaximumSize(new Dimension(220, 320));
        card.setMinimumSize(new Dimension(200, 300));

        JPanel coverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, primaryColor, getWidth(), getHeight(), accentColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

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

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        return card;
    }

    private JScrollPane createScrollPane(JPanel contentPanel) {
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(backgroundColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        return scrollPane;
    }
}