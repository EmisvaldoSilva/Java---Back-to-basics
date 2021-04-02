package utfpr.ppgcc.emisvaldo.silva.model;

import java.util.Random;

class Utils {
	
	int tamanhoDoTabuleiro;
	
	public Utils(int tamanhoTabuleiro) {
		this.tamanhoDoTabuleiro = tamanhoTabuleiro;
	}
	
/**********************************Utilitarios pra tratar numeros************************************************/
	
	//Metodo pra tarefas gerais de geração de numeros aleatorios 
	public static int getNumeroAleatorio(int low, int high, int[] except){
		boolean feito = false;
		int getRand = 0;

		if(high != low){
			while(!feito)
			{
				feito = true;
				getRand = (int)Math.round((high - low) * new Random().nextDouble() + low);
				for(int i = 0; i < except.length; i++){ // UBound (exceto) 
					if(getRand == except[i]){
						feito = false;
					}
				} // i
			}
			return getRand;
		}else{
			return high;
		}
	}
	
	//metodo para pegar um numero aleatorio nao repetido
	public static int getNumeroAleatorioExclusivo(final int high, final int exceto){
		boolean feito = false;
		int getRand = 0;

		while(!feito){
			getRand = new Random().nextInt(high);
			if(getRand != exceto){
				feito = true;
			}
		}

		return getRand;
	}
	
	public static int getNumeroAleatorio(final int low, final int high){
		return (int)Math.round((high - low) * new Random().nextDouble() + low);
	}

}
