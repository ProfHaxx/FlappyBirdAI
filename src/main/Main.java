package main;

import java.util.HashMap;
import java.util.Random;

public class Main {

    public static HashMap<Genome,Double> fitnessOfTheGivenGenes = new HashMap<>();

    public static void main(String[] args) {
        Random r = new Random();

        FlappyBirdGenome genome = new FlappyBirdGenome();
        FlappyBirdGenome clonedGenome = new FlappyBirdGenome(genome.genome.copy());

        genome.genome.mutateConnectionWeights(r);

        clonedGenome.genome.mutateConnectionWeights(r);

        Genome child = Genome.crossoverSameRights(clonedGenome.genome, genome.genome, r);

        FlappyBirdGenome newChild = new FlappyBirdGenome(child);

        Evaluator evaluator = new Evaluator(100,child) {
            @Override
            protected double fitness(Genome genome) {
                return fitnessOfTheGivenGenes.get(genome);
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

    private static double countFitness(Genome genome){
        FlappyBirdGenome f = new FlappyBirdGenome(genome);
        return f.count(f.OutputNode);
    }
}
