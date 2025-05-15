package classes;

import javax.swing.*;
import java.awt.*;

public abstract class Page extends JPanel {
    protected MainFrame mainFrame;
    public Page(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
    }

    /**
     * This method should be overridden to render the specific content of the panel.
     */
    public abstract void render();

    protected void changePage(String page) {
        mainFrame.showPage(page);
    }
}
