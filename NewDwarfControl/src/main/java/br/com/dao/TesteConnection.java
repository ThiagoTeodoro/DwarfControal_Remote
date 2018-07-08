package br.com.dao;

import br.com.connectionfactory.EntityManagerUtil;
import br.com.utilitarios.Encriptacao;

public class TesteConnection {

	public static void main(String[] args) {

		EntityManagerUtil entityManagerUtil = new EntityManagerUtil();
		
		if(entityManagerUtil.getConnection() != null) {			
			System.out.println("Conexão realizada com sucesso!");			
		} else {			
			System.out.println("### RED ALERT! A conexão com o banco de dados não foi realizada!");			
		}
		
	}

}
