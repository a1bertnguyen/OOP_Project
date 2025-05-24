package Tetris;

public class Main {
    public static void main(String[] args) {
        // Set look and feel to system default for better appearance
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show the game frame with menu
        new GameFrame();
    }
}
