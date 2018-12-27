package br.com.controleFinanceiro.model.services.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import br.com.controleFinanceiro.model.DTO.ChangeSenhaDTO;
import br.com.controleFinanceiro.model.entitys.Usuarios;

public interface IUsuariosService {
	
	/**
	 * Método responsável por cadastrar um novo usuário na base de dados.
	 * 
	 * Esse método verifica se o usuário é um administrador, pois apenas 
	 * usuários administradores podem adicionar novos usuários na base 
	 * de dados.
	 * 
	 * @param novoUsuario
	 * @return
	 */
	Usuarios cadastrarUsuario(Usuarios novoUsuario, HttpServletRequest request);
	
	
	/**
	 * Serviço para checar se o usuário de uma requisição (Token) é administrador
	 * ou não.
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<Boolean> checkAdministrador(HttpServletRequest request);
	
	
	/** 
	 * Serviço para obter os dados de um usuário logado
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<Usuarios> getDadosUsuario(HttpServletRequest request);
	
	
	/** 
	 * Serviço para atualizar os dados permitidos do perfil do usuário por enquanto 
	 * estamos atualizando o 
	 * 
	 * Nome
	 * 
	 * @param request
	 * @param nome
	 * @return
	 */
	ResponseEntity<Boolean> updatePerfil(String nome, HttpServletRequest request);
	
	/**
	 * Serviço para alterar a senha do usuário logado.
	 * 
	 * A atualização da senha só é executada caso a Senha antiga 
	 * seja igual a senha antiga enviada no DTO. É uma forma de 
	 * garantir a segurança.
	 * 
	 * @param changeSenhaDTO
	 * @return
	 */
	ResponseEntity<Boolean> changePassword(ChangeSenhaDTO changeSenhaDTO, HttpServletRequest request);
	
	/**
	 * Retorna a lista de todos os usuários do sistema.
	 * 
	 * Obs: O Hash das senhas é removido por segurança.
	 * 
	 * @return
	 */
	ResponseEntity<List<Usuarios>> listUsuarios(HttpServletRequest request);

	/**
	 * Método para ativar e desativar usuarios se o o usuário
	 * da requisição for um Administrador.
	 * 
	 * Se o usuário enviado estiver Ativo ele será desativado.
	 * Se estiver inativo será ativado.
	 * 
	 * @param request
	 */
	ResponseEntity<Boolean> ativarDesativarUsuario(HttpServletRequest request, int idUsuarioDesativarAtivar);
	
	/**
	 * Método para alterar o nivel de um determinado usuário.
	 * 
	 * @param IdUsuario
	 * @param NovoNivel
	 * @param request
	 * @return
	 */
	ResponseEntity<Boolean> changeNivelUsuario(int IdUsuario, int NovoNivel, HttpServletRequest request);
	
	/**
	 * Serviço para resetar a senha de um determinado usuário para 
	 * 123456 caso o usuário da requisição seja um ADM
	 * 
	 * @param request
	 * @return
	 */
	ResponseEntity<Boolean> resetPasswd(HttpServletRequest request, int idUsuario);
	
	
}
