package br.com.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import br.com.connectionfactory.EntityManagerUtil;
import br.com.entitys.Lancamento;
import br.com.models.TotalPorMesDTO;
import br.com.utilitarios.FuncoesData;

@Component
@Transactional
public class LancamentoDAO extends GenericDAO<Lancamento> implements Serializable{

	
	/**
	 * Obrigatório
	 */
	private static final long serialVersionUID = 1L;

	//Definindo Comportamento da Classe
	public LancamentoDAO() {		
		ordem = "Id";
		classePersistente = Lancamento.class;
	}
	
	
	/**
	 * Método responsável por selecionar os lançamentos de uma conta por 
	 * data inicial e final de uma determinada contal.
	 * 
	 * @param idConta id da conta
	 * @param dataInicial data incial para selecionar os lançamentos do periodo. Pattern "yyyy-MM-dd";
	 * @param dataFinal data final para selecionar os lançamentos do periodo. Pattern "yyyy-MM-dd";
	 * @return Lista com os lançamentos do período
	 */
	public List<Lancamento> selecionarPorContaDataInicialEFinal(long idConta, String dataInicial, String dataFinal){
		
		EntityManager em = EntityManagerUtil.getConnection();
		FuncoesData funcoesData = new FuncoesData();
		
		String hql = "FROM " + classePersistente.getName() + " WHERE data >= :dataInicial and data <= :dataFinal and conta_Id = :contaId";
		Query query = (Query) em.createQuery(hql);
		query.setParameter("dataInicial", funcoesData.dateToCalendar(funcoesData.stringToDate("yyyy-MM-dd", dataInicial)));
		query.setParameter("dataFinal", funcoesData.dateToCalendar(funcoesData.stringToDate("yyyy-MM-dd", dataFinal)));
		query.setParameter("contaId", idConta);
		
		List<Lancamento> retorno = query.getResultList();
		
		//Fechando Conexão com o Banco
		em.close();
		
		return retorno;
		
	}
	
	/**
	 * Método responsável por ober o saldo anterior de um periodo
	 * 
	 * @param idConta Id da conta que será calculado o saldo anterior
	 * @param dataInicial Inicio do periodo para calculo. Pattern yyyy-MM-dd
	 * @return valor do Saldo anterior a dataIncial.
	 */
	public Float sumSaldoAnterior(long idConta, String dataInicial) {
		
		EntityManager em = EntityManagerUtil.getConnection();
		FuncoesData funcoesData = new FuncoesData();
		
		String hql = "SELECT SUM(Valor) FROM " + classePersistente.getName() + " WHERE data < :dataInicial and conta_Id = :contaId";
		Query query = (Query) em.createQuery(hql);
		query.setParameter("dataInicial", funcoesData.dateToCalendar(funcoesData.stringToDate("yyyy-MM-dd", dataInicial)));
		query.setParameter("contaId", idConta);
		
		Object retorno =  query.getSingleResult();
		
		//Fechando Conexão com o Banco
		em.close();					
		
		Float retornoFloat;
		
		/**
		 * Como tenho que verificar se o rentorno não é nulo
		 * eu tenho que converter o OBJECT depois da verificação
		 * por isso esse if com essa trativa.
		 */
		if(retorno != null) {
			retornoFloat = Float.parseFloat(String.valueOf(((Double) retorno)));
		} else {
			retornoFloat = new Float(0);
		}

		return retornoFloat;
		
	}
	
	
	/**
	 * Método responsável por somar os lançamentos de uma conta desde de ínicio da tabela,
	 * até a data final enviada no Banco de Dados. Fornecendo assim o saldo total deste periodo, tomando 
	 * sempre como base de ínicio da soma o lançamento mais antigo da tabela ou seja tudo
	 * que está na tabela até a dataFinal.
	 * 
	 * @param idConta id da conta que será somada
	 * @param dataFinal data final do periodo que será somado para fornecer o saldo. Pattern = yyyy-MM-dd
	 * @return Soma de todos os saldos até a data final enviada INCLUSIVE.
	 */
	public float somarSaldo(int idConta, String dataFinal) {
		float saldo = 0;
		
		EntityManager em = EntityManagerUtil.getConnection();
		FuncoesData funcoesData = new FuncoesData();
		
		String hql = "SELECT SUM(Valor) FROM " + classePersistente.getName() + " WHERE conta_Id = :contaId AND  data <= :dataFinal";
		Query query = (Query) em.createQuery(hql);
		query.setParameter("dataFinal", funcoesData.dateToCalendar(funcoesData.stringToDate("yyyy-MM-dd", dataFinal)));
		query.setParameter("contaId", idConta);
		
		Object retorno =  query.getSingleResult();
	
		//Fechando Conexão com o Banco
		em.close();					
	
		//Trativa para não retornar null
		if(retorno != null) {
			return Float.parseFloat(String.valueOf(((Double) retorno)));
		}
		
		return saldo;
		
	}
	
	/***
	 * Método responsável por obter todas as despesas de um determinado
	 * ano e conta.
	 * 
	 * O resultado dessa consulta retorna o somatário das despesas dos meses 
	 * que contem lançamentos, aqueles que não contem não apareceram no resultado
	 * da consulta.
	 * 
	 * @param idConta Id da conta a ser calculada a soma das despesas de cada mês.
	 * @param ano Ano no qual será calculado a soma das despesas de cada mês. Pattern "yyyy"
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TotalPorMesDTO> obterDespesasAnoPorMes(int idConta, int ano){
		
		List<TotalPorMesDTO> desespesasDoAnoPorMes = new ArrayList<TotalPorMesDTO>();
		
		EntityManager em = EntityManagerUtil.getConnection();
		
		String hql = "SELECT MONTH(data) as mes, SUM(Valor) FROM " + classePersistente.getName() 
				   + " WHERE Valor < 0 AND YEAR(data) = :ano AND conta_Id = :idConta GROUP BY MONTH(data)";
		Query query = (Query) em.createQuery(hql);
		query.setParameter("ano", ano);
		query.setParameter("idConta", idConta);
		
		List<Object[]> result = query.getResultList();
		
		//Como os campos foram mudados temos que fazer o DE PARA apartir de Objetc 
		for(Object[] linhaResultado : result) {
			
			TotalPorMesDTO aux = new TotalPorMesDTO();
			
			//DE PARA
			aux.setMes((int) linhaResultado[0]);
			
			//As despesas vem negativadas temos que torna-la positiva
			aux.setTotalMes(((double) linhaResultado[1]) * -1);
			
			//Adicionando ao List
			desespesasDoAnoPorMes.add(aux);
			
			aux = null;
			
		}
		
		return desespesasDoAnoPorMes;
		
	}
	
	
	/***
	 * Método responsável por obter todas as receitas de um determinado
	 * ano e conta.
	 * 
	 * O resultado dessa consulta retorna o somatário das receitas dos meses 
	 * que contem lançamentos, aqueles que não contem não apareceram no resultado
	 * da consulta.
	 * 
	 * @param idConta Id da conta a ser calculada a soma das receitas de cada mês.
	 * @param ano Ano no qual será calculado a soma das recetas de cada mês. Pattern "yyyy"
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TotalPorMesDTO> obterReceitasAnoPorMes(int idConta, int ano){
		
		List<TotalPorMesDTO> receitasDoAnoPorMes = new ArrayList<TotalPorMesDTO>();
		
		EntityManager em = EntityManagerUtil.getConnection();
		
		String hql = "SELECT MONTH(data) as mes, SUM(Valor) FROM " + classePersistente.getName() 
				   + " WHERE Valor > 0 AND YEAR(data) = :ano AND conta_Id = :idConta GROUP BY MONTH(data)";
		Query query = (Query) em.createQuery(hql);
		query.setParameter("ano", ano);
		query.setParameter("idConta", idConta);
		
		List<Object[]> result = query.getResultList();
		
		//Como os campos foram mudados temos que fazer o DE PARA apartir de Objetc 
		for(Object[] linhaResultado : result) {
			
			TotalPorMesDTO aux = new TotalPorMesDTO();
			
			//DE PARA
			aux.setMes((int) linhaResultado[0]);
			aux.setTotalMes((double) linhaResultado[1]);
			
			//Adicionando ao List
			receitasDoAnoPorMes.add(aux);
			
			System.out.println(aux.toString());
			
			aux = null;
			
		}
		
		return receitasDoAnoPorMes;
		
	}
	
}