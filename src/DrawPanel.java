import elements.Axis;
import elements.Stars;
import elements.Sun;
import elements.Planet;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    private final int orbitCount = 8;
    private final int margin = 40;
    private final int sunDiameter = 120;
    private final int gapAfterSun = 25;
    private final int starsCount = 300;

    private Stars stars;
    private Sun sun;
    private Axis[] axes;
    private Planet[] planets;

    private long lastNs = System.nanoTime();
    private final Timer timer;

    // скорость “дрейфа” звёзд (px/sec)
    private double starVx = 15.0;
    private double starVy = 6.0;

    private int lastW = -1;
    private int lastH = -1;

    // чтобы звёзды не “перегенерились” при ресайзе случайно
    private long starsSeed = System.nanoTime();

    public DrawPanel() {
        setBackground(Color.BLACK);

        timer = new Timer(16, e -> {
            long now = System.nanoTime();
            double dt = (now - lastNs) / 1_000_000_000.0;
            lastNs = now;

            if (stars != null) {
                stars.update(dt, starVx, starVy);
            }
            if (planets != null) {
                for (Planet p : planets) p.update(dt);
            }

            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int cx = w / 2;
            int cy = h / 2;

            // 1) Инициализация сцены 1 раз
            if (sun == null) {
                initScene(w, h, cx, cy);
                lastW = w;
                lastH = h;
            }

            // 2) Реагируем на ресайз
            if (w != lastW || h != lastH) {
                lastW = w;
                lastH = h;

                // Пересоздаём звёзды с тем же seed (распределение стабильно),
                // а движение продолжится через update()
                stars = new Stars(0, 0, w, h, starsCount, starsSeed);

                // солнце/орбиты будут подстроены ниже через setCenter(...)
            }

            // 3) Центр всегда актуальный
            sun.setCenter(cx, cy);
            for (Axis a : axes) a.setCenter(cx, cy);
            for (Planet p : planets) p.setCenter(cx, cy); // это “центр системы”, не позиция планеты

            // 4) Рисование: фон -> орбиты -> солнце -> планеты
            stars.draw(g);

            for (Axis a : axes) a.draw(g);
            sun.draw(g);
            for (Planet p : planets) p.draw(g);

        } finally {
            g.dispose();
        }
    }

    private void initScene(int w, int h, int cx, int cy) {
        stars = new Stars(0, 0, w, h, starsCount, starsSeed);

        sun = new Sun(cx, cy, sunDiameter, sunDiameter);

        int sunR = sunDiameter / 2;
        int maxR = Math.min(w, h) / 2 - margin;
        int startR = sunR + gapAfterSun;
        int step = (maxR - startR) / orbitCount;

        axes = new Axis[orbitCount];
        planets = new Planet[orbitCount];

        for (int i = 0; i < orbitCount; i++) {
            int r = startR + (i + 1) * step;

            axes[i] = new Axis(cx, cy, 2 * r, 2 * r);

            double angle = i * (2.0 * Math.PI / orbitCount);
            double speed = 0.7;

            Planet p = new Planet(cx, cy, r, 10); // временный диаметр, потом зададим через setSize
            p.setAngularSpeed(speed);
            p.update(angle / speed);

            // ---- НАСТРОЙКИ КОНКРЕТНЫХ ПЛАНЕТ (размер + градиент) ----
            int planetD;
            Color c0, c1, c2;

            switch (i) {
                case 0: // Mercury
                    planetD = 10;
                    c0 = new Color(245, 245, 245);
                    c1 = new Color(170, 170, 170);
                    c2 = new Color(90, 90, 90);
                    break;

                case 1: // Venus
                    planetD = 16;
                    c0 = new Color(255, 245, 220);
                    c1 = new Color(230, 190, 120);
                    c2 = new Color(170, 120, 70);
                    break;

                case 2: // Earth
                    planetD = 18;
                    c0 = new Color(220, 245, 255);
                    c1 = new Color(60, 150, 255);
                    c2 = new Color(10, 70, 160);
                    break;

                case 3: // Mars
                    planetD = 14;
                    c0 = new Color(255, 230, 220);
                    c1 = new Color(220, 110, 70);
                    c2 = new Color(140, 60, 40);
                    break;

                case 4: // Jupiter
                    planetD = 32;
                    c0 = new Color(255, 250, 235);
                    c1 = new Color(210, 170, 120);
                    c2 = new Color(150, 110, 80);
                    break;

                case 5: // Saturn
                    planetD = 28;
                    c0 = new Color(255, 252, 235);
                    c1 = new Color(220, 200, 140);
                    c2 = new Color(160, 140, 90);
                    break;

                case 6: // Uranus
                    planetD = 22;
                    c0 = new Color(235, 255, 255);
                    c1 = new Color(140, 230, 230);
                    c2 = new Color(70, 170, 180);
                    break;

                case 7: // Neptune
                default:
                    planetD = 22;
                    c0 = new Color(230, 240, 255);
                    c1 = new Color(80, 140, 255);
                    c2 = new Color(20, 60, 160);
                    break;
            }

            p.setSize(planetD, planetD);
            p.setGradient(c0, c1, c2);

            planets[i] = p;
        }
    }
}
