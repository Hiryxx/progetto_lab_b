package pages;

import classes.MainFrame;
import classes.Page;
import components.ModernScrollBarUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ProfilePage extends Page {

    // Colori (stessi della HomePage)
    private Color primaryColor = new Color(99, 102, 241);      // Indaco
    private Color primaryHover = new Color(79, 70, 229);       // Indaco scuro
    private Color accentColor = new Color(248, 113, 113);      // Corallo
    private Color backgroundColor = new Color(248, 250, 252);   // Azzurro-grigio chiaro
    private Color cardColor = new Color(255, 255, 255);        // Bianco puro
    private Color textPrimary = new Color(15, 23, 42);         // Ardesia scura
    private Color textSecondary = new Color(100, 116, 139);    // Ardesia media
    private Color borderColor = new Color(226, 232, 240);      // Ardesia chiara
    private Color successColor = new Color(34, 197, 94);       // Verde per successo

    // Sfumature
    private Color gradientStart = new Color(139, 92, 246);     // Viola
    private Color gradientEnd = new Color(59, 130, 246);       // Blu

    // Pannello per le librerie, lo rendo un campo della classe per poterlo aggiornare
    private JPanel librariesContainer;

    public ProfilePage(MainFrame mainFrame) {
        super(mainFrame);
        this.render();
    }

    @Override
    public void render() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = createProfileHeader();
        this.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(backgroundColor);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        // Spazio aggiuntivo tra intestazione e "Le mie librerie"
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        contentPanel.add(createMyLibrariesPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        contentPanel.add(createSuggestionsPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        contentPanel.add(createFavoriteBooksPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        contentPanel.add(createSettingsPanel());

        JScrollPane scrollPane = createScrollPane(contentPanel);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomNavigationPanel();
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createProfileHeader() {
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
        header.setPreferredSize(new Dimension(0, 200));

        JPanel userInfoPanel = new JPanel(new BorderLayout(20, 0));
        userInfoPanel.setOpaque(false);

        JPanel avatarPanel = createAvatarPanel();
        userInfoPanel.add(avatarPanel, BorderLayout.WEST);

        JPanel textInfoPanel = new JPanel();
        textInfoPanel.setLayout(new BoxLayout(textInfoPanel, BoxLayout.Y_AXIS));
        textInfoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Mario Rossi");
        nameLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel("mario.rossi@email.com");
        emailLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        emailLabel.setForeground(new Color(255, 255, 255, 180));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel memberSinceLabel = new JLabel("Membro dal Gennaio 2023");
        memberSinceLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        memberSinceLabel.setForeground(new Color(255, 255, 255, 150));
        memberSinceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        memberSinceLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        textInfoPanel.add(nameLabel);
        textInfoPanel.add(emailLabel);
        textInfoPanel.add(memberSinceLabel);

        userInfoPanel.add(textInfoPanel, BorderLayout.CENTER);

        JPanel actionPanel = createHeaderActionPanel();
        userInfoPanel.add(actionPanel, BorderLayout.EAST);

        header.add(userInfoPanel, BorderLayout.CENTER);
        return header;
    }

    private JPanel createAvatarPanel() {
        JPanel avatarContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.WHITE);
                g2d.fillOval(5, 5, 90, 90);

                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawOval(5, 5, 90, 90);

                g2d.setFont(new Font("SF Pro Text", Font.PLAIN, 50));
                g2d.setColor(primaryColor);
                String userIcon = "👤";
                FontMetrics fm = g2d.getFontMetrics();
                int x = 5 + (90 - fm.stringWidth(userIcon)) / 2;
                int y = 5 + (90 + fm.getAscent()) / 2;
                g2d.drawString(userIcon, x, y - 5);

                g2d.dispose();
            }
        };
        avatarContainer.setPreferredSize(new Dimension(100, 100));
        avatarContainer.setOpaque(false);
        return avatarContainer;
    }

    private JPanel createHeaderActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);
        JButton editButton = createIconButton("✏️ Modifica", Color.WHITE);
        JButton logoutButton = createIconButton("➡️ Esci", new Color(255, 255, 255, 180));
        panel.add(editButton);
        panel.add(logoutButton);

        logoutButton.addActionListener(e -> changePage("login"));

        return panel;
    }

    private JButton createIconButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setForeground(color);
        button.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(primaryColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(color);
            }
        });
        return button;
    }

    private JPanel createMyLibrariesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        panel.setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout(0, 5));
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("📚 Le mie Librerie");
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        titleLabel.setForeground(textPrimary);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Organizza i tuoi libri di interesse");
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        subtitleLabel.setForeground(textSecondary);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        JButton addButton = new JButton("+ Aggiungi Libreria") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                super.paintComponent(g);
                g2d.dispose();
            }
        };

        addButton.setForeground(Color.WHITE);
        addButton.setBackground(primaryColor);
        addButton.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(primaryHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(primaryColor);
            }
        });

        addButton.addActionListener(e -> {
            String libraryName = JOptionPane.showInputDialog(this,
                    "Inserisci il nome della nuova libreria:",
                    "Nuova Libreria",
                    JOptionPane.QUESTION_MESSAGE);
            if (libraryName != null && !libraryName.trim().isEmpty()) {
                addLibraryCard(libraryName);
            }
        });

        addButton.setPreferredSize(new Dimension(180, 40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        librariesContainer = new JPanel();
        librariesContainer.setLayout(new BoxLayout(librariesContainer, BoxLayout.Y_AXIS));
        librariesContainer.setOpaque(false);

        addLibraryCard("Da Leggere", List.of("Il nome del vento", "Fondazione", "Neuromante", "Dune", "Il problema dei tre corpi", "L'anello di Re Salomone"));
        addLibraryCard("Fantascienza", List.of("Hyperion", "Ubik"));

        panel.add(librariesContainer, BorderLayout.CENTER);

        return panel;
    }

    private void addLibraryCard(String libraryName) {
        addLibraryCard(libraryName, new ArrayList<>());
    }

    private void addLibraryCard(String libraryName, List<String> bookTitles) {
        librariesContainer.add(createLibraryCard(libraryName, bookTitles));
        librariesContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        librariesContainer.revalidate();
        librariesContainer.repaint();
    }

    private JPanel createLibraryCard(String libraryName, List<String> bookTitles) {
        JPanel card = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);

                g2d.dispose();
            }
        };
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel(libraryName + " (" + bookTitles.size() + " libri)");
        nameLabel.setFont(new Font("SF Pro Display", Font.BOLD, 20));
        nameLabel.setForeground(textPrimary);
        card.add(nameLabel, BorderLayout.NORTH);

        JPanel booksPanel = new JPanel();
        booksPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        booksPanel.setOpaque(false);

        for (String title : bookTitles) {
            JPanel bookCard = createBookCard(title, "Autore di Esempio", "Genere", 4.0f);
            booksPanel.add(bookCard);
        }

        card.add(booksPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSuggestionsPanel() {
        JPanel booksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        booksPanel.setOpaque(false);
        booksPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        for (int i = 1; i <= 4; i++) {
            JPanel bookWrapper = createBookCard("Libro Consigliato " + i, "Autore Consigliato " + i, "Genere " + i, 4.5f);
            booksPanel.add(bookWrapper);
        }

        JScrollPane horizontalScrollPane = new JScrollPane(booksPanel) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        horizontalScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        horizontalScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        horizontalScrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        horizontalScrollPane.setBorder(null);
        horizontalScrollPane.setOpaque(false);
        horizontalScrollPane.getViewport().setOpaque(false);

        return createSectionPanel("💡 Suggerimenti", "Libri consigliati e correlati alle tue librerie", () -> horizontalScrollPane);
    }

    private JPanel createFavoriteBooksPanel() {
        JPanel booksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        booksPanel.setOpaque(false);
        booksPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        for (int i = 1; i <= 4; i++) {
            JPanel bookWrapper = createBookCard("Libro Preferito " + i, "Autore Preferito " + i, "Genere " + i, 5.0f - (i * 0.2f));
            booksPanel.add(bookWrapper);
        }

        JScrollPane horizontalScrollPane = new JScrollPane(booksPanel) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        horizontalScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        horizontalScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        horizontalScrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        horizontalScrollPane.setBorder(null);
        horizontalScrollPane.setOpaque(false);
        horizontalScrollPane.getViewport().setOpaque(false);

        return createSectionPanel("❤️ I Tuoi Preferiti", "Libri che ami di più", () -> horizontalScrollPane);
    }

    private JPanel createSettingsPanel() {
        return createSectionPanel("⚙️ Impostazioni Account", "Gestisci le tue preferenze", () -> {
            JPanel settingsPanel = new JPanel();
            settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
            settingsPanel.setOpaque(false);

            settingsPanel.add(createSettingEntry("Notifiche Push", "Ricevi aggiornamenti sui tuoi libri preferiti", true, null));
            settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            settingsPanel.add(createSettingEntry("Informazioni personali", "Modifica nome, email e password", false, e -> showPersonalInformationDialog()));

            return settingsPanel;
        });
    }

    private JPanel createSettingEntry(String title, String subtitle, boolean isToggle, java.awt.event.ActionListener action) {
        JPanel entry = new JPanel(new BorderLayout(20, 0));
        entry.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        entry.setBackground(cardColor);
        entry.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 16));
        titleLabel.setForeground(textPrimary);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        subtitleLabel.setForeground(textSecondary);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        entry.add(textPanel, BorderLayout.CENTER);

        if (isToggle) {
            JToggleButton toggle = new JToggleButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (isSelected()) {
                        g2d.setColor(primaryColor);
                    } else {
                        g2d.setColor(textSecondary);
                    }
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                    super.paintComponent(g);
                    g2d.dispose();
                }
            };

            toggle.setForeground(Color.WHITE);
            toggle.setFont(new Font("SF Pro Text", Font.BOLD, 14));
            toggle.setFocusPainted(false);
            toggle.setBorderPainted(false);
            toggle.setContentAreaFilled(false);
            toggle.setCursor(new Cursor(Cursor.HAND_CURSOR));

            toggle.addActionListener(e -> {
                if (toggle.isSelected()) {
                    toggle.setText("ON");
                } else {
                    toggle.setText("OFF");
                }
                toggle.repaint();
            });

            // Impostazione dello stato iniziale
            toggle.setSelected(true);
            toggle.setText("ON");
            entry.add(toggle, BorderLayout.EAST);

        } else {
            JButton arrowButton = new JButton(">");
            arrowButton.setForeground(textSecondary);
            arrowButton.setBackground(cardColor);
            arrowButton.setFocusPainted(false);
            arrowButton.setBorderPainted(false);
            if (action != null) {
                arrowButton.addActionListener(action);
            }
            entry.add(arrowButton, BorderLayout.EAST);
        }
        return entry;
    }

    private void showPersonalInformationDialog() {
        JDialog dialog = new JDialog(mainFrame, "Informazioni Personali", true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0)); // Rende il JDialog trasparente

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bordo arrotondato

                g2d.dispose();
                super.paintComponent(g); // Disegna i componenti sopra lo sfondo
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Il mio account");
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        titleLabel.setForeground(textPrimary);

        infoPanel.add(titleLabel);
        infoPanel.add(createDialogInfoEntry("Nome:", "Mario Rossi"));
        infoPanel.add(createDialogInfoEntry("Email:", "mario.rossi@email.com"));

        JButton resetPasswordButton = new JButton("Reimposta Password");
        resetPasswordButton.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        resetPasswordButton.setForeground(Color.WHITE);
        resetPasswordButton.setBackground(primaryColor);
        resetPasswordButton.setFocusPainted(false);
        resetPasswordButton.setBorderPainted(false);
        resetPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetPasswordButton.addActionListener(e -> JOptionPane.showMessageDialog(dialog, "Funzionalità di reimpostazione password in sviluppo."));
        resetPasswordButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(resetPasswordButton);

        contentPanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Pulsante di chiusura
        JButton closeButton = new JButton("✕");
        closeButton.setFont(new Font("SF Pro Text", Font.BOLD, 16));
        closeButton.setForeground(textSecondary);
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel headerDialogPanel = new JPanel(new BorderLayout());
        headerDialogPanel.setOpaque(false);
        headerDialogPanel.add(closeButton, BorderLayout.EAST);
        contentPanel.add(headerDialogPanel, BorderLayout.NORTH);

        dialog.setContentPane(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private JPanel createDialogInfoEntry(String label, String value) {
        JPanel entry = new JPanel(new BorderLayout(10, 0));
        entry.setOpaque(false);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        labelComponent.setForeground(textPrimary);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        valueComponent.setForeground(textSecondary);

        entry.add(labelComponent, BorderLayout.WEST);
        entry.add(valueComponent, BorderLayout.CENTER);

        return entry;
    }

    private JScrollPane createScrollPane(JPanel contentPanel) {
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(backgroundColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        return scrollPane;
    }

    private JPanel createBottomNavigationPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(cardColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(borderColor);
                g2d.fillRect(0, 0, getWidth(), 1);

                GradientPaint shadow = new GradientPaint(0, 1, new Color(0, 0, 0, 8), 0, 10, new Color(0, 0, 0, 0));
                g2d.setPaint(shadow);
                g2d.fillRect(0, 1, getWidth(), 10);
                g2d.dispose();
            }
        };

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton homeButton = createNavButton("🏠", "Home", textSecondary, false);
        homeButton.addActionListener(e -> changePage("home"));

        JButton libraryButton = createNavButton("📚", "Libreria", textSecondary, false);
        JButton recommendButton = createNavButton("⭐", "Scopri", textSecondary, false);
        JButton profileButton = createNavButton("👤", "Profilo", primaryColor, true);
        profileButton.addActionListener(e -> changePage("profile"));

        panel.add(homeButton);
        panel.add(libraryButton);
        panel.add(recommendButton);
        panel.add(profileButton);

        return panel;
    }

    private JButton createNavButton(String icon, String text, Color color, boolean active) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (active) {
                    g2d.setColor(new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 20));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setLayout(new BorderLayout(0, 5));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Apple Color Emoji", Font.PLAIN, 20));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        textLabel.setForeground(color);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        button.add(iconLabel, BorderLayout.CENTER);
        button.add(textLabel, BorderLayout.SOUTH);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    textLabel.setForeground(primaryColor);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) {
                    textLabel.setForeground(color);
                }
            }
        });

        return button;
    }

    private JPanel createBookCard(String title, String author, String genre, float rating) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(cardColor);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);

                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);

                g2d.dispose();
            }
        };

        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(180, 260));
        card.setMaximumSize(new Dimension(180, 260));
        card.setMinimumSize(new Dimension(180, 260));

        JPanel coverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, primaryColor, getWidth(), getHeight(), accentColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("SF Pro Text", Font.BOLD, 24));
                FontMetrics fm = g2d.getFontMetrics();
                String bookIcon = "📖";
                int x = (getWidth() - fm.stringWidth(bookIcon)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(bookIcon, x, y);

                g2d.dispose();
            }
        };
        coverPanel.setPreferredSize(new Dimension(0, 140));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 14));
        titleLabel.setForeground(textPrimary);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel authorLabel = new JLabel("di " + author);
        authorLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        authorLabel.setForeground(textSecondary);
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        authorLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

        JLabel genreLabel = new JLabel(genre);
        genreLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 11));
        genreLabel.setForeground(primaryColor);
        genreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        genreLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ratingPanel.setOpaque(false);
        ratingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= rating ? "⭐" : "☆");
            star.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
            ratingPanel.add(star);
        }

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(genreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(ratingPanel);

        card.add(coverPanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        return card;
    }

    private JPanel createSectionPanel(String title, String subtitle, Supplier<Component> contentSupplier) {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        panel.setOpaque(false);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        titleLabel.setForeground(textPrimary);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        subtitleLabel.setForeground(textSecondary);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        headerPanel.add(titlePanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentSupplier.get(), BorderLayout.CENTER);

        return panel;
    }
}