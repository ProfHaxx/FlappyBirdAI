import java.util.HashMap;

public class FlappyBirdGenome {

    public Genome genome;
    private HashMap<NodeGene, Double> inputs = new HashMap<>();
    public NodeGene OutputNode;
    public HashMap<Integer, NodeGene> InputNodes = new HashMap<>();

    public FlappyBirdGenome(){
        inputs.clear();
        genome = new Genome();
        NodeGene n1 = new NodeGene(innovationGeneration.getNextCounterForNodes(), 0, NodeGene.TYPE.INPUT);
        NodeGene n2 = new NodeGene(innovationGeneration.getNextCounterForNodes(), 0, NodeGene.TYPE.INPUT);
        NodeGene n3 = new NodeGene(innovationGeneration.getNextCounterForNodes(), 0, NodeGene.TYPE.INPUT);
        NodeGene n4 = new NodeGene(innovationGeneration.getNextCounterForNodes(), 0, NodeGene.TYPE.INPUT);
        NodeGene n5 = new NodeGene(innovationGeneration.getNextCounterForNodes(), 0, NodeGene.TYPE.OUTPUT);
        OutputNode = n5;

        ConnectionGene c1 = new ConnectionGene(n1.getNodeId(),n5.getNodeId(),0,true,innovationGeneration.getNextCounterForConnections());
        ConnectionGene c2 = new ConnectionGene(n2.getNodeId(),n5.getNodeId(),0,true,innovationGeneration.getNextCounterForConnections());
        ConnectionGene c3 = new ConnectionGene(n3.getNodeId(),n5.getNodeId(),0,true,innovationGeneration.getNextCounterForConnections());
        ConnectionGene c4 = new ConnectionGene(n4.getNodeId(),n5.getNodeId(),0,true,innovationGeneration.getNextCounterForConnections());

        InputNodes.put(n1.getNodeId(), n1);
        InputNodes.put(n2.getNodeId(), n2);
        InputNodes.put(n3.getNodeId(), n3);
        InputNodes.put(n4.getNodeId(), n4);

        genome.addNewConnection(c1);
        genome.addNewConnection(c2);
        genome.addNewConnection(c3);
        genome.addNewConnection(c4);
        genome.addNewNode(n1);
        genome.addNewNode(n2);
        genome.addNewNode(n3);
        genome.addNewNode(n4);
        genome.addNewNode(n5);
    }

    public FlappyBirdGenome(Genome genome){
        InputNodes.clear();
        int count = 0;
        this.genome = genome.copy();
        for(NodeGene n: this.genome.getNodeGenes().values()){
            if(n.getType() == NodeGene.TYPE.INPUT){
                InputNodes.put(n.getNodeId(),n);
            }
            if(n.getType() == NodeGene.TYPE.OUTPUT){
                OutputNode = n;
            }
        }
    }

    public void setInput(double a, double b, double c, double d){
        inputs.clear();
        inputs.put(InputNodes.get(1), a);
        inputs.put(InputNodes.get(2), b);
        inputs.put(InputNodes.get(3), c);
        inputs.put(InputNodes.get(4), d);
    }

    public double count(NodeGene Start){
        if (inputs.containsKey(Start)){
            //System.out.println(" input ");
            return inputs.get(Start);
        }
        else{
            double answer= 0.0;
            //System.out.println(" hidden ");
            try {
                for (ConnectionGene c : genome.getConnectionGenes().values()) {
                    if (c.getOutNodeGeneId() == Start.getNodeId()) {
                        answer += c.getWeight() * count(genome.getNodeGenes().get(c.getInNodeGeneId()));
                    }
                }
                return Start.getQ() + answer;
            }
            catch (StackOverflowError e){
                e.printStackTrace();
                return 0.0;
            }
        }
    }
}
