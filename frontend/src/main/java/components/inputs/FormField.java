package components.inputs;

import javax.swing.*;
import java.awt.*;

import static classes.styles.Colors.*;
import static classes.styles.Colors.textSecondary;

public class FormField extends JPanel {
    private String label;
    private String type;
    protected JPanel inputContainer;

    public FormField(String label, String type) {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);

        // Label
        JLabel fieldLabel = new JLabel(label, SwingConstants.CENTER);
        fieldLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        fieldLabel.setForeground(textPrimary);
        fieldLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input field container
        JPanel inputContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Field background
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Border
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

                g2d.dispose();
            }
        };
        inputContainer.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        inputContainer.setPreferredSize(new Dimension(0, 44));

        this.add(fieldLabel);
        this.add(Box.createRigidArea(new Dimension(0, 6)));
        this.inputContainer = inputContainer;

    }
    protected String getFieldIcon(String type) {
        return switch (type) {
            case "email" -> "✉️";
            case "user", "username" -> "👤";
            case "fiscal" -> "🇮🇹";
            case "password" -> "🔒";
            default -> "📝";
        };
    }

    public JTextField getField() {
        return null;
    }
}
