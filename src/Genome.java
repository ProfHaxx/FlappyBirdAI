import java.util.*;

public class Genome {

    private HashMap<Integer,ConnectionGene> connections = new HashMap<>();
    private HashMap<Integer, NodeGene> nodes = new HashMap<>();

    private static final double NodeGeneMutationRate = 0.3;
    private static final double ConnectionGeneMutationRate = 0.4;
    private static final double NodeGeneMutationMultiplication = 0.4;
    private static final double ConnectionGeneMutationMultiplication = 0.2;

    public Genome(){
    }

    public void addNewConnection(ConnectionGene newConnectionGene){
        connections.put(newConnectionGene.getInnovationNumber(), newConnectionGene);
    }

    public void addNewNode(NodeGene newNodeGene){
        nodes.put(newNodeGene.getNodeId(), newNodeGene);
    }

    public HashMap<Integer, ConnectionGene> getConnectionGenes(){
        return this.connections;
    }

    public HashMap<Integer, NodeGene> getNodeGenes(){
        return this.nodes;
    }

    public void mutateNodesQ(Random r){
        for(NodeGene node: nodes.values()){
            if (r.nextDouble() > NodeGeneMutationRate) {
                node.setQ((node.getQ() + r.nextDouble() * 2f - 1f) * NodeGeneMutationMultiplication);
            }
        }
    }

    public void mutateConnectionWeights(Random r){
        for(ConnectionGene connectionGene: connections.values()){
            if (r.nextDouble() > ConnectionGeneMutationRate) {
                connectionGene.setWeight((connectionGene.getWeight() + r.nextDouble() * 2f - 1f) * ConnectionGeneMutationMultiplication);
            }
        }
    }

    public void addNodeMutation(Random r){
        ConnectionGene con = (ConnectionGene) connections.values().toArray()[r.nextInt(connections.size())];

        NodeGene in = nodes.get(con.getInNodeGeneId());
        NodeGene out = nodes.get(con.getOutNodeGeneId());

        con.disable();

        NodeGene newNode = new NodeGene(innovationGeneration.getNextCounterForNodes(), r.nextDouble() * 2f - 1f, NodeGene.TYPE.HIDDEN);
        ConnectionGene newConnectionsGene1 = new ConnectionGene(in.getNodeId(), newNode.getNodeId(),con.getWeight(), true,innovationGeneration.getNextCounterForConnections());
        ConnectionGene newConnectionsGene2 = new ConnectionGene(newNode.getNodeId(), out.getNodeId(),1.0, true,innovationGeneration.getNextCounterForConnections());

        connections.put(newConnectionsGene1.getInnovationNumber(), newConnectionsGene1);
        connections.put(newConnectionsGene2.getInnovationNumber(), newConnectionsGene2);
        nodes.put(newNode.getNodeId(),newNode);
    }

    /**
     *
     * @param r
     * @param maxTries ammount of tries befor aborting
     */

    public void addConnectionMutation(Random r, int maxTries){
        boolean repeat = true;
        NodeGene g1;
        NodeGene g2;
        int count = 0;
        while (repeat) {
            g1 = nodes.get(Util.getRandomElementFromSet(nodes.keySet(),r));
            g2 = nodes.get(Util.getRandomElementFromSet(nodes.keySet(),r));

            boolean reversed = false;
            if ((g1.getType() == NodeGene.TYPE.OUTPUT) &&
                    (g2.getType() == NodeGene.TYPE.HIDDEN)) {
                reversed = true;
            } else if ((g1.getType() == NodeGene.TYPE.OUTPUT) && (g2.getType() == NodeGene.TYPE.INPUT)) {
                reversed = true;
            } else if ((g1.getType() == NodeGene.TYPE.HIDDEN) && (g2.getType() == NodeGene.TYPE.INPUT)) {
                reversed = true;
            } else if ((g1.getType() == NodeGene.TYPE.HIDDEN) && (g2.getType() == NodeGene.TYPE.HIDDEN)){
            }

            boolean possible = true;
            if ((g1.getType() == NodeGene.TYPE.OUTPUT) && (g2.getType() == NodeGene.TYPE.OUTPUT)) {
                possible = false;
            } else if (
                    (g1.getType() == NodeGene.TYPE.INPUT) &&
                            (g2.getType() == NodeGene.TYPE.INPUT)) {
                possible = false;
            } else if (g1.getNodeId() == g2.getNodeId()) {
                possible = false;
            }

            boolean exsists = false;
            boolean changed = false;
            for (ConnectionGene con : connections.values()) {
                if ((con.getInNodeGeneId() == g1.getNodeId()) && (con.getOutNodeGeneId() == g2.getNodeId())) {
                    exsists = true;
                    if (con.isExpressed() != true){
                        changed = true;
                        con.enable();
                    }
                    break;
                } else if ((con.getInNodeGeneId() == g2.getNodeId()) && (con.getOutNodeGeneId() == g1.getNodeId())) {
                    exsists = true;
                    if (con.isExpressed() != true){
                        changed = true;
                        con.enable();
                    }
                    break;
                }
            }

            if (count > maxTries){
                repeat = false;
                //System.out.println(" I tried, but could not succeed ");
            }

            if(changed){
                repeat = false;
                continue;
            }

            if(exsists||(possible != true)){
                count ++;
                continue;
            }

            ConnectionGene newConnection = new ConnectionGene(
                    reversed ? g1.getNodeId(): g2.getNodeId(),
                    reversed ? g2.getNodeId(): g1.getNodeId(),
                    r.nextDouble()*2f - 1f,
                    true,
                    innovationGeneration.getNextCounterForConnections()
            );

            Genome helper = this.copy();
            helper.addNewConnection(newConnection);

            if(!ContainsLoop(helper, helper.nodes.get(newConnection.getOutNodeGeneId()), new ArrayList<>())){
                //System.out.println("Loop!!!");
            }
            else {
                connections.put(newConnection.getInnovationNumber(), newConnection);
                repeat = false;
            }

        }
    }

    /**
     * @param parent1
     * @param parent2
     * @param r
     * @return
     */

    public static Genome crossoverSameRights(Genome parent1, Genome parent2, Random r){
        Genome child = new Genome();
        int counterNodesMax = innovationGeneration.getCounterForNodes();
        NodeGene parent1Node, parent2Node;


        for(int i = 0; i < counterNodesMax; i++){
            parent1Node = parent1.getNodeGenes().get(i + 1);
            parent2Node = parent2.getNodeGenes().get(i + 1);
            if((parent1Node != null)&&(parent2Node != null)){
                NodeGene n = new NodeGene(parent1Node.getNodeId(),r.nextBoolean() ? parent1Node.getQ(): parent2Node.getQ(), parent1Node.getType());
                child.addNewNode(n);
            }
            else if((parent1Node != null)&&(parent2Node == null)){
                NodeGene n = new NodeGene(parent1Node.getNodeId(),parent1Node.getQ(), parent1Node.getType());
                child.addNewNode(n);
            }
            else if((parent2Node != null)&&(parent1Node == null)){
                NodeGene n = new NodeGene(parent2Node.getNodeId(),parent2Node.getQ(), parent2Node.getType());
                child.addNewNode(n);
            }
        }

        int counterConnectionssMax = innovationGeneration.getCounterForConnections();
        ConnectionGene parent1Connection, parent2Connection;

        for(int i = 0; i < counterConnectionssMax; i++){
            parent1Connection = parent1.getConnectionGenes().get(i + 1);
            parent2Connection = parent2.getConnectionGenes().get(i + 1);
            if((parent1Connection != null)&&(parent2Connection != null)){
                ConnectionGene c = new ConnectionGene(parent1Connection.getInNodeGeneId(),
                        parent1Connection.getOutNodeGeneId(),
                        r.nextBoolean() ? parent1Connection.getWeight(): parent2Connection.getWeight(),
                        r.nextBoolean() ? parent1Connection.isExpressed(): parent2Connection.isExpressed(),
                        parent1Connection.getInnovationNumber());
                child.addNewConnection(c);
            }
            else if((parent1Connection == null)&&(parent2Connection != null)){
                ConnectionGene c = new ConnectionGene(
                        parent2Connection.getInNodeGeneId(),
                        parent2Connection.getOutNodeGeneId(),
                        parent2Connection.getWeight(),
                        parent2Connection.isExpressed(),
                        parent2Connection.getInnovationNumber());
                child.addNewConnection(c);
            }
            else if((parent1Connection != null)&&(parent2Connection == null)){
                ConnectionGene c = new ConnectionGene(parent1Connection.getInNodeGeneId(),
                        parent1Connection.getOutNodeGeneId(),
                        parent1Connection.getWeight(),
                        parent1Connection.isExpressed(),
                        parent1Connection.getInnovationNumber());
                child.addNewConnection(c);
            }
        }

        return child;
    }

    /**
     *
     * @param parent1  Is the fitter one
     * @param parent2  Is less fitter than parent1
     * @param r
     * @return
     */

    public static Genome crossover(Genome parent1, Genome parent2, Random r){
        Genome child = new Genome();
        int counterNodesMax = innovationGeneration.getCounterForNodes();
        NodeGene parent1Node, parent2Node;


        for(int i = 0; i < counterNodesMax; i++){
            parent1Node = parent1.getNodeGenes().get(i + 1);
            parent2Node = parent2.getNodeGenes().get(i + 1);
            if((parent1Node != null)&&(parent2Node != null)){
                NodeGene n = new NodeGene(parent1Node.getNodeId(),r.nextBoolean() ? parent1Node.getQ(): parent2Node.getQ(), parent1Node.getType());
                child.addNewNode(n);
            }
            else if((parent1Node != null)&&(parent2Node == null)){
                NodeGene n = new NodeGene(parent1Node.getNodeId(),parent1Node.getQ(), parent1Node.getType());
                child.addNewNode(n);
            }

        }

        int counterConnectionssMax = innovationGeneration.getCounterForConnections();
        ConnectionGene parent1Connection, parent2Connection;

        for(int i = 0; i < counterConnectionssMax; i++){
            parent1Connection = parent1.getConnectionGenes().get(i + 1);
            parent2Connection = parent2.getConnectionGenes().get(i + 1);
            if((parent1Connection != null)&&(parent2Connection != null)){
                ConnectionGene c = new ConnectionGene(
                        parent1Connection.getInNodeGeneId(),
                        parent1Connection.getOutNodeGeneId(),
                        r.nextBoolean() ? parent1Connection.getWeight():parent2Connection.getWeight(),
                        true,
                        parent1Connection.getInnovationNumber());
                child.addNewConnection(c);
            }
            else if((parent1Connection != null)&&(parent2Connection == null)){
                ConnectionGene c = new ConnectionGene(parent1Connection.getInNodeGeneId(),
                        parent1Connection.getOutNodeGeneId(),
                        parent1Connection.getWeight(),
                        parent1Connection.isExpressed(),
                        parent1Connection.getInnovationNumber());
                child.addNewConnection(c);
            }
        }

        return child;
    }

    public Genome copy(){
        Genome genome = new Genome();
        for (ConnectionGene c: connections.values()){
            genome.addNewConnection(c.copy());
        }
        for(NodeGene n: nodes.values()){
            genome.addNewNode(n.copy());
        }
        return genome;
    }

    private boolean ContainsLoop(Genome genomeToTest, NodeGene StartingNode, ArrayList<NodeGene> UsedGenes){

        if(StartingNode.getType() == NodeGene.TYPE.INPUT){
            return true;
        }
        else if(StartingNode.getType() == NodeGene.TYPE.OUTPUT){
            return false;
        }else if(UsedGenes.contains(StartingNode)){
            return false;
        }
        else{
            boolean answer = true;
            for(ConnectionGene c:genomeToTest.connections.values()){
                if(c.getOutNodeGeneId() == StartingNode.getNodeId()){
                    UsedGenes.add(StartingNode);
                    answer = answer && ContainsLoop(genomeToTest, genomeToTest.nodes.get(c.getInNodeGeneId()),UsedGenes );
                    if(!answer){
                        break;
                    }
                    UsedGenes.remove(StartingNode);
                }
            }
            return answer;
        }
    }
}
