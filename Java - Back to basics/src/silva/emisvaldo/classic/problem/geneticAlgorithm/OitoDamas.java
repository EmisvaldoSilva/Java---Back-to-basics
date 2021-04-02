package silva.emisvaldo.classic.problem.geneticAlgorithm;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator;

public class OitoDamas extends Chromossome<OitoDamas>{
	private int damas[] = new int[8];

	public OitoDamas() {
		for(int i = 0; i < 8; i++){
			this.damas[i] = i;
		}
	}

	@Override
	public double fitness() {
		if(damas[1] == 8) {
			return 1.0;
		}
		return 0;
	}

	@Override
	public List<OitoDamas> crossover(OitoDamas other) {

		return null;
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub

	}

	@Override
	public OitoDamas copy() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args)  {
		OitoDamas oitoDamas =  new OitoDamas();
		System.out.println("GA é Foda " + oitoDamas.damas[6]);
	}

}
