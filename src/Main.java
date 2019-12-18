import java.util.HashMap;
import java.util.Random;

public class Main {

    public static HashMap<Genome,Double> fitnessOfTheGivenGenes = new HashMap<>();

    public static void main(String[] args) {
        Random r = new Random();
        FlappyBirdGenome flappyBirdGenome = new FlappyBirdGenome();
        FlappyBirdGenome flappyBirdGenome2 = new FlappyBirdGenome(flappyBirdGenome.genome.copy());

        flappyBirdGenome.genome.mutateConnectionWeights(r);
        //System.out.println(flappyBirdGenome.count(flappyBirdGenome.OutputNode));

        flappyBirdGenome2.genome.mutateConnectionWeights(r);
        //System.out.println(flappyBirdGenome2.count(flappyBirdGenome2.OutputNode));

        Genome child = Genome.crossoverSameRights(flappyBirdGenome2.genome, flappyBirdGenome.genome, r);

        FlappyBirdGenome newChild = new FlappyBirdGenome(child);
        //System.out.println(newChild.count(newChild.OutputNode));

        //System.out.println(compatibilityDistance.count(flappyBirdGenome.genome, flappyBirdGenome2.genome));
        //System.out.println(compatibilityDistance.count(flappyBirdGenome.genome, child));

        Evaluator evaluator = new Evaluator(100,child) {
            @Override
            protected double fitness(Genome genome) {
                double answer = fitnessOfTheGivenGenes.get(genome);
                return answer;
            }
        };

        System.out.println(evaluator.getHighestFitness());
        for(int i = 0; i < 17; i++) {
            for(Genome g: evaluator.getGenomes()){
                fitnessOfTheGivenGenes.put(g,countFitness(g));
            }
            evaluator.evaluate();
            System.out.println(evaluator.getHighestFitness());
            fitnessOfTheGivenGenes.clear();
        }
    }

    public static double countFitness(Genome genome){
        FlappyBirdGenome f = new FlappyBirdGenome(genome);
        return f.count(f.OutputNode);
    }
}
