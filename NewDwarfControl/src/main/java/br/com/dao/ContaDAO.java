package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.connectionfactory.EntityManagerUtil;
import br.com.entitys.Conta;
import br.com.entitys.Usuario;

public class ContaDAO extends GenericDAO<Conta> implements Serializable{

	/**
	 * Obrigatória para evitar problemas de referência de memôria na implementação Serializable.
	 */
	private static final long serialVersionUID = 1L;
	
	//Definindo Comportamento GenericDAO
	public ContaDAO() {
		
		super();
		ordem = "Id";
		classePersistente = Conta.class;
		
	}
	
	
	//Metodos especificos deste DAO
	
	
	/**
	 * Metodo responsável por obter todas as contas de um usuário
	 * do banco de dados. 
	 * 
	 * @param usuario
	 * @return Lista com as contas do usuário ou null caso não haja ou aconteça um erro.
	 */
	public List<Conta> obterListaContasUsuario(Usuario usuario){
		
		try {
			
			EntityManager em = EntityManagerUtil.getConnection();
			
			String hql = " FROM " + classePersistente.getName() + " WHERE Usuario = :usuario";
			Query query = (Query) em.createQuery(hql);
			query.setParameter("usuario", usuario);
			
			List<Conta> retorno = query.getResultList();
			
			//Fechando Conexão com o banco de dados.
			em.close();
			
			//Apagando as Senhas em MD5.
			for(int i =0; i < retorno.size(); i++) {
				retorno.get(i).getUsuario().setSenha("");				
			}
						
			return retorno;
			
		} catch (NoResultException e) {
			
			System.out.println("Não houve resultados para está consulta! Exception : [ " + e.getMessage() + " ]");
			return null;				
		
		} catch (Exception e) {
			
			System.out.println("Ocorreu um erro ao tentar realizar está consulta, Exception : [ " + e.getMessage() + " ]");
			return null;
			
		}
		
	}

}
