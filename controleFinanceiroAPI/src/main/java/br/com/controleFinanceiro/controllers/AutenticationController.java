package br.com.controleFinanceiro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.controleFinanceiro.model.DTO.LoginDTO;
import br.com.controleFinanceiro.model.DTO.TokenDTO;
import br.com.controleFinanceiro.model.services.AutenticationService;

@RestController
@RequestMapping("/Autentication")
@CrossOrigin
public class AutenticationController {
	
	@Autowired
	private AutenticationService autenticationService;
	
	
	/**
	 * Endpoint para obter um TOKEN
	 * 
	 * @param loginDTO
	 * @return
	 */
	@RequestMapping(
						value="/Token",
						method = RequestMethod.POST,
						consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,
						produces=MediaType.APPLICATION_JSON_UTF8_VALUE
				   )
	public ResponseEntity<TokenDTO> getToken(@RequestBody LoginDTO loginDTO ) {
	
		return autenticationService.getToken(loginDTO);
		
	}
	
	/**
	 * Endpoint para checar se um Token é válido ou não
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(
				   		value="/CheckToken",
				   		method = RequestMethod.POST,						
						produces=MediaType.APPLICATION_JSON_UTF8_VALUE			
				   )
	public ResponseEntity<Boolean> checkToken(@RequestBody String token) {
		
		return autenticationService.checkToken(token);
		
	}
	
		
}
