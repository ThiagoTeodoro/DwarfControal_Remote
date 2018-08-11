package br.com.controllers;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.entitys.Lancamento;
import br.com.models.LancamentoDTO;
import br.com.models.RequestLancamentosDTO;
import br.com.services.LancamentosService;

@RestController
public class LancamentosController {
			
	private LancamentosService lancamentosService = new LancamentosService();
	
	/**
	 * Método responsável por entregar os lançamentos conforme o periodo
	 * e conta enviados em uma request.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/get/lancamentos", method=RequestMethod.POST)
	public ResponseEntity<List<Lancamento>> obterLancamentos(@RequestBody RequestLancamentosDTO request) {
		
		List<Lancamento> listaLancamentos = lancamentosService.obterLancamentos(request);
		
		if(listaLancamentos.size() == 0) {
			
			return new ResponseEntity<List<Lancamento>>(HttpStatus.NO_CONTENT);
			
		} else {
			
			return new ResponseEntity<List<Lancamento>>(listaLancamentos, HttpStatus.OK);
			
		}
	}
	
	
	/**
	 * Método responsável por entregar o saldo anterior conforme o periodo
	 * e conta enviados em uma request
	 * 
	 * @param requestLancamentos
	 * @return
	 */
	@RequestMapping(value="/get/saldoAnterior", method=RequestMethod.POST)
	public ResponseEntity<Float> getSaldoAnterior(@RequestBody RequestLancamentosDTO requestLancamentos){
		
		Float retorno = lancamentosService.getSaldoAnterior(requestLancamentos);
		
		return new ResponseEntity<Float>(retorno, HttpStatus.OK);
		
	}	
	
	
	/**
	 * Método responsável por entregar o saldo total conforme o periodo (dataFinal)
	 * e conta enviados em uma request
	 * 
	 * @param requestLancamentos
	 * @return
	 */
	@RequestMapping(value="/get/saldoTotalPeriodo", method=RequestMethod.POST)
	public ResponseEntity<Float> getSaldoTotalPeriodo(@RequestBody RequestLancamentosDTO requestLancamentos){
		
		Float retorno = lancamentosService.obterSaldoTotalAteFimPeriodo(requestLancamentos);
		
		return new ResponseEntity<Float>(retorno, HttpStatus.OK);
		
	}
	
	/***
	 * Método responsável por excluir um lançamento. É
	 * necessário antes de excluir veriricar se o lançamento pertence
	 * ao usuário logado!
	 * 
	 * @param idLancamento
	 * @return
	 */
	@RequestMapping(value="/delete/lancamento/{id}", method=RequestMethod.DELETE)
	private Boolean excluirLancamento(@PathVariable("id") int idLancamento, HttpSession session) {
		
		//Chamando método competente
		return lancamentosService.excluirLancamento(idLancamento, session);
		
	}
	
	
	/***
	 * Método responsável por cadastrar novos lançamentos. 
	 * 
	 * @param novoLancamento novo lançamento a ser cadastrado.
	 * @return
	 */
	@RequestMapping(value="/new/lancamento", method=RequestMethod.POST)
	private Boolean novoLancamento(@RequestBody LancamentoDTO novoLancamento) {
		
		return lancamentosService.cadastrarLancamento(novoLancamento.toLancamento(false));
		
	}
	
	
	/***
	 * Método responsável por obter os dados de lançamento para disponibilizar para 
	 * edição.
	 * 
	 * @param idLancamento
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/get/lancamento/{id}", method=RequestMethod.GET)
	private Lancamento getLancamento(@PathVariable("id") int idLancamento, HttpSession session) {
		
		//Chamando método competente
		return lancamentosService.getLancamento(idLancamento, session);
		
	}
	
	
	/**
	 * Serviço esposoto para atualizar Lancamento
	 * 
	 * @param lancamento
	 * @return
	 */
	@RequestMapping(value="/update/lancamento", method=RequestMethod.POST)
	private Boolean updateLancamento(@RequestBody LancamentoDTO lancamento) {
		
		return lancamentosService.updateLancamento(lancamento.toLancamento(true));
		
	}
	
}