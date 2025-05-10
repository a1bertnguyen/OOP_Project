package mino;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Mino {
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    public int autoDropCounter = 0;
    public int direction = 1; // there are 4 direction (1/2/3/4)
    public boolean leftCollision, rightCollision, bottomCollision; // kiểm soát logic chuyển dộng xoay
    public boolean active = true;
    public boolean deactivating;
    public int deactivateCounter = 0;

    public void create(Color c) {
        b[0] = new Block(c);//
        b[1] = new Block(c);//
        b[2] = new Block(c);//khởi tạo 4 block chính
        b[3] = new Block(c);//
        tempB[0] = new Block(c);//
        tempB[1] = new Block(c);// 4 block tạm nhưng do trùng vị trí
        tempB[2] = new Block(c);//
        tempB[3] = new Block(c);//
    }

    public void setXY(int x, int y) {

    }

    public void updateXY(int direction) { // kiểm tra va chạm, neeusko va chạm thì cập nhận tu tempb sang b
        checkRotationCollision();// kiểm tra va chạm từ tường với block khác
        if (!leftCollision && !rightCollision && !bottomCollision) {
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x ;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;

        }
    }

    public void getDirection1() {
    }

    public void getDirection2() {
    }

    public void getDirection3() {
    }

    public void getDirection4() {
    }

    public void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;
        checkStaticCollision();
        // check frame collison
        for (int i = 0; i < b.length; i++) {
            if (b[i].x == PlayManager.left_X) {
                leftCollision = true;
            }
            if (b[i].x + Block.Size == PlayManager.right_X) {
                rightCollision = true;
            }
            if (b[i].y + Block.Size == PlayManager.bottom_Y) {
                bottomCollision = true;
            }
        }
        //PNHU: giảm lặp
    }

    public void checkRotationCollision() {
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x < PlayManager.left_X) {
                leftCollision = true;
            }
            if (tempB[i].x + Block.Size > PlayManager.right_X) {
                rightCollision = true;
            }
            if (tempB[i].y + Block.Size > PlayManager.bottom_Y) {
                bottomCollision = true;
            }
        } //PNHU: giảm lặp
    }

    private void checkStaticCollision() {

        for (int i = 0; i < PlayManager.staticBlocks.size(); i++) {
            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            for (int ii = 0; ii < b.length; ii++) {
                if (b[ii].y + Block.Size == targetY && b[ii].x == targetX) {
                    bottomCollision = true;
                }
                if (b[ii].x - Block.Size == targetX && b[ii].y == targetY) {
                    leftCollision = true;
                }
                if (b[ii].x + Block.Size == targetX && b[ii].y == targetY) {
                    rightCollision = true;
                }
            } //PNHU: giảm lặp
        }
    }

    public void update() {
        if (deactivating) {
            deactivating();
        }
        // Move the mino
        if (KeyHandler.upPressed) {
            switch (direction) {
                case 1:
                    getDirection2();
                    break;
                case 2:
                    getDirection3();
                    break;
                case 3:
                    getDirection4();
                    break;
                case 4:
                    getDirection1();
                    break;
            }
            KeyHandler.upPressed = false;
        }
        checkMovementCollision();
        if (KeyHandler.downPressed) {
            // if the mino's bottom is not hitting, it can go down
            if (!bottomCollision) {
                b[0].y += Block.Size;
                b[1].y += Block.Size;
                b[2].y += Block.Size;
                b[3].y += Block.Size;
                // when moved down, reset the autoDropcouter
                autoDropCounter = 0;
            }
            KeyHandler.downPressed = false;
        }

        if (KeyHandler.leftPressed) {
            if (!leftCollision) {
                b[0].x -= Block.Size;
                b[1].x -= Block.Size;
                b[2].x -= Block.Size;
                b[3].x -= Block.Size;
            }

            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed) {
            if (!rightCollision) {
                b[0].x += Block.Size;
                b[1].x += Block.Size;
                b[2].x += Block.Size;
                b[3].x += Block.Size;
            }

            KeyHandler.rightPressed = false;
        }
        if (bottomCollision) {
            if (!deactivating) {
                //GamePanel.soundEffect.play(4, false); NGOC
            }
            deactivating = true;
        } else {
            autoDropCounter++;
            if (autoDropCounter == PlayManager.dropInterval) {
                // the mino goes down
                b[0].y += Block.Size;
                b[1].y += Block.Size;
                b[2].y += Block.Size;
                b[3].y += Block.Size;
                autoDropCounter = 0;
            }
        }
    }

    private void deactivating() {
        deactivateCounter++;
        // wait 45 frame until diactivate

        if (deactivateCounter == 45) {
            deactivateCounter = 0;
            checkMovementCollision(); // check if the bottom is still hiting

            // if the bottom is stil hitting afer 45 frame, deactivate the mino
            if (bottomCollision) {
                active = false;
            }
        }
    }

    public void draw(Graphics2D g2) {
        int margin = 2;

        g2.setColor(b[0].c);
        g2.fillRect(b[0].x + margin, b[0].y + margin, Block.Size - (margin * 2), Block.Size - (margin * 2));
        g2.fillRect(b[1].x + margin, b[1].y + margin, Block.Size - (margin * 2), Block.Size - (margin * 2));
        g2.fillRect(b[2].x + margin, b[2].y + margin, Block.Size - (margin * 2), Block.Size - (margin * 2));
        g2.fillRect(b[3].x + margin, b[3].y + margin, Block.Size - (margin * 2), Block.Size - (margin * 2));
    }

    public void addBlocksToStatic() {
    }
}