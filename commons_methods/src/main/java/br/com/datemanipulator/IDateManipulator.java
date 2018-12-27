package br.com.datemanipulator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface IDateManipulator {

	/**
	 * Método responsável por entregar a data de hoje.
	 * 
	 * @return
	 */
	Date todayDate();

	/**
	 * Método responsável por entregar a data de hoje.
	 * 
	 * @return
	 */
	Calendar todayCalendar();

	/**
	 * Método responsável por converter um tipo Date em uma String conforme pattern
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
	 * Método responsável por converter um tipo Date em uma String conforme pattern
	 * enviado, esse método permite a passagem de uma string pattern customizavel
	 * como parâmetro.
	 * 
	 * @param date
	 *            Data para ser convertida em String
	 * @param pattern
	 *            Pattern da data que desejar. Ex: yyyy-MM-dd
	 * @return
	 */
	String dateToString(Date date, String pattern);

	/**
	 * Método responsável por transformar uma data tipo String em uma data tipo
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
	 * Método responsável por transformar uma data tipo String em uma data tipo
	 * Date, esse método permite a passgem de um String customizavem como pattern.
	 * 
	 * @param data
	 *            Data tipo String
	 * @param pattern
	 *            Pattern que data tipo String se encontra
	 * @return
	 */
	Date stringToDate(String data, String pattern);

	/**
	 * Método responsável por converter um date em um Calendar.
	 * 
	 * @return
	 */
	Calendar dateToCalendar(Date date);

	/**
	 * Método responsável por adicionar dias úteis conforme parametro a uma data,
	 * como dia util a função entendi dias que não são feriados e não sabados e nem
	 * domingos.
	 * 
	 * Caso seja passado 0 como parametro de numero de dias para adicionar a função
	 * irá verificar se a data enviada é dia util e se não for vai adicionar dias
	 * até o proximo dia util aparecer, se já for dia util ela não fará nenhuma
	 * adição.
	 * 
	 * A lista de de feriados é importante para definição dos casos especias de dias
	 * uteis.
	 * 
	 * @param data
	 *            data a ser checada/adicionada
	 * @param numberOfDayAdd
	 *            numero de dias UTEIS que será adicionado o parâmetro 0 apenas
	 *            verifica a data enviada se a mesma é útil ou não.
	 * @param holidays
	 *            lista de feriados para levar em consideração
	 * @return
	 */
	Date dayUtilAdd(Date data, int numberOfDayAdd, List<Date> holidays);

	// Supressão do Metodo Anterior para casos em que ele não passar a lista de
	// feriados
	Date dayUtilAdd(Date data, int numberOfDayAdd);

	/**
	 * Metodo responsável por verificar se uma data está vencida ou não.
	 * 
	 * O metodo retorna true caso a conta esteja vencida
	 *
	 * @param dataReferencia
	 *            a data que vamos usar para comparar se estar vencido ou não,
	 *            geralmente passamos a data atual (now).
	 * @param dataVencimento
	 *            data que irar vencer.
	 * @return
	 */
	boolean verificaVencimento(Date dataReferencia, Date dataVencimento);

}
