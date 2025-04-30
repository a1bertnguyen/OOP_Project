package mino;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    public final static int width = 600;
    public final static int height = 600;
    public int FPS = 60;
    public Thread gameThread;
    public PlayManager pm;
    // public static music = new sound();
    // public static soundEffect = new sound();

    public GamePanel () {
        JFrame frame = new JFrame("Tetris");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this); // Thêm GamePanel vào frame
        frame.setLocationRelativeTo(null); //Center
        frame.setVisible(true);

        setBackground(Color.black);

        pm = new PlayManager(); // GamePanel được tạo thì sẽ tạo ra 1 PlayManager
    }

    public void launchGame() {
        gameThread = new Thread(this); //this (GamePanel) vì GamePlanel implements Runnable
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 10000000000.0 / FPS; //Khỏang thời gian giữa 2 lần update
        double delta = 0; // đếm thời gian đã trôi qua và khi nào nên update game 1 lần
        long lastTime = System.nanoTime(); // lưu thời gian băt đầu vòng lặp
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime(); // thời gian hiện tại (tính bằng đơn vị nano giây
            delta += (currentTime - lastTime) / drawInterval; //thời gian trôi qua kể từ lần cuôí update so với khoảng thời gian lí tưởng để update (drawInterval)
            lastTime = currentTime; // cập nhật lại thời gian cuối cùng

            if (delta >= 1) { //nếu đủ thời gian cho 1 lần update rồi thì sẽ cập nhật trạng thái game, vẽ lại
                update();
                repaint();
                delta--; //trừ bớt 1 lần update để delta < 1 và đợi vòng lặp tiếp thep
            }
        }
    }

    private void update() {
        pm.update(); // cập nhật logic gameplay
    }

    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        pm.draw((Graphics2D) g);
    } /* Vẽ giao diện game */
}
