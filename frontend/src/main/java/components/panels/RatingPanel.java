package components.panels;


import components.labels.StarLabel;

import javax.swing.*;
import java.awt.*;

public class RatingPanel extends JPanel {
    private float rating;

    private Color cardColor = new Color(255, 255, 255);

    public RatingPanel(FlowLayout flowLayout, float rating) {
        super(flowLayout);
        this.rating = rating;
        this.setBackground(cardColor);
        render();

    }

    public void render() {
        int fullStars = (int) rating;
        boolean hasHalfStar = rating - fullStars >= 0.5;

        // Create full stars
        for (int i = 0; i < fullStars; i++) {
            this.add(new StarLabel(true, false));
        }

        // Add half star if needed
        if (hasHalfStar) {
            this.add(new StarLabel(false, true));
            fullStars++;
        }

        // Add empty stars to make 5 stars total
        for (int i = fullStars + (hasHalfStar ? 0 : 0); i < 5; i++) {
            this.add(new StarLabel(false, false));
        }

        // Add numeric rating
        JLabel numericRating = new JLabel(String.format(" %.1f", rating));
        numericRating.setFont(new Font("Segoe UI", Font.BOLD, 12));
        numericRating.setForeground(new Color(100, 100, 100));
        this.add(numericRating);
    }
}
