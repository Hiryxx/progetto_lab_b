package pages;

import classes.MainFrame;
import classes.Page;
import components.ModernScrollBarUI;
import components.buttons.LibraryBookButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

import static classes.styles.Colors.*;

public class BookDetailsPage extends Page {
    private String bookTitle = "Il Nome della Rosa";
    private String bookAuthor = "Umberto Eco";
    private String bookCategory = "Narrativa Storica";
    private String bookDescription = "Un affascinante thriller medievale ambientato in un'abbazia benedettina nel 1327. Il frate francescano Guglielmo da Baskerville e il suo allievo Adso da Melk indagano su una serie di misteriose morti che scuotono la comunità monastica. Tra filosofia, teologia e investigazione, Eco ci conduce in un viaggio intellectuale attraverso il Medioevo, esplorando temi di conoscenza, fede e potere.";
    private String publicationDate = "Settembre 1980";
    private float bookRating = 4.7f;
    private String bookGenre = "Thriller Storico";
    private int totalReviews = 2847;

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

    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sfondo gradiente
                GradientPaint gradient = new GradientPaint(0, 0, gradientStart, getWidth(), 0, gradientEnd);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };

        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setPreferredSize(new Dimension(0, 80));

        // Pulsante indietro
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        JButton backButton = createBackButton();
        leftPanel.add(backButton);

        // Titolo pagina
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);

        JLabel pageTitle = new JLabel("Dettagli Libro");
        pageTitle.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        pageTitle.setForeground(Color.WHITE);
        centerPanel.add(pageTitle);

        // Azioni header
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        JButton favoriteButton = createHeaderIconButton("♥");
        JButton shareButton = createHeaderIconButton("↗");

        rightPanel.add(favoriteButton);
        rightPanel.add(shareButton);

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

                // Sfondo card
                g2d.setColor(new Color(0, 0, 0, 4));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);

                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 20, 20);

                // Bordo sottile
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 20, 20);

                g2d.dispose();
            }
        };

        mainSection.setLayout(new BorderLayout(30, 0));
        mainSection.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Copertina libro
        JPanel coverPanel = createBookCover();

        // Informazioni libro
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
        gbc.insets = new Insets(0, 0, 0, 0);

        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        JLabel categoryLabel = new JLabel(bookCategory.toUpperCase());
        categoryLabel.setFont(new Font("SF Pro Text", Font.BOLD, 12));
        categoryLabel.setForeground(primaryColor);
        infoPanel.add(categoryLabel, gbc);

        // Titolo
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 8, 0);
        JLabel titleLabel = new JLabel(bookTitle);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 32));
        titleLabel.setForeground(textPrimary);
        infoPanel.add(titleLabel, gbc);

        // Autore
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel authorLabel = new JLabel("di " + bookAuthor);
        authorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 18));
        authorLabel.setForeground(textSecondary);
        infoPanel.add(authorLabel, gbc);

        // Rating
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        JPanel ratingPanel = createRatingPanel();
        infoPanel.add(ratingPanel, gbc);

        // Data Pubblicazione e Genere
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 40);
        JPanel datePanel = createInfoItem("📅", "Data Pubblicazione:", publicationDate);
        infoPanel.add(datePanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        JPanel genrePanel = createInfoItem("🎭", "Genere:", bookGenre);
        infoPanel.add(genrePanel, gbc);

        // Add to library button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.weighty = 0.0;
        JButton addButton = new LibraryBookButton("+ Aggiungi alla Libreria");

        infoPanel.add(addButton, gbc);

        // Spaziatore per spingere tutto in alto
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        infoPanel.add(Box.createVerticalGlue(), gbc);

        return infoPanel;
    }

    private JPanel createInfoItem(String icon, String label, String value) {
        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
        item.setOpaque(false);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header con icona e label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 14));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        labelText.setForeground(textSecondary);
        labelText.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));

        headerPanel.add(iconLabel);
        headerPanel.add(labelText);

        // Valore
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        valueLabel.setForeground(textPrimary);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        item.add(headerPanel);
        item.add(valueLabel);

        return item;
    }

    private JPanel createRatingPanel() {
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ratingPanel.setOpaque(false);

        // Stelle
        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        starsPanel.setOpaque(false);

        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel("★");
            star.setFont(new Font("Apple Color Emoji", Font.PLAIN, 20));

            if (i <= bookRating) {
                star.setForeground(Color.YELLOW);
            } else {
                star.setForeground(new Color(200, 200, 200));
            }

            starsPanel.add(star);
        }

        // Rating numerico
        JLabel ratingValue = new JLabel(String.format("%.1f", bookRating));
        ratingValue.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        ratingValue.setForeground(textPrimary);
        ratingValue.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        // Numero recensioni
        JLabel reviewsCount = new JLabel("(" + totalReviews + " recensioni)");
        reviewsCount.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        reviewsCount.setForeground(textSecondary);

        ratingPanel.add(starsPanel);
        ratingPanel.add(ratingValue);
        ratingPanel.add(reviewsCount);

        return ratingPanel;
    }

    private JPanel createDescriptionSection() {
        return createSectionCard("📝 Descrizione", () -> {
            JPanel descPanel = new JPanel();
            descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
            descPanel.setOpaque(false);

            JTextArea descriptionArea = new JTextArea(bookDescription);
            descriptionArea.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
            descriptionArea.setForeground(textPrimary);
            descriptionArea.setOpaque(false);
            descriptionArea.setEditable(false);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

            descPanel.add(descriptionArea);
            return descPanel;
        });
    }

    private JPanel createDetailsSection() {
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
            detailsGrid.add(createDetailItem("Valutazione Media", String.format("%.1f/5.0", bookRating), "⭐"), gbc);

            gbc.gridx = 1; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 20, 0);
            detailsGrid.add(createDetailItem("Totale Recensioni", String.valueOf(totalReviews) + " recensioni", "💬"), gbc);

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

        button.addActionListener(e -> changePage("home"));

        return button;
    }

    private JButton createHeaderIconButton(String icon) {
        JButton button = new JButton(icon) {
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
        button.setFont(new Font("SF Pro Text", Font.PLAIN, 18));
        button.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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