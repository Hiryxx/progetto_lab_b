package components.cards;

import classes.MainFrame;
import data.BookData;
import state.BooksState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static classes.styles.Colors.*;

public class BookCard extends JPanel {
    private BookData book;

    private Color gradientStart;
    private Color gradientEnd;


    private void generateRandomGradient() {
        Color[][] gradientPairs = {
                // Purple to Pink
                {new Color(139, 92, 246), new Color(236, 72, 153)},
                // Blue to Purple
                {new Color(59, 130, 246), new Color(147, 51, 234)},
                // Green to Blue
                {new Color(34, 197, 94), new Color(59, 130, 246)},
                // Orange to Red
                {new Color(251, 146, 60), new Color(239, 68, 68)},
                // Teal to Blue
                {new Color(20, 184, 166), new Color(59, 130, 246)},
                // Pink to Orange
                {new Color(236, 72, 153), new Color(251, 146, 60)},
                // Indigo to Purple
                {new Color(99, 102, 241), new Color(139, 92, 246)},
                // Amber to Orange
                {new Color(245, 158, 11), new Color(251, 146, 60)},
                // Violet to Pink
                {new Color(167, 139, 250), new Color(251, 207, 232)},
                // Cyan to Teal
                {new Color(34, 211, 238), new Color(20, 184, 166)},
                // Lime to Green
                {new Color(132, 204, 22), new Color(34, 197, 94)},
                // Rose to Red
                {new Color(251, 113, 133), new Color(239, 68, 68)}
        };

        int randomIndex = (int) (Math.random() * gradientPairs.length);
        gradientStart = gradientPairs[randomIndex][0];
        gradientEnd = gradientPairs[randomIndex][1];
    }

    public BookCard(BookData book,  float rating) {
        generateRandomGradient();

        this.book = book;
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

                GradientPaint gradient = new GradientPaint(0, 0, gradientStart, getWidth(), getHeight(), gradientEnd);
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

        JLabel titleLabel = new JLabel(book.getTitle());
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        titleLabel.setForeground(textPrimary);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel authorLabel = new JLabel("di " + book.getAuthors());
        authorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        authorLabel.setForeground(textSecondary);
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        authorLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        JLabel genreLabel = new JLabel(book.getCategories());
        genreLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 11));
        genreLabel.setForeground(primaryColor);
        genreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        genreLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));


        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(genreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        this.add(coverPanel, BorderLayout.CENTER);
        this.add(infoPanel, BorderLayout.SOUTH);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                BooksState.bookDetail = book;
                MainFrame.showPage("bookDetails");
                System.out.println("Cliccato sul libro: " + book.getTitle());
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
