package br.com.numericmanipulator;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

public class NumericManipulatorImpl implements INumericManipulator {

	private static Logger logger = Logger.getLogger(NumericManipulatorImpl.class);

	/**
	 * Método responsável por converter uma String em um Double.
	 * 
	 * @param numeric
	 * @return
	 */
	public double stringToDouble(String numeric) {

		try {

			logger.debug(String.format("Parâmetro de entrada da função stringToDouble : [%s]", numeric));

			String strN = numeric.replace("R", "").replace("$", "").trim();

			String n = "";

			int start = strN.length() - 3;
			int end = start + 1;

			if (strN.substring(start, end).equals(".")) {
				n = strN.replace(",", "");
			} else {
				n = strN.replace(".", "").replace(",", ".");
			}

			logger.debug(String.format("Retorno da função stringToDouble : [%s].", new Double(n)));

			return new Double(n);

		} catch (Exception e) {

			logger.error(String.format("Houve um erro ao tentar realizar conversão de String para Double! Mensagem [%s]", e.getMessage()));
			e.printStackTrace();
			return 0.0;

		}
	}

	/**
	 * Método responsável por gerar uma exibição do tipo moeda do numero enviado.
	 * 
	 * @param numeric
	 *            numero a ser formatado para exibição do tipo currency tipo 90.53
	 * @param locale
	 *            locale para qual será a formatação
	 * @return Ex: R$ 90,53
	 */
	public String formaterInCurrencyExhibition(Double numeric, Locale locale) {

		try {

			logger.debug(String.format("Parâmetros de entrada da função formaterInCurrencyExhibition : numeric [%s], locale [%s]", numeric,	locale));

			Double numeroFormatar = numeric;

			NumberFormat formatador = NumberFormat.getCurrencyInstance(locale);

			logger.debug(String.format("Retorno da função formaterInCurrencyExhibition : [%s]",	formatador.format(numeroFormatar)));

			return formatador.format(numeroFormatar);

		} catch (Exception e) {

			logger.error(String.format("Houve um erro ao tentar realizar conversão para exibição no formato moeda! Mensagem [%s]", e.getMessage()));
			e.printStackTrace();
			return "";

		}
	}

}
