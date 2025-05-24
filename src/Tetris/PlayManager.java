package Tetris;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import mino.Block;
import mino.Mino;
import mino.Mino_Bar;
import mino.Mino_L1;
import mino.Mino_L2;
import mino.Mino_Square;
import mino.Mino_T;
import mino.Mino_Z1;
import mino.Mino_Z2;

import javax.swing.*;

public class PlayManager extends JPanel {
    // Kích thước khung chơi
    final int WIDTH = 360;
    final int HEIGHT = 600;

    public static int left_X, right_X, top_Y, bottom_Y;

    // Mino
    Mino currentMino; // Mino đang rơi
    Mino nextMino; // Mino kế tiếp sẽ rơi
    int MINO_START_X = 0, MINO_START_Y = 0;
    int NEXTMINO_X = 0, NEXTMINO_Y = 0;

    // Các block đã rơi cố định
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    public static int dropInterval = 120; // PNHU: chỉnh từ 60 -> 120 để giam tốc độ rơi cho phù hợp
    boolean Gameover;

    // Xóa dòng
    boolean effectCounterOn = false; // Check xem có đang bật hiệu ứng xóa dòng không? NGOC (khởi tạo = false)
    int effectCounter;
    public ArrayList<Integer> effectY = new ArrayList<>(); // Lưu tọa độ của các dòng cần xóa

    // Điểm số
    int level = 1;
    int line;
    int score;

    // Difficulty settings
    private String difficultyName = "Normal";
    private int baseLevelUpLines = 10; // Lines needed to level up
    private double speedIncreaseRate = 0.9; // Speed multiplier per level

    // Tham chiếu đến GamePanel
    GamePanel gp;

    public PlayManager(GamePanel gp) {
        this.gp = gp;

        // Biên khung chơi
        left_X = (gp.getWidth() / 2) - (WIDTH / 2);
        right_X = left_X + WIDTH;
        top_Y = 50;
        bottom_Y = top_Y + HEIGHT;

        // Tọa độ xuất hiện Mino
        MINO_START_X = left_X + (WIDTH / 2) - Block.Size;
        MINO_START_Y = top_Y + Block.Size;

        // Tọa độ hiển thị nextMino NGOC
        NEXTMINO_X = right_X + 100 + 70;
        NEXTMINO_Y = bottom_Y - 200 + 60 + 40;// left_X + 100; cái cũ tọa độ bị lệch ra ngoài

        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

        // Set difficulty name based on drop interval
        setDifficultyName();
    }

    private void setDifficultyName() {
        if (dropInterval >= 180) {
            difficultyName = "Easy";
            speedIncreaseRate = 0.95; // Slower increase
        } else if (dropInterval >= 120) {
            difficultyName = "Normal";
            speedIncreaseRate = 0.9;
        } else if (dropInterval >= 80) {
            difficultyName = "Hard";
            speedIncreaseRate = 0.85; // Faster increase
        } else {
            difficultyName = "Expert";
            speedIncreaseRate = 0.8; // Fastest increase
            baseLevelUpLines = 8; // Level up faster in expert mode
        }
    }

    public PlayManager() {
    }

    private Mino pickMino() { // random 1 loại Mino bất kỳ
        int random = (int) (Math.random() * 7); // random 0 -> 6 (7 giá trị)
        return switch (random) {
            case 1 -> new Mino_L1();
            case 2 -> new Mino_L2();
            case 3 -> new Mino_Square();
            case 4 -> new Mino_T();
            case 5 -> new Mino_Z1();
            case 6 -> new Mino_Z2();
            default -> new Mino_Bar();
        };
    }

    public void update() {
        // Nếu game đang tạm dừng thì không cho Mino rơi
        if (effectCounterOn)
            return; // tạm dừng game, không cho mảnh rơi tiếp nếu chưa xóa dòng xong
        if (!currentMino.active) {
            // Mino đã rớt xong
            // currentMino.addBlocksToStatic();
            // NGOC Add the blocks of the current Mino to the static blocks
            // cục nào rớt r lưu vào static block
            staticBlocks.addAll(Arrays.asList(currentMino.b));

            // Kiểm tra Game Over
            // Sau khi addBlocksToStatic()
            for (Block block : currentMino.b) {
                if (block.y < top_Y + Block.Size * 2) { // Điều chỉnh nếu cần
                    Gameover = true;
                    return;
                }
            }

            // Gán mino mới
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);

            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            // Kiểm tra xóa dòng
            checkDelete();
        }

        // Nếu còn active thì cập nhật mino hiện tại
        if (currentMino.active) {
            currentMino.update();
        }
    }

    private void checkDelete() {
        final int NUMBER_OF_COLUMNS = WIDTH / Block.Size;
        final int NUMBER_OF_ROWS = HEIGHT / Block.Size;

        // Đếm số block mỗi dòng
        int[] count = new int[NUMBER_OF_ROWS];

        for (Block block : staticBlocks) {
            int rowIndex = (block.y - top_Y) / Block.Size;
            if (rowIndex >= 0 && rowIndex < NUMBER_OF_ROWS) {
                count[rowIndex]++;
            }
        }

        ArrayList<Integer> rowsToDelete = new ArrayList<>();

        // Xác định dòng nào đầy
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            if (count[i] == NUMBER_OF_COLUMNS) {
                rowsToDelete.add(top_Y + i * Block.Size);
            }
        }

        if (rowsToDelete.isEmpty()) {
            return;
        }

        // Bật hiệu ứng
        effectCounterOn = true;
        effectY.addAll(rowsToDelete);

        // Xóa block ở dòng đầy
        ArrayList<Block> remainingBlocks = new ArrayList<>();
        for (Block block : staticBlocks) {
            if (!rowsToDelete.contains(block.y)) {
                remainingBlocks.add(block);
            }
        }
        // GamePanel.soundEffect.play(3, false);

        // Dịch block phía trên xuống
        Collections.sort(rowsToDelete);
        for (int deletedRow : rowsToDelete) {
            for (Block block : remainingBlocks) {
                if (block.y < deletedRow) {
                    block.y += Block.Size;
                }
            }
        }

        // Cập nhật staticBlocks
        staticBlocks.clear();
        staticBlocks.addAll(remainingBlocks);

        // Tính điểm
        int numberOfClearedLines = rowsToDelete.size();
        line += numberOfClearedLines;
        score += numberOfClearedLines * 100;

        // Level up logic adapted to difficulty
        if (line % baseLevelUpLines == 0) {
            level++;
            dropInterval = Math.max(1, (int) (dropInterval * speedIncreaseRate));
        }
    }

    public void draw(Graphics2D g2) {
        // Draw main game area
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_X - 4, top_Y - 4, WIDTH + 8, HEIGHT + 8);

        // Draw next Mino frame
        int x = right_X + 100;
        int y = bottom_Y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("Next", x + 60, y + 60);

        // Draw current and next mino
        if (currentMino != null) {
            currentMino.draw(g2);
        }
        if (nextMino != null) {
            nextMino.draw(g2);
        }

        // Draw static blocks
        for (Block staticBlock : staticBlocks) {
            staticBlock.draw(g2);
        }

        // Draw line clear effect
        if (effectCounterOn) {
            effectCounter++;
            g2.setColor(Color.red);
            for (Integer integer : effectY) {
                g2.fillRect(left_X, integer, WIDTH, Block.Size);
            }
            if (effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        // Draw game state messages
        if (Gameover) {
            g2.setColor(Color.red);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("GAME OVER", left_X + 50, top_Y + 300);

            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("Final Score: " + score, left_X + 100, top_Y + 350);
        }

        if (KeyHandler.pausedPressed && !Gameover) {
            g2.setColor(Color.yellow);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("PAUSED", left_X + 100, top_Y + 350);
        }

        // Draw score panel
        int scoreX = right_X + 50;
        g2.setColor(Color.white);
        g2.drawRect(scoreX, top_Y, 250, 180);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("DIFFICULTY: " + difficultyName, scoreX + 10, top_Y + 30);
        g2.drawString("LEVEL: " + level, scoreX + 20, top_Y + 60);
        g2.drawString("LINES: " + line, scoreX + 20, top_Y + 90);
        g2.drawString("SCORE: " + score, scoreX + 20, top_Y + 120);

        // Show next level progress
        int linesUntilNext = baseLevelUpLines - (line % baseLevelUpLines);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Next: " + linesUntilNext + " lines", scoreX + 20, top_Y + 150);

        // Draw game title
        x = 35;
        y = top_Y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 50));
        g2.drawString("Simple Tetris", x, y);

        // Draw controls hint
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("ESC - Menu", x, y + 50);
    }
}
