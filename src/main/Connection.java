package main;

public class Connection {
    private int inNodeId;
    private int outNodeId;
    private double weight;
    private int innovationNumber;
    private boolean enabled;

    Connection(int inNodeId, int outNodeId, double weight, boolean enabled, int innovationNumber){
        this.inNodeId = inNodeId;
        this.outNodeId = outNodeId;
        this.enabled = enabled;
        this.weight = weight;
        this.innovationNumber = innovationNumber;
    }

    int getInNodeId(){
        return this.inNodeId;
    }

    int getOutNodeId(){
        return this.outNodeId;
    }

    int getInnovationNumber(){
        return this.innovationNumber;
    }

    double getWeight(){
        return this.weight;
    }

    boolean isEnabled(){
        return this.enabled;
    }

    void setWeight(double newWeight){
        this.weight = newWeight;
    }

    void disable(){
        enabled = false;
    }

    void enable(){
        enabled = true;
    }

    Connection copy(){
        return new Connection(this.inNodeId, this.outNodeId, this.weight, this.enabled, this.innovationNumber);
    }
}
