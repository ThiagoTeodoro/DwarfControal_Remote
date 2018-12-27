package br.com.controleFinanceiro.model.DAOs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import br.com.controleFinanceiro.model.entitys.Contas;

/**
 * Essa classe trata das operações customizadas e mais especificas em 
 * SQL relacionadas as Contas precisa ser injetada com @Autowired
 * 
 * @author swb_thiago
 *
 */
@Component
@Transactional
public class CustomContasDAO  {
	
	//Injetando EntityManager
	@PersistenceContext
    private EntityManager em;
	
	//Pegando nome da Classe para montar os HQL's
	private static String tabela = Contas.class.getName();
	
	
	/**
	 * Método responsável por obter o numero de contas
	 * cadastradas no banco de dados.
	 * 
	 * (Esse método está aqui apenas a titulo de exemplo)
	 * 
	 * @return
	 */
	public long quantidadeContasCadastradas(){
		
		String hql = "SELECT COUNT(id) FROM " + tabela;
		Query query = (Query) em.createQuery(hql);
		
		long qtd = (long) query.getSingleResult();
		
		return qtd;
		
	}
	

}
