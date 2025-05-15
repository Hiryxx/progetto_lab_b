package components;

import classes.styles.DropShadowBorder;
import components.buttons.StyledButton;
import components.panels.RatingPanel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class BookCard extends JPanel {
    private String title;
    private String author;
    private String genre;
    private float rating;

    private Color cardColor = new Color(255, 255, 255);
    private Color accentColor = new Color(245, 124, 0);      // Orange


    public BookCard(String title, String author, String genre, float rating) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.rating = rating;
        render(); // todo for now
    }

    public void render() {
        System.out.println("Rendering BookCard: " + title);
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        card.setBackground(cardColor);

        // Add drop shadow
        card.setBorder(new DropShadowBorder(Color.BLACK, 5, 0.3f, 8, false, true, true, true));

        // Book cover (gradient colored panel as placeholder)
        JPanel coverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Generate a unique but consistent color based on the title
                int hash = title.hashCode();
                Color startColor = new Color(Math.abs(hash) % 200, Math.abs(hash / 10) % 200, Math.abs(hash / 100) % 200);
                Color endColor = startColor.darker();

                GradientPaint gradient = new GradientPaint(
                        0, 0, startColor,
                        0, getHeight(), endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add a book title to the cover
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));

                FontMetrics metrics = g2d.getFontMetrics();
                int titleWidth = metrics.stringWidth(title);
                int x = (getWidth() - titleWidth) / 2;
                int y = getHeight() / 2;

                g2d.drawString(title, x, y);
            }
        };
        coverPanel.setPreferredSize(new Dimension(150, 200));
        coverPanel.setMinimumSize(new Dimension(150, 200));
        coverPanel.setMaximumSize(new Dimension(150, 200));
        coverPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Details panel for title, author, etc.
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(cardColor);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Author
        JLabel authorLabel = new JLabel(author);
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        authorLabel.setForeground(Color.DARK_GRAY);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Genre
        JLabel genreLabel = new JLabel(genre);
        genreLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        genreLabel.setForeground(new Color(100, 100, 100));
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Rating
        JPanel ratingPanel = new RatingPanel(new FlowLayout(FlowLayout.CENTER, 2, 0), rating);
        ratingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ratingPanel.setBackground(cardColor);

        // View button
        JButton viewButton = new StyledButton("View Details", accentColor, Color.WHITE);
        viewButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //viewButton.addActionListener(e -> openBookDetails(title));


        // Add components to details panel
        detailsPanel.add(titleLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(authorLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(genreLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(ratingPanel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(viewButton);

        // Add components to card
        card.add(coverPanel);
        card.add(detailsPanel);

        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 245, 245));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(cardColor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                  /*
                openBookDetails(title);
                   */
            }
        });
        this.add(card);

    }
}
