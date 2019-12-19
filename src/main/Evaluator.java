package main;

import java.util.*;

public abstract class Evaluator {


    private static double DT = 2.0;
    private static final double PercentageOfSpeiciesToEvalate = 0.7;
    private static final double PercentageOfSpeiciesTooSmall = 0.3;

    private static final double  MUTATION_WEIGHT_RATE = 0.5f;
    private static final double  MUTATION_Q_RATE = 0.2f;
    private static final double  ADD_CONNECTION_RATE = 0.1f;
    private static final double  ADD_NODE_RATE = 0.05f;


    private int populationSize;

    private List<Genome> genomes;
    private List<Genome> nextGenGenomes;
    Random random = new Random();

    private List<Species> species;

    private HashMap<Genome, Species> mappedSpecies;
    private HashMap<Genome, Double> scoreMap;
    private double highestScore;
    private Genome fittestGenome;

    public Evaluator(int populationSize, Genome startingGenome){
        genomes = new ArrayList<>(populationSize);
        nextGenGenomes = new ArrayList<>(populationSize);
        species = new ArrayList<>();
        mappedSpecies = new HashMap<>();
        scoreMap = new HashMap<>();
        this.populationSize = populationSize;

        Species species1 = new Species(startingGenome);
        species.add(species1);
        Genome genome;
        for (int i = 0; i < populationSize; i++ ){
            genome = startingGenome.copy();
            mappedSpecies.put(genome, species1);
            scoreMap.put(genome, 0.0);
            genomes.add(genome);
        }
        highestScore = 0.0;

    }

    public void evaluate(){
        //reset all
        for(Species s: species){
            s.reset(random);
        }
        scoreMap.clear();
        nextGenGenomes.clear();
        highestScore = Float.MIN_VALUE;
        fittestGenome = null;
        mappedSpecies.clear();

        // Place genomes into species

        if (species.size() > main.Map.amount * PercentageOfSpeiciesToEvalate){
            DT++;
        }

        if (species.size() < main.Map.amount * PercentageOfSpeiciesTooSmall){
            DT--;
        }

        for(Genome genome: genomes){
            boolean needsOwnSpecies = true;
            for(Species s: species) {
                if (CompatibilityDistance.count(genome, s.mascot) < DT){
                    needsOwnSpecies = false;
                    mappedSpecies.put(genome, s);
                    s.members.add(genome);
                    break;
                }
            }
            if (needsOwnSpecies){
                Species sNew = new Species(genome);
                species.add(sNew);
                mappedSpecies.put(genome, sNew);
            }
        }

        // Remove unused species
        ArrayList<Species> helperArray = new ArrayList<>();
        for(Species s: species){
            if (s.members.isEmpty()){
                helperArray.add(s);
            }
        }
        for (Species s: helperArray){
            species.remove(s);
        }

        // Evaluate genomes and assign score
        for(Genome genome: genomes){
            Species species = mappedSpecies.get(genome);

            double fitnessOfTheGenome = fitness(genome);
            double adjustedFitnessOfTheGenome = fitnessOfTheGenome / species.members.size();

            FitnessGenome GenomeAndHisFitness = new FitnessGenome(genome, fitnessOfTheGenome);
            species.fitnessPop.add(GenomeAndHisFitness);
            scoreMap.put(genome,adjustedFitnessOfTheGenome);

            if(species.fittestGenome.fitness < fitnessOfTheGenome){
                species.changeFittestGenome(GenomeAndHisFitness);
            }
            if(fitnessOfTheGenome > highestScore){
                highestScore = fitnessOfTheGenome;
                fittestGenome = genome;
            }
        }

        //save all best Genomes from each species
        for(Species specie: species){
            nextGenGenomes.add(specie.fittestGenome.genome.copy());
        }

        // Breed the rest of the genomes

        while (nextGenGenomes.size() < populationSize) { // replace removed genomes by randomly breeding
            Species s = getRandomSpeciesBiasedAjdustedFitness(random);

            Genome p1 = getRandomGenomeBiasedAdjustedFitness(s, random);
            Genome p2 = getRandomGenomeBiasedAdjustedFitness(s, random);

            Genome child;
            if (scoreMap.get(p1) >= scoreMap.get(p2)) {
                child = Genome.crossover(p1, p2, random);
            } else {
                child = Genome.crossover(p2, p1, random);
            }
            if(random.nextDouble() > ADD_CONNECTION_RATE){
                child.addConnectionMutation(random, 50);
            }
            if (random.nextDouble() > ADD_NODE_RATE){
                child.addNodeMutation(random);
            }
            if (random.nextDouble() > MUTATION_Q_RATE){
                child.mutateNodesQ(random);
            }
            if (random.nextDouble() > MUTATION_WEIGHT_RATE){
                child.mutateConnectionWeights(random);
            }

            nextGenGenomes.add(child);
        }

        genomes = nextGenGenomes;
        nextGenGenomes = new ArrayList<>();
    }

    public int getSpeciesAmount() {
        return species.size();
    }

    public double getHighestFitness() {
        return highestScore;
    }

    public ArrayList<Genome> getGenomes(){
        return (ArrayList<Genome>) genomes;
    }

    /**
     * Selects a random species from the species list, where species with a higher total adjusted fitness have a higher chance of being selected
     */
    private Species getRandomSpeciesBiasedAjdustedFitness(Random random) {
        double completeWeight = 0.0;	// sum of probablities of selecting each species - selection is more probable for species with higher fitness
        for (Species s : species) {
            completeWeight += s.totalAdjustedFitness;
        }
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (Species s : species) {
            countWeight += s.totalAdjustedFitness;
            if (countWeight >= r) {
                return s;
            }
        }
        throw new RuntimeException("Couldn't find a species... Number is species in total is "+species.size()+", and the total adjusted fitness is "+completeWeight);
    }

    /**
     * Selects a random genome from the species chosen, where genomes with a higher adjusted fitness have a higher chance of being selected
     */
    private Genome getRandomGenomeBiasedAdjustedFitness(Species selectFrom, Random random) {
        double completeWeight = 0.0;	// sum of probablities of selecting each genome - selection is more probable for genomes with higher fitness
        for (FitnessGenome fg : selectFrom.fitnessPop) {
            completeWeight += fg.fitness;
        }
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (FitnessGenome fg : selectFrom.fitnessPop) {
            countWeight += fg.fitness;
            if (countWeight >= r) {
                return fg.genome;
            }
        }
        throw new RuntimeException("Couldn't find a genome... Number is genomes in selected species is "+selectFrom.fitnessPop.size()+", and the total adjusted fitness is "+completeWeight);
    }

    protected abstract double fitness(Genome genome);

    public class FitnessGenome {

        double fitness;
        Genome genome;

        public FitnessGenome(Genome genome, double fitness) {
            this.genome = genome;
            this.fitness = fitness;
        }
    }

    public class Species {

        public Genome mascot;
        public List<Genome> members;
        public List<FitnessGenome> fitnessPop;
        public double totalAdjustedFitness = 0f;
        public FitnessGenome fittestGenome;

        public Species(Genome mascot) {
            this.mascot = mascot;
            this.members = new LinkedList<>();
            this.members.add(mascot);
            this.fitnessPop = new ArrayList<>();
            fittestGenome =  new FitnessGenome(mascot, 0.0);
        }

        public void changeFittestGenome( FitnessGenome newFittestGenome){
            fittestGenome = newFittestGenome;
        }


        public void reset(Random r) {
            int newMascotIndex = r.nextInt(members.size());
            this.mascot = members.get(newMascotIndex);
            members.clear();
            fitnessPop.clear();
            totalAdjustedFitness = 0f;
            fittestGenome =  new FitnessGenome(mascot, 0.0);
        }
    }

}
