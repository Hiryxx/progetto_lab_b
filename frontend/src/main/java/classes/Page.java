package classes;

import javax.swing.*;
import java.awt.*;

public abstract class Page extends JPanel {
    public Page() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
    }

    /**
     * This method should be overridden to render the specific content of the panel.
     */
    public abstract void render();

    /**
     * This method should be overridden to update the content of the panel.
     */
    public abstract void refresh();


    protected void changePage(String page) {
        MainFrame.showPage(page);
    }
}
