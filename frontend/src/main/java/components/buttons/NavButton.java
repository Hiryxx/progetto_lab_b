package components.thiss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static classes.styles.Colors.primaryColor;
import static classes.styles.Colors.textSecondary;

public class NavButton extends JButton {
    private JLabel textLabel;
    private boolean isSelected = false;

    public NavButton (String icon, String text, String pageName, ActionListener action) {
        this.setLayout(new BorderLayout(0, 5));
        this.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 20));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        // Color is set in updateNavButtonStates
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(iconLabel, BorderLayout.CENTER);
        this.add(textLabel, BorderLayout.SOUTH);

        this.addActionListener(action);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                    textLabel.setForeground(primaryColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                    textLabel.setForeground(textSecondary);

            }
        });
        this.textLabel = textLabel;
    }
    @Override
    protected void paintComponent(Graphics g) {
        if (isSelected) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 20));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2d.dispose();
        }
        super.paintComponent(g);
    }

    public JLabel getTextLabel() {
        return textLabel;
    }

    public void setTextLabel(String text) {
        this.textLabel.setText(text);
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setActionListener(ActionListener action) {
        this.removeActionListener(this.getActionListeners()[0]);
        this.addActionListener(action);
    }
}
