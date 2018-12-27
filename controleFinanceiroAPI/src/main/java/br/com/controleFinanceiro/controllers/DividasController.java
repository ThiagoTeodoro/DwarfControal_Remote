package br.com.controleFinanceiro.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.controleFinanceiro.model.DTO.DividaDTO;
import br.com.controleFinanceiro.model.DTO.liquidarDTO;
import br.com.controleFinanceiro.model.entitys.Dividas;
import br.com.controleFinanceiro.model.services.DividasService;

@RestController
@CrossOrigin
@RequestMapping("/api/Dividas")
public class DividasController {
	
	@Autowired
	private DividasService dividasService;
	
	
	/**
	 * End-Point para criação de uma nova Divida.
	 * 
	 * @param divida
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Dividas newDivida(@RequestBody DividaDTO divida, HttpServletRequest request) {

		return this.dividasService.cadastrarDivida(divida, request);
		
	}
	
	
	/**
	 * End-point para selecionar todas as dividas de um usuário Logado (TOKEN)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/filter/{anoMes}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Dividas> getDividas(@PathVariable("anoMes") String anoMes, HttpServletRequest request){
		
		return this.dividasService.getDividas(anoMes, request);
		
	}
	
	/**
	 * End-point para liquidar uma determinada divida.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/liquidar", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean liquidar(@RequestBody liquidarDTO liquidarDTO, HttpServletRequest request){
		
		return this.dividasService.liquidarDivida(liquidarDTO.getContaOrigemDinheiro(), liquidarDTO.getIdDivida(), request);
		
	}
	
	
	/**
	 * End-point responsável por executar a deleção de uma divida, e se caso a mesma conter uma replica
	 * no plano de lançamentos de depesa o mesmo também será excluido.
	 * 
	 * @param idDivida
	 * @return
	 */
	@RequestMapping(value="/{idDivida}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean delete(@PathVariable("idDivida") int idDivida, HttpServletRequest request) {
		
		return this.dividasService.excluirDivida(idDivida, request);
		
	}
	
	/**
	 * End-point para realizar o update de dividas.
	 * O update sera realizado caso o usuário da divida seja o memso logado(TOKEN) e 
	 * se houver algum lançamento replicado como despesa o mesmo será também 
	 * atualiado.
	 * 
	 * @param divida
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean newDivida(@RequestBody Dividas divida, HttpServletRequest request) {

		return this.dividasService.updateDivida(divida, request);
		
	}
	
	
	/**
	 * End-point para solicitar o somatório total de um determinado mês, 
	 * as dividas serão filtradas por usuario.
	 * 
	 * @param mes
	 * @param ano
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{mes}/{ano}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public double getSomatorioMes(@PathVariable("mes") String mes, @PathVariable("ano") String ano, HttpServletRequest request) {
		
		return this.dividasService.getSomatorioMes(mes, ano, request);
		
	}

}
