public class compatibilityDistance {

    private static final double C1 = 0.5;
    private static  final double C2 = 0.5;
    private static final double C3 = 0.5;
    private static final double C4 = 0.5;

    public static double count(Genome genome1, Genome genome2){

        NodeGene nodeGeneGenome1, nodeGeneGenome2;

        int excess = 0;
        int disjoint = 0;
        double averageWeight = 0.0;
        double averageQ = 0.0;
        int N = 0;
        int N1 = 0;
        int N2 = 0;

        for (int i = 0; i < innovationGeneration.getCounterForNodes(); i++){
            nodeGeneGenome1 = genome1.getNodeGenes().get(i + 1);
            nodeGeneGenome2 = genome2.getNodeGenes().get(i + 1);
            if ((nodeGeneGenome1 != null)&&(nodeGeneGenome2 != null)){
                excess ++;
                averageQ += Math.abs(nodeGeneGenome1.getQ() - nodeGeneGenome2.getQ());
                N++;
            }
            else if((nodeGeneGenome1 == null)&&(nodeGeneGenome2 != null)){
                disjoint++;
                N2++;
            }
            else if((nodeGeneGenome1 != null)&&(nodeGeneGenome2 == null)){
                disjoint++;
                N1++;
            }
        }

        ConnectionGene connectionGeneGenome1;
        ConnectionGene connectionGeneGenome2;

        for(int i = 0; i < innovationGeneration.getCounterForConnections(); i++){
            connectionGeneGenome1 = genome1.getConnectionGenes().get(i + 1);
            connectionGeneGenome2 = genome2.getConnectionGenes().get(i + 1);
            if((connectionGeneGenome1 != null)&&(connectionGeneGenome2 != null)){
                excess++;
                averageWeight += Math.abs(connectionGeneGenome1.getWeight() - connectionGeneGenome2.getWeight());
                N++;
            }
            else if((connectionGeneGenome1 == null)&&(connectionGeneGenome2 != null)){
                disjoint++;
                N2++;
            }
            else if((connectionGeneGenome1 != null)&&(connectionGeneGenome2 == null)){
                disjoint++;
                N1++;
            }

        }

        N += N1 > N2 ? N1: N2;

        double answer = ((excess * C1) / N) + ((disjoint * C2) / N) + averageWeight * C3 + averageQ * C4;

        return answer;
    }
}
