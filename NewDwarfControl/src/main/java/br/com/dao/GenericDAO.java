package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.connectionfactory.EntityManagerUtil;



@SuppressWarnings(value = {"unchecked"})
public abstract class GenericDAO<T> implements Serializable {

	/**
	 * Toda vez aque você declarar uma Serializable Class você tem que 
	 * gerar o Id dela atravez dessa expressão se não você corre um risco imenso
	 * de fazer uma bagunça no seu banco de dados. 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/*
	 * Parametros da Classe
	 */
	private List<T> listaTodos;
	protected Class classePersistente;
	protected String mensagem = "";
	protected String ordem = "id"; // Ordenação das Consultas

	// /////////////////////////////////////////////
	// / Operações de Banco de Dados ///
	// /////////////////////////////////////////////

	/*
	 * Metodo que obtem todos os dados de uma Entidade(Tabela)
	 * 
	 * Checar a quantidade de registros retornado permite saber
	 * se a lista(Tabela) está vazia ou não.
	 * 
	 */
	public List<T> getListaTodos() {
		EntityManager em = EntityManagerUtil.getConnection();
		String HQL = "from " + classePersistente.getSimpleName()
				+ " order by " + ordem;
		List<T> retonro = em.createQuery(HQL).getResultList();
		em.close();
		return retonro;
	}

	/*
	 * Metodo que da roolBack no Banco de Dados
	 */
	public void roolBack() {				
		EntityManager em = EntityManagerUtil.getConnection();
		if (em.getTransaction().isActive() == false) {
			em.getTransaction().begin();
		}
		em.getTransaction().rollback();
		em.close();
	}

	/**
	 * Metodo responsável por Persistir Entidades
	 * 
	 * Seria Semelhante ao Insert
	 * 
	 * @param obj
	 *            Entidade que será "Persistida" no Banco de Dados
	 * @return True or False
	 */
	public boolean persist(T obj) {
		try {
			EntityManager em = EntityManagerUtil.getConnection();
			em.getTransaction().begin();
			em.persist(obj);
			em.getTransaction().commit();
			mensagem = "Objeto persistido com sucesso!";
			em.close();
			return Boolean.TRUE;
		} catch (Exception e) {
			this.roolBack();
			mensagem = "Erro ao persistir Objeto : " + e;
			return Boolean.FALSE;
		}
	}

	/**
	 * Metodo responsável por realizar o Merge de Entidades
	 * 
	 * Seria o equivalante ao Update (Necessita do Preenchimento do Id)
	 * 
	 * @param obj Entidade que será feito o Merge no Banco de Dados
	 * @return True or False
	 */
	public boolean marge(T obj) {
		try {
			EntityManager em = EntityManagerUtil.getConnection();
			em.getTransaction().begin();
			em.merge(obj);
			em.getTransaction().commit();
			mensagem = "Merge do Objeto realizado com sucesso!";
			em.close();
			return Boolean.TRUE;
		} catch (Exception e) {
			this.roolBack();
			mensagem = "Erro ao realizar o Marge do Objeto : " + e;
			return Boolean.FALSE;
		}
	}

	/**
	 * Metodo responsável por realizar o Remove de Entidades
	 * 
	 * Seria Semelhante ao Delete
	 * 
	 * @param obj que será Removido do Banco de Dados
	 * @return True or False
	 */
	public boolean remove(T obj) {
		try {
			EntityManager em = EntityManagerUtil.getConnection();
			em.getTransaction().begin();
			em.remove(obj);
			em.getTransaction().commit();
			mensagem = "Objeto removido com sucesso!";
			em.close();
			return Boolean.TRUE;
		} catch (Exception e) {
			this.roolBack();
			mensagem = "Erro ao realizar a remoção do Objeto : " + e;
			return Boolean.FALSE;
		}
	}

	/**
	 * Metodo que localiza um Dado (Objeto) pelo Id no Banco de Dados.
	 * 
	 * Lembrando que se seu Id for Long então a seleção também tem que trabalhar
	 * com Long.
	 * 
	 * @param id a ser localizado
	 * @return Dado Localizado ou null
	 */
	public T localizarPorId(Long id) {
		EntityManager em = EntityManagerUtil.getConnection();
		roolBack();
		T obj = (T) em.find(classePersistente, id);
		em.close();
		return obj;
	}

	// /////////////////////////////////////////////
	// / Getters and Setters ///
	// /////////////////////////////////////////////

	public void setListaTodos(List<T> listaTodos) {
		this.listaTodos = listaTodos;
	}

	public Class getClassePersistente() {
		return classePersistente;
	}

	public void setClassePersistente(Class classePersistente) {
		this.classePersistente = classePersistente;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getOrdem() {
		return ordem;
	}

	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}

}