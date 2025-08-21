package pages;

import classes.MainFrame;
import classes.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import components.ModernScrollBarUI;
import components.cards.BookCard;
import components.panels.StatItem;
import connection.SocketConnection;
import data.BookData;
import data.LibraryData;
import json.JsonObject;
import json.JsonUtil;
import state.LibraryDetailState;
import state.UserState;

import javax.swing.*;
import java.awt.*;

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


    public LibraryDetailPage() {
        super();
        this.render();
    }


    private void initializeBooks() {
        libraryBooks = new ArrayList<>();

        SocketConnection sc = MainFrame.getSocketConnection();
        JsonObject libraryDetail = new JsonObject();
        libraryDetail.put("id", LibraryDetailState.libraryId);

        System.out.println("Requesting library books with id " + LibraryDetailState.libraryId);
        sc.send("GET_LIBRARY_BOOKS", libraryDetail, UserState.cf);

        List<String> books = sc.receiveUntilStop();

        for (String bookJson : books){
            if (bookJson.startsWith("ERROR:")) {
                System.err.println("Error fetching libraries: " + bookJson.substring(6));
                return;
            }

            BookData book;

            try {
                book = JsonUtil.fromString(bookJson, BookData.class);
                System.out.println("Parsed book: " + book.getTitle());

                libraryBooks.add(book);
            } catch (JsonProcessingException e) {
                System.out.println("Error parsing library JSON: " + e.getMessage());
            }

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

/*
        refreshBooksGrid();
*/

        // Scroll pane for books
        JScrollPane scrollPane = createScrollPane(booksContainer);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        this.add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        titleLabel.setText("📚 " + LibraryDetailState.libraryName);
        initializeBooks();

    /*    refreshBooksGrid();*/
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
     /*   refreshBooksGrid();*/
    }

   /* private void refreshBooksGrid() {
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
            ratedStat.setLabelText("Valutati");
            ratedStat.setValueText(String.valueOf(ratedCount));


            reviewCount = (int) libraryBooks.stream().filter(b -> b.hasReview).count();
            reviewStat.setLabelText("Recensiti");
            reviewStat.setValueText(String.valueOf(reviewCount));
        }


        booksContainer.revalidate();
        booksContainer.repaint();
    }*/


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