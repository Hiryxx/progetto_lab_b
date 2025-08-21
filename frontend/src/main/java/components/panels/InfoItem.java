package components.panels;

import javax.swing.*;
import java.awt.*;

import static classes.styles.Colors.textPrimary;
import static classes.styles.Colors.textSecondary;

public class InfoItem extends JPanel {
    private JLabel valueLabel;

    public InfoItem(String icon, String label, String value) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header con icona e label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 14));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        labelText.setForeground(textSecondary);
        labelText.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));

        headerPanel.add(iconLabel);
        headerPanel.add(labelText);

        valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        valueLabel.setForeground(textPrimary);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.add(headerPanel);
        this.add(valueLabel);

    }

    public void setLabelValue(String labelValue) {
        this.valueLabel.setText(labelValue);
    }
}
