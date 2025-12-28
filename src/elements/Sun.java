package elements;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Sun {

    private int cx, cy;
    private int width, height;

    public Sun(int centerX, int centerY, int width, int height) {
        this.cx = centerX;
        this.cy = centerY;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float radius = Math.min(width, height) / 2f;

            float x = cx - width  / 2f;
            float y = cy - height / 2f;

            RadialGradientPaint gradient = new RadialGradientPaint(
                    new Point.Float(cx, cy),
                    radius,
                    new float[] { 0.0f, 0.75f, 1.0f },
                    new Color[] {
                            new Color(255, 250, 220),
                            new Color(255, 200, 0),
                            new Color(255, 60, 0)
                    }
            );

            g2.setPaint(gradient);
            Shape sun = new Ellipse2D.Float(x, y, width, height);
            g2.fill(sun);

        } finally {
            g2.dispose();
        }
    }

    public void setCenter(int centerX, int centerY) { this.cx = centerX; this.cy = centerY; }
    public void setSize(int width, int height) { this.width = width; this.height = height; }

    public int getCenterX() { return cx; }
    public int getCenterY() { return cy; }
}
