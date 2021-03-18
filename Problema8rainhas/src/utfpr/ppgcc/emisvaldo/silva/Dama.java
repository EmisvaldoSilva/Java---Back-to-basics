package utfpr.ppgcc.emisvaldo.silva;

import java.util.ArrayList;
import java.util.Random;

public class Dama{
	private static int popInicial  = 75;              	 	  // Tamanho da população inicial.
	private static int geracaoMaxima = 100;                  // Número de ciclos de teste. 
	private static double probabilidadeDeCruzamento = 0.7;  // Probabilidade de cruzamento de dois cromossomos. TAxa: 0.0 < probabilidadeDeCruzamento < 1.0
	private static double taxaDeMutacao = 0.001;            // Taxa de mutacao. Rate: 0.0 < taxaDeMutacao < 1.0
	private static int selecaoMinima = 2;                  // Pais mínimos permitidos para seleção.
	private static int selecaoMaxima = 50;                // Máximo de pais permitidos para seleção. RAte: selecaoMinima < selecaoMaxima < START_SIZE
	private static int prolePorGeracao = 20;      	 	 // Nova prole criada por geração. Range: 0 < OFFSPRING_PER_GENERATION < selecaoMaxima.
	private static int randomizacaoMinima = 8;          // Pra randomizar cromossomos iniciais 
	private static int randomizacaoMaxima = 20;
	private static int PBC_MAX = 4;                   // Pontos máximos de cruzamento baseados na posição. Range: 0 < PBC_MAX < 8 (> 8 não é bom).
	private static int geracao = 0;
	private static int filhoCont = 0;
	private static int proxMutacao = 0;            // Pra programar mutações. 
	private static int mutacoes = 0;

	private static ArrayList<Cromossomo> populacao = new ArrayList<Cromossomo>();

	private static void algoritimoGenetico(){
		int popSize = 0;
		Cromossomo thisChromo = null;
		boolean done = false;

		inicializarCromossomos();
		mutacoes = 0;
		proxMutacao = getNumeroAleatorio(0, (int)Math.round(1.0 / taxaDeMutacao));

		while(!done)
		{
			popSize = populacao.size();
			for(int i = 0; i < popSize; i++)
			{
				thisChromo = populacao.get(i);
				if((thisChromo.ataques() == 0) || geracao == geracaoMaxima){
					done = true;
				}
			}

			getFitness();

			roletaSelecao();

			cruzamento();

			prepProxGeracao();

			geracao++;
			// This is here simply to show the runtime status.
			System.out.println("geracao: " + geracao);
		}

		System.out.println("done.");

		if(geracao != geracaoMaxima){
			popSize = populacao.size();
			for(int i = 0; i < popSize; i++)
			{
				thisChromo = populacao.get(i);
				if(thisChromo.ataques() == 0){
					mostrarMelhorSolucao(thisChromo);
				}
			}
		}
		System.out.println("Completed " + geracao + " geracaos.");
		System.out.println("Encountered " + mutacoes + " mutacoes in " + filhoCont + " offspring.");
		return;
	}

	private static void getFitness(){
		// Erros mais baixos = 100%, erros mais altos = 0%
		int tamanhoPop = populacao.size();
		Cromossomo esseCromo = null;
		double melhorScore = 0;
		double piorScore = 0;

		// A pior pontuação seria aquela com a pontuacao mais alta, a melhor seria a mais baixa. 
		piorScore = populacao.get(maximo()).ataques();

		// Converta em uma porcentagem ponderada.
		melhorScore = piorScore - populacao.get(minimo()).ataques();

		for(int i = 0; i < tamanhoPop; i++){
			esseCromo = populacao.get(i);
			esseCromo.fitness((piorScore - esseCromo.ataques()) * 100.0 / melhorScore);
		}

		return;
	}

	private static void roletaSelecao(){
		int j = 0;
		int popSize = populacao.size();
		double genTotal = 0.0;
		double selTotal = 0.0;
		int maximumToSelect = getNumeroAleatorio(selecaoMinima, selecaoMaxima);
		double rouletteSpin = 0.0;
		Cromossomo thisChromo = null;
		Cromossomo thatChromo = null;
		boolean done = false;

		for(int i = 0; i < popSize; i++){
			thisChromo = populacao.get(i);
			genTotal += thisChromo.fitness();
		}

		genTotal *= 0.01;

		for(int i = 0; i < popSize; i++){
			thisChromo = populacao.get(i);
			thisChromo.ProbabilidadeDeSelecao(thisChromo.fitness() / genTotal);
		}

		for(int i = 0; i < maximumToSelect; i++){
			rouletteSpin = getNumeroAleatorio(0, 99);
			j = 0;
			selTotal = 0;
			done = false;
			while(!done){
				thisChromo = populacao.get(j);
				selTotal += thisChromo.ProbabilidadeDeSelecao();
				if(selTotal >= rouletteSpin){
					if(j == 0){
						thatChromo = populacao.get(j);
					}else if(j >= popSize - 1){
						thatChromo = populacao.get(popSize - 1);
					}else{
						thatChromo = populacao.get(j - 1);
					}
					thatChromo.Selecionado(true);
					done = true;
				}else{
					j++;
				}
			}
		}
		return;
	}

	//  É aqui que você pode escolher entre as opções: 
	//  Para escolher entre as opções de crossover, descomente uma das seguintes:  
	//     partiallyMappedCrossover(),
	//     positionBasedCrossover(), while keeping the other two commented out.

	//  Lembre-se de que o código ainda será executado se (você tentar combinações ou descomentar todos eles, 
	//  mas isso pode atrapalhar o algoritmo em geral. 
	//  Claro, eu sempre posso estar errado, experimente e descubra! 
	private static void cruzamento(){
		int getRand = 0;
		int parA = 0;
		int parB = 0;
		int novoIndex1 = 0;
		int novoIndex2 = 0;
		Cromossomo novoCromo1 = null;
		Cromossomo novoCromo2 = null;

		for(int i = 0; i < prolePorGeracao; i++){
			parA = chooseParent();
			// teste de probabilidade de cruzamento .
			getRand = getNumeroAleatorio(0, 100);
			if(getRand <= probabilidadeDeCruzamento * 100){
				parB = escolherPar(parA);
				novoCromo1 = new Cromossomo();
				novoCromo2 = new Cromossomo();
				populacao.add(novoCromo1);
				novoIndex1 = populacao.indexOf(novoCromo1);
				populacao.add(novoCromo2);
				novoIndex2 = populacao.indexOf(novoCromo2);

				// Escolha um ou ambos: 
					//crossoverParcialmenteMapeado(parA, parB, novoIndex1, novoIndex2);
				positionBasedCrossover(parA, parB, novoIndex1, novoIndex2);

				if(filhoCont - 1 == proxMutacao){
					comutaMutacao(novoIndex1, 1);
				}else if(filhoCont == proxMutacao){
					comutaMutacao(novoIndex2, 1);
				}

				populacao.get(novoIndex1).contarAtaques();
				populacao.get(novoIndex2).contarAtaques();

				filhoCont += 2;

				// Programe a próxima mutação. 
				if(filhoCont % (int)Math.round(1.0 / taxaDeMutacao) == 0){
					proxMutacao = filhoCont + getNumeroAleatorio(0, (int)Math.round(1.0 / taxaDeMutacao));
				}
			}
		} // i
		return;
	}



	private static void positionBasedCrossover(int chromA, int chromB, int child1, int child2){
		int k = 0;
		int numPoints = 0;
		int tempArray1[] = new int[Cromossomo.tamanhoTabuleiro];
		int tempArray2[] = new int[Cromossomo.tamanhoTabuleiro];
		boolean matchFound = false;
		Cromossomo thisChromo = populacao.get(chromA);
		Cromossomo thatChromo = populacao.get(chromB);
		Cromossomo newChromo1 = populacao.get(child1);
		Cromossomo newChromo2 = populacao.get(child2);

		// Escolha e classifique os pontos de cruzamento.
		numPoints = getNumeroAleatorio(0, PBC_MAX);
		int crossPoints[] = new int[numPoints];
		for(int i = 0; i < numPoints; i++){
			crossPoints[i] = getRandomNumber(0, Cromossomo.tamanhoTabuleiro - 1, crossPoints);
		} // i

		// Obtenha não escolhidos do pai 2 
		k = 0;
		for(int i = 0; i < Cromossomo.tamanhoTabuleiro; i++){
			matchFound = false;
			for(int j = 0; j < numPoints; j++){
				if(thatChromo.data(i) == thisChromo.data(crossPoints[j])){
					matchFound = true;
				}
			} // j
			if(matchFound == false){
				tempArray1[k] = thatChromo.data(i);
				k++;
			}
		} // i

		// Insira escolhas na criança 1. 
		for(int i = 0; i < numPoints; i++){
			newChromo1.data(crossPoints[i], thisChromo.data(crossPoints[i]));
		}

		// Preencha os não escolhidos para a criança 1. 
		k = 0;
		for(int i = 0; i < Cromossomo.tamanhoTabuleiro; i++){
			matchFound = false;
			for(int j = 0; j < numPoints; j++){
				if(i == crossPoints[j]){
					matchFound = true;
				}
			} // j
			if(matchFound == false){
				newChromo1.data(i, tempArray1[k]);
				k++;
			}
		} // i

		// Obtenha não escolhidos do pai 1 
		k = 0;
		for(int i = 0; i < Cromossomo.tamanhoTabuleiro; i++){
			matchFound = false;
			for(int j = 0; j < numPoints; j++)
			{
				if(thisChromo.data(i) == thatChromo.data(crossPoints[j])){
					matchFound = true;
				}
			} // j
			if(matchFound == false){
				tempArray2[k] = thisChromo.data(i);
				k++;
			}
		} // i

		// Insira escolhas na criança 2. 
		for(int i = 0; i < numPoints; i++){
			newChromo2.data(crossPoints[i], thatChromo.data(crossPoints[i]));
		}

		// Preencha os não escolhidos para a criança 2. 
		k = 0;
		for(int i = 0; i < Cromossomo.tamanhoTabuleiro; i++){
			matchFound = false;
			for(int j = 0; j < numPoints; j++)
			{
				if(i == crossPoints[j]){
					matchFound = true;
				}
			} // j
			if(matchFound == false){
				newChromo2.data(i, tempArray2[k]);
				k++;
			}
		} // i
		return;
	}

	private static void comutaMutacao(final int index, final int exchanges){
		int i =0;
		int tempData = 0;
		Cromossomo thisChromo = null;
		int gene1 = 0;
		int gene2 = 0;
		boolean done = false;

		thisChromo = populacao.get(index);

		while(!done){
			gene1 = getNumeroAleatorio(0, Cromossomo.tamanhoTabuleiro - 1);
			gene2 = getExclusiveRandomNumber(Cromossomo.tamanhoTabuleiro - 1, gene1);

			// Troque os genes escolhidos. 
			tempData = thisChromo.data(gene1);
			thisChromo.data(gene1, thisChromo.data(gene2));
			thisChromo.data(gene2, tempData);

			if(i == exchanges){
				done = true;
			}
			i++;
		}
		mutacoes++;
		return;
	}

	private static int chooseParent(){
		// Função sobrecarregada, consulte também "chooseparent (ByVal parentA As Integer)". 
		int parent = 0;
		Cromossomo thisChromo = null;
		boolean done = false;

		while(!done) {
			// Randomly choose an eligible parent.
			parent = getNumeroAleatorio(0, populacao.size() - 1);
			thisChromo = populacao.get(parent);
			if(thisChromo.Selecionado() == true){
				done = true;
			}
		}

		return parent;
	}

	private static int escolherPar(final int parentA){
		// Função sobrecarregada, consulte também "chooseparent ()". 
		int parent = 0;
		Cromossomo thisChromo = null;
		boolean done = false;

		while(!done){
			// Escolha aleatoriamente um pai elegível. 
			parent = getNumeroAleatorio(0, populacao.size() - 1);
			if(parent != parentA){
				thisChromo = populacao.get(parent);
				if(thisChromo.Selecionado() == true){
					done = true;
				}
			}
		}

		return parent;
	}

	private static void prepProxGeracao(){
		int popSize = 0;
		Cromossomo thisChromo = null;

		// Redefina sinalizadores para indivíduos selecionados. 
		popSize = populacao.size();
		for(int i = 0; i < popSize; i++){
			thisChromo = populacao.get(i);
			thisChromo.Selecionado(false);
		}
		return;
	}

	private static void mostrarMelhorSolucao(Cromossomo melhorSolucao){
		String board[][] = new String[Cromossomo.tamanhoTabuleiro][Cromossomo.tamanhoTabuleiro];

		// Limpar tabuleiro.
		for(int x = 0; x < Cromossomo.tamanhoTabuleiro; x++){
			for(int y = 0; y < Cromossomo.tamanhoTabuleiro; y++){
				board[x][y] = "";
			}
		}

		//popular tabuleiro com a melhor solução
		for(int x = 0; x < Cromossomo.tamanhoTabuleiro; x++){
			board[x][melhorSolucao.data(x)] = "D";
		}

		// Mostrar tabuleiro.
		System.out.println("Tabuleiro:");
		for(int y = 0; y < Cromossomo.tamanhoTabuleiro; y++){
			for(int x = 0; x < Cromossomo.tamanhoTabuleiro; x++){
				if(board[x][y] == "D"){
					System.out.print("D ");
				}else{
					System.out.print(". ");
				}
			}
			System.out.print("\n");
		}

		return;
	}

	private static int getNumeroAleatorio(final int low, final int high){
		return (int)Math.round((high - low) * new Random().nextDouble() + low);
	}

	private static int getExclusiveRandomNumber(final int high, final int exceto){
		boolean done = false;
		int getRand = 0;

		while(!done){
			getRand = new Random().nextInt(high);
			if(getRand != exceto){
				done = true;
			}
		}

		return getRand;
	}

	private static int getRandomNumber(int low, int high, int[] except){
		boolean done = false;
		int getRand = 0;

		if(high != low){
			while(!done)
			{
				done = true;
				getRand = (int)Math.round((high - low) * new Random().nextDouble() + low);
				for(int i = 0; i < except.length; i++){ // UBound (exceto) 
					if(getRand == except[i]){
						done = false;
					}
				} // i
			}
			return getRand;
		}else{
			return high; // ou baixo (não importa). 
		}
	}

	private static int minimo(){
		// Retorna um índice de matriz. 
		int tamanhoPop = 0;
		Cromossomo esseCromo = null;
		Cromossomo aqueleCromo = null;
		int vencedor = 0;
		boolean encontrouNovoVencedor = false;
		boolean feito = false;

		while(!feito){
			encontrouNovoVencedor = false;
			tamanhoPop = populacao.size();
			for(int i = 0; i < tamanhoPop; i++){
				if(i != vencedor){             // Avoid self-comparison.
					esseCromo = populacao.get(i);
					aqueleCromo = populacao.get(vencedor);
					if(esseCromo.ataques() < aqueleCromo.ataques()){
						vencedor = i;
						encontrouNovoVencedor = true;
					}
				}
			}
			if(encontrouNovoVencedor == false){
				feito = true;
			}
		}
		return vencedor;
	}

	private static int maximo(){
		// Retorna um índice de matriz. 
		int TamanhoPop = 0;
		Cromossomo thisChromo = null;
		Cromossomo thatChromo = null;
		int vencedor = 0;
		boolean encontrouNovoVencedor = false;
		boolean feito = false;

		while(!feito){
			encontrouNovoVencedor = false;
			TamanhoPop = populacao.size();
			for(int i = 0; i < TamanhoPop; i++){
				if(i != vencedor){             // Evitando a auto-comparação. 
					thisChromo = populacao.get(i);
					thatChromo = populacao.get(vencedor);
					if(thisChromo.ataques() > thatChromo.ataques()){
						vencedor = i;
						encontrouNovoVencedor = true;
					}
				}
			}
			if(encontrouNovoVencedor == false){
				feito = true;
			}
		}
		return vencedor;
	}

	private static void inicializarCromossomos(){
		int shuffles = 0;
		Cromossomo newCromo = null;
		int chromoIndex = 0;

		for(int i = 0; i < popInicial ; i++){
			newCromo = new Cromossomo();
			populacao.add(newCromo);
			chromoIndex = populacao.indexOf(newCromo);

			// Escolha aleatoriamente o número de embaralhamentos a serem executados. 
			shuffles = getNumeroAleatorio(randomizacaoMinima, randomizacaoMaxima);

			comutaMutacao(chromoIndex, shuffles);

			populacao.get(chromoIndex).contarAtaques();

		}
		return;
	}



	public static void main(String[] args)
	{
		algoritimoGenetico();
		return;
	}

}