package br.com.controleFinanceiro.model.services.interfaces;

import javax.servlet.http.HttpServletRequest;

public interface ILimitesService {
	
	/**
	 * Método que consulta um limite de um determinado usuário
	 * e o devolve.
	 * 
	 *  #Caso o limite não exista na base de dados, será criado
	 *  um automaticamente de valor 0, visto que to usuário deva 
	 *  ter um limite na API.
	 *  
	 * @param request
	 * @return
	 */
	double getLimite(HttpServletRequest request);
	
	
	/**
	 * Método responsável por atualizar um limite na base de dados.
	 * 
	 * @param valor
	 * @param request
	 * @return
	 */
	boolean updateLimite(double valor, HttpServletRequest request);
	
	
	/**
	 * Método responsável por calcular quanto um valor de referência é
	 * em relação ao limite do usuário logado(TOKEN)
	 * 
	 * @param valorReferencia
	 * @param request
	 * @return
	 */
	double percentagemLimite(double valorReferencia, HttpServletRequest request);
	
}
