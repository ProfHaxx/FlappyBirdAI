public class ConnectionGene {
    private int InNodeGeneId;
    private int OutNodeGeneId;
    private double weight;
    private int innovationNumber;
    private boolean expressed;

    public ConnectionGene(int InNodeGeneId, int OutNodeGeneId, double weight, boolean expressed, int innovationNumber){
        this.InNodeGeneId = InNodeGeneId;
        this.OutNodeGeneId = OutNodeGeneId;
        this.expressed = expressed;
        this.weight = weight;
        this.innovationNumber = innovationNumber;
    }

    public int getInNodeGeneId(){
        return this.InNodeGeneId;
    }

    public int getOutNodeGeneId(){
        return this.OutNodeGeneId;
    }

    public int getInnovationNumber(){
        return this.innovationNumber;
    }

    public double getWeight(){
        return this.weight;
    }

    public boolean isExpressed(){
        return this.expressed;
    }

    public void setWeight(double newWeight){
        this.weight = newWeight;
    }

    public void setExpressed(boolean isExpressed){
        this.expressed = isExpressed;
    }

    public void disable(){
        expressed = false;
    }

    public void enable(){
        expressed = true;
    }

    public ConnectionGene copy(){
        return new ConnectionGene(this.InNodeGeneId, this.OutNodeGeneId, this.weight, this.expressed, this.innovationNumber);
    }
}
