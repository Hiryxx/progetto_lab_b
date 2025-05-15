package classes.styles;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class DropShadowBorder extends AbstractBorder {
    private Color shadowColor;
    private int shadowSize;
    private float shadowOpacity;
    private int cornerRadius;
    private boolean showTopShadow;
    private boolean showLeftShadow;
    private boolean showBottomShadow;
    private boolean showRightShadow;

    public DropShadowBorder(Color shadowColor, int shadowSize, float shadowOpacity,
                            int cornerRadius, boolean showTopShadow,
                            boolean showLeftShadow, boolean showBottomShadow,
                            boolean showRightShadow) {
        this.shadowColor = shadowColor;
        this.shadowSize = shadowSize;
        this.shadowOpacity = shadowOpacity;
        this.cornerRadius = cornerRadius;
        this.showTopShadow = showTopShadow;
        this.showLeftShadow = showLeftShadow;
        this.showBottomShadow = showBottomShadow;
        this.showRightShadow = showRightShadow;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int shadowAlpha = (int)(shadowOpacity * 255);
        Color shadow = new Color(
                shadowColor.getRed(),
                shadowColor.getGreen(),
                shadowColor.getBlue(),
                shadowAlpha);

        // Draw shadow
        for (int i = 0; i < shadowSize; i++) {
            int opacity = shadowAlpha * (shadowSize - i) / shadowSize / 2;
            Color currentShadow = new Color(
                    shadowColor.getRed(),
                    shadowColor.getGreen(),
                    shadowColor.getBlue(),
                    opacity);
            g2.setColor(currentShadow);

            // Bottom shadow
            if (showBottomShadow) {
                g2.fillRoundRect(x + shadowSize, y + height - shadowSize + i,
                        width - 2 * shadowSize, shadowSize,
                        cornerRadius, cornerRadius);
            }

            // Right shadow
            if (showRightShadow) {
                g2.fillRoundRect(x + width - shadowSize + i, y + shadowSize,
                        shadowSize, height - 2 * shadowSize,
                        cornerRadius, cornerRadius);
            }

            // Bottom-right corner
            if (showBottomShadow && showRightShadow) {
                g2.fillRoundRect(x + width - shadowSize + i, y + height - shadowSize + i,
                        shadowSize, shadowSize,
                        cornerRadius, cornerRadius);
            }

            // Top shadow
            if (showTopShadow) {
                g2.fillRoundRect(x + shadowSize, y + i,
                        width - 2 * shadowSize, shadowSize,
                        cornerRadius, cornerRadius);
            }

            // Left shadow
            if (showLeftShadow) {
                g2.fillRoundRect(x + i, y + shadowSize,
                        shadowSize, height - 2 * shadowSize,
                        cornerRadius, cornerRadius);
            }

            // Top-left corner
            if (showTopShadow && showLeftShadow) {
                g2.fillRoundRect(x + i, y + i,
                        shadowSize, shadowSize,
                        cornerRadius, cornerRadius);
            }

            // Bottom-left corner
            if (showBottomShadow && showLeftShadow) {
                g2.fillRoundRect(x + i, y + height - shadowSize + i,
                        shadowSize, shadowSize,
                        cornerRadius, cornerRadius);
            }

            // Top-right corner
            if (showTopShadow && showRightShadow) {
                g2.fillRoundRect(x + width - shadowSize + i, y + i,
                        shadowSize, shadowSize,
                        cornerRadius, cornerRadius);
            }
        }

        // Draw the actual border
        g2.setColor(c.getBackground());
        g2.fillRoundRect(x + (showLeftShadow ? shadowSize : 0),
                y + (showTopShadow ? shadowSize : 0),
                width - (showLeftShadow ? shadowSize : 0) - (showRightShadow ? shadowSize : 0),
                height - (showTopShadow ? shadowSize : 0) - (showBottomShadow ? shadowSize : 0),
                cornerRadius, cornerRadius);

        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(
                showTopShadow ? shadowSize : 0,
                showLeftShadow ? shadowSize : 0,
                showBottomShadow ? shadowSize : 0,
                showRightShadow ? shadowSize : 0);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = showTopShadow ? shadowSize : 0;
        insets.left = showLeftShadow ? shadowSize : 0;
        insets.bottom = showBottomShadow ? shadowSize : 0;
        insets.right = showRightShadow ? shadowSize : 0;
        return insets;
    }
}