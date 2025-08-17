package components.checks;

import javax.swing.*;
import java.awt.*;

import static classes.styles.Colors.textSecondary;

public class PasswordCheck extends JCheckBox {
    public PasswordCheck(String text){
        super(text);
        this.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        this.setForeground(textSecondary);
        this.setOpaque(false);
        this.setFocusPainted(false);
    }
}
