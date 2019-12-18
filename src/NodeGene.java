public class NodeGene {

    public enum TYPE {
        INPUT,
        HIDDEN,
        OUTPUT}

    private int NodeId;
    private double Q;
    private TYPE type;


    NodeGene(int NodeId, double Q, TYPE type){
        this.NodeId = NodeId;
        this.Q = Q;
        this.type = type;
    }

    public int getNodeId(){
        return this.NodeId;
    }

    public double getQ(){
        return this.Q;
    }

    public TYPE getType(){
        return this.type;
    }

    public void setQ(double newQ){
        this.Q = newQ;
    }

    public NodeGene copy(){
        return new NodeGene(this.NodeId, this.Q,this.type);
    }
}
