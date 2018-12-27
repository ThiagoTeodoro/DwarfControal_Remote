package br.com.datemanipulator.tests;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

import br.com.numericmanipulator.NumericManipulatorImpl;

public class NumericManipulatorTest {
	
	@Test
	public void stringToDoubleTeste() {
		
		String valor1 = "R$ 34,80";
		String valor2 = "52,00";
		String valor3 = "5.00";
		
		Double valor1Correto = 34.80;
		Double valor2Correto = 52.00;
		Double valor3Correto = 5.0;
		
		Double resultado1 = new NumericManipulatorImpl().stringToDouble(valor1);
		Double resuttado2 =  new NumericManipulatorImpl().stringToDouble(valor2);
		Double resuttado3 =  new NumericManipulatorImpl().stringToDouble(valor3);
		
		assertEquals(valor1Correto, resultado1);
		assertEquals(valor2Correto, resuttado2);
		assertEquals(valor3Correto, resuttado3);
		
		
	}
	
	@Test
	public void formaterInCurrencyExhibitionTest() {
		
		Double valor1 = 50.99;
		Double valor2 = 1989.90;
		Double valor3 = 234.00;
		
		String valor1Correto = "$50.99";
		String valor2Correto = "R$ 1.989,90";
		String valor3Correto = "€ 234,00";
		
		//Padrão Americano Moeda Dolar
		String resultado1 = new NumericManipulatorImpl().formaterInCurrencyExhibition(valor1, Locale.US);
		
		//Padrão Brasileiro Moeda Real
		String resultado2 = new NumericManipulatorImpl().formaterInCurrencyExhibition(valor2, new Locale("pt", "BR"));
		
		//Padrão Italiano Moeda Euro
		String resultado3 = new NumericManipulatorImpl().formaterInCurrencyExhibition(valor3, Locale.ITALY);
		
		assertEquals(valor1Correto, resultado1);
		assertEquals(valor2Correto, resultado2);
		assertEquals(valor3Correto, resultado3);
		
	}

}
