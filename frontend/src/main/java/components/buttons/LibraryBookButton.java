package components.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static classes.styles.Colors.primaryColor;
import static classes.styles.Colors.primaryHover;

public class LibraryBookButton extends JButton {
    public LibraryBookButton(String text) {
        super(text);

        this.setFont(new Font("SF Pro Text", Font.BOLD, 16));
        this.setForeground(Color.WHITE);
        this.setPreferredSize(new Dimension(180, 45));
        this.setMinimumSize(new Dimension(150, 40));
        this.setMaximumSize(new Dimension(200, 50));
        this.setBorder(null);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                repaint();
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2d.setColor(primaryHover);
        } else if (getModel().isRollover()) {
            g2d.setColor(primaryHover);
        } else {
            g2d.setColor(primaryColor);
        }

        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2d.setColor(Color.WHITE);
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - 2;
        g2d.drawString(getText(), x, y);

        g2d.dispose();
    }
}
