package mino;

import java.awt.Color;
public class Mino_L2 extends Mino {
    public Mino_L2(){
        create(Color.blue);

    }
    public void setXY(int x, int y){
        //  *
        //  *
        //* *
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.Size;
        b[2].x = b[0].x;
        b[3].y = b[0].y + Block.Size;
        b[3].x = b[0].x - Block.Size;
        b[3].y = b[0].y + Block.Size;

    }
    public void getDirection1(){
        //  *
        //  *
        //* *
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].x = b[0].y - Block.Size;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.Size;
        tempB[3].x = b[0].x - Block.Size;
        tempB[3].y = b[0].y + Block.Size;

        updateXY(1);

    }
    public void getDirection2() {
        //*
        //*  *  *
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.Size;
        tempB[1].x = b[0].y;
        tempB[2].x = b[0].x - Block.Size;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x - Block.Size;
        tempB[3].y = b[0].y - Block.Size;

        updateXY(2);
    }
    public void getDirection3(){
        //* *
        //*
        //*
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].x = b[0].y + Block.Size;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.Size;
        tempB[3].x = b[0].x + Block.Size;
        tempB[3].y = b[0].y - Block.Size;

        updateXY(3);
    }
    public void getDirection4(){
        //*  *  *
        //      *
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.Size;
        tempB[1].x = b[0].y;
        tempB[2].x = b[0].x + Block.Size;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.Size;
        tempB[3].y = b[0].y + Block.Size;

        updateXY(4);
    }
}

