package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.hibernate.query.Query;

import br.com.connectionfactory.EntityManagerUtil;
import br.com.entitys.QuandoPagar;
import br.com.entitys.Usuario;

public class QuandoPagarDAO extends GenericDAO<QuandoPagar> implements Serializable {
	
	/**
	 * Toda classe Serializable necessita de SerialVersionID
	 */
	private static final long serialVersionUID = 1L;
	
	public QuandoPagarDAO() {
		super();
		ordem = "Data";
		classePersistente = QuandoPagar.class;
	}
	
	
	
	//Metodos deste DAO
	
	/**
	 * Metodo responsável por selecionar todos os "QuandoPagar" de um determinado
	 * Usuário filtrando por Mês e Ano.
	 * 
	 * @param usuario Usuário do qual pertence os os lançamentos
	 * @param mesAno Mes e ano que será filtrado formato YYYY-MM
	 * @return Lista com os Quando Pagar ou null caso não seja encontrado nenhum lançamento.
	 */
	public List<QuandoPagar> selectQuandoPagarUsuarioMesAno(Usuario usuario, String mesAno){
		
		try {
			
			String[] mesAnoSplit = mesAno.split("-");
			String ano = mesAnoSplit[0];
			String mes = mesAnoSplit[1];
					
			EntityManager em = EntityManagerUtil.getConnection();
			
			String hql = " FROM " + classePersistente.getName() + " WHERE Usuario = :usuario AND MONTH(Data) = :mes AND YEAR(Data) = :ano ORDER BY Data";
			Query query = (Query) em.createQuery(hql);
			query.setParameter("usuario", usuario);
			query.setParameter("mes", Integer.parseInt(mes));
			query.setParameter("ano", Integer.parseInt(ano));
			
			List<QuandoPagar> retorno = query.getResultList();
	
			//Fechando Conexão com o banco de dados
			em.close();
			
			//Ocultando Senha presente neste retorno
			for(int i = 0; i < retorno.size(); i++) {
				retorno.get(i).getUsuario().setSenha("");				
			}
			
			return retorno;
			
		} catch (NoResultException ex) {
			
			System.out.println("Não houve resultados na Consulta!");
			return null;
			
		} catch (Exception ex) {
			
			System.out.println(ex.getMessage());
			System.out.println("Houve um erro ao tentar selecionar os Dados de 'QuandoPagar'!");
			return null;
			
		}
		
	}	
	
	
	/**
	 * Metodo que soma o total de gastos previsto de um mês enviado, do usuário que está logado.
	 * 
	 * @param usuario Usuário que está logado
	 * @param mesAno Mes e ano que será calculado no formato YYYY-MM
	 * @return valor do Mês selecionado.
	 */
	public Double getTotalPrevisto(Usuario usuario, String mesAno) {
		
		try {
			
			String[] mesAnoSplit = mesAno.split("-");
			String ano = mesAnoSplit[0];
			String mes = mesAnoSplit[1];
					
			EntityManager em = EntityManagerUtil.getConnection();
			
			String hql = "SELECT SUM(Valor) FROM " + classePersistente.getName() + " WHERE Usuario = :usuario AND MONTH(Data) = :mes AND YEAR(Data) = :ano";
			Query query = (Query) em.createQuery(hql);
			query.setParameter("usuario", usuario);
			query.setParameter("mes", Integer.parseInt(mes));
			query.setParameter("ano", Integer.parseInt(ano));
			
			Double retorno = (Double) query.getSingleResult();
	
			//Fechando Conexão com o banco de dados
			em.close();
			
			return retorno;
			
		} catch (NoResultException ex) {
			
			System.out.println("Não houve resultados na Consulta!");
			return 0.00;
			
		} catch (Exception ex) {
			
			System.out.println(ex.getMessage());
			System.out.println("Houve um erro ao tentar selecionar os Dados de 'QuandoPagar' para realizar a soma!");
			return 0.00;
			
		}
		
	}
	
}
