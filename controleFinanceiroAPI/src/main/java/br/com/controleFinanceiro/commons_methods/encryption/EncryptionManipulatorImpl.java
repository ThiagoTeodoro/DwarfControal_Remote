package br.com.controleFinanceiro.commons_methods.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.log4j.Logger;

public class EncryptionManipulatorImpl implements IEncryptionManipulator {
	
	private static Logger LOGGER = Logger.getLogger(EncryptionManipulatorImpl.class);
	private static String KEYCOMPLETE = "636f6d6d6f6e735f6d6574686f6473";

	/**
	 * M�todo respons�vel por converter uma String para 
	 * Base64. Encripta��o bas�ca de baixa seguran�a.
	 * 
	 * @param str String a ser convertida
	 * @return
	 */
	public String toBase64(String str) {
		
		LOGGER.debug(String.format("String recebida para 'Encriptar' : [%s]", str));
		
		String strOriginal = str;
		
		String strEncoder = Base64.getEncoder().encodeToString(strOriginal.getBytes());

		LOGGER.debug(String.format("String ap�s 'Encripita��o' : [%s]", strEncoder));
		
		return strEncoder;
	}

	
	/**
	 * M�todo respons�vel por decodificar uma String em
	 * Base64. 
	 * 
	 * @param strBase64
	 * @return
	 */
	public String decodeBase64(String strBase64) {
		
		LOGGER.debug(String.format("String recebida para decodifica��o : [%s]", strBase64));
		
		String strToDecode = strBase64;
		
		byte[] decodeBytes = Base64.getDecoder().decode(strToDecode);
		String strDecode = new String(decodeBytes);
		
		LOGGER.debug(String.format("String decodifica : [%s]", strDecode));
	
		return strDecode;
		
	}


	/**
	 * M�todo respons�vel por gerar um MD5 da String enviada.
	 * 
	 * @param str
	 * @return
	 */
	public String toMD5(String str) {
		
		//LOGGER.debug(String.format("String recebida para 'Encoding' : [%s]", str));
		
		try {
			
			String strToEncode = str + KEYCOMPLETE;
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(strToEncode.getBytes(), 0, strToEncode.length());
			
			String strEncode = new BigInteger(1, md.digest()).toString(16);
			
			LOGGER.debug(String.format("MD5 gerado :  [%s]", strEncode));
			
			return strEncode;
			
		} catch (NoSuchAlgorithmException e) {
			
			LOGGER.debug(String.format("Houve um erro ao tentar gerar o MD5, erro :  [%s]", e.getMessage()));
			e.printStackTrace();
			return "";
			
		}
	
	}

}
