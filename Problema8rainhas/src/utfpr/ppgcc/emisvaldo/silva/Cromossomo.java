package utfpr.ppgcc.emisvaldo.silva;


public class Cromossomo{
	static int tamanhoTabuleiro = 8;                    	 // tAmanho do tabuleiro de xadrez.

	private int mData[] = new int[tamanhoTabuleiro]; //Array com as rainhas
    private double mFitness = 0.0;
    private boolean mSelecionado = false;
    private double mProbabilidadeDeSelecao = 0.0;
    private int mAtaques = 0;

    //Ao ser instaciado um novo cromossomo ele vai montarum tabuleiro com tamanho definido tamanhoTabuleiro
    public Cromossomo(){
        for(int i = 0; i < tamanhoTabuleiro; i++){
            this.mData[i] = i;
        }
        return;
    }
    
    public void contarAtaques(){
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
            tabuleiro[i][this.mData[i]] = "D";
        }

        // Percorra cada uma das Rainhas e calcule o número de ataques.
        for(int i = 0; i < tamanhoTabuleiro; i++){
            x = i;
            y = this.mData[i];

            // Checar diagonais.
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

        this.mAtaques = ataques;
    }
    
    public void ataques(int value){
        this.mAtaques = value;
        return;
    }
    
    public int ataques(){
        return this.mAtaques;
    }

    public double ProbabilidadeDeSelecao()    {
        return mProbabilidadeDeSelecao;
    }
    
    public void ProbabilidadeDeSelecao(final double ProbSel){
        mProbabilidadeDeSelecao = ProbSel;
        return;
    }

    public boolean Selecionado(){
        return mSelecionado;
    }
    
    public void Selecionado(final boolean sValue)
    {
        mSelecionado = sValue;
        return;
    }

    public double fitness(){
        return mFitness;
    }
    
    public void fitness(final double score){
        mFitness = score;
        return;
    }

    public int data(final int index){
        return mData[index];
    }
    
    public void data(final int index, final int value){
        mData[index] = value;
        return;
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
}