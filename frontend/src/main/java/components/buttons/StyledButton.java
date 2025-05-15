package components.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {
    String text;
    Color bgColor;
    Color textColor;

    public StyledButton(String text, Color bgColor, Color textColor) {
        this.text = text;
        this.bgColor = bgColor;
        this.textColor = textColor;

        this.setFont(new Font("Segoe UI", Font.BOLD, 14));
        this.setForeground(textColor);
        this.setBackground(bgColor);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setContentAreaFilled(true);
        this.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        this.setText(text);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setOpaque(true);


        // hover effect
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                StyledButton.this.setBackground(bgColor.darker());
                StyledButton.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                StyledButton.this.setBackground(bgColor);
            }
        });
    }
}
