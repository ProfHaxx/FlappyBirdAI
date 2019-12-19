package main;

import graphics.Pipe;

import java.awt.*;
import java.util.Random;

public class Pipes {
    private static Random random = new Random();
    static int[][] Pipes = new int[3][2];
    static int toTheNextPipe;
    static int NextPipeHeight;

    Pipes() {
        for (int i = 0; i < 3;i++) {
            Pipes[i][0] = Math.abs(random.nextInt() %  200) + 60;
            Pipes[i][1] = 400 + i * 200;
        }
    }

    private static void newPosition(){
        for (int i = 0; i < 2;i++) {
            Pipes[i][0] = Pipes[i+1][0];
            Pipes[i][1] = Pipes[i+1][1];
        }
        Pipes[2][0] = Math.abs(random.nextInt() % 200) + 60;
        Pipes[2][1] = 400;
    }

    static void move(){
        boolean helper = true;
        for (int i = 0; i < 3;i++) {
            Pipes[i][1] -= 2;
            if((Pipes[i][1] >= 57) && helper){
                helper = false;
                toTheNextPipe = Pipes[i][1];
                NextPipeHeight = Pipes[i][0];
            }
        }
    }

    public static void checkForNewPipe(){
        if(Pipes[0][1] == -200){
            newPosition();
        }
    }

    public static void draw(Graphics g){
        for(int[] pipe: Pipes){
            Pipe.draw(g, pipe[1], pipe[0]);
        }
    }
}
