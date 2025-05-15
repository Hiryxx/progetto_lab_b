package pages;

import classes.MainFrame;
import classes.Page;
import components.BookCard;
import components.buttons.StyledButton;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePage extends Page {
    private JTextField searchField;
    private Color primaryColor = new Color(25, 118, 210);    // Blue
    private Color accentColor = new Color(245, 124, 0);      // Orange
    private Color backgroundColor = new Color(245, 245, 250); // Light gray
    private Color cardColor = new Color(255, 255, 255);      // White

    public HomePage(MainFrame mainFrame) {
        super(mainFrame);
        this.render();
    }

    @Override
    public void render() {
        // Set background color for the entire panel
        this.setBackground(backgroundColor);

        // Create main layout for home page
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel for app name and search bar
        JPanel topPanel = createTopPanel();
        this.add(topPanel, BorderLayout.NORTH);

        // Center panel with main content
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(backgroundColor);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Featured books section
        contentPanel.add(createFeaturedBooksPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Categories section
        contentPanel.add(createCategoriesPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Recent books section
        contentPanel.add(createRecentBooksPanel());

        // Put content in a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(backgroundColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Customize the scrollbar
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        this.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for navigation
        JPanel bottomPanel = createBottomNavigationPanel();
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // App title with logo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(backgroundColor);

        // Logo (book icon)
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(createBookIcon(40, 40));
        titlePanel.add(logoLabel);

        // App title
        JLabel titleLabel = new JLabel("Book Recommender");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(primaryColor);
        titlePanel.add(titleLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(backgroundColor);

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton searchButton = new StyledButton("Search", primaryColor, Color.WHITE);
        //searchButton.addActionListener(e -> handleSearch());

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFeaturedBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(backgroundColor);

        // Section title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(backgroundColor);

        JLabel sectionTitle = new JLabel("Featured Books");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sectionTitle.setForeground(primaryColor);
        titlePanel.add(sectionTitle);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Book cards
        JPanel booksPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        booksPanel.setBackground(backgroundColor);

        for (int i = 1; i <= 4; i++) {
            booksPanel.add(new BookCard( "Featured Book " + i, "Author " + i,
                    "Genre " + i, 4.5f - (i * 0.3f) % 2));
        }

        panel.add(booksPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(backgroundColor);

        // Section title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(backgroundColor);

        JLabel sectionTitle = new JLabel("Browse by Category");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sectionTitle.setForeground(primaryColor);
        titlePanel.add(sectionTitle);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Categories
        String[] categories = {"Fiction", "Non-Fiction", "Science", "History",
                "Biography", "Fantasy", "Mystery", "Self-Help"};

        JPanel categoriesPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        categoriesPanel.setBackground(backgroundColor);

        Color[] categoryColors = {
                new Color(233, 30, 99),   // Pink
                new Color(156, 39, 176),  // Purple
                new Color(33, 150, 243),  // Blue
                new Color(0, 150, 136),   // Teal
                new Color(255, 193, 7),   // Amber
                new Color(103, 58, 183),  // Deep Purple
                new Color(76, 175, 80),   // Green
                new Color(255, 87, 34)    // Deep Orange
        };

        for (int i = 0; i < categories.length; i++) {
            JPanel categoryCard = createCategoryCard(categories[i], categoryColors[i]);
            //categoryButton.addActionListener(e -> handleCategoryClick(category));
            categoriesPanel.add(categoryCard);
        }

        panel.add(categoriesPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCategoryCard(String category, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel label = new JLabel(category);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(label, BorderLayout.CENTER);

        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(color.darker());
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(color);
            }
        });

        return card;
    }

    private JPanel createRecentBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(backgroundColor);

        // Section title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(backgroundColor);

        JLabel sectionTitle = new JLabel("Recently Added");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sectionTitle.setForeground(primaryColor);
        titlePanel.add(sectionTitle);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Book cards
        JPanel booksPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        booksPanel.setBackground(backgroundColor);

        for (int i = 1; i <= 4; i++) {
            booksPanel.add(new BookCard("Recent Book " + i, "Author " + i,
                    "Genre " + ((i + 2) % 5 + 1), 4.0f + (i * 0.2f) % 1));
        }

        panel.add(booksPanel, BorderLayout.CENTER);
        return panel;
    }





    private JPanel createBottomNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton homeButton = createNavButton("Home", "home", primaryColor);
        JButton libraryButton = createNavButton("My Library", "library", Color.DARK_GRAY);
        JButton recommendationsButton = createNavButton("Recommendations", "recommend", Color.DARK_GRAY);
        JButton profileButton = createNavButton("Profile", "profile", Color.DARK_GRAY);

        // Disable home button since we're already on home page
        homeButton.setEnabled(true);

        // Add action listeners
          /*
        libraryButton.addActionListener(e -> navigateToLibrary());
        recommendationsButton.addActionListener(e -> navigateToRecommendations());
        profileButton.addActionListener(e -> navigateToProfile());

           */

        panel.add(homeButton);
        panel.add(libraryButton);
        panel.add(recommendationsButton);
        panel.add(profileButton);

        return panel;
    }

    private JButton createNavButton(String text, String iconName, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(null);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Add icon based on name (you'd need to implement these icons)
        button.setIcon(createNavIcon(iconName, textColor));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setForeground(primaryColor);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setForeground(textColor);
                }
            }
        });

        return button;
    }

    private Icon createNavIcon(String name, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);

                int size = getIconWidth();

                switch (name) {
                    case "home":
                        // Home icon
                        int[] homeX = {x + size/2, x + size, x + size*3/4, x + size*3/4, x + size/4, x + size/4, x};
                        int[] homeY = {y, y + size/2, y + size/2, y + size, y + size, y + size/2, y + size/2};
                        g2d.fillPolygon(homeX, homeY, homeX.length);
                        break;
                    case "library":
                        // Library/Books icon
                        g2d.fillRect(x, y + size/4, size*3/4, size*3/4);
                        g2d.fillRect(x + size/4, y, size*3/4, size*3/4);
                        break;
                    case "recommend":
                        // Star icon for recommendations
                        Polygon star = new Polygon();
                        int centerX = x + size / 2;
                        int centerY = y + size / 2;
                        int outerRadius = size / 2;
                        int innerRadius = outerRadius / 2;

                        for (int i = 0; i < 10; i++) {
                            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
                            double angle = Math.PI / 2 + i * Math.PI / 5;
                            int pointX = (int) (centerX + radius * Math.cos(angle));
                            int pointY = (int) (centerY - radius * Math.sin(angle));
                            star.addPoint(pointX, pointY);
                        }
                        g2d.fill(star);
                        break;
                    case "profile":
                        // Profile icon
                        g2d.fillOval(x + size/4, y, size/2, size/2);
                        g2d.fillArc(x, y + size/3, size, size, 0, 180);
                        break;
                }

                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 24;
            }

            @Override
            public int getIconHeight() {
                return 24;
            }
        };
    }

    private Icon createBookIcon(int width, int height) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Book body
                g2d.setColor(primaryColor);
                g2d.fillRect(x, y + height/5, width*3/4, height*4/5);

                // Book pages
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x + width*3/4, y + height/5, width/20, height*4/5);

                // Book cover
                g2d.setColor(accentColor);
                g2d.fillRect(x + width*3/4 + width/20, y + height/5, width/5, height*4/5);

                // Book binding details
                g2d.setColor(new Color(50, 50, 50));
                g2d.setStroke(new BasicStroke(2));
                for (int i = 1; i < 4; i++) {
                    int lineY = y + height/5 + i * height/6;
                    g2d.drawLine(x, lineY, x + width*3/4, lineY);
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

    /*
    // Handler methods
    private void handleSearch() {
        String query = searchField.getText();
        if (query != null && !query.trim().isEmpty()) {
            // Create search results page and navigate to it
            SearchResultsPage searchPage = new SearchResultsPage(
                    (MainFrame)SwingUtilities.getWindowAncestor(this), query);
            ((MainFrame)SwingUtilities.getWindowAncestor(this)).addPage("search", searchPage);
            this.changePage("search");
        }
    }

    private void handleCategoryClick(String category) {
        // Create category page and navigate to it
        CategoryPage categoryPage = new CategoryPage(
                (MainFrame)SwingUtilities.getWindowAncestor(this), category);
        ((MainFrame)SwingUtilities.getWindowAncestor(this)).addPage("category_" + category, categoryPage);
        this.changePage("category_" + category);
    }

    private void openBookDetails(String bookTitle) {
        // Create book details page and navigate to it
        BookDetailsPage detailsPage = new BookDetailsPage(
                (MainFrame)SwingUtilities.getWindowAncestor(this), bookTitle);
        ((MainFrame)SwingUtilities.getWindowAncestor(this)).addPage("book_" + bookTitle, detailsPage);
        this.changePage("book_" + bookTitle);
    }

    private void navigateToLibrary() {
        LibraryPage libraryPage = new LibraryPage((MainFrame)SwingUtilities.getWindowAncestor(this));
        ((MainFrame)SwingUtilities.getWindowAncestor(this)).addPage("library", libraryPage);
        this.changePage("library");
    }

    private void navigateToRecommendations() {
        RecommendationsPage recommendationsPage = new RecommendationsPage((MainFrame)SwingUtilities.getWindowAncestor(this));
        ((MainFrame)SwingUtilities.getWindowAncestor(this)).addPage("recommendations", recommendationsPage);
        this.changePage("recommendations");
    }

    private void navigateToProfile() {
        ProfilePage profilePage = new ProfilePage((MainFrame)SwingUtilities.getWindowAncestor(this));
        ((MainFrame)SwingUtilities.getWindowAncestor(this)).addPage("profile", profilePage);
        this.changePage("profile");
    }

     */
}

// Custom Modern ScrollBar UI
class ModernScrollBarUI extends BasicScrollBarUI {
    private final int THUMB_SIZE = 8;

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(new Color(240, 240, 240));
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        JScrollBar sb = (JScrollBar) c;
        g2.setColor(sb.isEnabled() ? new Color(180, 180, 180) : new Color(200, 200, 200));

        if (sb.getOrientation() == JScrollBar.VERTICAL) {
            g2.fillRoundRect(thumbBounds.x + (thumbBounds.width - THUMB_SIZE) / 2,
                    thumbBounds.y, THUMB_SIZE, thumbBounds.height, THUMB_SIZE, THUMB_SIZE);
        } else {
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y + (thumbBounds.height - THUMB_SIZE) / 2,
                    thumbBounds.width, THUMB_SIZE, THUMB_SIZE, THUMB_SIZE);
        }

        g2.dispose();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        scrollbar.repaint();
    }
}

