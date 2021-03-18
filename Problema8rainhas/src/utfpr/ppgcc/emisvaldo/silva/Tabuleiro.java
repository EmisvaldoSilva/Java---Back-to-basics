package utfpr.ppgcc.emisvaldo.silva;

class Tabuleiro {
	private static int damas[];
	private static int tamanhoTabuleiro;
	private static double fitness;
	
	public Tabuleiro(int tamanhoTabuleiro){
		setDamas(new int[tamanhoTabuleiro]);
        for(int i = 0; i < tamanhoTabuleiro; i++){
            getDamas()[i] = i;
        }
        setTamanhoTabuleiro(tamanhoTabuleiro);
        return;
    }
	
	int contarAtaques(){
        int x = 0;
        int y = 0;
        int tempx = 0;
        int tempy = 0;
        String tabuleiro[][] = new String[getTamanhoTabuleiro()][getTamanhoTabuleiro()];
        int ataques = 0;
        int dx[] = new int[] {-1, 1, -1, 1};
        int dy[] = new int[] {-1, 1, 1, -1};
        boolean ok = false;

        // limpar tabuleiro.
        for(int i = 0; i < getTamanhoTabuleiro(); i++){
            for(int j = 0; j < getTamanhoTabuleiro(); j++){
                tabuleiro[i][j] = "";
            }
        }

        for(int i = 0; i < getTamanhoTabuleiro(); i++){
            tabuleiro[i][ getDamas()[i]] = "D";
        }

        // Percorra cada uma das Rainhas e calcule o número de ataques.
        for(int i = 0; i < getTamanhoTabuleiro(); i++){
            x = i;
            y = getDamas()[i];

            // Checar diagonais.
            for(int j = 0; j <= 3; j++){
                tempx = x;
                tempy = y;
                ok = false;
                while(!ok){
                    tempx += dx[j];
                    tempy += dy[j];
                    if((tempx < 0 || tempx >= getTamanhoTabuleiro()) || (tempy < 0 || tempy >= getTamanhoTabuleiro())){
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
	
	/**
	 * @return the damas
	 */
	public static int[] getDamas() {
		return damas;
	}

	/**
	 * @param damas the damas to set
	 */
	public static void setDamas(int damas[]) {
		Tabuleiro.damas = damas;
	}

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
		Tabuleiro.tamanhoTabuleiro = tamanhoTabuleiro;
	}

	/**
	 * @return the fitness
	 */
	public static double getFitness() {
		return fitness;
	}

	/**
	 * @param fitness the fitness to set
	 */
	public static void setFitness(double fitness) {
		Tabuleiro.fitness = fitness;
	}
}
