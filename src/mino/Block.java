package mino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Block extends Rectangle {
    public int x, y;
    public static final int Size = 30; // 30x30 block
    public Color c;

    public Block(Color c) {
        this.c = c;

    }

    public void draw(Graphics2D g2) {
        int margine = 2;

        g2.setColor(c);
        g2.fillRect(x + margine, y + margine, Size - (margine * 2), Size - (margine * 2));
    }

}