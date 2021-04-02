package utfpr.ppgcc.emisvaldo.silva.model;
import java.util.ArrayList;

/**
 * @author emisvaldo
 *
 */
public class Cromossomo{
	static int tamanhoTabuleiro = 8;                    	 // tAmanho do tabuleiro de xadrez.
	private int dama[] = new int[tamanhoTabuleiro]; //Array com as rainhas
	private double fitnes = 0.0; //garda a pontuação do cromossomo
	private boolean selecionado = false;
	private double mProbabilidadeDeSelecao = 0.0;
	
	//Ao ser instaciado um novo cromossomo ele vai montarum tabuleiro com tamanho definido tamanhoTabuleiro
	public Cromossomo(){
		for(int i = 0; i < tamanhoTabuleiro; i++){
			this.dama[i] = i;
		}
	}

	public int contarAtaques(){
		int x = 0;
		int y = 0;
		int tempx = 0;
		int tempy = 0;
		String tabuleiro[][] = new String[tamanhoTabuleiro][tamanhoTabuleiro];
		int ataques = 0;
		int dx[] = new int[] {-1, 1, -1, 1};
		int dy[] = new int[] {-1, 1, 1, -1};
		boolean ok = false;

		// limpar tabuleiro.
		for(int i = 0; i < tamanhoTabuleiro; i++){
			for(int j = 0; j < tamanhoTabuleiro; j++){
				tabuleiro[i][j] = "";
			}
		}

		for(int i = 0; i < tamanhoTabuleiro; i++){
			tabuleiro[i][this.dama[i]] = "D";
		}

		// Percorra cada uma das Rainhas e calcule o número de ataques.
		for(int i = 0; i < tamanhoTabuleiro; i++){
			x = i;
			y = this.dama[i];

			// Checar diagonais por meio das coordenadas.
			for(int j = 0; j <= 3; j++){
				tempx = x;
				tempy = y;
				ok = false;
				while(!ok){
					tempx += dx[j];
					tempy += dy[j];
					if((tempx < 0 || tempx >= tamanhoTabuleiro) || (tempy < 0 || tempy >= tamanhoTabuleiro)){
						ok = true;
					}else{
						if(tabuleiro[tempx][tempy].compareToIgnoreCase("D") == 0){
							ataques++;
						}
					}
				}
			}
		}

		return ataques;
	}

	public static double getFitness(ArrayList<Cromossomo> populacao){
		// Erros mais baixos = 100%, erros mais altos = 0%
		Cromossomo esseCromo = null;
		double melhorScore = 0;
		double piorScore = 0;

		// A pior pontuação seria aquela com a pontuacao mais alta, a melhor seria a mais baixa. 
		piorScore = populacao.get(Dama.maximo()).contarAtaques();

		melhorScore = piorScore - populacao.get(Dama.minimo()).contarAtaques();

		for(int i = 0; i < populacao.size(); i++){
			esseCromo = populacao.get(i);
			esseCromo.setFitnes((piorScore - esseCromo.contarAtaques()) * 100.0 / melhorScore);
		}
		return melhorScore;
	}

	public double ProbabilidadeDeSelecao()    {
		return mProbabilidadeDeSelecao;
	}

	public void ProbabilidadeDeSelecao(final double ProbSel){
		mProbabilidadeDeSelecao = ProbSel;
	}

	public int data(final int index){
		return dama[index];
	}

	public void data(final int index, final int value){
		dama[index] = value;
	}

	//###################### Metodos de acesso e modificadores ###################################

	/**
	 * @return the tamanhoTabuleiro
	 */
	public static int getTamanhoTabuleiro() {
		return tamanhoTabuleiro;
	}

	/**
	 * @param tamanhoTabuleiro the tamanhoTabuleiro to set
	 */
	public static void setTamanhoTabuleiro(int tamanhoTabuleiro) {
		Cromossomo.tamanhoTabuleiro = tamanhoTabuleiro;
	}

	/**
	 * @return the fitnes
	 */
	public double getFitness() {
		return fitnes;
	}

	/**
	 * @param fitnes the fitnes to set
	 */
	public void setFitnes(double fitness) {
		this.fitnes = fitness;
	}

	/**
	 * @return the selecionado
	 */
	public boolean isSelecionado() {
		return selecionado;
	}

	/**
	 * @param selecionado the selecionado to set
	 */
	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}
}