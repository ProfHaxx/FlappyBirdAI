package main;

public class Control{
    private double pos;
    public double state;
    boolean alive;
    private boolean isInThePipeOnce;
    private int isInThePipeOnceChecker;
    Counter counter;
    double lengthBetweenPipes;
    
    Control(){
        pos = Map.xPosOfTheBird;
        state = 0;
        alive = true;
        isInThePipeOnce = true;
        isInThePipeOnceChecker = 0;
        counter = new Counter();
        lengthBetweenPipes = 0;
    }


    double changeAll(int jump, int number){
        double acceleration = 0.076;
        if(jump == 1){
            state = acceleration * -28;
        } else {
            state += acceleration;
        }

        pos += state * Math.abs(state);
        if(pos <= 0){
            pos = 0;
            alive = false;
        } else if(pos >= 385){
            pos = 385;
            alive = false;
        }

        if(checkPipes(number)){
            alive = false;
        }
        return pos;
    }

    private  boolean checkPipes(int number){
        boolean insidePipeGap = false;
        for(int i = 0; i < 3; i++){
            if((Pipes.Pipes[i][1] >= 57)&&(Pipes.Pipes[i][1] <= 107)){
                if (!((Pipes.Pipes[i][0] < Map.yPosOfTheBird[number])&&(Pipes.Pipes[i][0] > Map.yPosOfTheBird[number] - 65))){
                        insidePipeGap = true;
                } else {
                    if (isInThePipeOnce){
                        counter.add();
                        lengthBetweenPipes += Math.abs( Pipes.Pipes[i][0] - Map.yPosOfTheBird[number] - 32);
                        isInThePipeOnce = false;
                        isInThePipeOnceChecker = i;
                    }
                }
            } else if (isInThePipeOnceChecker == i){
                isInThePipeOnce = true;
            }
        }
        return insidePipeGap;
    }

    void setNew(){
        lengthBetweenPipes = 0;
        pos = Map.xPosOfTheBird;
        state = 0;
        alive = true;
    }
}