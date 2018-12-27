package br.com.controleFinanceiro.commons_methods.encryption;

public interface IEncryptionManipulator {
	
	/**
	 * M�todo respons�vel por converter uma String para 
	 * Base64. Encripta��o bas�ca de baixa seguran�a.
	 * 
	 * @param str String a ser convertida
	 * @return
	 */
	String toBase64(String str);
	
	/**
	 * M�todo respons�vel por decodificar uma String em
	 * Base64. 
	 * 
	 * @param strBase64
	 * @return
	 */
	String decodeBase64(String strBase64);
	
	
	/**
	 * M�todo respons�vel por gerar um MD5 da String enviada.
	 * 
	 * @param str
	 * @return
	 */
	String toMD5(String str);

}
