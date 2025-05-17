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
    // PNHU: xóa bớt 2 dòng note cũ do hai dòng đó tạo attribute nma kh cần tới nữa

    public GamePanel() {
        JFrame frame = new JFrame("Tetris");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this); // Thêm GamePanel vào frame
        frame.setLocationRelativeTo(null); // Center
        frame.setVisible(true);

        setBackground(Color.BLACK); // Set the background color
        // Initialize PlayManager
        pm = new PlayManager(this); // Pass the current GamePanel instance to PlayManager

        // PNHU: add KeyHandler
        addKeyListener(new KeyHandler());
        setFocusable(true); // để panel nhận focus và nhận sự kiện phím
        requestFocusInWindow(); // đảm bảo nó có focus ngay khi khởi chạy

        // PNHU: add soundEffect
        soundEffect = new Sound();
    }

    public void launchGame() {
        gameThread = new Thread(this); // this (GamePanel) vì GamePlanel implements Runnable
        gameThread.start();
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

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        pm.draw((Graphics2D) g);

    } /* Vẽ giao diện game */
}
