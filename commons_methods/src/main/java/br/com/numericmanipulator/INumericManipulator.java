package br.com.numericmanipulator;

import java.util.Locale;

/**
 * 
 * Interface para regularizar a implementação de NumericManipulatorImpl.
 *
 */
public interface INumericManipulator {

	/**
	 * Método responsável por converter uma String em um Double.
	 * 
	 * @param numeric
	 *            numero para ser transformado em Double
	 * @return
	 */
	double stringToDouble(String numeric);

	/**
	 * Método responsável por gerar uma exibição do tipo moeda do numero enviado.
	 * 
	 * @param numeric
	 *            numero a ser formatado para exibição do tipo currency tipo 90.53
	 * @param locale
	 *            locale para qual será a formatação
	 * @return Ex: R$ 90,53
	 */
	String formaterInCurrencyExhibition(Double numeric, Locale locale);

}
