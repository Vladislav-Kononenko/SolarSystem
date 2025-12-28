import elements.Stars;
import elements.Sun;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {
    public  DrawPanel() {

    }

    @Override
    public void paint(Graphics gr) {
        super.paint(gr);
        Graphics2D g = (Graphics2D) gr;
        Sun sun = new Sun(900, 500, 100, 100);
        sun.draw(g);
        Stars stars = new Stars(0, 0, 1920, 1080, 300);
        stars.draw(g);
    }
}
