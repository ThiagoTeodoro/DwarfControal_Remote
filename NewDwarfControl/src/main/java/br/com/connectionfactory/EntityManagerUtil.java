package br.com.connectionfactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/*
 * Classe responsável por através do arquivo de configuração META-INF/persistence.xml
 * realizar a conexão usando as configurações da Persistence Dwarf_Control.
 * 
 * (Semelhante a ConnectionFactory que você já conhece)
 */
public class EntityManagerUtil
{

	//Obtendo ManagerFactory
	public static EntityManagerFactory Factory = Persistence.createEntityManagerFactory("Dwarf_Control");
	
	
	/**
	 * Metódo responsável por realizar a conexão através do EntityManager e EntityManagerFactory e retornalá
	 * em um EntityManager.
	 * 
	 * @return EntityManager or null
	 */
	public static EntityManager getConnection() {		      	
		return Factory.createEntityManager();
	}
	
}