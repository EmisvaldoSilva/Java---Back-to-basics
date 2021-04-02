package silva.emisvaldo.classic.problem.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import silva.emisvaldo.classic.problem.geneticAlgorithm.GeneticAlgorithm.SelectionType;

public class Stringsss extends Chromossome<Stringsss>{

	private List<Character> letters;
	private Random random = new Random();
	private static String parametro = "emisvaldo";

	public Stringsss(List<Character> letters) {
		this.letters = letters;
		this.random = new Random();
	}

	public static Stringsss randomIstance() {
		List<Character> letters = new ArrayList<>(List.of('a', 'b', 'c', 'd', 'e', 'f', 
				'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
				'v', 'w', 'x', 'y', 'z'));
		Collections.shuffle(letters);
		return new Stringsss(letters.subList(0, 9));
		
	}

	@Override
	public double fitness() {
		double pontos = 0;
		for (int i = 0; i < parametro.length(); i++) {
			if(letters.get(i) == parametro.charAt(i)) {
				pontos ++;
			}
		}
		return pontos / parametro.length();
	}

	@Override
	public List<Stringsss> crossover(Stringsss other) {
		Stringsss child1 = new Stringsss(new ArrayList<>(letters));
		Stringsss child2 = new Stringsss(new ArrayList<>(other.letters));
		int idx1 = random.nextInt(letters.size());	
		int idx2 = random.nextInt(other.letters.size());
		Character l1 = letters.get(idx1);
		Character l2 = other.letters.get(idx2);
		int idx3 = letters.indexOf(l2 +1);
		int idx4 = other.letters.indexOf(l1);
		Collections.swap(child1.letters, idx1, 3);
		Collections.swap(child2.letters, idx2, 4);
		return List.of(child1, child2);
	}

	@Override
	public void mutate() {
		int idx1 = random.nextInt(letters.size());
		int idx2 = random.nextInt(letters.size());
		Collections.swap(letters, idx1, idx2);
	}

	@Override
	public Stringsss copy() {
		return new Stringsss(new ArrayList<>(letters));
	}
	
	@Override
	public String toString() {
		return letters.stream()
				.map(n -> n.toString())
				.collect(Collectors.joining());
	}

	public static void main(String[] args) {
		final int POPULAION_SIZE = 1000;
		final double THRESHOLD = 1.0;
		ArrayList<Stringsss> initialPopulation = new ArrayList<Stringsss>();
		for(int i = 0; i < POPULAION_SIZE; i++) {
			initialPopulation.add(Stringsss.randomIstance());
		}

		GeneticAlgorithm<Stringsss> ga = new GeneticAlgorithm<>(initialPopulation, 0.3, 0.7, SelectionType.ROULLETE);
		Stringsss result = ga.run(POPULAION_SIZE, THRESHOLD);
		System.out.println(result);
	}

}
