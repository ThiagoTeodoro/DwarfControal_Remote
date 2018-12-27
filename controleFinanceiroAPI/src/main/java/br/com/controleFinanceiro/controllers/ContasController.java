package br.com.controleFinanceiro.controllers;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.controleFinanceiro.model.entitys.Contas;
import br.com.controleFinanceiro.model.services.ContasService;

@RestController
@CrossOrigin
@RequestMapping("/api/Contas")
public class ContasController {
	
	@Autowired
	private ContasService contasService;

	/**
	 * Método responsável por retornar todas as contas cadastradas no Banco de Dados
	 * 
	 * @return
	 */
	@RequestMapping(
					value="/All",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Contas> obterTodasAsContas(HttpServletRequest request){
		
		return contasService.allContasByToken(request);
		
	}
	
	/**
	 * Endpoint para o serviço de cadastramento de contas
	 * 
	 * @param request
	 * @param nomeConta
	 * @return
	 */
	@RequestMapping(value="/New",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Contas> novaConta(HttpServletRequest request,@RequestBody String nomeConta){
		
		return contasService.novaConta(request, nomeConta);
		
	}
	
	/**
	 * EndPoint para obter os dados de uma conta, caso o usuário logado(Token)
	 * seja dono dessa mesma conta que está sendo solicitada.
	 * 
	 * @param idConta
	 * @param request
	 * @return
	 */
	@RequestMapping(value="{idConta}",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Contas> obterConta(@PathVariable("idConta") long idConta, HttpServletRequest request){
		
		return this.contasService.getConta(request, idConta);
		
	}
	
	
	/**
	 * Endpoint responsável pelo Update de Contas 
	 *  
	 * @param contaEditar
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Update",
					method=RequestMethod.PUT,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Contas> updateConta(@RequestBody Contas contaEditar, HttpServletRequest request){
		
		return this.contasService.updateConta(contaEditar, request);
		
	}
	
	/**
	 * Endpoint responsável pela exclusão de contas caso ela 
	 * seja do mesmo usuário logado(Token) e não possua lancamentos
	 * 
	 * @param contaExcluir
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Delete",
			method=RequestMethod.POST,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Boolean> deleteConta(@RequestBody Contas contaExcluir, HttpServletRequest request){
	
		return this.contasService.deleteConta(contaExcluir, request);
	
	}

	
} 
