

class Control{
    public  double pos;
    public  double state;
    public  boolean alive;
    public  boolean isInThePipeOnce;
    public  int isInThePipeOnceChecker;
    public Counter counter;
    public double lengthBetweenPipes;
    public boolean Flap;





    //-----------------------------------------//
    private static double acceleration = 0.076;
    private static final double gravity = 0.1;
    //-----------------------------------------//
    
    public Control(){
        pos = Map.xPosOfTheBird;
        state = 0;
        alive = true;
        isInThePipeOnce = true;
        isInThePipeOnceChecker = 0;
        counter = new Counter();
        lengthBetweenPipes = 0;
    }


    public double ChangeAll(int jump, int number){
        if(jump == 1){
            state = acceleration * -28;

        }
        else{
            state += acceleration;
            Flap = false;
        }
        pos += state * Math.abs(state);
        if(pos <= 0){
            pos = 0;
            alive = false;
        }
        else if(pos >= 385){
            pos = 385;
            alive = false;
        }
        if(checkPipes(number)){
            alive = false;
        }
        return pos;
    }

    private  boolean checkPipes(int number){
        boolean insedeAPapeGap = false;
        for(int i = 0; i < 3; i++){
            if((Pipes.Pipes[i][1] >= 57)&&(Pipes.Pipes[i][1] <= 107)){
                if (!((Pipes.Pipes[i][0] < Map.yPosOfTheBird[number])&&(Pipes.Pipes[i][0] > Map.yPosOfTheBird[number] - 65))){
                        insedeAPapeGap = true;
                }
                else{
                    if (isInThePipeOnce){
                        counter.add();
                        lengthBetweenPipes += Math.abs( Pipes.Pipes[i][0] - Map.yPosOfTheBird[number] - 32);
                        isInThePipeOnce = false;
                        isInThePipeOnceChecker = i;
                    }
                }
            }
            else if (isInThePipeOnceChecker == i){
                isInThePipeOnce = true;
            }
        }
        return insedeAPapeGap;
    }

    public  void setNew(){
        lengthBetweenPipes = 0;
        pos = Map.xPosOfTheBird;
        state = 0;
        alive = true;
    }
}