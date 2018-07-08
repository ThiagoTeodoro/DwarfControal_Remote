package br.com.utilitarios;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FuncoesData {

	/**
	 * Função que recebe uma data em formato String, o formato(regex)
	 * que ela se encontra no formato String e transorma isso em objeto
	 * Date para ser trabalhado.
	 * 
	 * @param formato formato(regex) que a data em String se encontra. 
	 * Ex : "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".
	 * @param data em String
	 * @return Date ou null
	 */
	public Date stringToDate(String formato, String data) {
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat(formato);			
			return sdf.parse(data);
			
		} catch(Exception ex) {
			
			System.err.println("Erro ao converter data em String, para data em Date(object). Execption : [" + ex.getMessage() + "]");
			return null;
			
		}
		
	}
	
	
	/**
	 * Metodo responsável por retornar a data e hora atual em um formato
	 * Calendar;
	 * 
	 * @return Calendar
	 */
	public Calendar dataHoraAtualCalendar() {
		
		return Calendar.getInstance();
		
	}
	
	
	/**
	 * Metodo responsável por transformar um Date em Calendar.
	 * 
	 * @param data no Formato Date(object)
	 * @return Data convertida em Calendar
	 */
	public Calendar dateToCalendar(Date data) {
		
		Calendar retorno = this.dataHoraAtualCalendar();
		retorno.setTime(data);
		
		return  retorno;
		
	}
}
