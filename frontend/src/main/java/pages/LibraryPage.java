package pages;

import classes.Page;
import components.ModernScrollBarUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static classes.styles.Colors.*;

public class LibraryPage extends Page {
    private JPanel librariesContainer;
    private List<LibraryData> userLibraries;

    private static class LibraryData {
        String name;
        int bookCount;
        List<String> bookTitles;
        String lastModified;

        LibraryData(String name, int bookCount, List<String> bookTitles, String lastModified) {
            this.name = name;
            this.bookCount = bookCount;
            this.bookTitles = bookTitles;
            this.lastModified = lastModified;
        }
    }

    public LibraryPage() {
        super();
        initializeLibraries();
        this.render();
    }

    private void initializeLibraries() {
        userLibraries = new ArrayList<>();

        userLibraries.add(new LibraryData(
                "Da Leggere",
                6,
                List.of("Il nome del vento", "Fondazione", "Neuromante", "Dune", "Il problema dei tre corpi", "L'anello di Re Salomone"),
                "Modificata 2 giorni fa"
        ));

        userLibraries.add(new LibraryData(
                "Fantascienza",
                8,
                List.of("Hyperion", "Ubik", "1984", "Fahrenheit 451", "Il mondo nuovo", "Guida galattica per autostoppisti", "Solaris", "La mano sinistra delle tenebre"),
                "Modificata 1 settimana fa"
        ));

        userLibraries.add(new LibraryData(
                "Classici Italiani",
                5,
                List.of("Il nome della rosa", "Se questo è un uomo", "Il barone rampante", "La coscienza di Zeno", "Il fu Mattia Pascal"),
                "Modificata 3 settimane fa"
        ));

        userLibraries.add(new LibraryData(
                "Libri Preferiti",
                12,
                List.of("Harry Potter e la pietra filosofale", "Il Signore degli Anelli", "Il piccolo principe", "1984", "Il grande Gatsby"),
                "Modificata 1 mese fa"
        ));
    }

    @Override
    public void render() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = createHeader();
        this.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(backgroundColor);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel librariesSection = createLibrariesSection();
        contentPanel.add(librariesSection, BorderLayout.CENTER);

        JScrollPane scrollPane = createScrollPane(contentPanel);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
    }

    private JPanel createHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, gradientStart, getWidth(), 0, gradientEnd);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };

        header.setLayout(new BorderLayout(20, 0));
        header.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        header.setPreferredSize(new Dimension(0, 150));

        JPanel titleSection = new JPanel();
        titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
        titleSection.setOpaque(false);

        JLabel titleLabel = new JLabel("📚 Le Mie Librerie");
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Organizza e gestisci le tue collezioni di libri");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JPanel statsPanel = createStatsPanel();

        titleSection.add(titleLabel);
        titleSection.add(subtitleLabel);
        titleSection.add(Box.createRigidArea(new Dimension(0, 20)));
        titleSection.add(statsPanel);

        header.add(titleSection, BorderLayout.WEST);

        return header;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        statsPanel.setOpaque(false);

        JPanel libraryStat = createStatItem(String.valueOf(userLibraries.size()), "Librerie");
        statsPanel.add(libraryStat);

        int totalBooks = userLibraries.stream().mapToInt(lib -> lib.bookCount).sum();
        JPanel bookStat = createStatItem(String.valueOf(totalBooks), "Libri Totali");
        statsPanel.add(bookStat);

        return statsPanel;
    }

    private JPanel createStatItem(String value, String label) {
        JPanel stat = new JPanel();
        stat.setLayout(new BoxLayout(stat, BoxLayout.Y_AXIS));
        stat.setOpaque(false);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        textLabel.setForeground(new Color(255, 255, 255, 180));
        textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        stat.add(valueLabel);
        stat.add(textLabel);

        return stat;
    }

    private JPanel createLibrariesSection() {
        JPanel section = new JPanel(new BorderLayout(0, 20));
        section.setOpaque(false);

        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setOpaque(false);
        buttonWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);

        JButton addButton = createAddLibraryButton();
        buttonPanel.add(addButton);
        buttonWrapper.add(buttonPanel, BorderLayout.CENTER);

        section.add(buttonWrapper, BorderLayout.NORTH);

        librariesContainer = new JPanel();
        librariesContainer.setOpaque(false);
        librariesContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        refreshLibrariesGrid();

        JScrollPane gridScroll = new JScrollPane(librariesContainer);
        gridScroll.setBorder(null);
        gridScroll.setOpaque(false);
        gridScroll.getViewport().setOpaque(false);
        gridScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gridScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        section.add(gridScroll, BorderLayout.CENTER);

        return section;
    }

    private JButton createAddLibraryButton() {
        JButton addButton = new JButton("+ Aggiungi Libreria") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(primaryHover);
                } else if (getModel().isRollover()) {
                    g2d.setColor(primaryHover);
                } else {
                    g2d.setColor(primaryColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        addButton.setFont(new Font("SF Pro Text", Font.BOLD, 16));
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(180, 45));
        addButton.setMinimumSize(new Dimension(150, 40));
        addButton.setMaximumSize(new Dimension(200, 50));
        addButton.setBorder(null);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.repaint();
            }
        });

        addButton.addActionListener(e -> {
            String libraryName = JOptionPane.showInputDialog(
                    this,
                    "Inserisci il nome della nuova libreria:",
                    "Nuova Libreria",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (libraryName != null && !libraryName.trim().isEmpty()) {
                addNewLibrary(libraryName);
            }
        });

        return addButton;
    }

    private void refreshLibrariesGrid() {
        librariesContainer.removeAll();

        librariesContainer.setLayout(new GridLayout(0, 1, 15, 15)); // Single column by default

        librariesContainer.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = librariesContainer.getWidth();
                int columns = 1;

                if (width > 1100) {
                    columns = 3;
                } else if (width > 750) {
                    columns = 2;
                }

                librariesContainer.setLayout(new GridLayout(0, columns, 15, 15));
                librariesContainer.revalidate();
            }
        });

        for (LibraryData library : userLibraries) {
            JPanel libraryCard = createLibraryCard(library);
            librariesContainer.add(libraryCard);
        }

        librariesContainer.revalidate();
        librariesContainer.repaint();
    }

    private JPanel createLibraryCard(LibraryData library) {
        JPanel card = new JPanel() {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isHovered) {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 20, 20);
                } else {
                    g2d.setColor(new Color(0, 0, 0, 10));
                    g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);
                }

                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 20, 20);

                g2d.dispose();
            }

            public void setHovered(boolean hovered) {
                this.isHovered = hovered;
                repaint();
            }
        };

        card.setLayout(new BorderLayout(0, 15));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setPreferredSize(new Dimension(300, 200));
        card.setMinimumSize(new Dimension(250, 180));
        card.setMaximumSize(new Dimension(400, 220));

        JPanel topSection = new JPanel(new BorderLayout(15, 0));
        topSection.setOpaque(false);

        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 36));
        topSection.add(iconLabel, BorderLayout.WEST);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel nameLabel = new JLabel(library.name);
        nameLabel.setFont(new Font("SF Pro Display", Font.BOLD, 20));
        nameLabel.setForeground(textPrimary);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel countLabel = new JLabel(library.bookCount + " libri");
        countLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        countLabel.setForeground(textSecondary);
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(nameLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(countLabel);

        topSection.add(titlePanel, BorderLayout.CENTER);

        JPanel booksPreview = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        booksPreview.setOpaque(false);
        booksPreview.setPreferredSize(new Dimension(0, 60)); // Fixed height


        int maxPreview = Math.min(3, library.bookTitles.size());
        for (int i = 0; i < maxPreview; i++) {
            String bookTitle = library.bookTitles.get(i);
            if (bookTitle.length() > 15) {
                bookTitle = bookTitle.substring(0, 12) + "...";
            }

            JLabel bookLabel = new JLabel(bookTitle);
            bookLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 11));
            bookLabel.setForeground(textSecondary);
            bookLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 1),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            booksPreview.add(bookLabel);
        }

        if (library.bookTitles.size() > 3) {
            JLabel moreLabel = new JLabel("+" + (library.bookTitles.size() - 3));
            moreLabel.setFont(new Font("SF Pro Text", Font.BOLD, 11));
            moreLabel.setForeground(primaryColor);
            booksPreview.add(moreLabel);
        }

        JPanel bottomSection = new JPanel(new BorderLayout());
        bottomSection.setOpaque(false);

        JLabel modifiedLabel = new JLabel(library.lastModified);
        modifiedLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        modifiedLabel.setForeground(textSecondary);

        JButton viewButton = new JButton("Visualizza →");
        viewButton.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        viewButton.setForeground(primaryColor);
        viewButton.setBorder(null);
        viewButton.setContentAreaFilled(false);
        viewButton.setFocusPainted(false);
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomSection.add(modifiedLabel, BorderLayout.WEST);
        bottomSection.add(viewButton, BorderLayout.EAST);

        // Add sections to card
        card.add(topSection, BorderLayout.NORTH);
        card.add(booksPreview, BorderLayout.CENTER);
        card.add(bottomSection, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                //card.setHovered(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //((JPanel)card).setHovered(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Navigate to library detail page
                openLibraryDetail(library);
            }
        });

        viewButton.addActionListener(e -> openLibraryDetail(library));

        return card;
    }

    private void addNewLibrary(String name) {
        LibraryData newLibrary = new LibraryData(
                name,
                0,
                new ArrayList<>(),
                "Creata ora"
        );

        userLibraries.add(newLibrary);
        refreshLibrariesGrid();

        JOptionPane.showMessageDialog(
                this,
                "Libreria \"" + name + "\" creata con successo!",
                "Libreria Creata",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void openLibraryDetail(LibraryData library) {

        System.out.println("Opening library: " + library.name);

        // changePage("libraryDetail", library);

        JOptionPane.showMessageDialog(
                this,
                "Apertura libreria: " + library.name + "\n" +
                        "Contiene " + library.bookCount + " libri",
                "Dettagli Libreria",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private JScrollPane createScrollPane(JPanel contentPanel) {
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(backgroundColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        return scrollPane;
    }
}