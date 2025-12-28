package elements;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Sun {

    int x, y, width, height;

    public Sun(int x, int y, int width, int height) {
        this.x = x;           // ЛЕВЫЙ ВЕРХНИЙ УГОЛ
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Сглаживание
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // 1. Реальный центр круга
        float centerX = x + width / 2f;
        float centerY = y + height / 2f;

        // 2. Радиус
        float radius = Math.min(width, height) / 2f;

        // 3. Радиальный градиент ПРИВЯЗАН К ЦЕНТРУ
        RadialGradientPaint gradient = new RadialGradientPaint(
                centerX,
                centerY,
                radius,
                new float[] { 0.0f, 0.8f, 1.0f },
                new Color[] {
                        Color.WHITE,
                        Color.YELLOW,
                        Color.RED
                }
        );

        g2.setPaint(gradient);

        // 4. Рисуем круг в том же bounding box
        Shape sun = new Ellipse2D.Float(x, y, width, height);
        g2.fill(sun);
    }
}
