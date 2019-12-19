package main;

import java.util.HashMap;

public class FlappyBirdGenome {

    public Genome genome;
    private HashMap<Node, Double> inputs = new HashMap<>();
    public Node OutputNode;
    public HashMap<Integer, Node> InputNodes = new HashMap<>();

    public FlappyBirdGenome(){
        inputs.clear();
        genome = new Genome();
        Node n1 = new Node(InnovationGeneration.getNextCounterForNodes(), 0, Node.Type.INPUT);
        Node n2 = new Node(InnovationGeneration.getNextCounterForNodes(), 0, Node.Type.INPUT);
        Node n3 = new Node(InnovationGeneration.getNextCounterForNodes(), 0, Node.Type.INPUT);
        Node n4 = new Node(InnovationGeneration.getNextCounterForNodes(), 0, Node.Type.INPUT);
        Node n5 = new Node(InnovationGeneration.getNextCounterForNodes(), 0, Node.Type.OUTPUT);
        OutputNode = n5;

        Connection c1 = new Connection(n1.getId(),n5.getId(),0,true, InnovationGeneration.getNextCounterForConnections());
        Connection c2 = new Connection(n2.getId(),n5.getId(),0,true, InnovationGeneration.getNextCounterForConnections());
        Connection c3 = new Connection(n3.getId(),n5.getId(),0,true, InnovationGeneration.getNextCounterForConnections());
        Connection c4 = new Connection(n4.getId(),n5.getId(),0,true, InnovationGeneration.getNextCounterForConnections());

        InputNodes.put(n1.getId(), n1);
        InputNodes.put(n2.getId(), n2);
        InputNodes.put(n3.getId(), n3);
        InputNodes.put(n4.getId(), n4);

        genome.addConnection(c1);
        genome.addConnection(c2);
        genome.addConnection(c3);
        genome.addConnection(c4);
        genome.addNode(n1);
        genome.addNode(n2);
        genome.addNode(n3);
        genome.addNode(n4);
        genome.addNode(n5);
    }

    public FlappyBirdGenome(Genome genome){
        InputNodes.clear();
        this.genome = genome.copy();
        for(Node n: this.genome.getNodeGenes().values()){
            if(n.getType() == Node.Type.INPUT){
                InputNodes.put(n.getId(),n);
            }
            if(n.getType() == Node.Type.OUTPUT){
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

    public double count(Node Start){
        if (inputs.containsKey(Start)){
            return inputs.get(Start);
        } else {
            double answer= 0.0;
            try {
                for (Connection c : genome.getConnectionGenes().values()) {
                    if (c.getOutNodeId() == Start.getId()) {
                        answer += c.getWeight() * count(genome.getNodeGenes().get(c.getInNodeId()));
                    }
                }
                return Start.getQ() + answer;
            } catch (StackOverflowError e) {
                e.printStackTrace();
                return 0.0;
            }
        }
    }
}
