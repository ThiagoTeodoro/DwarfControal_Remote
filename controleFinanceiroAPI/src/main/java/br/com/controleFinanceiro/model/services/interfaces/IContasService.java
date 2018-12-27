package br.com.controleFinanceiro.model.services.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import br.com.controleFinanceiro.model.entitys.Contas;

public interface IContasService {
	
	/**
	 * Método que retorna uma lista com todas as contas do usuário
	 * do TOKEN da requisição
	 * 
	 * @return
	 */
	List<Contas> allContasByToken(HttpServletRequest request);
	
	
	/**
	 * Método responável por cadastrar uma nova conta do Banco de Dados
	 *  
	 * @param request
	 * @param novaConta
	 * @return
	 */
	ResponseEntity<Contas> novaConta(HttpServletRequest request, String nomeConta);
	
	
	/**
	 * Método para recuperar uma conta do banco de dados, o método só entrega
	 * contas que pertence ao mesmo usuário da requisição, essa distinção é feita
	 * atravéz do TOKEN na request.
	 * 
	 * @param request
	 * @param idConta
	 * @return
	 */
	ResponseEntity<Contas> getConta(HttpServletRequest request, long idConta);
	
	
	/**
	 * Método responsável por realizar o Update de uma determinda conta
	 * caso essa conta pertença ao usuário logado(Token). 
	 * 
	 * @param contaUpdate
	 * @param request
	 * @return
	 */
	ResponseEntity<Contas> updateConta(Contas contaUpdate,HttpServletRequest request);
	
	
	/**
	 * Método responsável por deletar a conta caso a conta pertença ao mesmo usuário
	 * da requisição(Token) e a conta não possua nenhum lançamento.
	 * 
	 * @return
	 */
	ResponseEntity<Boolean> deleteConta(Contas conta, HttpServletRequest request);
}
