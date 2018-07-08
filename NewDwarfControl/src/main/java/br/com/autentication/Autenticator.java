package br.com.autentication;

import javax.servlet.http.HttpSession;

import br.com.dao.UsuarioDAO;
import br.com.entitys.Usuario;

/**
 * Classe responsável por conter os métodos de Autenticação do Sistema
 * 
 * @author Thiago Teodoro
 *
 */
public class Autenticator {
	
	/**
	 * Método que recebe um email e uma senha, descobre pelo banco de dados
	 * se essa senha confere, se se conferir abre um session com usuário 
	 * selecionado. Se as senhas não conferirem, a sessão não é aberta e é 
	 * retornado false. 
	 * 
	 * @param email String com o email que está tentando logar.
	 * @param senha Hash MD5 do usuário que está tentando logar.
	 * @return true or false.
	 */
	public boolean login(String email, String senha, HttpSession session) {
		
		Usuario usuarioBanco = new UsuarioDAO().getUsuarioPorEmail(email);
		
		if(usuarioBanco != null) {
			
			if(usuarioBanco.getSenha().equals(senha)) {
				
				//O usuário existe e a senha está correta, criando Session,
				//você pode setar o Objeto na Session isso é novo e é muito util xD
				session.setAttribute("usuario", usuarioBanco);
				
				return true;				
				
			} else {
				
				System.out.println("### Yellow Alert! A senha digitada não confere!");
				return false;
				
			}			
			
		} else {
			
			System.out.println("### Yellow Alert! O usuário de email [ " + email + " ] não existe no banco de dados!");
			return false;
			
		}
			
	}
	
	
	/**
	 * Metodo que verifica se o usuário da Sessão(Logado) é um Administrador
	 * 
	 * 
	 * @param session atual
	 * @return True caso seja um ADM false caso não seja.
	 */
	public boolean verificaAdministrador(HttpSession session) {
		
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		
		if(usuarioLogado.getNivel().equals("ADMINISTRADOR")) {
			
			System.out.println("### Este usuário É um usuário Administrador!");
			return true;
			
		} else {
			
			System.out.println("### Este usuário NÃO é um usuário Administrador");
			return false;
			
		}
		
	}
	
}
