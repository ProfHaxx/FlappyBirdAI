public class Counter {
    public int counter;

    public Counter(){
        counter = 0;
    }

    public void resetCounter(){
        counter = 0;
    }

    public void add(){
        counter ++;
    }

    public static int GlobalCounter = 0;

    public static void resetGlobalCounter(){
        GlobalCounter = 0;
    }

    public static void addToGlobalCounter(){
        GlobalCounter ++;
    }
}
