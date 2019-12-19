package main;

import config.ImageConfig;
import graphics.Bird;
import util.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Map extends JFrame {

    static int amount = 100;

    private static Control[] allControls = new Control[amount];
    private static int[] distance = new int[amount];
    private static int[] toPipeMiddle = new int[amount];
    private static int[] duration = new int[amount];

    public static Image UpperPipe;
    public static Image LowerPipe;
    private static Image background;

    static int xPosOfTheBird = 100;
    static int[] yPosOfTheBird = new int[amount];
    private static int timer = 0;

    private static HashMap<Genome, Double> GenomesAndItsFitness = new HashMap<>();
    private static Evaluator evaluator;
    private static speedSituation speed = speedSituation.ONE;
    private static int generationCounter = 1;
    private static ArrayList<FlappyBirdGenome> helperArray = new ArrayList<>();
    private static ArrayList<Genome> helperArray2 = new ArrayList<>();
    private static boolean helper = true;
    private static boolean starting = true;

    private enum speedSituation {
        ONE,
        TW0,
        THREE,
        FOUR
    }

    public void paint(Graphics g) {
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        paintOneScene(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    private void paintOneScene(Graphics g){
        g.drawImage(background, 0,0,this);

        while (starting){
            Util.sleep(5);
        }

        for(int i = 0; i < amount; i++) {
            helperArray.get(i).setInput(
                    Pipes.NextPipeHeight - yPosOfTheBird[i] + 65,
                    allControls[i].state,
                    Pipes.NextPipeHeight - yPosOfTheBird[i],
                    Pipes.toTheNextPipe);

            yPosOfTheBird[i] =
                    (int) allControls[i].changeAll(
                            (int) (0.05 * helperArray.get(i).count(helperArray.get(i).OutputNode)), i);
        }

        timer++;

        for(int i = 0; i < amount; i++){
            try {
                if (allControls[i].alive) {
                    Bird.DrawBird(g, xPosOfTheBird - 1, yPosOfTheBird[i] - 1, allControls[i]);

                } else {
                    distance[i] = allControls[i].counter.counter;
                    if (distance[i] >= 1) {
                        toPipeMiddle[i] = (int) allControls[i].lengthBetweenPipes / distance[i] + 1;
                    } else {
                        toPipeMiddle[i] = 1000;
                    }

                    if(duration[i] == 0) {
                        duration[i] = timer;
                    }
                }
                if(Counter.GlobalCounter < allControls[i].counter.counter){
                    Counter.addToGlobalCounter();
                }
            } catch (NullPointerException ignored){ }
        }

        if(KeyListener.key_situationV1[5] == 1){
            helper = false;
            Util.sleep(50);
        }

        if(KeyListener.key_situationV1[6] == 1){
            speed = speedSituation.ONE;
        } else if(KeyListener.key_situationV1[7] == 1){
            speed = speedSituation.TW0;
        } else if(KeyListener.key_situationV1[8] == 1){
            speed = speedSituation.THREE;
        } else if(KeyListener.key_situationV1[9] == 1){
            speed = speedSituation.FOUR;
        }

        if(helpChecker() && helper) {
            Pipes.draw(g);
            WAIT();
            Pipes.checkForNewPipe();
            Pipes.move();
            g.drawString(Counter.GlobalCounter + "", 190,50);
            g.drawString("Generation " + generationCounter , 10,50);
        } else {
            generationCounter++;
            System.out.println("------------------");
            Util.sleep(50);

            for(int i = 0; i < amount; i++){
                double score = 100 * (distance[i] + 1) + 5 * duration[i] + 100 / toPipeMiddle[i];
                GenomesAndItsFitness.put(helperArray2.get(i), score);
            }
            helperArray.clear();
            helperArray2.clear();
            evaluator.evaluate();
            System.out.println(evaluator.getSpeciesAmount()  + "Amount of species");
            System.out.println(evaluator.getHighestFitness() + " Highest fitness");
            Util.sleep(5);
            new Pipes();

            for(int i = 0; i < amount; i++){
                yPosOfTheBird[i] = 100;
                xPosOfTheBird = 100;
                allControls[i].setNew();
                allControls[i].counter.resetCounter();
                distance[i] = -1;
                duration[i] = 0;
            }
            timer = 0;
            helper = true;
            Counter.resetGlobalCounter();
            for (Genome genome : evaluator.getGenomes()) {
                helperArray.add(new FlappyBirdGenome(genome));
                helperArray2.add(genome);
            }
        }
        repaint();
    }

    public static void main(String[] a) {
        try {
            background = ImageIO.read(new File(ImageConfig.background));
            UpperPipe = ImageIO.read(new File(ImageConfig.pipe_down));
            LowerPipe = ImageIO.read(new File(ImageConfig.pipe_up));
        } catch (IOException e){
            System.out.print("Error 404: File not found");
        }

        FlappyBirdGenome flappyBirdGenome = new FlappyBirdGenome();

        evaluator = new Evaluator(amount,flappyBirdGenome.genome) {
            @Override
            protected double fitness(Genome genome) {
                return GenomesAndItsFitness.get(genome);
            }
        };


        Bird.setupPlayer();
        new Map();
    }

    private boolean helpChecker(){
        for(int i: distance){
            if(i < 0){
                return true;
            }
        }
        return false;
    }

    private Map(){
        addKeyListener(new KeyListener());
        setSize(400, 400);
        setTitle("Flappy graphics.Bird A.I.");
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(50,50);

        for(int i = 0; i < amount; i++){
            allControls[i] = new Control();
            yPosOfTheBird[i] = 100;
            distance[i] = -1;
            duration[i] = 0;
            toPipeMiddle[i] = 1000;
        }

        for (Genome genome : evaluator.getGenomes()) {
            helperArray.add(new FlappyBirdGenome(genome));
            helperArray2.add(genome);
        }

        speed = speedSituation.THREE;
        starting = false;

    }

    private static void WAIT(){
        if(speed == speedSituation.ONE) {
            Util.sleep(30000);
        }
        else if(speed == speedSituation.TW0){
            Util.sleep(3000);
        }
        else if(speed == speedSituation.THREE){
            Util.sleep(1000);
        }
        else if(speed == speedSituation.FOUR){
            Util.sleep(100);
        }
    }
}