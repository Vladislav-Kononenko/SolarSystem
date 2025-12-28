import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() throws  HeadlessException {
        DrawPanel dp = new DrawPanel();
        dp.setBackground(Color.BLACK);
        this.add(dp);
    }
}
