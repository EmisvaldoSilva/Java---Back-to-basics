package silva.emisvaldo.classic.Problem.SmallProblems.fibonnacci;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Fibonacci {
	
	private static int last = 0, next = 1;//fib(0), fib(1)
	
	private static IntStream stream() {
		return IntStream.generate(() -> {
			int oldLast = last;
			last = next;
			next = oldLast + next;
			
			return oldLast;
		});
	}
	
	//fibonacci usando iteraçoes
	private static int fib3(int n) {
		int last = 0, next = 1; //fib(0), fib(1)
		for(int i = 0; i < n; i++) {
			int oldLast = last;
			last = next;
			next = oldLast + next;
		}
		return last;
	}
	
	// Map.of () foi introduzido no Java 9, mas retorna um mapa imutável
	// Isso cria um mapa com 0-> 0 e 1-> 1 que representam nossos casos básicos 
	static Map<Integer, Integer> memo = new HashMap<>(Map.of(0,0,1,1));
	
	//Esse metodo tem desempeho bom
	private static int fib2(int n) {
		if(!memo.containsKey(n)) {
			// etapa de memorização
			memo.put(n, fib2(n - 1) + fib2(n-2));
		}
		return memo.get(n);
	}
	
	//metodo recursivo maldito sugador de memoria, lembrar de nunca usar
	private static int fibo(int n) {
		if (n < 2) {
			return n;
		}
		return fibo(n - 1) + fibo(n -2);
	}
	
		
	public static void main(String[] args) {
		stream().limit(41).forEachOrdered(System.out::println);
//		for(int i = 0; i < 10; i++) {
//			System.out.println(fib3(i));
//		}
		
	}

}
