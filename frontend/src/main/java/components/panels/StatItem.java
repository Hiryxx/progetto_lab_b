package components.panels;

import javax.swing.*;
import java.awt.*;

public class StatItem extends JPanel {
    private JLabel textLabel;
    private JLabel valueLabel;

    public StatItem (String value, String label) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);

        valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textLabel = new JLabel(label);
        textLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        textLabel.setForeground(new Color(255, 255, 255, 180));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(valueLabel);
        this.add(textLabel);
    }

    public void setLabelText(String  text) {
        this.textLabel.setText(text);
    }

    public void setValueText(String text) {
        this.valueLabel.setText(text);
    }
}
