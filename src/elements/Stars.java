package elements;

import java.awt.*;
import java.util.Random;

public class Stars {

    private Color color;
    private int x, y, width, height;

    // Координаты каждой звезды (точки)
    private int[] xs;
    private int[] ys;
    private int count;

    // Чтобы можно было пересоздавать тот же рисунок по seed (удобно для дебага)
    private long seed;

    public Stars(int x, int y, int width, int height, int count) {
        this(x, y, width, height, count, System.nanoTime());
    }

    public Stars(int x, int y, int width, int height, int count, long seed) {
        this.color = Color.WHITE;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.count = Math.max(0, count);
        this.seed = seed;

        generateStars();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = Math.max(0, count);
        generateStars(); // пересоздаём распределение
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        generateStars(); // пересоздаём распределение
    }

    /**
     * Алгоритм равномерного распределения:
     * 1) выбираем cols и rows так, чтобы cols*rows >= count
     * 2) в каждую клетку кладём не более 1 звезды (перебираем клетки)
     * 3) внутри клетки выбираем случайную позицию (jitter)
     */
    private void generateStars() {
        xs = new int[count];
        ys = new int[count];

        if (count == 0 || width <= 0 || height <= 0) return;

        Random rnd = new Random(seed);

        // Подбираем сетку примерно квадратную: cols ~ sqrt(count * aspect)
        double aspect = (height == 0) ? 1.0 : (width / (double) height);
        int cols = (int) Math.ceil(Math.sqrt(count * aspect));
        int rows = (int) Math.ceil(count / (double) cols);

        // Размер клетки
        double cellW = width / (double) cols;
        double cellH = height / (double) rows;

        int i = 0;
        for (int r = 0; r < rows && i < count; r++) {
            for (int c = 0; c < cols && i < count; c++) {

                // База клетки (левый верх)
                double baseX = x + c * cellW;
                double baseY = y + r * cellH;

                // Jitter: случайно внутри клетки, но с небольшими полями, чтобы не прилипало к границам
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

    /**
     * Рисуем звёзды как белые точки.
     * Можно легко заменить fillRect на fillOval, или добавить размер/яркость позже.
     */
    public void draw(Graphics g) {
        g.setColor(color);

        for (int i = 0; i < count; i++) {
            g.fillRect(xs[i], ys[i], 1, 1); // 1x1 пиксель
        }
    }
}

