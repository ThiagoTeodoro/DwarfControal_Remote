package br.com.controleFinanceiro.model.services.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import br.com.controleFinanceiro.model.DTO.LancamentoDTO;
import br.com.controleFinanceiro.model.DTO.ResumoDTO;
import br.com.controleFinanceiro.model.entitys.Contas;
import br.com.controleFinanceiro.model.entitys.Lancamentos;

public interface ILancamentosService {
	
	/**
	 * Serviço para gravação de novos lançamentos no Banco de Dados.
	 * 
	 * @param lancamentoDTO
	 * @param request
	 * @return
	 */
	ResponseEntity<Lancamentos> novoLancamento(LancamentoDTO lancamentoDTO, HttpServletRequest request);


	/**
	 * Serviço para obter a lista de lançamento de receitas e despesas e um determinado usuário (TOKEN)
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<List<Lancamentos>> getLancamentos(HttpServletRequest request);
	
	
	/**
	 * Serviço para obter o somátorio (Resultado) total de todos os lançamentos de um determinado usuário
	 * sejam eles de Receita ou Despesas.
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<Double> getTotalLancamentos(HttpServletRequest request);
	
	
	/**
	 * Serviço para obter o resumo das receitas por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<List<ResumoDTO>> getResumoReceitas(HttpServletRequest request);
	
	
	/**
	 * Serviço responsável por obter o somátorio total das receitas de um determinado usuário.
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<Double> getSomatorioReceitas(HttpServletRequest request);
	
	
	/**
	 * Serviço para obter o resumo das despesas por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<List<ResumoDTO>> getResumoDespesas(HttpServletRequest request);
	
	
	/**
	 * Serviço responsável por obter o somátorio total das despesas de um determinado usuário.
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<Double> getSomatorioDespesas(HttpServletRequest request);
	
	
	/**
	 * Serviço para obter o resumo de Resultado por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<List<ResumoDTO>> getResumoResultado(HttpServletRequest request);
	
	
	/**
	 * Serviço para obter as receitas por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<List<Lancamentos>> getReceitas(HttpServletRequest request);
	
	
	/**
	 * Serviço para obter as despesas por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<List<Lancamentos>> getDespesas(HttpServletRequest request);
	
	
	/**
	 * Serviço para exclusão de lançamentos, o lançamento só será excluido
	 * caso ele pertença ao usuário logado (Token) requisição. 
	 * 
	 * @param request
	 * @param idLancamento
	 * @return
	 */
	ResponseEntity<Boolean> excluirLancamento(HttpServletRequest request, int idLancamento);
	
	
	/**
	 * Serviço para realizar o update de um determinado lançamento caso esse lançamento
	 * pertença ao usuário da requisição(TOKEN)
	 * 
	 * @param request
	 * @param lancamentoUpdate
	 * @return
	 */
	Lancamentos updateLancamento(HttpServletRequest request, Lancamentos lancamentoUpdate);
	
	/**
	 * Serviço para informar o numero de lançamentos pertecente a uma determianda conta.
	 * 
	 * @param conta
	 * @return
	 */
	int qtdLancamentosConta(Contas conta);
	

}

