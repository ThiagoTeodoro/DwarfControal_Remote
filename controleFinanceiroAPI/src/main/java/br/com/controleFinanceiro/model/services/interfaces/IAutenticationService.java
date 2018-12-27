package br.com.controleFinanceiro.model.services.interfaces;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import br.com.controleFinanceiro.model.DTO.LoginDTO;
import br.com.controleFinanceiro.model.DTO.TokenDTO;
import br.com.controleFinanceiro.model.entitys.Usuarios;

public interface IAutenticationService {
	
	/**
	 * Método responsável por recuperar o usuário da requisição
	 * que está sendo feita.
	 * 
	 * Esse método recupera o Token, estrai o email do Token
	 * e retorna os dados de usuário deste email.
	 * @param request
	 * @return
	 */
	Usuarios getUsuarioRequisicao(HttpServletRequest request);
	
	
	/**
	 * Método responsável por gerar um Token para utilização caso
	 * o email e a senha enviados em Login, conferir com o email
	 * e senha do usuário conrrespondente no banco de dados.
	 * 
	 * @param loginDTO
	 * @return
	 */
	ResponseEntity<TokenDTO> getToken(LoginDTO loginDTO);
	
	
	/**
	 * Método responsável por checar se um Token está valido
	 * atravéz da JWT
	 * 
	 * @param Token
	 * @return
	 */
	ResponseEntity<Boolean> checkToken(String token);
	
}
