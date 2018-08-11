package br.com.utilitarios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
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
	
	/**
	 * Método responsável por converter uma data String de 
	 * um determinado formato para outro.
	 * 
	 * @param data String da data que quer formatar.
	 * @param formatoAtual formato atual da data que será convertida.
	 * @param formatoDesejado formato desejado após conversão
	 * @return data convertida conform Pattern enviado.s
	 */
	public String converterDataString(String data, String formatoAtual, String formatoDesejado) {
		
		SimpleDateFormat sDFFormatoAtual = new SimpleDateFormat(formatoAtual);
		SimpleDateFormat sDFFormatoDesejado = new SimpleDateFormat(formatoDesejado);
		
		try {
			
			Date dataAtual = sDFFormatoAtual.parse(data);
			
			return sDFFormatoDesejado.format(dataAtual);
			
		} catch (ParseException e) {
			
			System.err.println("Erro ao converter a data, Exception : [" + e.getMessage() + "]");
			
			return null;
		}
		
	}
}
