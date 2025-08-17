package components.panels;

import components.checks.PasswordCheck;

import javax.swing.*;
import java.awt.*;

public class AuthOptionPanel extends JPanel {
    JCheckBox showPasswordCheck;

    public AuthOptionPanel(JPasswordField passwordField) {
        this.setOpaque(false);

        // Remember me checkbox
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        JCheckBox showPasswordCheck = new PasswordCheck("Mostra password");



        showPasswordCheck.addActionListener(e -> {
            boolean isSelected = showPasswordCheck.isSelected();
            if (isSelected) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        leftPanel.add(showPasswordCheck);

        // Forgot password link
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);

        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);

        this.showPasswordCheck = showPasswordCheck;

    }

    public JCheckBox getShowPasswordCheck() {
        return showPasswordCheck;
    }
}
