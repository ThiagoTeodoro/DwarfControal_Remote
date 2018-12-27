package br.com.encryption;

public interface IEncryptionManipulator {
	
	/**
	 * Método responsável por converter uma String para 
	 * Base64. Encriptação basíca de baixa segurança.
	 * 
	 * @param str String a ser convertida
	 * @return
	 */
	String toBase64(String str);
	
	/**
	 * Método responsável por decodificar uma String em
	 * Base64. 
	 * 
	 * @param strBase64
	 * @return
	 */
	String decodeBase64(String strBase64);
	
	
	/**
	 * Método responsável por gerar um MD5 da String enviada.
	 * 
	 * @param str
	 * @return
	 */
	String toMD5(String str);

}
