package elements;

import java.awt.*;
import java.util.Random;

public class Stars {

    private Color color = Color.WHITE;
    private int x, y, width, height;

    private int[] xs;
    private int[] ys;
    private int count;

    private long seed;
    private double twinkleAcc = 0.0;


    // смещение "камеры"
    private double offX = 0.0;
    private double offY = 0.0;

    public Stars(int x, int y, int width, int height, int count) {
        this(x, y, width, height, count, System.nanoTime());
    }

    public Stars(int x, int y, int width, int height, int count, long seed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.count = Math.max(0, count);
        this.seed = seed;

        generateStars();
    }

    // ВАЖНО: не пересоздаём звёзды каждый кадр.
    // Пересоздаём только если реально поменялась область (например ресайз).
    public void setBounds(int x, int y, int width, int height) {
        boolean changed = (this.x != x) || (this.y != y) || (this.width != width) || (this.height != height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (changed) {
            // оставляем offset как есть, чтобы "камера" не сбрасывалась
            generateStars();
        }
    }

    public void update(double dt, double vx, double vy) {
        if (count == 0 || width <= 0 || height <= 0) return;

        // "Мерцание": копим время и раз в небольшой интервал
        // полностью пересэмпливаем позиции звёзд.
        twinkleAcc += dt;

        // подбери: 0.03..0.12 (меньше = чаще мелькает)
        final double interval = 0.06;

        if (twinkleAcc >= interval) {
            twinkleAcc = 0.0;

            // Делаем новый seed (быстро и детерминированно перемешиваем)
            seed = seed * 6364136223846793005L + 1442695040888963407L;

            // Перегенерация координат = "мелькание"
            generateStars();
        }
    }


    private void generateStars() {
        xs = new int[count];
        ys = new int[count];

        if (count == 0 || width <= 0 || height <= 0) return;

        Random rnd = new Random(seed);

        double aspect = (height == 0) ? 1.0 : (width / (double) height);
        int cols = (int) Math.ceil(Math.sqrt(count * aspect));
        int rows = (int) Math.ceil(count / (double) cols);

        double cellW = width / (double) cols;
        double cellH = height / (double) rows;

        int i = 0;
        for (int r = 0; r < rows && i < count; r++) {
            for (int c = 0; c < cols && i < count; c++) {

                double baseX = x + c * cellW;
                double baseY = y + r * cellH;

                double marginX = cellW * 0.15;
                double marginY = cellH * 0.15;

                double px = baseX + marginX + rnd.nextDouble() * Math.max(0.0, cellW - 2 * marginX);
                double py = baseY + marginY + rnd.nextDouble() * Math.max(0.0, cellH - 2 * marginY);

                xs[i] = (int) Math.round(px);
                ys[i] = (int) Math.round(py);
                i++;
            }
        }
    }

    public void draw(Graphics g) {
        if (count == 0 || width <= 0 || height <= 0) return;

        g.setColor(color);

        int dx = (int) Math.round(offX);
        int dy = (int) Math.round(offY);

        for (int i = 0; i < count; i++) {
            int px = xs[i] + dx;
            int py = ys[i] + dy;

            // wrap по области (x..x+width), (y..y+height)
            px = x + Math.floorMod(px - x, width);
            py = y + Math.floorMod(py - y, height);

            g.fillRect(px, py, 1, 1);
        }
    }
}
