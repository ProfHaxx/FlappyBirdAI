package main;

public class CompatibilityDistance {

    //Constants
    private static final double C1 = 0.5;
    private static final double C2 = 0.5;
    private static final double C3 = 0.5;
    private static final double C4 = 0.5;

    //Applies the Compatibility Distance Formula on 2 Genomes
    public static double count(Genome genome1, Genome genome2){

        Node node1, node2;

        int excess = 0;
        int disjoint = 0;
        double averageWeight = 0.0;
        double averageQ = 0.0;
        int N = 0;
        int N1 = 0;
        int N2 = 0;

        for (int i = 1; i < InnovationGeneration.getCounterForNodes(); i++){
            node1 = genome1.getNodeGenes().get(i);
            node2 = genome2.getNodeGenes().get(i);
            if ((node1 != null)&&(node2 != null)){
                excess ++;
                averageQ += Math.abs(node1.getQ() - node2.getQ());
                N++;
            } else if((node1 == null)&&(node2 != null)){
                disjoint++;
                N2++;
            }
            else if(node1 != null){
                disjoint++;
                N1++;
            }
        }

        Connection connectionGenome1;
        Connection connectionGenome2;

        for(int i = 0; i < InnovationGeneration.getCounterForConnections(); i++){
            connectionGenome1 = genome1.getConnectionGenes().get(i + 1);
            connectionGenome2 = genome2.getConnectionGenes().get(i + 1);
            if((connectionGenome1 != null)&&(connectionGenome2 != null)){
                excess++;
                averageWeight += Math.abs(connectionGenome1.getWeight() - connectionGenome2.getWeight());
                N++;
            }
            else if((connectionGenome1 == null)&&(connectionGenome2 != null)){
                disjoint++;
                N2++;
            }
            else if(connectionGenome1 != null){
                disjoint++;
                N1++;
            }

        }

        N += N1 > N2 ? N1: N2;

        return ((excess * C1) / N) + ((disjoint * C2) / N) + averageWeight * C3 + averageQ * C4;
    }
}
