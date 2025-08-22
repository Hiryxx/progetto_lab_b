package pages;

import classes.MainFrame;
import classes.Page;
import components.ModernScrollBarUI;
import components.buttons.LibraryBookButton;
import components.panels.InfoItem;
import connection.Response;
import data.BookData;
import data.LibraryData;
import state.BooksState;
import state.LibraryState;
import state.UserState;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

import static classes.styles.Colors.*;

public class BookDetailsPage extends Page {
    private BookData bookData = new BookData("", 0, "", "", "");
    private LibraryBookButton addLibraryButton = new LibraryBookButton("+ Aggiungi alla Libreria");

    private JLabel titleLabel = new JLabel();
    private JLabel authorLabel = new JLabel();
    private JLabel categoryLabel = new JLabel();
    private JTextArea descriptionArea = new JTextArea();

    private InfoItem datePanel;
    private InfoItem genrePanel;

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
        System.out.println("BOOK DETAILS REFRESHED: " + bookData);
        titleLabel.setText(bookData.getTitle());
        authorLabel.setText("di " + bookData.getAuthors());
        categoryLabel.setText(bookData.getCategories().toUpperCase());
        datePanel.setLabelValue(String.valueOf(bookData.getYear()));
        genrePanel.setLabelValue(bookData.getCategories());
        descriptionArea.setText(bookData.getDescription() != null ? bookData.getDescription() : "Nessuna descrizione disponibile.");

        // TODO ALSO CHECK IF IN LIBRARY
        if (UserState.isLoggedIn){
            addLibraryButton.setVisible(true);
            //addLibraryButton.setBookData(bookData);
        } else {
            addLibraryButton.setVisible(false);
        }
        repaint();
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


        // Azioni header
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        /*JButton favoriteButton = createHeaderIconButton("♥");
        JButton shareButton = createHeaderIconButton("↗");

        rightPanel.add(favoriteButton);
        rightPanel.add(shareButton);*/

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

                        // Background with rounded corners
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

                for (LibraryData lib:  LibraryState.libraries) {
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
                        public int getIconWidth() { return 16; }

                        @Override
                        public int getIconHeight() { return 16; }
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
                        libraryPopup.setVisible(false);


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

        // Spaziatore per spingere tutto in alto
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
        // TODO MODIFY COMPLETELY
        return createSectionCard("ℹ️ Dettagli Aggiuntivi", () -> {
            JPanel detailsContainer = new JPanel(new BorderLayout());
            detailsContainer.setOpaque(false);

            // Grid organizzata per i dettagli
            JPanel detailsGrid = new JPanel(new GridBagLayout());
            detailsGrid.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 20, 30);

            // Prima riga
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
            detailsGrid.add(createDetailItem("Valutazione Media", String.format("%.1f/5.0", 2.0), "⭐"), gbc);

            gbc.gridx = 1; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 20, 0);
            detailsGrid.add(createDetailItem("Totale Recensioni", String.valueOf(0) + " recensioni", "💬"), gbc);

            // Seconda riga
            gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 20, 30);
            detailsGrid.add(createDetailItem("Casa Editrice", "Bompiani", "🏢"), gbc);

            gbc.gridx = 1; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 20, 0);
            detailsGrid.add(createDetailItem("Pagine", "624 pagine", "📄"), gbc);

            // Terza riga
            gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 0, 30);
            detailsGrid.add(createDetailItem("Lingua", "Italiano", "🌍"), gbc);

            gbc.gridx = 1; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 0, 0);
            detailsGrid.add(createDetailItem("ISBN", "978-8845292613", "🔖"), gbc);

            detailsContainer.add(detailsGrid, BorderLayout.CENTER);
            return detailsContainer;
        });
    }

    private JPanel createDetailItem(String label, String value, String icon) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setPreferredSize(new Dimension(200, 60));

        // Panel superiore con icona e label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 16));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("SF Pro Text", Font.BOLD, 12));
        labelText.setForeground(textSecondary);
        labelText.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        headerPanel.add(iconLabel);
        headerPanel.add(labelText);

        // Valore principale
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        valueLabel.setForeground(textPrimary);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        item.add(headerPanel, BorderLayout.NORTH);
        item.add(valueLabel, BorderLayout.CENTER);

        return item;
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