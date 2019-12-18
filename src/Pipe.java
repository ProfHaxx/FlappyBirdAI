import java.awt.*;

public class Pipe {
    public static void Draw(Graphics g, int x,int y){
        //g.fillRect(x,0,50, y);
        //g.fillRect(x,y + 80, 50, 350);
        g.drawImage(Map.UpperPipe, x, y + 50 - 400, null);
        g.drawImage(Map.LowerPipe, x, y + 80, null);
    }
}
