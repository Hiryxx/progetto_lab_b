package components.buttons;

import javax.swing.*;
import java.awt.*;

import static classes.styles.Colors.borderColor;
import static classes.styles.Colors.primaryHover;

public class AuthButton extends JButton {
    String text;
    Color bgColor;
    Color textColor;
    boolean isPrimary;

    public AuthButton(String text, Color bgColor, Color textColor, boolean isPrimary) {
        this.text = text;
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.isPrimary = isPrimary;

        this.setFont(new Font("SF Pro Text", Font.BOLD, 16));
        this.setText(text);
        this.setForeground(textColor);
        this.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setPreferredSize(new Dimension(0, 50));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

    }
    @Override
    protected void paintComponent (Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color currentBg = bgColor;
        if (getModel().isPressed()) {
            currentBg = isPrimary ? primaryHover : bgColor.darker();
        } else if (getModel().isRollover()) {
            currentBg = isPrimary ? primaryHover : bgColor.brighter();
        }

        if (isPrimary) {
            // Gradient for primary button
            GradientPaint gradient = new GradientPaint(0, 0, currentBg, getWidth(), getHeight(),
                    currentBg.darker());
            g2d.setPaint(gradient);
        } else {
            g2d.setColor(currentBg);
        }

        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

        // Border for secondary buttons
        if (!isPrimary) {
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        }

        g2d.dispose();
        super.paintComponent(g);
    }
}
