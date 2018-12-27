package br.com.controleFinanceiro.commons_methods.datemanipulator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface IDateManipulator {

	/**
	 * M�todo respons�vel por entregar a data de hoje.
	 * 
	 * @return
	 */
	Date todayDate();

	/**
	 * M�todo respons�vel por entregar a data de hoje.
	 * 
	 * @return
	 */
	Calendar todayCalendar();

	/**
	 * M�todo respons�vel por converter um tipo Date em uma String conforme pattern
	 * enviado.
	 * 
	 * @param date
	 *            Data para ser convertida em String
	 * @param pattern
	 *            Pattern da data que desejar. Ex: yyyy-MM-dd
	 * @return
	 */
	String dateToString(Date date, DatePatterns pattern);

	/**
	 * M�todo respons�vel por converter um tipo Date em uma String conforme pattern
	 * enviado, esse m�todo permite a passagem de uma string pattern customizavel
	 * como par�metro.
	 * 
	 * @param date
	 *            Data para ser convertida em String
	 * @param pattern
	 *            Pattern da data que desejar. Ex: yyyy-MM-dd
	 * @return
	 */
	String dateToString(Date date, String pattern);

	/**
	 * M�todo respons�vel por transformar uma data tipo String em uma data tipo
	 * Date.
	 * 
	 * @param data
	 *            Data tipo String
	 * @param pattern
	 *            Pattern que data tipo String se encontra
	 * @return
	 */
	Date stringToDate(String data, DatePatterns pattern);

	/**
	 * M�todo respons�vel por transformar uma data tipo String em uma data tipo
	 * Date, esse m�todo permite a passgem de um String customizavem como pattern.
	 * 
	 * @param data
	 *            Data tipo String
	 * @param pattern
	 *            Pattern que data tipo String se encontra
	 * @return
	 */
	Date stringToDate(String data, String pattern);

	/**
	 * M�todo respons�vel por converter um date em um Calendar.
	 * 
	 * @return
	 */
	Calendar dateToCalendar(Date date);

	/**
	 * M�todo respons�vel por adicionar dias �teis conforme parametro a uma data,
	 * como dia util a fun��o entendi dias que n�o s�o feriados e n�o sabados e nem
	 * domingos.
	 * 
	 * Caso seja passado 0 como parametro de numero de dias para adicionar a fun��o
	 * ir� verificar se a data enviada � dia util e se n�o for vai adicionar dias
	 * at� o proximo dia util aparecer, se j� for dia util ela n�o far� nenhuma
	 * adi��o.
	 * 
	 * A lista de de feriados � importante para defini��o dos casos especias de dias
	 * uteis.
	 * 
	 * @param data
	 *            data a ser checada/adicionada
	 * @param numberOfDayAdd
	 *            numero de dias UTEIS que ser� adicionado o par�metro 0 apenas
	 *            verifica a data enviada se a mesma � �til ou n�o.
	 * @param holidays
	 *            lista de feriados para levar em considera��o
	 * @return
	 */
	Date dayUtilAdd(Date data, int numberOfDayAdd, List<Date> holidays);

	// Supress�o do Metodo Anterior para casos em que ele n�o passar a lista de
	// feriados
	Date dayUtilAdd(Date data, int numberOfDayAdd);

	/**
	 * Metodo respons�vel por verificar se uma data est� vencida ou n�o.
	 * 
	 * O metodo retorna true caso a conta esteja vencida
	 *
	 * @param dataReferencia
	 *            a data que vamos usar para comparar se estar vencido ou n�o,
	 *            geralmente passamos a data atual (now).
	 * @param dataVencimento
	 *            data que irar vencer.
	 * @return
	 */
	boolean verificaVencimento(Date dataReferencia, Date dataVencimento);

}
