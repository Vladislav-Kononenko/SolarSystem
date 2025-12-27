import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {
    public  DrawPanel() {

    }

    @Override
    public void paint(Graphics gr) {
        super.paint(gr);
        Graphics2D g = (Graphics2D) gr;
        g.setColor(Color.black);
        g.drawLine(10, 10, 200, 100);
        g.drawRect(50, 10, 30, 100);
        g.drawOval(50, 10, 30, 100);
    }
}
