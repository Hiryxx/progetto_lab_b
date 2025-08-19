package components.cards;

import classes.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static classes.styles.Colors.*;

public class BookCard extends JPanel {
    public BookCard(String title, String author, String genre, float rating) {
        this.setLayout(new BorderLayout(0, 10));
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        this.setPreferredSize(new Dimension(180, 260));
        this.setMaximumSize(new Dimension(180, 260));
        this.setMinimumSize(new Dimension(180, 260));

        JPanel coverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Copertina
                GradientPaint gradient = new GradientPaint(0, 0, primaryColor, getWidth(), getHeight(), accentColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Icona libro
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

        this.add(coverPanel, BorderLayout.CENTER);
        this.add(infoPanel, BorderLayout.SOUTH);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Quando si clicca, cambia la pagina del MainFrame
                MainFrame.showPage("bookDetails");
                System.out.println("Cliccato sul libro: " + title); // Log di verifica
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ombra this
        g2d.setColor(new Color(0, 0, 0, 8));
        g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 16, 16);

        // Sfondo this
        g2d.setColor(cardColor);
        g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);

        // Bordo
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);

        g2d.dispose();
    }
}
