package br.com.controleFinanceiro.model.DAOs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.controleFinanceiro.model.entitys.Dividas;

@Component
@Transactional
public class CustomDividasDAO {
	
	//Injetando EntityManager
	@PersistenceContext
    private EntityManager em;
	
	//Pegando nome da Classe para montar os HQL's
	private static String tabelaDividas = Dividas.class.getName();
	
	//Logger
	private static Logger logger = LoggerFactory.getLogger(CustomDividasDAO.class);
	
	/**
	 * Método para recupear o somatorio total de dividas de um
	 * determinado mês e usuário.
	 * 
	 * @param mesAno Ex. 2018-10
	 * @return
	 */
	public Double getSomatorioDividasMes(String mes, String ano, int idUsuario) {
		
		this.logger.info("Obtendo somatório total do ano mês " + ano + "-" + mes + " do banco de dados em relação as dividas!");
		
		String hql = "SELECT SUM(valor) FROM " + tabelaDividas + " WHERE usuario_id = " + idUsuario + " AND MONTH(data_vencimento) = " + mes + " AND YEAR(data_vencimento) = " + ano;		
		Query query = (Query) em.createQuery(hql);
		
		double somatorio = 0.00;
		
		//Verificando se não é nulo
		if(query.getSingleResult() != null) {
		
			somatorio = (double) query.getSingleResult();
		
		}		
		
		this.logger.info(String.format("Somatório total do ano e mês [%s], obtido com sucesso : Somatório [%s]", (mes + "-" + ano), somatorio));
		
				
		return somatorio;
	}

}
