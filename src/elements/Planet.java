package elements;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Planet {
    private int centerX, centerY;
    private int orbitRadius;
    private int diameter;

    private double angle;
    private double angularSpeed;

    // Градиент (центр -> середина -> край)
    private Color cInner = new Color(240, 240, 240);
    private Color cMid   = new Color(180, 180, 180);
    private Color cOuter = new Color(110, 110, 110);

    public Planet(int centerX, int centerY, int orbitRadius, int diameter) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.orbitRadius = orbitRadius;
        this.diameter = diameter;

        this.angle = 0.0;
        this.angularSpeed = 0.6;
    }

    public void setCenter(int cx, int cy) {
        this.centerX = cx;
        this.centerY = cy;
    }

    public void setSize(int width, int height) {
        this.diameter = Math.max(1, Math.min(width, height));
    }


    // --- Внешний вид ---
    public void setGradient(Color inner, Color mid, Color outer) {
        this.cInner = inner;
        this.cMid = mid;
        this.cOuter = outer;
    }

    // --- Движение ---
    public void setAngularSpeed(double radPerSec) {
        this.angularSpeed = radPerSec;
    }

    public void update(double dtSeconds) {
        angle += angularSpeed * dtSeconds;
        angle %= (Math.PI * 2.0);
        if (angle < 0) angle += Math.PI * 2.0;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // центр планеты на орбите
        double px = centerX + orbitRadius * Math.cos(angle);
        double py = centerY + orbitRadius * Math.sin(angle);
        float r = diameter / 2f;
        float left = (float) px - r;
        float top = (float) py - r;

        // “блик” (смещение источника градиента)
        float hx = (float) (px - r * 0.35);
        float hy = (float) (py - r * 0.35);
        RadialGradientPaint paint = new RadialGradientPaint(
                new Point.Float(hx, hy),
                r,
                new float[]{0.0f, 0.75f, 1.0f},
                new Color[]{cInner, cMid, cOuter}
        );
        g2.setPaint(paint);
        g2.fill(new Ellipse2D.Float(left, top, diameter, diameter));
        g2.dispose();
    }
}
