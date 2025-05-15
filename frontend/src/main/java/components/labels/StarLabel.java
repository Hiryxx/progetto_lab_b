package components.labels;

import javax.swing.*;
import java.awt.*;

public class StarLabel extends JLabel {
    private boolean filled;
    private boolean half;

    public StarLabel(boolean filled, boolean half) {
        this.filled = filled;
        this.half = half;
        setPreferredSize(new Dimension(15, 15));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 2;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        // Draw star shape
        Polygon starShape = createStarShape(x, y, size);

        if (filled) {
            g2d.setColor(new Color(255, 193, 7)); // Gold
            g2d.fill(starShape);
        } else if (half) {
            // For half star, clip to the left half
            g2d.setClip(0, 0, getWidth() / 2, getHeight());
            g2d.setColor(new Color(255, 193, 7)); // Gold
            g2d.fill(starShape);

            // Reset clip and draw the right half empty
            g2d.setClip(null);
            g2d.setClip(getWidth() / 2, 0, getWidth() / 2, getHeight());
            g2d.setColor(new Color(220, 220, 220)); // Light gray
            g2d.fill(starShape);
        } else {
            g2d.setColor(new Color(220, 220, 220)); // Light gray
            g2d.fill(starShape);
        }

        g2d.setClip(null);
        g2d.setColor(new Color(200, 200, 200));
        g2d.draw(starShape);
    }

    private Polygon createStarShape(int x, int y, int size) {
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

        return star;
    }
}
