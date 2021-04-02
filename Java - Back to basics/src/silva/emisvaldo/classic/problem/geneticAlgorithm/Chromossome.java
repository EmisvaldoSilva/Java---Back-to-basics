package silva.emisvaldo.classic.problem.geneticAlgorithm;

import java.util.List;

public abstract class Chromossome <T extends Chromossome<T>> implements Comparable<T>{
	public abstract double fitness();
	
	public abstract List<T> crossover(T other);

	public abstract void mutate();
	
	public abstract T copy();
	
	@Override
	public int compareTo(T other) {
		Double mine = this.fitness();
		Double  theirs = other.fitness();
		return mine.compareTo(theirs);
	}
}
