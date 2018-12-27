package br.com.datemanipulator.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.encryption.EncryptionManipulatorImpl;

public class EncryptionManipulatorTest {

	@Test
	public void toBase64Test() {
		
		EncryptionManipulatorImpl encryptionManipulator = new EncryptionManipulatorImpl();
		String str = encryptionManipulator.toBase64("Thiago");
		
		assertEquals("VGhpYWdv", str);
		
	}
	
	
	@Test
	public void decodeBase64Test() {
		
		EncryptionManipulatorImpl encryptionManipulator = new EncryptionManipulatorImpl();
		String str = encryptionManipulator.decodeBase64("VGhpYWdv");
		
		assertEquals("Thiago", str);
		
	}
	
	
	@Test
	public void toMD5Teste() {
		
		
		EncryptionManipulatorImpl encryptionManipulator = new EncryptionManipulatorImpl();
		String str = encryptionManipulator.toMD5("Thiago");
		
		assertEquals("40977fc4ab38ad5ab6dd423098774de6", str);
		
		
	}
	
}
