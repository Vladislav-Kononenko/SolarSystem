package elements;

import java.awt.*;

public class Axis {
    private Color color = Color.WHITE;
    private int cx, cy, width, height;

    public Axis(int cx, int cy, int width, int height) {
        this.cx = cx;
        this.cy = cy;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        int left = cx - width / 2;
        int top  = cy - height / 2;

        g.setColor(color);
        g.drawOval(left, top, width, height);
    }
}
