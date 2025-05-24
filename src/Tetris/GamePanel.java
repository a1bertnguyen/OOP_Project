package Tetris;

import javax.swing.*;

import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public final static int width = 1010; // NGOC old: 600, cái cũ quá nhỏ
    public final static int height = 750; // NGOC 600
    public int FPS = 60;
    public Thread gameThread;
    public PlayManager pm;
    public static Sound soundEffect;
    private GameFrame gameFrame;
    private boolean gameStarted = false;
    // PNHU: xóa bớt 2 dòng note cũ do hai dòng đó tạo attribute nma kh cần tới nữa

    public GamePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        // Implement Keylistener
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        pm = new PlayManager(this);
    }

    public void hideMenu() {
        if (gameFrame != null) {
            gameFrame.showGame();
        }
    }

    public void showMenu() {
        if (gameFrame != null) {
            gameFrame.showMenu();
        }
    }

    public void launchGame() {
        // Initialize PlayManager with selected difficulty
        pm = new PlayManager(this);
        gameThread = new Thread(this); // this (GamePanel) vì GamePlanel implements Runnable
        gameThread.start();
        gameStarted = true;
    }

    public void resetGame() {
        // Stop current game
        gameThread = null;
        gameStarted = false;

        // Reset key states
        KeyHandler.upPressed = false;
        KeyHandler.downPressed = false;
        KeyHandler.leftPressed = false;
        KeyHandler.rightPressed = false;
        KeyHandler.pausedPressed = false;

        // Clear static blocks
        if (PlayManager.staticBlocks != null) {
            PlayManager.staticBlocks.clear();
        }

        // Reset PlayManager
        pm = null;
    }

    @Override
    public void run() {
        double drawInterval = 100000000.0 / FPS; // NGOC Khỏang thời gian giữa 2 lần update (giảm tgian lại để chạy
                                                 // nhanh hơn 1 chút)
        double delta = 0; // đếm thời gian đã trôi qua và khi nào nên update game 1 lần
        long lastTime = System.nanoTime(); // lưu thời gian băt đầu vòng lặp
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime(); // thời gian hiện tại (tính bằng đơn vị nano giây
            delta += (currentTime - lastTime) / drawInterval; // thời gian trôi qua kể từ lần cuôí update so với khoảng
                                                              // thời gian lí tưởng để update (drawInterval)
            lastTime = currentTime; // cập nhật lại thời gian cuối cùng

            if (delta >= 1) { // nếu đủ thời gian cho 1 lần update rồi thì sẽ cập nhật trạng thái game, vẽ lại
                update();
                repaint();
                delta--; // trừ bớt 1 lần update để delta < 1 và đợi vòng lặp tiếp thep
            }
        }
    }

    private void update() {
        if (KeyHandler.pausedPressed == false && pm.Gameover == false) {
            pm.update(); // cập nhật logic game nếu bấm phím pause thì ko cập nhật
        }
        if (pm != null && pm.Gameover) {
            // Add delay before showing game over options
            Timer timer = new Timer(200, e -> {
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Game Over!\nScore: " + pm.score + "\nLines: " + pm.line,
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[] { "Play Again", "Main Menu" },
                        "Play Again");

                if (choice == 0) {
                    // Play again
                    resetGame();
                    launchGame();
                } else {
                    // Return to menu
                    showMenu();
                }
            });
            timer.setRepeats(false);
            timer.start();

            // Prevent multiple dialogs
            pm.Gameover = false;
            gameThread = null;
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (pm != null) {
            pm.draw((Graphics2D) g);
        } else {
            // Show loading or instruction screen
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Loading...", width / 2 - 80, height / 2);
        }
    }
}
