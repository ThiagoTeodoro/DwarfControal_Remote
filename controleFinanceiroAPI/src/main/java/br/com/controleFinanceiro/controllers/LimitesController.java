package br.com.controleFinanceiro.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.controleFinanceiro.model.DTO.limiteDTO;
import br.com.controleFinanceiro.model.services.LimitesService;

@RestController
@CrossOrigin
@RequestMapping("/api/Limites")
public class LimitesController {

	@Autowired
	private LimitesService limiteService;
	
	
	/**
	 * End point para obter um limite da API, o limite retornado é o do 
	 * usuário logado(Token)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	private Double getLimite(HttpServletRequest request) {
		
		return this.limiteService.getLimite(request);
		
	}
	
	
	/**
	 * End point para atualização do limite do usuário logaod(TOKEN)
	 * 
	 * 
	 * @param newLimite
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	private boolean updateLimite(@RequestBody limiteDTO limite, HttpServletRequest request) {
		
		return this.limiteService.updateLimite(limite.getValor(), request);
		
	}
	
	/**
	 * End-point para obter a percentagem do limite que já foi 
	 * atiginda em relação ao um valor base referência enviado.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/percent", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	private double getPercentagemLimite(@RequestBody limiteDTO valorReferencia, HttpServletRequest request) {
		
		return this.limiteService.percentagemLimite(valorReferencia.getValor(), request);
		
	}
	
}
