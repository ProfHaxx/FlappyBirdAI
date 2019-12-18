
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Map extends JFrame {
    private Image dbImage;
    private Graphics dbg;
    public static Pipes pipes = new Pipes();

    public static int ammount = 100;

    public static Control[] allc = new Control[ammount];
    public static int[] howfar = new int[ammount];
    public static int[] toPipeMiddle = new int[ammount];
    public static int[] duration = new int[ammount];
    //public static Selection[] evolution = new Selection[ammount];

    public static String imagePath = "./images/pixil-frame-0-7.png";
    public static String backgroundPath = "./images/6a72573ec9585f1523ddfec450c0d255.jpg";
    public static String UpperPipePath = "./images/pixil-frame-0-4.png";
    public static String LowerPipePath = "./images/pixil-frame-0-5.png";
    public static String EndPath = "./images/pixil-frame-0-9.png";

    public static Image player;
    public static Image UpperPipe;
    public static Image LowerPipe;
    public static Image background;
    public static Image End;

    public static int xPosOfTheBird = 100;
    public static int[] yPosOfTheBird = new int[ammount];
    private static int timer = 0;

    public static HashMap<Genome, Double> GenomesAndItsFitness = new HashMap<>();
    private static Evaluator evaluator;
    private static boolean allAlive = true;
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
        dbImage = createImage(getWidth(), getHeight());
        dbg = dbImage.getGraphics();
        paintOneScene(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    private void paintOneScene(Graphics g){
        //Graphics2D g2 = (Graphics2D) g;
        g.drawImage(background, 0,0,this);

        while (starting){
            Utility.waitMilliseconds(5);
        }

        for(int i = 0; i < ammount; i++) {
            helperArray.get(i).setInput(pipes.NextPipeHight - yPosOfTheBird[i] + 65, allc[i].state, pipes.NextPipeHight - yPosOfTheBird[i],pipes.toTheNextPipe);
            yPosOfTheBird[i] = (int) allc[i].ChangeAll((int) (0.05 * helperArray.get(i).count(helperArray.get(i).OutputNode)), i);
        }

        timer++;
        for(int i = 0; i < ammount; i++){
            try {
                if (allc[i].alive) {
                    Bird.DrawBird(g, xPosOfTheBird - 1, yPosOfTheBird[i] - 1, allc[i]);

                } else {
                    howfar[i] = allc[i].counter.counter;
                    if (howfar[i] >= 1) {
                        toPipeMiddle[i] = (int) allc[i].lengthBetweenPipes / howfar[i] + 1;
                    }
                    else{
                        toPipeMiddle[i] = 1000;
                    }
                    if(duration[i] == 0) {
                        duration[i] = timer;
                    }
                }
                if(Counter.GlobalCounter < allc[i].counter.counter){
                    Counter.addToGlobalCounter();
                }
            }
            catch (NullPointerException e){
            }
        }

        if(KeyListener.key_situationV1[5] == 1){
            helper = false;
            Utility.waitMilliseconds(50);
        }
        if(KeyListener.key_situationV1[6] == 1){
            speed = speedSituation.ONE;
        }
        else if(KeyListener.key_situationV1[7] == 1){
            speed = speedSituation.TW0;
        }
        else if(KeyListener.key_situationV1[8] == 1){
            speed = speedSituation.THREE;
        }
        else if(KeyListener.key_situationV1[9] == 1){
            speed = speedSituation.FOUR;
        }

        if(helpChecker() && helper) {
            Pipes.Draw(g);
            WAIT();
            Pipes.checkIfNeedsToBeSetNewPipe();
            Pipes.move();
            g.drawString(Counter.GlobalCounter + "", 190,50);
            g.drawString("Generation " + generationCounter , 10,50);
            for(int i = 0; i < ammount; i++) {
                //System.out.println(evolution[i].calculate());
            }
        }
        else{
            generationCounter++;
            System.out.println("------------------");
            Utility.waitMilliseconds(50);

            for(int i = 0; i < ammount; i++){
                double score = 100 * (howfar[i] + 1) + 5 * duration[i] + 100 / toPipeMiddle[i];
                GenomesAndItsFitness.put(helperArray2.get(i), score);
            }
            helperArray.clear();
            helperArray2.clear();
            allAlive = true;
            evaluator.evaluate();
            System.out.println(evaluator.getSpeciesAmount()  + "Amount of species");
            System.out.println(evaluator.getHighestFitness() + " Highest fitness");
            Utility.waitMilliseconds(5);
            pipes = new Pipes();
             //Selection.evaluet();
            for(int i = 0; i < ammount; i++){
                yPosOfTheBird[i] = 100;
                xPosOfTheBird = 100;
                allc[i].setNew();
                allc[i].counter.resetCounter();
                howfar[i] = -1;
                duration[i] = 0;
            }
            allAlive = true;
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
            player = ImageIO.read(new File(imagePath));
            background = ImageIO.read(new File(backgroundPath));
            UpperPipe = ImageIO.read(new File(UpperPipePath));
            LowerPipe = ImageIO.read(new File(LowerPipePath));
            End = ImageIO.read(new File(EndPath));
        }
        catch (IOException e){
            System.out.print("Error 404: " +
                    "File not found");
        }

        Random r = new Random();
        FlappyBirdGenome flappyBirdGenome = new FlappyBirdGenome();

        evaluator = new Evaluator(ammount,flappyBirdGenome.genome) {
            @Override
            protected double fitness(Genome genome) {
                double answer = GenomesAndItsFitness.get(genome);
                return answer;
            }
        };


        Bird.SetupPlayer();
        Map map = new Map();
    }

    private boolean helpChecker(){
        for(int i: howfar){
            if(i < 0){
                return true;
            }
        }
        return false;
    }

    private Map(){
        addKeyListener(new KeyListener());
        setSize(400, 400);
        setTitle("Flappybird");
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(50,50);

        for(int i = 0; i < ammount; i++){
            allc[i] = new Control();
            yPosOfTheBird[i] = 100;
            howfar[i] = -1;
            duration[i] = 0;
            toPipeMiddle[i] = 1000;
        }

        for (Genome genome : evaluator.getGenomes()) {
            helperArray.add(new FlappyBirdGenome(genome));
            helperArray2.add(genome);
        }

        speed =speedSituation.THREE;
        starting = false;

    }

    private static void FlapOrNotToFlap(){}

    private static void WAIT(){
        if(speed == speedSituation.ONE) {
            Utility.waitMilliseconds(30000);
        }
        else if(speed == speedSituation.TW0){
            Utility.waitMilliseconds(3000);
        }
        else if(speed == speedSituation.THREE){
            Utility.waitMilliseconds(1000);
        }
        else if(speed == speedSituation.FOUR){
            Utility.waitMilliseconds(100);
        }
    }
}