package br.com.utilitarios;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Encriptacao {

	/**
	 * Classe que realiza um MD5 de um texto Enviado.
	 * 
	 * Atenção: Ao realizaro o MD5 adicionamos a String uma "Chave do Sistema" para
	 * difultar o acesso de não autorizados, assim o sistema para ter uma Chave que
	 * o usuário conhece e uma chave que o sistema adiciona, o resultado do MD5 é
	 * uma junção dos 2 fatores.
	 * 
	 * @param string
	 *            String a ser Encriptada
	 * @return String ou null
	 */
	public String toMD5(String string) {

		try {

			// Adicionando Chave de Segurança do Sistema
			string = string + "Dwarf_Control_123456789";
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(string.getBytes(), 0, string.length());
			return new BigInteger(1, m.digest()).toString(16);

		} catch (Exception e) {

			System.err.println("Erro ao gerar MD5 da String enviada, erro : [ " + e.getMessage() + " ]");
			return null;

		}

	}

}
