package components.inputs;

import javax.swing.*;
import java.awt.*;

import static classes.styles.Colors.*;

public class TextFormField extends FormField{
    private JTextField textField;

    public TextFormField(String label, String type) {
        super(label, type);
        // Text field
        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        textField.setBorder(null);
        textField.setFont(new Font("SF Pro Text", Font.PLAIN, 15));
        textField.setForeground(textPrimary);
        textField.setCaretColor(primaryColor);

        // Icon
        JLabel iconLabel = new JLabel(super.getFieldIcon(type));
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 15));
        iconLabel.setForeground(textSecondary);

        inputContainer.add(iconLabel, BorderLayout.WEST);
        inputContainer.add(Box.createHorizontalStrut(8));
        inputContainer.add(textField, BorderLayout.CENTER);


        this.add(inputContainer);

        this.textField = textField;
    }
}
