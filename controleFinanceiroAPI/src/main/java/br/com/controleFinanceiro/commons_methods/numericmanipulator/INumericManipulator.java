package br.com.controleFinanceiro.commons_methods.numericmanipulator;

import java.util.Locale;

/**
 * 
 * Interface para regularizar a implementa��o de NumericManipulatorImpl.
 *
 */
public interface INumericManipulator {

	/**
	 * M�todo respons�vel por converter uma String em um Double.
	 * 
	 * @param numeric
	 *            numero para ser transformado em Double
	 * @return
	 */
	double stringToDouble(String numeric);

	/**
	 * M�todo respons�vel por gerar uma exibi��o do tipo moeda do numero enviado.
	 * 
	 * @param numeric
	 *            numero a ser formatado para exibi��o do tipo currency tipo 90.53
	 * @param locale
	 *            locale para qual ser� a formata��o
	 * @return Ex: R$ 90,53
	 */
	String formaterInCurrencyExhibition(Double numeric, Locale locale);

}
