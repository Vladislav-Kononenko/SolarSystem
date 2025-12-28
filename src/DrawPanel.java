import elements.Axis;
import elements.Stars;
import elements.Sun;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    private final int orbitCount = 8;
    private final int margin = 40;
    private final int sunDiameter = 120;
    private final int gapAfterSun = 25;
    private final int starsCount = 300;

    @Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr;

        int w = getWidth();
        int h = getHeight();

        int cx = w / 2;
        int cy = h / 2;

        Stars stars = new Stars(0, 0, w, h, starsCount);
        stars.draw(g);

        Sun sun = new Sun(cx, cy, sunDiameter, sunDiameter);
        sun.draw(g);

        int sunR = sunDiameter / 2;
        int maxR = Math.min(w, h) / 2 - margin;
        int startR = sunR + gapAfterSun;

        int step = (maxR - startR) / orbitCount;

        for (int i = 0; i < orbitCount; i++) {
            int r = startR + (i + 1) * step;
            Axis axis = new Axis(cx, cy, 2 * r, 2 * r);
            axis.draw(g);
        }
    }
}
