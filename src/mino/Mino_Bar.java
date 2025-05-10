package mino;

import java.awt.Color;

public class Mino_Bar extends Mino {
    public Mino_Bar() {
        create(Color.cyan);
    }

    @Override
    public void setXY(int x, int y) { //xác định tọa độ ban đầu của các block.
        //
        // *  *  *  *
        //
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x - Block.Size;
        b[1].y = b[0].y;
        b[2].x = b[0].x + Block.Size;
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.Size * 2;
        b[3].y = b[0].y;
    }

    public void getDirection1() { // xác định trạng thái xoay thứ X (1–4).
        //
        // *  *  *  *
        //
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.Size;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.Size;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.Size*2;
        tempB[3].y = b[0].y;

        updateXY(1); //cập nhật trạng thái mới sau khi xoay.
    }
    public void getDirection2() {
        // *
        // *
        // *
        // *
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.Size;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.Size;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y + Block.Size*2;

        updateXY(2);
    }
    public void getDirection3() {
        getDirection1();
    }
    public void getDirection4() {
        getDirection2();
    }
}
