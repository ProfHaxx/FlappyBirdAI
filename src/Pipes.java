

import java.awt.*;
import java.util.Random;

public class Pipes {
    private static Random random = new Random();
    public static int[][] Pipes = new int[3][2];
    public static int toTheNextPipe;
    public static int NextPipeHight;

    public Pipes()
    {
        for (int i = 0; i < 3;i++) {
            Pipes[i][0] = Math.abs(random.nextInt() %  200) + 60;
            //Pipes[i][0] = 100;
            Pipes[i][1] = 400 + i * 200;
        }
    }

    public static void newPosition(){
        for (int i = 0; i < 2;i++) {
            Pipes[i][0] = Pipes[i+1][0];
            Pipes[i][1] = Pipes[i+1][1];
        }
        Pipes[2][0] = Math.abs(random.nextInt() % 200) + 60;
        //Pipes[2][0] = 100;
        Pipes[2][1] = 400;
    }

    public static void move(){
        boolean helper = true;
        for (int i = 0; i < 3;i++) {
            Pipes[i][1] -= 2;
            if((Pipes[i][1] >= 57) && helper){
                helper = false;
                toTheNextPipe = Pipes[i][1];
                NextPipeHight = Pipes[i][0];
            }
        }
    }

    public static void checkIfNeedsToBeSetNewPipe(){
        if(Pipes[0][1] == -200){
            newPosition();
        }
    }

    public static void Draw(Graphics g){
        for(int[] pipe: Pipes){
            Pipe.Draw(g, pipe[1], pipe[0]);
        }
    }
}
