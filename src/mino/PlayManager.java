package mino;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlayManager extends JPanel {
    //Kích thước khung chơi
    final int WIDTH = 360;
    final int HEIGHT = 600;

    public static int left_X, right_X, top_Y, bottom_Y;

    //Mino
    Mino currentMino; //Mino đang rơi
    Mino nextMino;  //Mino kế tiếp sẽ rơi
    int MINO_START_X = 0 , MINO_START_Y = 0;
    int NEXTMINO_X = 0 , NEXTMINO_Y = 0;

    //Các block đã rơi cố định
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    public static int dropInterval = 120; //PNHU: chỉnh từ 60 -> 120 để giam tốc độ rơi cho phù hợp
    boolean Gameover;

    //Xóa dòng
    boolean effectCounterOn = false; //Check xem có đang bật hiệu ứng xóa dòng không? NGOC (khởi tạo = false)
    int effectCounter;
    public ArrayList<Integer> effectY = new ArrayList<>(); //Lưu tọa độ của các dòng cần xóa

    //Điểm số
    int level = 1;
    int line;
    int score;

    //Tham chiếu đến GamePanel
    GamePanel gp;

    public PlayManager(GamePanel gp) {
        this.gp = gp;

        //Biên khung chơi
        left_X = (gp.getWidth() / 2) - (WIDTH / 2);
        right_X = left_X + WIDTH;
        top_Y = 50;
        bottom_Y = top_Y + HEIGHT;

        //Tọa độ xuất hiện Mino
        MINO_START_X = left_X + (WIDTH / 2) - Block.Size;
        MINO_START_Y = top_Y + Block.Size;

        //Tọa độ hiển thị nextMino NGOC
        NEXTMINO_X = right_X + 100 + 70 ;
        NEXTMINO_Y = bottom_Y - 200 + 60 + 40;//left_X + 100; cái cũ tọa độ bị lệch ra ngoài

        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    public PlayManager() {
    }

    private Mino pickMino() { //random 1 loại Mino bất kỳ
        int random = (int)(Math.random() * 7); //random 0 -> 6 (7 giá trị)
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
        //Nếu game đang tạm dừng thì không cho Mino rơi
        if (effectCounterOn) return; //tạm dừng game, không cho mảnh rơi tiếp nếu chưa xóa dòng xong
        if (!currentMino.active) {
            // Mino đã rớt xong
            //currentMino.addBlocksToStatic();
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

        //Đếm số block mỗi dòng
        int[] count = new int[NUMBER_OF_ROWS];

        for (Block block : staticBlocks) {
            int rowIndex = (block.y - top_Y) / Block.Size;
            if (rowIndex >= 0 && rowIndex < NUMBER_OF_ROWS) {
                count[rowIndex]++;
            }
        }

        ArrayList<Integer> rowsToDelete = new ArrayList<>();

        //Xác định dòng nào đầy
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            if (count[i] == NUMBER_OF_COLUMNS) {
                rowsToDelete.add(top_Y + i * Block.Size);
            }
        }

        if (rowsToDelete.isEmpty()) {
            return;
        }

        //Bật hiệu ứng
        effectCounterOn = true;
        effectY.addAll(rowsToDelete);

        //Xóa block ở dòng đầy
        ArrayList<Block> remainingBlocks = new ArrayList<>();
        for (Block block : staticBlocks) {
            if (!rowsToDelete.contains(block.y)) {
                remainingBlocks.add(block);
            }
        }
        GamePanel.soundEffect.play(3, false);

        //Dịch block phía trên xuống
        Collections.sort(rowsToDelete);
        for (int deletedRow : rowsToDelete) {
            for (Block block : remainingBlocks) {
                if (block.y < deletedRow) {
                    block.y += Block.Size;
                }
            }
        }

        //Cập nhật staticBlocks
        staticBlocks.clear();
        staticBlocks.addAll(remainingBlocks);

        //Tính điểm
        int numberOfClearedLines = rowsToDelete.size();
        line += numberOfClearedLines;
        score += numberOfClearedLines * 100;

        if (line % 10 == 0) {
            level++;
            dropInterval = Math.max(1, (int)(dropInterval * 0.9));
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_X - 4, top_Y - 4, WIDTH + 8, HEIGHT + 8);

        // Draw next Mino Frame
        int x = right_X + 100;
        int y = bottom_Y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("Next", x + 60, y + 60);

        // Draw the currentMino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        // Draw the next Mino
        nextMino.draw(g2);

        // Draw the static block
        for (Block staticBlock : staticBlocks) {
            staticBlock.draw(g2);
        }

        // draw effect
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

        // draw pause and game over
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (Gameover) {
            g2.setColor(Color.red);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("GAME OVER", left_X + 50, top_Y + 300);
        }

        if (KeyHandler.pausedPressed && !Gameover) {
            g2.setColor(Color.yellow);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("PAUSED", left_X + 100, top_Y + 350);
        }
        int scoreX = right_X + 50;
        g2.drawRect(scoreX, top_Y, 250, 150);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("LEVEL: " + level, scoreX + 20, top_Y + 50);
        g2.drawString("LINES: " + line, scoreX + 20, top_Y + 90);
        g2.drawString("SCORE: " + score, scoreX + 20, top_Y + 130);

        // draw game title
        x = 35;
        y = top_Y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 50));
        g2.drawString("Simple Tetris", x, y);

    }
}