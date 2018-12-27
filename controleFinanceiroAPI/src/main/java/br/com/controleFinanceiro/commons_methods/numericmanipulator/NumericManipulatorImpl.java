package br.com.controleFinanceiro.commons_methods.numericmanipulator;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.log4j.Logger;

public class NumericManipulatorImpl implements INumericManipulator {

	private static Logger logger = Logger.getLogger(NumericManipulatorImpl.class);

	/**
	 * M�todo respons�vel por converter uma String em um Double.
	 * 
	 * @param numeric
	 * @return
	 */
	public double stringToDouble(String numeric) {

		try {

			logger.debug(String.format("Par�metro de entrada da fun��o stringToDouble : [%s]", numeric));

			String strN = numeric.replace("R", "").replace("$", "").trim();

			String n = "";

			int start = strN.length() - 3;
			int end = start + 1;

			if (strN.substring(start, end).equals(".")) {
				n = strN.replace(",", "");
			} else {
				n = strN.replace(".", "").replace(",", ".");
			}

			logger.debug(String.format("Retorno da fun��o stringToDouble : [%s].", new Double(n)));

			return new Double(n);

		} catch (Exception e) {

			logger.error(String.format("Houve um erro ao tentar realizar convers�o de String para Double! Mensagem [%s]", e.getMessage()));
			e.printStackTrace();
			return 0.0;

		}
	}

	/**
	 * M�todo respons�vel por gerar uma exibi��o do tipo moeda do numero enviado.
	 * 
	 * @param numeric
	 *            numero a ser formatado para exibi��o do tipo currency tipo 90.53
	 * @param locale
	 *            locale para qual ser� a formata��o
	 * @return Ex: R$ 90,53
	 */
	public String formaterInCurrencyExhibition(Double numeric, Locale locale) {

		try {

			logger.debug(String.format("Par�metros de entrada da fun��o formaterInCurrencyExhibition : numeric [%s], locale [%s]", numeric,	locale));

			Double numeroFormatar = numeric;

			NumberFormat formatador = NumberFormat.getCurrencyInstance(locale);

			logger.debug(String.format("Retorno da fun��o formaterInCurrencyExhibition : [%s]",	formatador.format(numeroFormatar)));

			return formatador.format(numeroFormatar);

		} catch (Exception e) {

			logger.error(String.format("Houve um erro ao tentar realizar convers�o para exibi��o no formato moeda! Mensagem [%s]", e.getMessage()));
			e.printStackTrace();
			return "";

		}
	}

}
