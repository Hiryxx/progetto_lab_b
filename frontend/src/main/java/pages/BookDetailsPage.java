package pages;

import classes.MainFrame;
import classes.Page;
import components.ModernScrollBarUI;
import components.buttons.LibraryBookButton;
import components.panels.InfoItem;
import connection.Response;
import data.BookData;
import data.BookRatingData;
import data.LibraryData;
import state.BooksState;
import state.LibraryDetailState;
import state.LibraryState;
import state.UserState;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static classes.styles.Colors.*;

public class BookDetailsPage extends Page {
    private BookData bookData = new BookData("", 0, "", "", "");
    private LibraryBookButton addLibraryButton = new LibraryBookButton("+ Aggiungi alla Libreria");
    private boolean isBookInLibrary = false;
    private boolean isBookRated = false;
    private boolean isBookSuggested = false;

    private JLabel titleLabel = new JLabel();
    private JLabel authorLabel = new JLabel();
    private JLabel categoryLabel = new JLabel();
    private JTextArea descriptionArea = new JTextArea();

    private InfoItem datePanel;
    private InfoItem genrePanel;

    private JPanel ratingsPanel;
    private JPanel suggestionsPanel;
    private JButton addRatingButton;
    private JButton suggestBooksButton;

    public BookDetailsPage() {
        super();
        this.render();
    }

    @Override
    public void render() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));

        // Header con navigazione
        JPanel headerPanel = createHeaderPanel();
        this.add(headerPanel, BorderLayout.NORTH);

        // Contenuto principale con spaziatura migliorata
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(backgroundColor);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Sezione principale del libro
        contentPanel.add(createBookMainSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Descrizione
        contentPanel.add(createDescriptionSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Dettagli aggiuntivi
        contentPanel.add(createDetailsSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Scroll pane
        JScrollPane scrollPane = createScrollPane(contentPanel);
        this.add(scrollPane, BorderLayout.CENTER);

    }

    @Override
    public void refresh() {
        bookData = BooksState.getDetailBook();

        isBookInLibrary = LibraryDetailState.isBookInLibrary(bookData.getId());


        System.out.println("BOOK DETAILS REFRESHED: " + bookData);
        titleLabel.setText(bookData.getTitle());
        authorLabel.setText("di " + bookData.getAuthors());
        categoryLabel.setText(bookData.getCategories().toUpperCase());
        datePanel.setLabelValue(String.valueOf(bookData.getYear()));
        genrePanel.setLabelValue(bookData.getCategories());
        descriptionArea.setText(bookData.getDescription() != null ? bookData.getDescription() : "Nessuna descrizione disponibile.");

        if (UserState.isLoggedIn) {
            System.out.println("User is logged in, showing add to library button.");
            addLibraryButton.setVisible(true);

            isBookRated = LibraryDetailState.isbBookRated(bookData.getId());
            isBookSuggested = LibraryDetailState.isBookSuggested(bookData.getId());

            if (isBookInLibrary) {
                System.out.println("Book is already in library, disabling add button.");
                addLibraryButton.setText("✓ Già in Libreria");
                addLibraryButton.setEnabled(false);
            } else {
                addLibraryButton.setText("+ Aggiungi alla Libreria");
                addLibraryButton.setEnabled(true);
                System.out.println("Book is not in library, enabling add button." + isBookInLibrary);
            }
        } else {
            addLibraryButton.setVisible(false);
        }


            boolean showRatingButtons = UserState.isLoggedIn && isBookInLibrary;
            addRatingButton.setVisible(showRatingButtons);
            if (isBookRated){
                addRatingButton.setText("✓ Hai già valutato questo libro");
                addRatingButton.setEnabled(false);
            } else {
                addRatingButton.setText("⭐ Aggiungi Valutazione");
                addRatingButton.setEnabled(true);
            }


        if (suggestBooksButton != null) {
            boolean showSuggestButtons = UserState.isLoggedIn && isBookInLibrary;
            suggestBooksButton.setVisible(showSuggestButtons);
            if (isBookSuggested){
                suggestBooksButton.setText("✓ Hai già suggerito libri per questo libro");
                suggestBooksButton.setEnabled(false);
            } else {
                suggestBooksButton.setText("📚 Suggerisci Libri");
                suggestBooksButton.setEnabled(true);
            }
        }

        updateRatingsPanel();

        updateSuggestionsPanel();

        revalidate();
        repaint();
    }

    private void updateRatingsPanel() {

        ratingsPanel.removeAll();

        // Title
        JLabel ratingsTitle = new JLabel("Valutazioni Utenti");
        ratingsTitle.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        ratingsTitle.setForeground(textPrimary);
        ratingsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        ratingsPanel.add(ratingsTitle);
        ratingsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        BookRatingData ratings = BooksState.getBookRatings(bookData.getId());

        if (ratings == null) {
            JLabel noRatingsLabel = new JLabel("Nessuna valutazione disponibile per questo libro");
            noRatingsLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 14));
            noRatingsLabel.setForeground(textSecondary);
            noRatingsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            ratingsPanel.add(noRatingsLabel);
        } else {
            // Update with actual ratings data
            String[] categories = {"Stile", "Contenuto", "Gradevolezza", "Originalità", "Edizione", "Voto Finale"};
            double[] averages = {ratings.stile, ratings.contenuto,ratings.gradevolezza, ratings.originalita,ratings.edizione,ratings.votofinale};
            int totalReviews = ratings.rating_count;

            JLabel reviewsCountLabel = new JLabel("Basato su " + totalReviews + " recensione/i");
            reviewsCountLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
            reviewsCountLabel.setForeground(textSecondary);
            reviewsCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            ratingsPanel.add(reviewsCountLabel);
            ratingsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            for (int i = 0; i < categories.length; i++) {
                ratingsPanel.add(createRatingBar(categories[i], averages[i]));
                if (i < categories.length - 1) {
                    ratingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }

        ratingsPanel.revalidate();
        ratingsPanel.repaint();
    }

    private void updateSuggestionsPanel() {
        suggestionsPanel.removeAll();

        // Title
        JLabel suggestionsTitle = new JLabel("Libri Consigliati dagli Utenti");
        suggestionsTitle.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        suggestionsTitle.setForeground(textPrimary);
        suggestionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        suggestionsPanel.add(suggestionsTitle);
        suggestionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        List<BookData> suggestions = BooksState.getBookSuggestions(bookData.getId());

        if (suggestions.isEmpty()) {
            JLabel noSuggestionsLabel = new JLabel("Nessun libro consigliato dagli utenti");
            noSuggestionsLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 14));
            noSuggestionsLabel.setForeground(textSecondary);
            noSuggestionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            suggestionsPanel.add(noSuggestionsLabel);
        } else {

            for (BookData book : suggestions) {
                suggestionsPanel.add(createSuggestedBookItem(book.getTitle(), book.getAuthors(), book.getSuggestionCount()));
                suggestionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        suggestionsPanel.revalidate();
        suggestionsPanel.repaint();
    }

    private JPanel createHeaderPanel() {
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

        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setPreferredSize(new Dimension(0, 80));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        JButton backButton = createBackButton();
        leftPanel.add(backButton);


        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JLabel pageTitle = new JLabel("Dettagli Libro", SwingConstants.CENTER);
        pageTitle.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        pageTitle.setForeground(Color.WHITE);
        centerPanel.add(pageTitle, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createBookMainSection() {
        JPanel mainSection = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 4));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);

                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 20, 20);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 20, 20);

                g2d.dispose();
            }
        };

        mainSection.setLayout(new BorderLayout(30, 0));
        mainSection.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel coverPanel = createBookCover();

        JPanel infoPanel = createBookInfoPanel();

        mainSection.add(coverPanel, BorderLayout.WEST);
        mainSection.add(infoPanel, BorderLayout.CENTER);

        return mainSection;
    }

    private JPanel createBookCover() {
        JPanel coverContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombra copertina
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(8, 8, getWidth() - 8, getHeight() - 8, 16, 16);

                // Copertina
                GradientPaint gradient = new GradientPaint(0, 0, primaryColor, getWidth(), getHeight(), accentColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 16, 16);

                // Effetto libro
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(10, 10, getWidth() - 28, 4, 8, 8);
                g2d.fillRoundRect(10, 20, getWidth() - 28, 2, 4, 4);

                // Icona libro centrale
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("SF Pro Text", Font.BOLD, 48));
                FontMetrics fm = g2d.getFontMetrics();
                String bookIcon = "📖";
                int x = (getWidth() - fm.stringWidth(bookIcon)) / 2 - 4;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;
                g2d.drawString(bookIcon, x, y);

                g2d.dispose();
            }
        };

        coverContainer.setPreferredSize(new Dimension(220, 320));
        coverContainer.setMaximumSize(new Dimension(220, 320));

        return coverContainer;
    }

    private JPanel createBookInfoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        categoryLabel.setFont(new Font("SF Pro Text", Font.BOLD, 12));
        categoryLabel.setForeground(primaryColor);
        infoPanel.add(categoryLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 8, 0);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 32));
        titleLabel.setForeground(textPrimary);
        infoPanel.add(titleLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        authorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 18));
        authorLabel.setForeground(textSecondary);
        infoPanel.add(authorLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 40);
        datePanel = new InfoItem("📅", "Data Pubblicazione :", "");
        infoPanel.add(datePanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        genrePanel = new InfoItem("🎭", "Categoria: ", "");
        infoPanel.add(genrePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        addLibraryButton.setPreferredSize(new Dimension(250, 45));
        addLibraryButton.addActionListener(e -> {
            if (UserState.isLoggedIn) {

                JPopupMenu libraryPopup = new JPopupMenu() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        g2d.setColor(cardColor);
                        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                        g2d.dispose();
                    }
                };

                libraryPopup.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(borderColor, 1),
                        BorderFactory.createEmptyBorder(8, 0, 8, 0)
                ));
                libraryPopup.setBackground(cardColor);

                LibraryState.fetchLibraries();


                JLabel headerLabel = new JLabel("  Seleziona una libreria");
                headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
                headerLabel.setForeground(textSecondary);
                headerLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 8, 12));
                libraryPopup.add(headerLabel);
                libraryPopup.addSeparator();

                for (LibraryData lib : LibraryState.libraries) {
                    final String libName = lib.getName();
                    final int libId = lib.getId();

                    JMenuItem libraryItem = new JMenuItem(libName) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            if (getModel().isArmed()) {
                                Graphics2D g2d = (Graphics2D) g.create();
                                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 20));
                                g2d.fillRect(0, 0, getWidth(), getHeight());
                                g2d.dispose();
                            }
                            super.paintComponent(g);
                        }
                    };

                    libraryItem.setFont(new Font("SF Pro Text", Font.PLAIN, 15));
                    libraryItem.setForeground(textPrimary);
                    libraryItem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                    libraryItem.setBackground(cardColor);
                    libraryItem.setOpaque(true);

                    // Add icon
                    libraryItem.setIcon(new Icon() {
                        @Override
                        public void paintIcon(Component c, Graphics g, int x, int y) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2d.setColor(primaryColor);
                            g2d.fillRoundRect(x, y + 2, 12, 12, 4, 4);
                            g2d.setColor(Color.WHITE);
                            g2d.setFont(new Font("SF Pro Text", Font.BOLD, 8));
                            g2d.drawString("L", x + 4, y + 10);
                            g2d.dispose();
                        }

                        @Override
                        public int getIconWidth() {
                            return 16;
                        }

                        @Override
                        public int getIconHeight() {
                            return 16;
                        }
                    });

                    libraryItem.addActionListener(evt -> {
                        // Add book to selected library
                        Response response = LibraryState.addBookToLibrary(bookData, libId);
                        if (response.isError()) {
                            JOptionPane.showMessageDialog(
                                    BookDetailsPage.this,
                                    "Errore nell'aggiunta del libro: " + response.getResponseText(),
                                    "Errore",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            libraryPopup.setVisible(false);
                            return;
                        }

                        addLibraryButton.setText("✓ Aggiunto a " + libName);
                        addLibraryButton.setEnabled(false);

                        isBookInLibrary = true;
                        libraryPopup.setVisible(false);

                        refresh();


                        JOptionPane.showMessageDialog(
                                BookDetailsPage.this,
                                "Libro aggiunto a \"" + libName + "\"",
                                "Successo",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    });

                    libraryPopup.add(libraryItem);
                }

                libraryPopup.addSeparator();

                JMenuItem createNewItem = new JMenuItem("+ Crea nuova libreria") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        if (getModel().isArmed()) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
                            g2d.fillRect(0, 0, getWidth(), getHeight());
                            g2d.dispose();
                        }
                        super.paintComponent(g);
                    }
                };

                createNewItem.setFont(new Font("SF Pro Text", Font.BOLD, 15));
                createNewItem.setForeground(accentColor);
                createNewItem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                createNewItem.setBackground(cardColor);
                createNewItem.setOpaque(true);

                createNewItem.addActionListener(evt -> {
                    String newLibraryName = JOptionPane.showInputDialog(
                            BookDetailsPage.this,
                            "Nome della nuova libreria:",
                            "Crea nuova libreria",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (newLibraryName != null && !newLibraryName.trim().isEmpty()) {
                        // TODO: Create library and add book
                        // LibraryState.createLibraryAndAddBook(newLibraryName, bookData);
                        addLibraryButton.setText("✓ Aggiunto a " + newLibraryName);
                        addLibraryButton.setEnabled(false);
                        libraryPopup.setVisible(false);
                    }
                });

                libraryPopup.add(createNewItem);

                libraryPopup.show(addLibraryButton, 0, addLibraryButton.getHeight() + 5);

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Devi essere loggato per aggiungere libri alla tua libreria.",
                        "Non sei loggato",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        infoPanel.add(addLibraryButton, gbc);

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        infoPanel.add(Box.createVerticalGlue(), gbc);

        return infoPanel;
    }


    private JPanel createDescriptionSection() {
        return createSectionCard("📝 Descrizione", () -> {
            JPanel descPanel = new JPanel();
            descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
            descPanel.setOpaque(false);


            descriptionArea.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
            descriptionArea.setForeground(textPrimary);
            descriptionArea.setOpaque(false);
            descriptionArea.setEditable(false);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            descriptionArea.setLineWrap(true);

            descPanel.add(descriptionArea);
            return descPanel;
        });
    }

    private JPanel createDetailsSection() {
        return createSectionCard("📊 Valutazioni e Consigli", () -> {
            JPanel mainContainer = new JPanel();
            mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
            mainContainer.setOpaque(false);

            // Ratings section
            ratingsPanel = createRatingsPanel();
            mainContainer.add(ratingsPanel);

            // Add rating button
            mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

            addRatingButton = new JButton("⭐ Aggiungi Valutazione");
            addRatingButton.setFont(new Font("SF Pro Text", Font.BOLD, 14));
            addRatingButton.setForeground(Color.BLACK);
            addRatingButton.setBackground(primaryColor);
            addRatingButton.setBorderPainted(false);
            addRatingButton.setFocusPainted(false);
            addRatingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addRatingButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            addRatingButton.setMaximumSize(new Dimension(250, 35));

            // Check visibility based on current state
            boolean canRateAndSuggest = UserState.isLoggedIn && isBookInLibrary;
            addRatingButton.setVisible(canRateAndSuggest);

            addRatingButton.addActionListener(e -> showRatingDialog());
            mainContainer.add(addRatingButton);

            // Separator
            mainContainer.add(Box.createRigidArea(new Dimension(0, 30)));
            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            mainContainer.add(separator);
            mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

            // Suggestions section
            suggestionsPanel = createSuggestionsPanel();
            mainContainer.add(suggestionsPanel);

            // Suggest books button
            mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));

            suggestBooksButton = new JButton("📚 Suggerisci Libri");
            suggestBooksButton.setFont(new Font("SF Pro Text", Font.BOLD, 14));
            suggestBooksButton.setForeground(Color.BLACK);
            suggestBooksButton.setBackground(accentColor);
            suggestBooksButton.setBorderPainted(false);
            suggestBooksButton.setFocusPainted(false);
            suggestBooksButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            suggestBooksButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            suggestBooksButton.setMaximumSize(new Dimension(200, 35));

            // Check visibility based on current state
            suggestBooksButton.setVisible(canRateAndSuggest);

            suggestBooksButton.addActionListener(e -> showSuggestBooksDialog());
            mainContainer.add(suggestBooksButton);

            return mainContainer;
        });
    }


    private JPanel createRatingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Title
        JLabel ratingsTitle = new JLabel("Valutazioni Utenti");
        ratingsTitle.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        ratingsTitle.setForeground(textPrimary);
        ratingsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(ratingsTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        return panel;
    }

    private JPanel createRatingBar(String category, double average) {
        JPanel barPanel = new JPanel(new BorderLayout(10, 0));
        barPanel.setOpaque(false);
        barPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        categoryLabel.setForeground(textPrimary);
        categoryLabel.setPreferredSize(new Dimension(100, 20));

        JPanel progressContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(200, 200, 200, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Fill based on average
                int fillWidth = (int) ((average / 5.0) * getWidth());
                GradientPaint gradient = new GradientPaint(0, 0, primaryColor, fillWidth, 0, accentColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, fillWidth, getHeight(), 10, 10);

                g2d.dispose();
            }
        };
        progressContainer.setPreferredSize(new Dimension(200, 20));

        JLabel avgLabel = new JLabel(String.format("%.1f/5.0", average));
        avgLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        avgLabel.setForeground(textPrimary);
        avgLabel.setPreferredSize(new Dimension(50, 20));

        barPanel.add(categoryLabel, BorderLayout.WEST);
        barPanel.add(progressContainer, BorderLayout.CENTER);
        barPanel.add(avgLabel, BorderLayout.EAST);

        return barPanel;
    }

    private JPanel createSuggestionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel suggestionsTitle = new JLabel("Libri Consigliati dagli Utenti");
        suggestionsTitle.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        suggestionsTitle.setForeground(textPrimary);
        suggestionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(suggestionsTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));


        return panel;
    }

    private JPanel createSuggestedBookItem(String title, String author, int suggestionCount) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setOpaque(false);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        titleLabel.setForeground(textPrimary);

        JLabel authorLabel = new JLabel(author);
        authorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        authorLabel.setForeground(textSecondary);

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);

        JLabel countLabel = new JLabel(suggestionCount + " utenti");
        countLabel.setFont(new Font("SF Pro Text", Font.BOLD, 12));
        countLabel.setForeground(primaryColor);

        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(countLabel, BorderLayout.EAST);

        // Make clickable
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // BooksState.searchBookByTitle(title);
            }
        });

        return itemPanel;
    }

    private void showRatingDialog() {
        // Create rating dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Valuta Libro", true);
        dialog.setLayout(new BorderLayout());
        int dialogWidth = (int) (MainFrame.mainFrame.getWidth() * 0.8);
        int dialogHeight = (int) (MainFrame.mainFrame.getHeight() * 0.7);
        dialog.setSize(Math.max(dialogWidth, 400), Math.max(dialogHeight, 500));
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] categories = {"Stile", "Contenuto", "Gradevolezza", "Originalità", "Edizione"};
        JSlider[] sliders = new JSlider[categories.length];

        for (int i = 0; i < categories.length; i++) {
            JLabel label = new JLabel(categories[i]);
            label.setFont(new Font("SF Pro Text", Font.BOLD, 14));
            contentPanel.add(label);

            sliders[i] = new JSlider(1, 5, 3);
            sliders[i].setMajorTickSpacing(1);
            sliders[i].setPaintTicks(true);
            sliders[i].setPaintLabels(true);
            sliders[i].setSnapToTicks(true);
            contentPanel.add(sliders[i]);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JLabel commentLabel = new JLabel("Recensione (opzionale, max 256 caratteri):");
        commentLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        contentPanel.add(commentLabel);

        JTextArea commentArea = new JTextArea(4, 40);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentArea);
        contentPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Annulla");
        JButton saveButton = new JButton("Salva Valutazione");

        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            BookRatingData ratingData = new BookRatingData();

            ratingData.bookid = bookData.getId();
            ratingData.stile = sliders[0].getValue();
            ratingData.contenuto = sliders[1].getValue();
            ratingData.gradevolezza = sliders[2].getValue();
            ratingData.originalita = sliders[3].getValue();
            ratingData.edizione = sliders[4].getValue();
            ratingData.recensione = commentArea.getText().length() > 256 ? commentArea.getText().substring(0, 256) : commentArea.getText();

            BooksState.saveBookRating(ratingData);
            dialog.dispose();
            refresh();
            JOptionPane.showMessageDialog(this, "Valutazione salvata con successo!");
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showSuggestBooksDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Suggerisci Libri", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel instructionLabel = new JLabel("Seleziona fino a 3 libri dalla tua libreria da consigliare:");
        instructionLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        List<BookData> userRecBooks = LibraryState.getRecommendableBooks(BooksState.bookDetail.getId());

        JPanel booksPanel = new JPanel();
        booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));
        booksPanel.setBackground(backgroundColor);

        // Create a list to store selected books
        List<BookData> selectedBooks = new ArrayList<>();

        for (BookData book : userRecBooks) {
            class SelectableBookPanel extends JPanel {
                private boolean isSelected = false;

                public SelectableBookPanel() {
                    super(new BorderLayout(15, 0));
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (isSelected) {
                        g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 20));
                        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                        g2d.setColor(primaryColor);
                        g2d.setStroke(new BasicStroke(2f));
                        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
                    } else {
                        g2d.setColor(cardColor);
                        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                        g2d.setColor(borderColor);
                        g2d.setStroke(new BasicStroke(1f));
                        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                    }
                    g2d.dispose();
                }

                public void setSelected(boolean selected) {
                    this.isSelected = selected;
                    repaint();
                }

                public boolean isSelected() {
                    return isSelected;
                }
            }

            // Create the selectable panel
            SelectableBookPanel bookItem = new SelectableBookPanel();
            bookItem.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            bookItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bookItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            bookItem.setOpaque(false); // Make it transparent so our custom painting shows

            // Book icon
            JLabel iconLabel = new JLabel("📚");
            iconLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 24));
            iconLabel.setPreferredSize(new Dimension(40, 40));

            // Book info
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);

            JLabel titleLabel = new JLabel(book.getTitle());
            titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 16));
            titleLabel.setForeground(textPrimary);

            JLabel authorLabel = new JLabel("di " + book.getAuthors());
            authorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
            authorLabel.setForeground(textSecondary);

            infoPanel.add(titleLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            infoPanel.add(authorLabel);

            JLabel checkLabel = new JLabel("○");
            checkLabel.setFont(new Font("SF Pro Text", Font.BOLD, 20));
            checkLabel.setForeground(textSecondary);

            bookItem.add(iconLabel, BorderLayout.WEST);
            bookItem.add(infoPanel, BorderLayout.CENTER);
            bookItem.add(checkLabel, BorderLayout.EAST);

            bookItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (bookItem.isSelected()) {
                        selectedBooks.remove(book);
                        bookItem.setSelected(false);
                        checkLabel.setText("○");
                        checkLabel.setForeground(textSecondary);
                    } else if (selectedBooks.size() < 3) {
                        selectedBooks.add(book);
                        bookItem.setSelected(true);
                        checkLabel.setText("●");
                        checkLabel.setForeground(primaryColor);
                    } else {
                        // Max reached
                        JOptionPane.showMessageDialog(dialog,
                                "Puoi selezionare al massimo 3 libri",
                                "Limite raggiunto",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            booksPanel.add(bookItem);
            booksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane booksScrollPane = new JScrollPane(booksPanel);
        booksScrollPane.setBorder(null);
        booksScrollPane.setBackground(backgroundColor);
        booksScrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        contentPanel.add(booksScrollPane);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton cancelButton = new JButton("Annulla");
        cancelButton.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        cancelButton.setForeground(textSecondary);
        cancelButton.setBackground(cardColor);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(100, 35));

        JButton suggestButton = new JButton("Suggerisci");
        suggestButton.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        suggestButton.setBackground(primaryColor);
        suggestButton.setBorderPainted(false);
        suggestButton.setFocusPainted(false);
        suggestButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        suggestButton.setPreferredSize(new Dimension(120, 35));

        cancelButton.addActionListener(e -> dialog.dispose());
        suggestButton.addActionListener(e -> {
            if (selectedBooks.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Seleziona almeno un libro da consigliare",
                        "Nessun libro selezionato",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            BooksState.suggestBooks(bookData.getId(), selectedBooks);
            dialog.dispose();
            JOptionPane.showMessageDialog(this,
                    "Suggerimenti salvati con successo!",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
            refresh();
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(suggestButton);

        dialog.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }




    private JPanel createSectionCard(String title, java.util.function.Supplier<JPanel> contentSupplier) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombra
                g2d.setColor(new Color(0, 0, 0, 4));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 16, 16);

                // Sfondo
                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 16, 16);

                // Bordo
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.dispose();
            }
        };

        card.setLayout(new BorderLayout(0, 20));
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Titolo sezione
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 22));
        titleLabel.setForeground(textPrimary);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(contentSupplier.get(), BorderLayout.CENTER);

        return card;
    }

    private JButton createBackButton() {
        JButton button = new JButton("← Indietro") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(255, 255, 255, 30));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 20));
                } else {
                    g2d.setColor(new Color(255, 255, 255, 10));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("SF Pro Text", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // TODO FIX GO BACK
        button.addActionListener(e -> changePage("home"));

        return button;
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
}