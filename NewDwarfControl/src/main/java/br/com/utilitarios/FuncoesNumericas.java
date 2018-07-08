package br.com.utilitarios;

/**
 * Classe responsável por tratamento e manipulações envolvendo números
 * 
 * @author Thiago Teodoro
 *
 */
public class FuncoesNumericas {

	/**
	 * Essa função prepara e converte um float String com virgulas e pontos
	 * para converção para o float Float. 
	 * 
	 * @param Numero numero em String a ser convertido.
	 * @return float convertido ou null.
	 */
	public float toFloat(String Numero) {
		
		//Remove pontos da String que separam unidade de milhar isso não existe no float.
		Numero = Numero.replace(".", "");
		
		//Substitui as virgulas da String por pontos, que é o Padrão Americano do Float para converção
		Numero = Numero.replace(",", ".");
		
		//Realizando Parse
		return Float.parseFloat(Numero); 
		
	}
	
	
	
}
