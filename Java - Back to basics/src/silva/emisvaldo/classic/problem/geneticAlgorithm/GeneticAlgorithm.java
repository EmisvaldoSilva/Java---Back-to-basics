package silva.emisvaldo.classic.problem.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm <C extends Chromossome<C>>{
	
	public enum SelectionType{
		ROULLETE, TOURNAMENT;
	}
	
	private ArrayList<C> population;
	private double mutationChance;
	private double crossoverChance;
	private SelectionType selectionType;
	private Random random;
	
	public GeneticAlgorithm(List<C> initialPopulation, double mutationChance, double crossoverChance, SelectionType selectionType) {
		super();
		this.population = new ArrayList<>(initialPopulation);
		this.mutationChance = mutationChance;
		this.crossoverChance = crossoverChance;
		this.selectionType = selectionType;
		this.random = new Random();
	}

	//Use the probability distribution whell to pick numPicks individuals 
	private List<C> pickRoullete(double[] wheel, int numPicks){
		List<C> picks = new ArrayList<>();
		for(int i = 0; i < numPicks; i++) {
			double pick = random.nextDouble();
			for(int j = 0; j < wheel.length; j++) {
				pick -= wheel[j];
				if(pick <= 0) {
					picks.add(population.get(j));
					break;
				}
			}
		}
		return picks;
	}
	
	// Pick a certain number of individuals via tourneament
	private List<C> pickTourneament(int numberParticipants, int numPicks){
		//Find numParticipants random participants  to be in the tourneament
		Collections.shuffle(population);
		List<C> tourneamet = population.subList(0, numberParticipants);
		//Find the numPicks highest fitnesses in the tourneament
		Collections.sort(tourneamet, Collections.reverseOrder());
		
		return tourneamet.subList(0, numPicks);
	}
	
	//Replace the population with a new generation of individuals
	private void reproceAndReplace() {
		ArrayList<C> nextPopulation = new ArrayList<>();
		//keep going until we've filled the new generation
		while(nextPopulation.size() < population.size()) {
			//Pick the two parants 
			List<C> parents;
			if(selectionType == SelectionType.ROULLETE) {
				//Create the probability distribuition wheel
				double totalFitness = population.stream()
						.mapToDouble(C::fitness)
						.sum();
				double[] wheel = population.stream()
						.mapToDouble(C -> C.fitness() / totalFitness)
						.toArray();
				parents = pickRoullete(wheel, 2);
			}else {//Tourneament
				parents = pickTourneament(population.size() /2, 2);
			}
			//potentially crossover the 2 parents
			if(random.nextDouble() < crossoverChance) {
				C parent1 = parents.get(0);
				C parent2 = parents.get(1);
				nextPopulation.addAll(parent1.crossover(parent2));
			}else {//Just add two parents
				nextPopulation.addAll(parents);
			}
		}
		//If we have an odd number, we'll have 1 extra so we remove it
		if(nextPopulation.size() > population.size()) {
			nextPopulation.remove(0);
		}
		//Replace the reference /generation
		population = nextPopulation;
	}
	//With mutationChance probability, mutate each individual 
	private void mutate () {
		for(C individual : population) {
			if(random.nextDouble() < mutationChance) {
				individual.mutate();
			}
		}
	}
	
	//Rum the algorithm for maxGenerations iterations and return the best individual found
	public C run(int maxGenerations, double threshold) {
		C  best = Collections.max(population).copy();
		for(int generation = 0; generation < maxGenerations; generation++) {
			//early exist if we beat threshold
			if(best.fitness() >= threshold) {
				return best;
			}
			//Debug printout
			System.out.println("Generation " + generation + " Best " + best.fitness() + " AVG " + population.stream()
			.mapToDouble(C::fitness)
			.average()
			.orElse(0.0));
			reproceAndReplace();
			mutate();
			C highest = Collections.max(population);
			if(highest.fitness() > best.fitness()) {
				best = highest.copy();
			}
		}
		return best;
	}
}
