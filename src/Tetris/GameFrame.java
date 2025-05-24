package Tetris;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private MenuScreen menuScreen;
    private GamePanel gamePanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public GameFrame() {
        setTitle("Tetris Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Initialize card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create game panel
        gamePanel = new GamePanel(this);

        // Create menu screen
        menuScreen = new MenuScreen(gamePanel);

        // Add panels to card layout
        mainPanel.add(menuScreen, "MENU");
        mainPanel.add(gamePanel, "GAME");

        add(mainPanel);

        // Show menu first
        cardLayout.show(mainPanel, "MENU");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
        // Stop game if running
        if (gamePanel.gameThread != null) {
            gamePanel.gameThread = null;
        }
        // Reset game state
        gamePanel.resetGame();
    }

    public void showGame() {
        cardLayout.show(mainPanel, "GAME");
        gamePanel.requestFocusInWindow(); // Ensure game panel has focus for key events
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}