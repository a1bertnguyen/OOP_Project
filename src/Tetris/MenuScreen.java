package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuScreen extends JPanel {
    private GamePanel gamePanel;
    private String[] difficultyLevels = { "Easy", "Normal", "Hard", "Expert" };
    private int selectedDifficulty = 1; // Default: Normal

    // Độ khó ảnh hưởng đến tốc độ rơi ban đầu
    private int[] dropIntervals = { 180, 120, 80, 40 }; // Easy, Normal, Hard, Expert

    public MenuScreen(GamePanel gp) {
        this.gamePanel = gp;
        initializeMenu();
    }

    private void initializeMenu() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Title Panel
        JPanel titlePanel = createTitlePanel();

        // Center Panel với các nút
        JPanel centerPanel = createCenterPanel();

        // Footer Panel
        JPanel footerPanel = createFooterPanel();

        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        titlePanel.setPreferredSize(new Dimension(GamePanel.width, 200));

        JLabel titleLabel = new JLabel("TETRIS GAME");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Classic Block Puzzle Game");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titlePanel.setLayout(new GridLayout(2, 1));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        return titlePanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Start Game Button
        JButton startButton = createStyledButton("START GAME", Color.GREEN);
        startButton.addActionListener(e -> startGame());

        // Difficulty Selection
        JPanel difficultyPanel = createDifficultyPanel();

        // Instructions Button
        JButton instructionsButton = createStyledButton("INSTRUCTIONS", Color.YELLOW);
        instructionsButton.addActionListener(e -> showInstructions());

        // Exit Button
        JButton exitButton = createStyledButton("EXIT", Color.RED);
        exitButton.addActionListener(e -> System.exit(0));

        // Layout components
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;

        gbc.gridy = 0;
        centerPanel.add(startButton, gbc);

        gbc.gridy = 1;
        centerPanel.add(difficultyPanel, gbc);

        gbc.gridy = 2;
        centerPanel.add(instructionsButton, gbc);

        gbc.gridy = 3;
        centerPanel.add(exitButton, gbc);

        return centerPanel;
    }

    private JPanel createDifficultyPanel() {
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setBackground(Color.BLACK);
        difficultyPanel.setLayout(new FlowLayout());

        JLabel diffLabel = new JLabel("Difficulty: ");
        diffLabel.setFont(new Font("Arial", Font.BOLD, 20));
        diffLabel.setForeground(Color.WHITE);

        JButton prevButton = new JButton("<-");
        prevButton.setFont(new Font("Arial", Font.BOLD, 16));
        prevButton.setPreferredSize(new Dimension(50, 40));
        prevButton.addActionListener(e -> changeDifficulty(-1));
        styleSmallButton(prevButton);

        JLabel currentDiffLabel = new JLabel(difficultyLevels[selectedDifficulty]);
        currentDiffLabel.setFont(new Font("Arial", Font.BOLD, 20));
        currentDiffLabel.setForeground(getDifficultyColor());
        currentDiffLabel.setPreferredSize(new Dimension(100, 40));
        currentDiffLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton nextButton = new JButton("->");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setPreferredSize(new Dimension(50, 40));
        nextButton.addActionListener(e -> changeDifficulty(1));
        styleSmallButton(nextButton);

        // Store reference to update label
        prevButton.putClientProperty("diffLabel", currentDiffLabel);
        nextButton.putClientProperty("diffLabel", currentDiffLabel);

        difficultyPanel.add(diffLabel);
        difficultyPanel.add(prevButton);
        difficultyPanel.add(currentDiffLabel);
        difficultyPanel.add(nextButton);

        return difficultyPanel;
    }

    private void changeDifficulty(int direction) {
        selectedDifficulty += direction;
        if (selectedDifficulty < 0)
            selectedDifficulty = difficultyLevels.length - 1;
        if (selectedDifficulty >= difficultyLevels.length)
            selectedDifficulty = 0;

        // Update display - find the difficulty label and update it
        Component[] components = getComponents();
        updateDifficultyDisplay();
    }

    private void updateDifficultyDisplay() {
        // Refresh the entire panel to update difficulty display
        repaint();
        // You might need to implement a more sophisticated update mechanism
        // For now, we'll recreate the center panel
        removeAll();
        initializeMenu();
        revalidate();
        repaint();
    }

    private Color getDifficultyColor() {
        return switch (selectedDifficulty) {
            case 0 -> Color.GREEN; // Easy
            case 1 -> Color.YELLOW; // Normal
            case 2 -> Color.ORANGE; // Hard
            case 3 -> Color.RED; // Expert
            default -> Color.WHITE;
        };
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setPreferredSize(new Dimension(250, 60));
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(color);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.DARK_GRAY);
                button.setForeground(color);
            }
        });

        return button;
    }

    private void styleSmallButton(JButton button) {
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.RED);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.BLACK);
        footerPanel.setPreferredSize(new Dimension(GamePanel.width, 80));

        JLabel footerLabel = new JLabel("Use Arrow Keys to Play • P to Pause");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        footerLabel.setForeground(Color.LIGHT_GRAY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        footerPanel.add(footerLabel);
        return footerPanel;
    }

    private void startGame() {
        // Set difficulty in PlayManager
        PlayManager.dropInterval = dropIntervals[selectedDifficulty];

        // Hide menu and start game
        gamePanel.hideMenu();
        gamePanel.launchGame();
    }

    private void showInstructions() {
        String instructions = """
                TETRIS INSTRUCTIONS:

                ← → : Move left/right
                ↓ : Move down faster
                ↑ : Rotate piece
                P : Pause/Resume game

                GOAL:
                Fill complete horizontal lines to clear them.
                Game ends when pieces reach the top.

                SCORING:
                • 1 line = 100 points
                • Level increases every 10 lines
                • Higher levels = faster falling speed

                Good luck!
                """;

        JOptionPane.showMessageDialog(this, instructions, "How to Play",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public int getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public String getDifficultyName() {
        return difficultyLevels[selectedDifficulty];
    }
}
