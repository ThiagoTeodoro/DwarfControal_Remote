package br.com.controleFinanceiro.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.controleFinanceiro.model.DTO.LancamentoDTO;
import br.com.controleFinanceiro.model.DTO.ResumoDTO;
import br.com.controleFinanceiro.model.entitys.Contas;
import br.com.controleFinanceiro.model.entitys.Lancamentos;
import br.com.controleFinanceiro.model.services.LancamentosService;

@RestController
@RequestMapping("/api/Lancamento")
@CrossOrigin
public class LancamentosController {
	
	@Autowired
	private LancamentosService lancamentosService;
	
	
	/**
	 * Endpoint para gravar um novo lançamento no banco de dados.
	 * 
	 * @param newLancamento
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/New",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Lancamentos> newLancamento(@RequestBody LancamentoDTO newLancamento, HttpServletRequest request){
		
		
		return this.lancamentosService.novoLancamento(newLancamento, request);
	
	}
	
	
	/**
	 * Endpoint para obter todos lançamentos de RECEITAS E DESPESAS de um determinado
	 * usuário. 
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/All",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Lancamentos>> getLancamentos(HttpServletRequest request){
		
		return this.lancamentosService.getLancamentos(request);
		
	};
	
	/**
	 * Endpoint para obter o somatório total dos lançamentos de RECEITAS E DESPESAS de um determinado
	 * usuário. 
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/All/Somatorio",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Double> getSomatorioLancamentos(HttpServletRequest request){
		
		return this.lancamentosService.getTotalLancamentos(request);
		
	};
	
	
	/**
	 * Endpoint para obter o resomo de cada conta em relação a receitas 
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Resumo/Receitas",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<ResumoDTO>> getResumoReceitas(HttpServletRequest request){
		
		return this.lancamentosService.getResumoReceitas(request);
		
	}
	
	
	/**
	 * Endpoint responsável por fornecer o somatório das receitas de um determinado usuário
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Receitas/Somatorio",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Double> getSomatorioReceitas(HttpServletRequest request){
		
		return this.lancamentosService.getSomatorioReceitas(request);
		
	}
	
	
	/**
	 * Endpoint para obter o resumo de cada conta em relação a despesa 
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Resumo/Despesas",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<ResumoDTO>> getResumoDespesas(HttpServletRequest request){
		
		return this.lancamentosService.getResumoDespesas(request);
		
	}
	
	
	/**
	 * Endpoint responsável por fornecer o somatório das despesas de um determinado usuário
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Despesas/Somatorio",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Double> getSomatorioDespesas(HttpServletRequest request){
		
		return this.lancamentosService.getSomatorioDespesas(request);
		
	}
	
	
	/**
	 * Endpoint para obter o resumo de cada conta em relação a Resultado 
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Resumo/Resultado",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<ResumoDTO>> getResumoResultado(HttpServletRequest request){
		
		return this.lancamentosService.getResumoResultado(request);
		
	}
	
	
	
	/**
	 * Endpoint para obter os lançamentos de receitas
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Receitas",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Lancamentos>> getReceitas(HttpServletRequest request){
		
		return this.lancamentosService.getReceitas(request);
		
	}
	
	
	/**
	 * Endpoint para obter os lançamentos de despesas
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Despesas",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Lancamentos>> getDespesas(HttpServletRequest request){
		
		return this.lancamentosService.getDespesas(request);
		
	}
	
	/**
	 * Endpoint para deletar lançamentos
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Delete",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Boolean> deleteLancamento(HttpServletRequest request,@RequestBody int idLancamento){
		
		return this.lancamentosService.excluirLancamento(request, idLancamento);
		
	}
	
	
	/**
	 * Endpoint para atualizar os lançamentos
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Update",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Lancamentos updateLancamento(HttpServletRequest request,@RequestBody Lancamentos lancamentoUpdate){
		
		return this.lancamentosService.updateLancamento(request, lancamentoUpdate);
		
	}
	
	
	/**
	 * Endpoint para obter o numero de lançamento de uma determinada conta.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/qtdLancamentosPorConta",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int qtdLancamentoPorConta(@RequestBody Contas conta){
		
		return this.lancamentosService.qtdLancamentosConta(conta);
		
	}
	
	
}
