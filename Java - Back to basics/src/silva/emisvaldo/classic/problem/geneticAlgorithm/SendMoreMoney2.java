package silva.emisvaldo.classic.problem.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import silva.emisvaldo.classic.problem.geneticAlgorithm.GeneticAlgorithm.SelectionType;

public class SendMoreMoney2 extends Chromossome<SendMoreMoney2>{
	
	private List<Character> letters;
	private Random random;

	public SendMoreMoney2(List<Character> letters) {
		this.letters = letters;
		this.random = new Random();
	}
	
	public static SendMoreMoney2 ramdomInstance() {
		List<Character> letters = new ArrayList<>(
				List.of('S', 'E', 'N', 'D', 'M', 'O', 'R', 'Y', ' ', ' '));
		Collections.shuffle(letters);
		return new SendMoreMoney2(letters);
	}

	@Override
	public double fitness() {
		int s = letters.indexOf('S');
		int e = letters.indexOf('E');
		int n = letters.indexOf('N');
		int d = letters.indexOf('D');
		int m = letters.indexOf('M');
		int o = letters.indexOf('O');
		int r = letters.indexOf('R');
		int y = letters.indexOf('Y');
		int send = s * 1000 + e * 100 + n * 10 + d;  
		int more = m * 1000 + o * 100 + r * 10 + e;
		int money = m * 10000 + o * 1000 + n * 100 + e * 10 + y;
		int difference = Math.abs(money - (send + more));
		return 1.0 / (difference + 1.0);
	}

	@Override
	public List<SendMoreMoney2> crossover(SendMoreMoney2 other) {
		SendMoreMoney2 child1 = new SendMoreMoney2(new ArrayList<>(letters));
		SendMoreMoney2 child2 = new SendMoreMoney2(new ArrayList<>(other.letters));
		int idx1 = random.nextInt(letters.size());	
		int idx2 = random.nextInt(other.letters.size());	
		Character l1 = letters.get(idx1);
		Character l2 = other.letters.get(idx2);
		int idx3 = letters.indexOf(l2);
		int idx4 = other.letters.indexOf(l1);
		Collections.swap(child1.letters, idx1, idx3);
		Collections.swap(child2.letters, idx2, idx4);
		return List.of(child1, child2);
	}

	@Override
	public void mutate() {
		int idx1 = random.nextInt(letters.size());
		int idx2 = random.nextInt(letters.size());
		Collections.swap(letters, idx1, idx2);
	}

	@Override
	public SendMoreMoney2 copy() {
		return new SendMoreMoney2(new ArrayList<>(letters));
	}
	
	@Override
	public String toString() {
		int s = letters.indexOf('S');
		int e = letters.indexOf('E');
		int n = letters.indexOf('N');
		int d = letters.indexOf('D');
		int m = letters.indexOf('M');
		int o = letters.indexOf('O');
		int r = letters.indexOf('R');
		int y = letters.indexOf('Y');
		int send    = s * 1000 + e * 100 + n * 10 + d;
		int more    = m * 1000 + o * 100 + r * 10 + e;
		int money = m * 10000 + o * 1000 + n * 100 + e * 10 + y;
		int difference = Math.abs(money - (send   +  more   ));
		return (send   + " + " + more   + " = " + money + " Difference: " + difference);
	}
	
	public static void main(String[] args) {
		ArrayList<SendMoreMoney2> initialPolpulation = new ArrayList<>();
		final int POPULATION_SIZE = 1000;
		final int GENERATIONS = 1000;
		final double THRESHOLD = 1.0;
		for (int i = 0; i < POPULATION_SIZE; i++) {
			initialPolpulation.add(SendMoreMoney2.ramdomInstance());
		}
		
		GeneticAlgorithm<SendMoreMoney2> ga = new GeneticAlgorithm<>(initialPolpulation, 0.2, 0.7, SelectionType.TOURNAMENT);
		SendMoreMoney2 result = ga.run(GENERATIONS, THRESHOLD);
		System.out.println(result);

	}

}
