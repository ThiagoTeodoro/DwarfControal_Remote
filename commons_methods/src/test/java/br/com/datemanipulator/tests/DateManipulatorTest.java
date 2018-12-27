package br.com.datemanipulator.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.com.datemanipulator.DateManipulatorImpl;
import br.com.datemanipulator.DatePatterns;

public class DateManipulatorTest {
	
	@Test
	public void dateToStringTeste() {
		
		//Essa data está setada como 01/10/2018
		@SuppressWarnings("deprecation")
		Date dataReferencia = new Date(118, 9, 1);
		
		//Convertendo data para o formato dd/MM/yyyy
		String dataGerada = new DateManipulatorImpl().dateToString(dataReferencia, DatePatterns.BR);
		
		//Printando no Console
		System.out.println(String.format("Data desejada : [01/10/2018], Data gerada pela Classe [%s]", dataGerada));
		
		//Checando resultado
		assertEquals("01/10/2018", dataGerada);
		
	}
	
	@Test
	public void dateToStringTestePattern() {
		
		//Essa data está setada como 01/10/2018
		@SuppressWarnings("deprecation")
		Date dataReferencia = new Date(118, 9, 1);
		
		//Convertendo data para o formato dd/MM/yyyy
		String dataGerada = new DateManipulatorImpl().dateToString(dataReferencia, "dd/MM/yyyy");
		
		//Printando no Console
		System.out.println(String.format("Data desejada : [01/10/2018], Data gerada pela Classe [%s]", dataGerada));
		
		//Checando resultado
		assertEquals("01/10/2018", dataGerada);
		
	}
	
	@Test
	public void stringToDatePattern() {
		
		//Essa data está setada como 01/10/2018
		@SuppressWarnings("deprecation")
		Date dataReferencia = new Date(118, 9, 1);

		//Criando um Date apartir de uma String para realizar comparação
		Date dataGerada = new DateManipulatorImpl().stringToDate("01/10/2018", "dd/MM/yyyy");
		
		//Printando no Cosole
		System.out.println(String.format("Data desejada : [%s], Data gerada pela Classe [%s]", dataReferencia, dataGerada));
		
		//Checando Resultado
		assertEquals(dataReferencia, dataGerada);
		
	}
	
	@Test
	public void stringToDate() {
		
		//Essa data está setada como 01/10/2018]
		@SuppressWarnings("deprecation")
		Date dataReferencia = new Date(118, 9, 1);

		//Criando um Date apartir de uma String para realizar comparação
		Date dataGerada = new DateManipulatorImpl().stringToDate("01/10/2018", DatePatterns.BR);
		
		//Printando no Cosole
		System.out.println(String.format("Data desejada : [%s], Data gerada pela Classe [%s]", dataReferencia, dataGerada));
		
		//Checando Resultado
		assertEquals(dataReferencia, dataGerada);
		
	}
	
	
	@Test
	public void enumPattern() {
		
		//Essas datas estão setadas como 01/10/2018		
		String dataReferencia = "01/10/2018";
		@SuppressWarnings("deprecation")
		Date dataReferenciaDate = new Date(118, 9, 1);
		
		//Criando uma String de Data apartir de um Pattern para teste
		String dataGerada = new DateManipulatorImpl().dateToString(dataReferenciaDate, DatePatterns.BR);
		
		
		//Printando no Cosole
		System.out.println(String.format("Data desejada : [%s], Data gerada pela Classe [%s]", dataReferencia, dataGerada));
		
		//Checando Resultado
		assertEquals(dataReferencia, dataGerada);
		
	}
	
	@Test
	public void todayCalendar() {
		
		//Printando no Cosole
		System.out.println(new DateManipulatorImpl().todayCalendar().getTime());
				
		
	}

	
	@Test
	public void dayUtilTest() {
		
		//Criando lista de feriados
		List<Date> feriados = new ArrayList<Date>();
		
		feriados.add(new DateManipulatorImpl().stringToDate("2018-10-12", DatePatterns.SQL));
		feriados.add(new DateManipulatorImpl().stringToDate("16/10/2018", DatePatterns.BR));
		feriados.add(new DateManipulatorImpl().stringToDate("30/10/2018", DatePatterns.BR));
		
		//Criando uma data 1 dia após o feriado que seria o certo para dia util.
		Date dataDesejada = new DateManipulatorImpl().stringToDate("15/10/2018", DatePatterns.BR);
		
		//Dia utilizado para manipulação um dia antes de um feriado
		Date dataManipualr = new DateManipulatorImpl().stringToDate("12/10/2018", DatePatterns.BR);		
		
		Date diaUtilTest = new DateManipulatorImpl().dayUtilAdd(dataManipualr, 1, feriados);
		
		assertEquals(dataDesejada, diaUtilTest);
		
	}
	
	@Test
	public void dayUtilTestNoHolidays() {
		
	
		//Criando uma data 1 dia após o feriado que seria o certo para dia util.
		Date dataDesejada = new DateManipulatorImpl().stringToDate("15/10/2018", DatePatterns.BR);
		
		/**
		 * Dia utilizado para manipulação em uma sexta feria estou adicionando 2 dias o que 
		 * vai dar em um domingo logo ele tem que me retornar segunda dia 15.
		 */
		Date dataManipualr = new DateManipulatorImpl().stringToDate("12/10/2018", DatePatterns.BR);		
		
		Date diaUtilTest = new DateManipulatorImpl().dayUtilAdd(dataManipualr, 2);
	
		
		assertEquals(dataDesejada, diaUtilTest);
		
	}
	
	
	@Test
	public void verificarVencimentoTeste() {
		
		Date dataVencimento = new DateManipulatorImpl().stringToDate("02/10/2018", DatePatterns.BR);
		
		boolean venceu = new DateManipulatorImpl().verificaVencimento(new DateManipulatorImpl().todayDate(), dataVencimento);
		
		//Segundo a logica, como hoje é dia 03/10/2018 tem que retornar que venceu
		assertEquals(true, venceu);
		
	}
	
}
