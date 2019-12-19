package graphics;

import main.Map;

import java.awt.*;

public class Pipe {
    public static void draw(Graphics g, int x, int y){
        g.drawImage(Map.UpperPipe, x, y + 50 - 400, null);
        g.drawImage(Map.LowerPipe, x, y + 80, null);
    }
}
