package pages;

import classes.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import components.cards.BookCard;
import components.ModernScrollBarUI;
import data.BookData;
import data.FilterData;
import state.BooksState;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

import static classes.styles.Colors.*;

public class HomePage extends Page {
    private JTextField searchField;
    private JTextField yearTextField;
    private JComboBox<FilterData> authorFilter;
    private JComboBox<FilterData> categoryFilter;
    private List<BookData> filteredBooks;

    private JPanel booksContainer;
    private JScrollPane mainScrollPane;

    public HomePage() {
        super();


        BooksState.fetchCategories();
        BooksState.fetchAuthors();

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                revalidate();
                repaint();
            }
        });
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

        if (BooksState.books == null || BooksState.books.isEmpty()) {
            try {
                BooksState.fetchBooks();
                filteredBooks = new ArrayList<>(BooksState.books);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errore durante il caricamento dei libri: " + e.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Use existing books if already loaded
            if (filteredBooks == null) {
                filteredBooks = new ArrayList<>(BooksState.books);
            }
        }

        contentPanel.add(createHeroSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        booksContainer = new JPanel();
        booksContainer.setLayout(new BorderLayout());
        booksContainer.setOpaque(false);
        booksContainer.add(createFeaturedBooksPanel());
        contentPanel.add(booksContainer);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        mainScrollPane = createScrollPane(contentPanel);
        this.add(mainScrollPane, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        updateBooksPanel();
    }


    private void updateBooksPanel() {
        if (booksContainer != null) {
            booksContainer.removeAll();

            booksContainer.add(createFeaturedBooksPanel());

            booksContainer.revalidate();
            booksContainer.repaint();

            // show top results
            if (mainScrollPane != null) {
                SwingUtilities.invokeLater(() -> {
                    mainScrollPane.getVerticalScrollBar().setValue(0);
                });
            }
        }
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

        filterBar.setLayout(new GridBagLayout());
        filterBar.setPreferredSize(new Dimension(0, 70));
        filterBar.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        JLabel filterLabel = new JLabel("Filtri:");
        filterLabel.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        filterLabel.setForeground(textPrimary);
        gbc.gridx = 0;
        gbc.weightx = 0;
        filterBar.add(filterLabel, gbc);

        JPanel yearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        yearPanel.setOpaque(false);
        JLabel yearLabel = new JLabel("📅 Anno:");
        yearLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        yearLabel.setForeground(textSecondary);
        yearPanel.add(yearLabel);

        yearTextField = createStyledTextField("es. 2024");
        yearPanel.add(yearTextField);
        gbc.gridx = 1;
        gbc.weightx = 0.15;
        filterBar.add(yearPanel, gbc);

        authorFilter = createStyledComboBox(BooksState.authors);
        gbc.gridx = 2;
        gbc.weightx = 0.25;
        filterBar.add(createFilterContainer("✍️ Autore", authorFilter), gbc);

        categoryFilter = createStyledComboBox(BooksState.categories);
        gbc.gridx = 3;
        gbc.weightx = 0.25;
        filterBar.add(createFilterContainer("📚 Categoria", categoryFilter), gbc);

        gbc.gridx = 4;
        gbc.weightx = 1.0;
        filterBar.add(Box.createHorizontalGlue(), gbc);

        JButton resetButton = createResetButton();
        gbc.gridx = 5;
        gbc.weightx = 0;
        filterBar.add(resetButton, gbc);

        JButton applyButton = createApplyButton();
        gbc.gridx = 6;
        gbc.weightx = 0;
        filterBar.add(applyButton, gbc);

        return filterBar;
    }


    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(10) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        textField.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        textField.setForeground(textSecondary);
        textField.setBackground(new Color(0, 0, 0, 0));
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        textField.setPreferredSize(new Dimension(100, 35));

        textField.setText(placeholder);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(textPrimary);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(textSecondary);
                    textField.setText(placeholder);
                }
            }
        });

        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume();
                }
            }
        });

        return textField;
    }

    private JPanel createFilterContainer(String label, JComboBox<FilterData> comboBox) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        container.setOpaque(false);

        JLabel filterLabel = new JLabel(label);
        filterLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        filterLabel.setForeground(textSecondary);

        container.add(filterLabel);
        container.add(comboBox);

        return container;
    }

    private void setupAutoComplete(final JComboBox<FilterData> comboBox, final FilterData[] allItems) {
        final JTextField textField = (JTextField) comboBox.getEditor().getEditorComponent();

        final boolean[] isUpdating = {false};

        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            private Timer filterTimer = new Timer(300, e -> filterItems());

            {
                filterTimer.setRepeats(false);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Don't filter on navigation keys
                if (e.getKeyCode() == KeyEvent.VK_DOWN ||
                        e.getKeyCode() == KeyEvent.VK_UP ||
                        e.getKeyCode() == KeyEvent.VK_ENTER ||
                        e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    return;
                }

                filterTimer.restart();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    comboBox.hidePopup();
                    filterTimer.stop();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

                    comboBox.hidePopup();
                    textField.setText(allItems[0].toString());
                    filterTimer.stop();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && !comboBox.isPopupVisible()) {
                    // Show all items when pressing down arrow
                    if (isUpdating[0]) return;
                    isUpdating[0] = true;
                    comboBox.removeAllItems();
                    for (FilterData item : allItems) {
                        comboBox.addItem(item);
                    }
                    comboBox.showPopup();
                    isUpdating[0] = false;
                }
            }

            private void filterItems() {
                if (isUpdating[0]) return;

                SwingUtilities.invokeLater(() -> {
                    if (isUpdating[0]) return;
                    isUpdating[0] = true;

                    String input = textField.getText();
                    String inputLower = input.toLowerCase();

                    String currentText = input;

                    List<FilterData> filteredItems = new ArrayList<>();

                    if (input.isEmpty()) {
                        filteredItems.addAll(List.of(allItems));
                    } else {
                        if (allItems[0].toString().toLowerCase().contains(inputLower)) {
                            filteredItems.add(allItems[0]);
                        }

                        for (int i = 1; i < allItems.length; i++) {
                            FilterData item = allItems[i];
                            if (item == null) continue;
                            if (item.toString().toLowerCase().contains(inputLower)) {
                                filteredItems.add(item);
                            }
                        }
                    }

                    comboBox.removeAllItems();
                    for (FilterData item : filteredItems) {
                        comboBox.addItem(item);
                    }

                    textField.setText(currentText);

                    if (!filteredItems.isEmpty() && !input.isEmpty()) {
                        comboBox.showPopup();
                    }

                    isUpdating[0] = false;
                });
            }
        });

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(textField::selectAll);
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        comboBox.addActionListener(e -> {
            if (!isUpdating[0] && e.getActionCommand().equals("comboBoxChanged")) {
                Object selected = comboBox.getSelectedItem();
                if (selected != null && !selected.toString().isEmpty()) {
                    textField.setText(selected.toString());
                }
            }
        });
    }

    private JComboBox<FilterData> createStyledComboBox(FilterData[] options) {
        JComboBox<FilterData> comboBox = new JComboBox<>(options) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
                g2d.dispose();
            }
        };

        comboBox.setEditable(true);

        setupAutoComplete(comboBox, options);

        comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        comboBox.setForeground(textPrimary);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        comboBox.setPreferredSize(new Dimension(180, 35));

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

        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
        editor.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        editor.setForeground(textPrimary);
        editor.setBorder(null);

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


    private void resetFilters() {
        yearTextField.setText("es. 2024");
        yearTextField.setForeground(textSecondary);

        if (authorFilter.getItemCount() > 0) {
            authorFilter.setSelectedIndex(0);
        }

        if (categoryFilter.getItemCount() > 0) {
            categoryFilter.setSelectedIndex(0);
        }

        searchField.setText("Cerca libri...");

        applyFilters();
    }

    private void applyFilters() {
        String yearText = yearTextField.getText();
        FilterData selectedAuthor = (FilterData) authorFilter.getEditor().getItem();
        FilterData selectedCategory = (FilterData) categoryFilter.getEditor().getItem();

        String textTitle = searchField.getText();

        if (textTitle.equals("Cerca libri...") || textTitle.isEmpty()) {
            textTitle = null;
        }


        try {
            BooksState.fetchFilteredBooks(textTitle, Objects.equals(yearText, "es. 2024") ? null : yearText, selectedAuthor.getId(), selectedCategory.getId());
            filteredBooks = BooksState.books;
        } catch (JsonProcessingException e) {
            System.out.println("Error fetching filtered books: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Errore durante il filtraggio dei libri: " + e.getMessage(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }


        updateBooksPanel();

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
        searchField.setText("Cerca libri...");
        searchField.setForeground(textSecondary);

        searchField.addActionListener(e -> {
            if (searchField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Per favore inserisci un termine di ricerca valido.",
                        "Errore di ricerca", JOptionPane.ERROR_MESSAGE);
                return;
            }
            applyFilters();
        });
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Cerca libri...")) {
                    searchField.setText("");
                    searchField.setForeground(textPrimary);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(textSecondary);
                    searchField.setText("Cerca libri...");
                }
            }

        });

        JButton searchButton = createIconButton("🔍");

        searchButton.addActionListener(e -> {
            if (searchField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Per favore inserisci un termine di ricerca valido.",
                        "Errore di ricerca", JOptionPane.ERROR_MESSAGE);
                return;
            }
            applyFilters();
        });

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
        return createSectionPanel("🔍 Risultati Ricerca", "Libri trovati con i filtri applicati", () -> {
            JPanel booksPanel = new JPanel();
            booksPanel.setOpaque(false);
            booksPanel.setLayout(new GridLayout(0, 1, 15, 15)); // Start with 1 column

            booksPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    updateHomeBooksGridColumns(booksPanel);
                }
            });

            List<BookData> booksToShow = filteredBooks != null ? filteredBooks : BooksState.books;

            if (booksToShow.isEmpty()) {
                JLabel noResultsLabel = new JLabel("Nessun libro trovato con i filtri selezionati");
                noResultsLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
                noResultsLabel.setForeground(textSecondary);
                booksPanel.add(noResultsLabel);
            } else {
                for (BookData book : booksToShow) {
                    JPanel bookWrapper = new BookCard(book);
                    booksPanel.add(bookWrapper);
                }
            }

            SwingUtilities.invokeLater(() -> {
                updateHomeBooksGridColumns(booksPanel);
                booksPanel.revalidate();
                booksPanel.repaint();
            });

            return booksPanel;
        });
    }

    private void updateHomeBooksGridColumns(JPanel booksPanel) {
        int width = booksPanel.getWidth();
        int columns = 1;

        if (width > 1200) {
            columns = 4;
        } else if (width > 900) {
            columns = 3;
        } else if (width > 600) {
            columns = 2;
        }

        booksPanel.setLayout(new GridLayout(0, columns, 15, 15));
        booksPanel.revalidate();
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