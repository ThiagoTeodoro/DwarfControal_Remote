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

import br.com.controleFinanceiro.model.DTO.ChangeSenhaDTO;
import br.com.controleFinanceiro.model.entitys.Usuarios;
import br.com.controleFinanceiro.model.services.UsuariosService;

@RestController
@RequestMapping(value="/api/Usuario")
@CrossOrigin
public class UsuarioController {
	
	@Autowired
	private UsuariosService usuariosService;
	
	/**
	 * Endpoint para cadastro de novos usuários
	 * 
	 * @param novoUsuario
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Novo", 
					method=RequestMethod.POST,
					consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, 
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE
				   )
	public Usuarios cadastrarUsuario(@RequestBody Usuarios novoUsuario, HttpServletRequest request) {
	
		return this.usuariosService.cadastrarUsuario(novoUsuario, request);
		
	}
	
	
	/**
	 * Endpoint para obter os dados do usuário que está logado
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Dados", 
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE
				   )
	public ResponseEntity<Usuarios> getDadosUsuarioRequisicao(HttpServletRequest request) {
		
		return this.usuariosService.getDadosUsuario(request);
		
	}
	
	/**
	 * Endpoint atualizar os dados de perfil do usuário
	 * 
	 * @param novoUsuario
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Update", 
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE
				   )
	public ResponseEntity<Boolean> updatePerfil(@RequestBody String nome, HttpServletRequest request) {
		
		return this.usuariosService.updatePerfil(nome, request);
		
	}
	
	
	/**
	 * Endpoint atualizar a senha do usuário
	 * 
	 * @param novoUsuario
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/Update/Password", 
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE
				   )
	public ResponseEntity<Boolean> changePassword(@RequestBody ChangeSenhaDTO changeSenhaDTO, HttpServletRequest request) {
		
		return this.usuariosService.changePassword(changeSenhaDTO, request);
		
	}
	
	
	
	/**
	 * Endpoint para checar se o usuário logado(Token) é um administrador ou não.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/IsAdminstrador",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Boolean> checkAdministrador(HttpServletRequest request){

		return this.usuariosService.checkAdministrador(request); 
		
	}
	
	/**
	 * Endpoint para obter os usuários cadastrados na aplicação
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/All", 
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE
				   )
	public ResponseEntity<List<Usuarios>> getUsuarios(HttpServletRequest request) {

		return this.usuariosService.listUsuarios(request);
		
	}

	
	/**
	 * Endpoint para ativar ou desativar usuários!
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/enableDesable",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Boolean> enableDesable(HttpServletRequest request, @RequestBody int idUsuarioAtivarDesativar){

		return this.usuariosService.ativarDesativarUsuario(request, idUsuarioAtivarDesativar); 
		
	}

	/**
	 * Endpoint para mudar o nivel do usuário enviado, caso o usuário 
	 * da requisição seja um Administrador.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/changeNivel",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Boolean> changeNivel(HttpServletRequest request, @RequestBody Usuarios usuarioAtt){

		return this.usuariosService.changeNivelUsuario(usuarioAtt.getId(), usuarioAtt.getNivel(), request); 
		
	}		
	
	
	/**
	 * Endpoint para restar se senha de  um usuário enviado, caso o usuário 
	 * da requisição seja um Administrador.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/resetPasswd",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Boolean> resetPasswd(HttpServletRequest request, @RequestBody int usuarioId){

		return this.usuariosService.resetPasswd(request, usuarioId); 
		
	}		
	
}
