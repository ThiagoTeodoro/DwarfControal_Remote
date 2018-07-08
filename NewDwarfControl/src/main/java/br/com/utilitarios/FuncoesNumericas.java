package br.com.utilitarios;

/**
 * Classe respons�vel por tratamento e manipula��es envolvendo n�meros
 * 
 * @author Thiago Teodoro
 *
 */
public class FuncoesNumericas {

	/**
	 * Essa fun��o prepara e converte um float String com virgulas e pontos
	 * para conver��o para o float Float. 
	 * 
	 * @param Numero numero em String a ser convertido.
	 * @return float convertido ou null.
	 */
	public float toFloat(String Numero) {
		
		//Remove pontos da String que separam unidade de milhar isso n�o existe no float.
		Numero = Numero.replace(".", "");
		
		//Substitui as virgulas da String por pontos, que � o Padr�o Americano do Float para conver��o
		Numero = Numero.replace(",", ".");
		
		//Realizando Parse
		return Float.parseFloat(Numero); 
		
	}
	
	
	
}
