package utfpr.ppgcc.emisvaldo.silva.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
public class Dama{
	private static int popInicial  = 5;              	 	  // Tamanho da população inicial.
	private static int geracaoMaxima = 100;                  // Número de ciclos de teste. 
	private static double probabilidadeDeCruzamento = 0.7;  // Probabilidade de cruzamento de dois cromossomos. TAxa: 0.0 < probabilidadeDeCruzamento < 1.0
	private static double taxaDeMutacao = 0.001;            // Taxa de mutacao. Rate: 0.0 < taxaDeMutacao < 1.0
	private static int selecaoMinima = 2;                  // pars mínimos permitidos para seleção.
	private static int selecaoMaxima = 40;                // Máximo de pars permitidos para seleção. Avalia: selecaoMinima < selecaoMaxima < tamanho do tabuleiro
	private static int prolePorGeracao = 40;      	 	 // Nova prole criada por geração. Faixa: 0 < plole por geracao < selecaoMaxima.
	private static int randomizacaoMinima = 8;          // Pra randomizar cromossomos iniciais 
	private static int randomizacaoMaxima = 20;
	private static int PBC_MAX = 4;                   // Pontos máximos de cruzamento baseados na posição. Faixa: 0 < PBC_MAX < 8 (> 8 não é bom).
	private static int geracao = 0;
	private static int filhoCont = 0;
	private static int proxMutacao = 0;            // Pra programar mutações. 
	private static int mutacoes = 0;
	private static ArrayList<Cromossomo> populacao = new ArrayList<Cromossomo>();

	/*
	 *  have never encountered any problem where genetic algorithms seemed to me 
	 *  the right wayto attack it. Further, I have never seen any computational 
	 *  results reported using geneticalgorithms that have favorably impressed me.
	 *  
	 *  Steven Skiena, The Algorithm Design Manual, 2nd ed. (Springer, 2009), p. 267.
	*/
	private static void algoritimoGenetico(){
		int tamanhoPop = 0;
		Cromossomo esseCromo = null;
		boolean feito = false;

		/******************************INICIALIZACAO*****************************************/
		inicializarCromossomos();
		mutacoes = 0;
		proxMutacao = Utils.getNumeroAleatorio(0, (int)Math.round(1.0 / taxaDeMutacao));
		while(!feito){
			tamanhoPop = populacao.size();
			for(int i = 0; i < tamanhoPop; i++)	{
				esseCromo = populacao.get(i);
				if((esseCromo.contarAtaques() == 0) || geracao == geracaoMaxima){
					feito = true;
				}
			}
			if(!feito) {

				/******************************MEDE A ADAPTACAO****************************************/			
				Cromossomo.getFitness(populacao);

				/******************************ETAPA DE SELECAO****************************************/
				roletaSelecao();

				/******************************NOVA  GERACAO******************************************/
				cruzamento();
				prepProxGeracao();

				geracao++;
				// Mostar status das gerações.
				System.out.println("geracao: " + geracao);
				mostrarSolucao(esseCromo);
			}
		}
		
		Boolean solucao = false;

		//Criterios de parada
		if(geracao != geracaoMaxima){
			tamanhoPop = populacao.size();
			for(int i = 0; i < tamanhoPop; i++){
				esseCromo = populacao.get(i);
				if(esseCromo.contarAtaques() == 0){
					mostrarSolucao(esseCromo);
					solucao = true;

				}
			}
			if (solucao == true) {
				System.out.println("Essa é a solução: ");
			}else {
				System.out.println("Não conseguiu resolver");
			}
		}
		System.out.println(geracao + " Geraçoes completas.");
		System.out.println("Encontrada " + mutacoes + " mutações em " + filhoCont + " proles.");
	}

	//////////////////////////////////////

	private static void roletaSelecao(){
		int j = 0;
		int popSize = populacao.size();
		double genTotal = 0.0;
		double selTotal = 0.0;
		int maximumToSelect = Utils.getNumeroAleatorio(selecaoMinima, selecaoMaxima);
		//System.out.println("-----------------" + " Selecionados " + maximumToSelect);
		double rouletteSpin = 0.0;
		Cromossomo esseCromo = null;
		Cromossomo aqueleCromo = null;
		boolean feito = false;

		for(int i = 0; i < popSize; i++){
			esseCromo = populacao.get(i);
			genTotal += esseCromo.getFitness();
		}

		genTotal *= 0.01;

		for(int i = 0; i < popSize; i++){
			esseCromo = populacao.get(i);
			esseCromo.ProbabilidadeDeSelecao(esseCromo.getFitness() / genTotal);
		}

		for(int i = 0; i < maximumToSelect; i++){
			rouletteSpin =  Utils.getNumeroAleatorio(0, 99);
			//System.out.println("-----------------" + " giro roleta " + rouletteSpin);
			j = 0;
			selTotal = 0;
			feito = false;
			while(!feito){
				esseCromo = populacao.get(j);
				selTotal += esseCromo.ProbabilidadeDeSelecao();
				if(selTotal >= rouletteSpin){
					if(j == 0){
						aqueleCromo = populacao.get(j);
					}else if(j >= popSize - 1){
						aqueleCromo = populacao.get(popSize - 1);
					}else{
						aqueleCromo = populacao.get(j - 1);
					}
					aqueleCromo.setSelecionado(true);
					feito = true;
				}else{
					j++;
				}
			}
		}
	}


	//  Utilizei o cruzamento baseando em na posição
	private static void cruzamento(){
		int getRand = 0;
		int parA = 0;
		int parB = 0;
		int novoIndex1 = 0;
		int novoIndex2 = 0;
		Cromossomo novoCromo1 = null;
		Cromossomo novoCromo2 = null;

		for(int i = 0; i < prolePorGeracao; i++){
			parA = escolherPar();
			// teste de probabilidade de cruzamento .
			getRand = Utils.getNumeroAleatorio(0, 100);
			if(getRand <= probabilidadeDeCruzamento * 100){
				parB = escolherPar(parA);
				novoCromo1 = new Cromossomo();
				novoCromo2 = new Cromossomo();
				populacao.add(novoCromo1);
				novoIndex1 = populacao.indexOf(novoCromo1);
				///////////////////////////////////////    Teste para pega o Index de um par System.out.println(novoIndex1);
				populacao.add(novoCromo2);
				novoIndex2 = populacao.indexOf(novoCromo2);

				CrossoverBaseadoPosicao(parA, parB, novoIndex1, novoIndex2);

				if(filhoCont - 1 == proxMutacao){
					mutacao(novoIndex1, 1);
				}else if(filhoCont == proxMutacao){
					mutacao(novoIndex2, 1);
				}

				populacao.get(novoIndex1).contarAtaques();
				populacao.get(novoIndex2).contarAtaques();

				filhoCont += 2;

				// Programe a próxima mutação. 
				if(filhoCont % (int)Math.round(1.0 / taxaDeMutacao) == 0){
					proxMutacao = filhoCont + Utils.getNumeroAleatorio(0, (int)Math.round(1.0 / taxaDeMutacao));
				}
			}
		} // i
	}

	private static void CrossoverBaseadoPosicao(int cromA, int cromB, int filho1, int filho2){
		int k = 0;
		int numPontos = 0;
		int tempArray1[] = new int[Cromossomo.tamanhoTabuleiro];
		int tempArray2[] = new int[Cromossomo.tamanhoTabuleiro];
		boolean matchFound = false;
		Cromossomo esseCromo = populacao.get(cromA);
		Cromossomo aqueleCromo = populacao.get(cromB);
		Cromossomo novoCromo1 = populacao.get(filho1);
		Cromossomo nevoCromo2 = populacao.get(filho2);

		// Escolha e classifique os pontos de cruzamento.
		numPontos = Utils.getNumeroAleatorio(0, PBC_MAX);
		int crossPoints[] = new int[numPontos];
		for(int i = 0; i < numPontos; i++){
			crossPoints[i] = Utils.getNumeroAleatorio(0, Cromossomo.tamanhoTabuleiro - 1, crossPoints);
		} // i

		// Obtenha não escolhidos do par 2 
		k = 0;
		for(int i = 0; i < Cromossomo.tamanhoTabuleiro; i++){
			matchFound = false;
			for(int j = 0; j < numPontos; j++){
				if(aqueleCromo.data(i) == esseCromo.data(crossPoints[j])){
					matchFound = true;
				}
			} // j
			if(matchFound == false){
				tempArray1[k] = aqueleCromo.data(i);
				k++;
			}
		} // i

		// Insira escolhas no filho 1. 
		for(int i = 0; i < numPontos; i++){
			novoCromo1.data(crossPoints[i], esseCromo.data(crossPoints[i]));
		}

		// Preencha os não escolhidos para o filho 1. 
		k = 0;
		for(int i = 0; i < Cromossomo.tamanhoTabuleiro; i++){
			matchFound = false;
			for(int j = 0; j < numPontos; j++){
				if(i == crossPoints[j]){
					matchFound = true;
				}
			} // j
			if(matchFound == false){
				novoCromo1.data(i, tempArray1[k]);
				k++;
			}
		} // i

		// Obtenha não escolhidos do par 1 
		k = 0;
		for(int i = 0; i < Cromossomo.tamanhoTabuleiro; i++){
			matchFound = false;
			for(int j = 0; j < numPontos; j++)
			{
				if(esseCromo.data(i) == aqueleCromo.data(crossPoints[j])){
					matchFound = true;
				}
			} // j
			if(matchFound == false){
				tempArray2[k] = esseCromo.data(i);
				k++;
			}
		} // i

		// Insira escolhas no filho 2. 
		for(int i = 0; i < numPontos; i++){
			nevoCromo2.data(crossPoints[i], aqueleCromo.data(crossPoints[i]));
		}

		// Preencha os não escolhidos para o filho 2. 
		k = 0;
		for(int i = 0; i < Cromossomo.tamanhoTabuleiro; i++){
			matchFound = false;
			for(int j = 0; j < numPontos; j++){
				if(i == crossPoints[j]){
					matchFound = true;
				}
			} // j
			if(matchFound == false){
				nevoCromo2.data(i, tempArray2[k]);
				k++;
			}
		} // i
	}

	private static void mutacao(final int index, final int mudancas){
		int i =0;
		int tempData = 0;
		Cromossomo esseCromo = null;
		int gene1 = 0;
		int gene2 = 0;
		boolean feito = false;

		esseCromo = populacao.get(index);

		while(!feito){
			gene1 = Utils.getNumeroAleatorio(0, Cromossomo.tamanhoTabuleiro - 1);
			gene2 = Utils.getNumeroAleatorioExclusivo(Cromossomo.tamanhoTabuleiro - 1, gene1);

			// Troque os genes escolhidos. 
			tempData = esseCromo.data(gene1);
			esseCromo.data(gene1, esseCromo.data(gene2));
			esseCromo.data(gene2, tempData);

			if(i == mudancas){
				feito = true;
			}
			i++;
		}
		mutacoes++;
	}

	private static int escolherPar(){
		// consulte também "escolherPar (em que o valor do parA é um inteiro)". 
		int par = 0;
		Cromossomo esseCromo = null;
		boolean feito = false;

		while(!feito) {
			// Escolha aleatoriamente um par
			par = Utils.getNumeroAleatorio(0, populacao.size() - 1);
			esseCromo = populacao.get(par);
			if(esseCromo.isSelecionado() == true){
				feito = true;
			}
		}

		return par;
	}

	private static int escolherPar(final int parA){
		//consulte também "escolherPar ()". 
		int par = 0;
		Cromossomo esseCromo = null;
		boolean feito = false;

		while(!feito){
			// Escolha aleatoriamente um par elegível. 
			par = Utils.getNumeroAleatorio(0, populacao.size() - 1);
			if(par != parA){
				esseCromo = populacao.get(par);
				if(esseCromo.isSelecionado() == true){
					feito = true;
				}
			}
		}

		return par;
	}

	private static void prepProxGeracao(){
		Cromossomo esseCromo = null;

		for (Iterator<Cromossomo> iterator = populacao.iterator(); iterator.hasNext();) {
			esseCromo = iterator.next();
			esseCromo.setSelecionado(false);
		}
	}

	private static void mostrarSolucao(Cromossomo melhorSolucao){
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
		System.out.println();
		for(int y = 0; y < Cromossomo.tamanhoTabuleiro; y++){
			for(int x = 0; x < Cromossomo.tamanhoTabuleiro; x++){
				if(board[x][y] == "D"){
					System.out.print(new String(Character.toChars(0xd8)) + " ");
				}else{
					System.out.print(". ");
				}
			}
			System.out.print("\n");
		}
	}

	public static int minimo(){
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
				if(i != vencedor){             // Evite a auto-comparação. 
					esseCromo = populacao.get(i);
					aqueleCromo = populacao.get(vencedor);
					if(esseCromo.contarAtaques() < aqueleCromo.contarAtaques()){
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

	public static int maximo(){
		// Retorna um índice de matriz. 
		int TamanhoPop = 0;
		Cromossomo esseCromo = null;
		Cromossomo aqueleCromo = null;
		int vencedor = 0;
		boolean encontrouNovoVencedor = false;
		boolean feito = false;

		while(!feito){
			encontrouNovoVencedor = false;
			TamanhoPop = populacao.size();
			for(int i = 0; i < TamanhoPop; i++){
				if(i != vencedor){             // Evitando a auto-comparação. 
					esseCromo = populacao.get(i);
					aqueleCromo = populacao.get(vencedor);
					if(esseCromo.contarAtaques() > aqueleCromo.contarAtaques()){
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
		int cromoIndex = 0;

		for(int i = 0; i < popInicial ; i++){
			newCromo = new Cromossomo();
			populacao.add(newCromo);
			cromoIndex = populacao.indexOf(newCromo);

			// Escolha aleatoriamente o número de embaralhamentos a serem executados. 
			shuffles = Utils.getNumeroAleatorio(randomizacaoMinima, randomizacaoMaxima);

			mutacao(cromoIndex, shuffles);


		}
	}

	public static void main(String[] args){
		Scanner ler = new Scanner(System.in);
		String resposta = "";

		algoritimoGenetico();
	}

}