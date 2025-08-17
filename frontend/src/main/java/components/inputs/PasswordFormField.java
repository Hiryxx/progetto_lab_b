package components.inputs;

import javax.swing.*;
import java.awt.*;

import classes.styles.Colors;

public class PasswordFormField extends FormField{
    private JPasswordField passwordField;

    public PasswordFormField(String label) {
        super(label, "password");
        JPasswordField passwordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        passwordField.setBorder(null);
        passwordField.setFont(new Font("SF Pro Text", Font.PLAIN, 15));
        passwordField.setForeground(Colors.textPrimary);
        passwordField.setCaretColor(Colors.primaryColor);
        passwordField.setEchoChar('•'); // Default hidden

        JLabel iconLabel = new JLabel("🔒");
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 15));

        inputContainer.add(iconLabel, BorderLayout.WEST);
        inputContainer.add(Box.createHorizontalStrut(8));
        inputContainer.add(passwordField, BorderLayout.CENTER);

        this.add(inputContainer);

        this.passwordField = passwordField;
    }

    @Override
    public JTextField getField() {
        return passwordField;
    }
}
