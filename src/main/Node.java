package main;

public class Node {

    public enum Type {
        INPUT, HIDDEN, OUTPUT
    }

    private int id;
    private double q;
    private Type type;


    Node(int id, double Q, Type type){
        this.id = id;
        this.q = Q;
        this.type = type;
    }

    public int getId(){
        return this.id;
    }

    public double getQ(){
        return this.q;
    }

    public Type getType(){
        return this.type;
    }

    public void setQ(double newQ){
        this.q = newQ;
    }

    public Node copy(){
        return new Node(this.id, this.q,this.type);
    }
}
