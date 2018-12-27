package br.com.controleFinanceiro.model.DAOs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.controleFinanceiro.model.DTO.ResumoDTO;
import br.com.controleFinanceiro.model.entitys.Contas;
import br.com.controleFinanceiro.model.entitys.Lancamentos;

/**
 * Essa classe trata das operações customizadas e mais especificas em SQL
 * relacionadas aos Lançamentos precisa ser injetada com @Autowired
 * 
 * @author swb_thiago
 *
 */
@Component
@Transactional
public class CustomLancamentosDAO {

	// Injetando EntityManager
	@PersistenceContext
	private EntityManager em;

	// Pegando nome da Classe para montar os HQL's
	private static String tabelaLancamentos = Lancamentos.class.getName();
	private static String tabelaContas = Contas.class.getName();
	
	private Logger logger = LoggerFactory.getLogger(CustomLancamentosDAO.class);

	/**
	 * Método responsável por obter o somátorio total de todos os lançamentos por 
	 * usuário.
	 * @param id_Usuario
	 * @return
	 */
	public double getTotalLancamentosPorUsuario(int idUsuario) {
		
		this.logger.info("Obtendo somatório total (Resultado) do banco de dados de todos os lançamentos!");
		
		String hql = "SELECT SUM(valor) FROM " + tabelaLancamentos + " WHERE usuario_id = " + idUsuario;
		Query query = (Query) em.createQuery(hql);
		
		double somatorio = 0.00;
		
		//Verificando se não é nulo
		if(query.getSingleResult() != null) {
		
			somatorio = (double) query.getSingleResult();
		
		}
		
		this.logger.info(String.format("Somatório total (Resultado) do banco de dados de todos os lançamentos obtido com sucesso [%s]", somatorio));
		
		return somatorio;
		
	}
	
	
	/**
	 * Método pra obeter os Resumo das receitas de um determinado usuário
	 * 
	 * @param usuarioId
	 * @return
	 */
	public List<ResumoDTO> getResumoReceita(int usuarioId){
		
		this.logger.info("Obtendo Resumo de Receitas por conta e usuário ...");

		List<ResumoDTO> resumoReceitas = new ArrayList<ResumoDTO>();
		
		/**
		 * Observe que tudo tem que ser feito em relação aos atributos e nome dos atributos
		 * e nomes das classes!
		 */
		String hql = "SELECT c.descricao, SUM(l.valor) "
				   + "FROM " + tabelaLancamentos + " as l, " + tabelaContas + " as c "
				   + "WHERE l.conta.id= c.id " 
				   + "AND l.valor > 0 "
				   + "AND l.usuario.id = :usuarioId "
				   + "GROUP BY c.descricao";
		
		Query query = (Query) em.createQuery(hql);
		query.setParameter("usuarioId", usuarioId);
		
		List<Object[]> result = query.getResultList();
		
		//Como os campos foram mudados temos que fazer o DE PARA apartir de Objetc 
		for(Object[] linhaResultado : result) {

			ResumoDTO aux = new ResumoDTO();
			
			//DE PARA
			aux.setDescricaoConta((String) linhaResultado[0]);
			aux.setValor((double) linhaResultado[1]);
			
			//Adicionando ao Retorno
			resumoReceitas.add(aux);
			
			aux = null;
			
		}
		
		this.logger.info("Resumo de contas receitas por usuário obtido para um total de [" + resumoReceitas.size() + "] contas.");

		return resumoReceitas;
	}
	
	
	/**
	 * Método responsável por obter o somatório total de todas as receitas
	 * 
	 * @param usuarioId usuário logado (Token)
	 * @return
	 */
	public Double getTotalResumoReceitas(int usuarioId) {
		
		this.logger.info("Obtendo total de receitas ....");
		
		double totalResumoReceitas = 0.00;
		
		String hql = "SELECT SUM(valor) "
				   + "FROM " + tabelaLancamentos + " "
				   + "WHERE usuario_id = :usuarioId "
				   + "AND valor > 0";
		

		Query query = (Query) em.createQuery(hql);
		query.setParameter("usuarioId", usuarioId);
		
		Object resultadoQuery = query.getSingleResult();
		
		if(resultadoQuery == null) {
			
			totalResumoReceitas = 0.00;
			
		} else {
			
			totalResumoReceitas = (double) resultadoQuery;		
					
		}		
		
		this.logger.info(String.format("Total de receitas obtido com sucesso! Total de Receitas : [%s]", totalResumoReceitas));
		
		return totalResumoReceitas;
		
	}
	
	
	/**
	 * Método responsável por obter o somatório total de todas as despesas
	 * 
	 * @param usuarioId usuário logado (Token)
	 * @return
	 */
	public Double getTotalResumoDespesa(int usuarioId) {
		
		this.logger.info("Obtendo total de despesas ....");
		
		double totalResumoDespesas = 0.00;
		
		String hql = "SELECT SUM(valor) "
				   + "FROM " + tabelaLancamentos + " "
				   + "WHERE usuario_id = :usuarioId "
				   + "AND valor < 0";
		

		Query query = (Query) em.createQuery(hql);
		query.setParameter("usuarioId", usuarioId);
		
		Object resultadoQuery = query.getSingleResult();
		
		if(resultadoQuery == null) {
			
			totalResumoDespesas = 0.00;
			
		} else {
			
			totalResumoDespesas = (double) resultadoQuery;		
					
		}
		
		this.logger.info(String.format("Total de despesas obtido com sucesso! Total de Despesas : [%s]", totalResumoDespesas));
		
		return totalResumoDespesas;
		
	}
	
	
	/**
	 * Método pra obeter os Resumo das despesas de um determinado usuário
	 * 
	 * @param usuarioId
	 * @return
	 */
	public List<ResumoDTO> getResumoDespesas(int usuarioId){
		
		this.logger.info("Obtendo Resumo de Despesas por conta e usuário ...");

		List<ResumoDTO> resumoDespesas = new ArrayList<ResumoDTO>();
		
		/**
		 * Observe que tudo tem que ser feito em relação aos atributos e nome dos atributos
		 * e nomes das classes!
		 */
		String hql = "SELECT c.descricao, SUM(l.valor) "
				   + "FROM " + tabelaLancamentos + " as l, " + tabelaContas + " as c "
				   + "WHERE l.conta.id= c.id " 
				   + "AND l.valor < 0 "
				   + "AND l.usuario.id = :usuarioId "
				   + "GROUP BY c.descricao";
		
		Query query = (Query) em.createQuery(hql);
		query.setParameter("usuarioId", usuarioId);
		
		List<Object[]> result = query.getResultList();
		
		//Como os campos foram mudados temos que fazer o DE PARA apartir de Objetc 
		for(Object[] linhaResultado : result) {

			ResumoDTO aux = new ResumoDTO();
			
			//DE PARA
			aux.setDescricaoConta((String) linhaResultado[0]);
			aux.setValor((double) linhaResultado[1]);
			
			//Adicionando ao Retorno
			resumoDespesas.add(aux);
			
			aux = null;
			
		}
		
		this.logger.info("Resumo de contas despesas por usuário obtido para um total de [" + resumoDespesas.size() + "] contas.");

		return resumoDespesas;
	}
	
	
	/**
	 * Método pra obeter os Resumo do Resultado de um determinado usuário
	 * 
	 * @param usuarioId
	 * @return
	 */
	public List<ResumoDTO> getResumoResultado(int usuarioId){
		
		this.logger.info("Obtendo Resumo de Resultado por conta e usuário ...");

		List<ResumoDTO> resumoResultado = new ArrayList<ResumoDTO>();
		
		/**
		 * Observe que tudo tem que ser feito em relação aos atributos e nome dos atributos
		 * e nomes das classes!
		 */
		String hql = "SELECT c.descricao, SUM(l.valor) "
				   + "FROM " + tabelaLancamentos + " as l, " + tabelaContas + " as c "
				   + "WHERE l.conta.id= c.id " 
				   + "AND l.usuario.id = :usuarioId "
				   + "GROUP BY c.descricao";
		
		Query query = (Query) em.createQuery(hql);
		query.setParameter("usuarioId", usuarioId);
		
		List<Object[]> result = query.getResultList();
		
		//Como os campos foram mudados temos que fazer o DE PARA apartir de Objetc 
		for(Object[] linhaResultado : result) {

			ResumoDTO aux = new ResumoDTO();
			
			//DE PARA
			aux.setDescricaoConta((String) linhaResultado[0]);
			aux.setValor((double) linhaResultado[1]);
			
			//Adicionando ao Retorno
			resumoResultado.add(aux);
			
			aux = null;
			
		}
		
		this.logger.info("Resumo de contas em caracter de Resultado por usuário obtido para um total de [" + resumoResultado.size() + "] contas.");

		return resumoResultado;
	}
	
	
}
