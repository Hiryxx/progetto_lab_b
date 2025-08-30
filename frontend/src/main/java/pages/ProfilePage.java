package pages;

import classes.Page;
import components.ModernScrollBarUI;
import components.cards.BookCard;
import data.FavouriteCategoryBook;
import state.BooksState;
import state.LibraryDetailState;
import state.LibraryState;
import state.UserState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static classes.styles.Colors.*;

public class ProfilePage extends Page {
    private JLabel nameLabel = new JLabel();
    private JLabel emailLabel = new JLabel();

    public ProfilePage() {
        super();
        this.render();
    }

    @Override
    public void render() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = createProfileHeader();
        this.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(backgroundColor);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        contentPanel.add(createFavoriteBooksPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JScrollPane scrollPane = createScrollPane(contentPanel);
        this.add(scrollPane, BorderLayout.CENTER);

    }

    @Override
    public void refresh() {
        nameLabel.setText(UserState.name + " " + UserState.lastname);
        emailLabel.setText(UserState.email);

        if (UserState.cf != null && !UserState.cf.isEmpty()) {
            loadFavoriteBooks();
        }
    }

    private void loadFavoriteBooks() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<FavouriteCategoryBook> profileBooks = BooksState.fetchProfileBooks();

                updateFavoriteBooksContainer(profileBooks);

            } catch (Exception e) {
                System.err.println("Error loading favorite books: " + e.getMessage());
                updateFavoriteBooksContainer(null);
            }
        });
    }

    private void updateFavoriteBooksContainer(List<FavouriteCategoryBook> profileBooks) {
        Component[] components = ((JPanel) ((JScrollPane) this.getComponent(1)).getViewport().getView()).getComponents();

        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                // Look for the panel with the favorite books title
                if (panel.getLayout() instanceof BorderLayout) {
                    Component north = ((BorderLayout) panel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
                    if (north instanceof JPanel) {
                        Component[] headerComps = ((JPanel) north).getComponents();
                        if (headerComps.length > 0 && headerComps[0] instanceof JPanel) {
                            Component[] titleComps = ((JPanel) headerComps[0]).getComponents();
                            if (titleComps.length > 0 && titleComps[0] instanceof JLabel) {
                                JLabel titleLabel = (JLabel) titleComps[0];
                                if ("❤️ La Tua Categoria Preferita".equals(titleLabel.getText())) {
                                    updateFavoriteBooksContent(panel, profileBooks);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateFavoriteBooksContent(JPanel favoriteBooksPanel, List<FavouriteCategoryBook> profileBooks) {
        Component center = ((BorderLayout) favoriteBooksPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (center != null) {
            favoriteBooksPanel.remove(center);
        }

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        if (profileBooks == null) {
            JLabel errorLabel = new JLabel("Errore nel caricamento dei libri preferiti");
            errorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
            errorLabel.setForeground(textSecondary);
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            container.add(errorLabel, BorderLayout.CENTER);
        } else if (profileBooks.isEmpty()) {
            JLabel noBooks = new JLabel("Aggiungi libri alle tue librerie per vedere i tuoi preferiti!");
            noBooks.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
            noBooks.setForeground(textSecondary);
            noBooks.setHorizontalAlignment(SwingConstants.CENTER);
            container.add(noBooks, BorderLayout.CENTER);
        } else {
            FavouriteCategoryBook firstBook = profileBooks.getFirst();
            String favoriteCategory = firstBook.getFavoriteCategory();
            int categoryBookCount = firstBook.getCategoryBookCount();

            JLabel categoryLabel = new JLabel(String.format(
                    "Hai %d libro/i di %s nelle tue librerie - è la tua categoria preferita!",
                    categoryBookCount, favoriteCategory));
            categoryLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 14));
            categoryLabel.setForeground(primaryColor);
            categoryLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));
            container.add(categoryLabel, BorderLayout.NORTH);

            JPanel booksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            booksPanel.setOpaque(false);
            booksPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

            for (FavouriteCategoryBook book : profileBooks) {
                JPanel bookWrapper = new BookCard(book);
                booksPanel.add(bookWrapper);
            }

            JScrollPane horizontalScrollPane = new JScrollPane(booksPanel) {
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
                    super.paintComponent(g);
                }
            };

            horizontalScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            horizontalScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            horizontalScrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
            horizontalScrollPane.setBorder(null);
            horizontalScrollPane.setOpaque(false);
            horizontalScrollPane.getViewport().setOpaque(false);

            container.add(horizontalScrollPane, BorderLayout.CENTER);
        }

        favoriteBooksPanel.add(container, BorderLayout.CENTER);
        favoriteBooksPanel.revalidate();
        favoriteBooksPanel.repaint();
    }

    private JPanel createProfileHeader() {
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
        header.setPreferredSize(new Dimension(0, 180));

        JPanel userInfoPanel = new JPanel(new BorderLayout(20, 0));
        userInfoPanel.setOpaque(false);

        JPanel avatarPanel = createAvatarPanel();
        userInfoPanel.add(avatarPanel, BorderLayout.WEST);

        JPanel textInfoPanel = new JPanel();
        textInfoPanel.setLayout(new BoxLayout(textInfoPanel, BoxLayout.Y_AXIS));
        textInfoPanel.setOpaque(false);

        nameLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        emailLabel.setForeground(new Color(255, 255, 255, 180));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        textInfoPanel.add(nameLabel);
        textInfoPanel.add(emailLabel);

        userInfoPanel.add(textInfoPanel, BorderLayout.CENTER);

        JPanel actionPanel = createHeaderActionPanel();
        userInfoPanel.add(actionPanel, BorderLayout.EAST);

        header.add(userInfoPanel, BorderLayout.CENTER);
        return header;
    }

    private JPanel createAvatarPanel() {
        JPanel avatarContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.WHITE);
                g2d.fillOval(5, 5, 90, 90);

                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawOval(5, 5, 90, 90);

                g2d.setFont(new Font("SF Pro Text", Font.PLAIN, 50));
                g2d.setColor(primaryColor);
                String userIcon = "👤";
                FontMetrics fm = g2d.getFontMetrics();
                int x = 5 + (90 - fm.stringWidth(userIcon)) / 2;
                int y = 5 + (90 + fm.getAscent()) / 2;
                g2d.drawString(userIcon, x, y - 5);

                g2d.dispose();
            }
        };
        avatarContainer.setPreferredSize(new Dimension(100, 100));
        avatarContainer.setOpaque(false);
        return avatarContainer;
    }

    private JPanel createHeaderActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);
        JButton logoutButton = createIconButton("➡️ Esci", new Color(255, 255, 255, 180));
        panel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            UserState.logout();
            LibraryState.libraries = new ArrayList<>();
            LibraryDetailState.libraryId = -1;
            LibraryDetailState.libraryName = null;

            changePage("home");
        });

        return panel;
    }

    private JButton createIconButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setForeground(color);
        button.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(primaryColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(color);
            }
        });
        return button;
    }



    private JPanel createFavoriteBooksPanel() {
        return createSectionPanel("❤️ La Tua Categoria Preferita",
                "I libri della categoria che ami di più", () -> {

                    JPanel container = new JPanel(new BorderLayout());
                    container.setOpaque(false);

                    // Show loading message initially
                    JLabel loadingLabel = new JLabel("Caricamento libri preferiti...");
                    loadingLabel.setFont(new Font("SF Pro Text", Font.ITALIC, 16));
                    loadingLabel.setForeground(textSecondary);
                    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    container.add(loadingLabel, BorderLayout.CENTER);

                    return container;
                });
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
        card.setPreferredSize(new Dimension(180, 260));
        card.setMaximumSize(new Dimension(180, 260));
        card.setMinimumSize(new Dimension(180, 260));

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

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                changePage("bookDetails");
            }
        });

        return card;
    }

    private JPanel createSectionPanel(String title, String subtitle, Supplier<Component> contentSupplier) {
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
}