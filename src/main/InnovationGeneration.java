package main;

public class InnovationGeneration {
    private static int counterConnections = 0;
    private static int counterNodes = 0;

    public static int getNextCounterForConnections(){
        counterConnections++;
        return counterConnections;
    }

    public static int getNextCounterForNodes(){
        counterNodes++;
        return counterNodes;
    }

    public static int getCounterForConnections(){
        return counterConnections;
    }

    public static int getCounterForNodes(){
        return counterNodes;
    }
}
