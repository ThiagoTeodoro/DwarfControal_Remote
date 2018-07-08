package br.com.controllers;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.com.dao.UsuarioDAO;
import br.com.entitys.Usuario;
import br.com.utilitarios.Encriptacao;

@Controller
public class PerfilController {
	
	/**
	 * Metodo responsável por obter os dados do Usuário da Session logada
	 * remover a senha e devolver para exibição no Front-End.
	 * 
	 * @param session Sesão que está logada. 
	 * @return JSON com os dados do Usuário que está logado.
	 */
	@RequestMapping(value="/Perfil", method=RequestMethod.GET)
	@ResponseBody
	public String getPerfilLogado(HttpSession session) {
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		
		/*
		 * Vou selecionar o Usuário pelo Banco Baseado 
		 * no ID da Session por que dai posso aproveitar 
		 * esse metodos para atualisar os dados após um Update 
		 * por exemplo.
		 */
		usuarioLogado = new UsuarioDAO().localizarPorId(usuarioLogado.getId());
		
		return new Gson().toJson(usuarioLogado);				
	}
	
	
	/**
	 * Metodo responsável por atualisar o unico dados permitido do Perfil por enquanto.
	 * O nome.
	 * 
	 * @param session Sessão atual do perfil.
	 * @param jsonUsuarioAtualisar Dados para atualisar.
	 * @return Status da Operação
	 */
	@RequestMapping(value="/Perfil", method=RequestMethod.PUT)
	@ResponseBody
	public String updatePerfilLogado(HttpSession session, @RequestBody String jsonUsuarioAtualisar) {
		
		//Obtendo o usuário que está logado pela Session para atulisar o unico dado permitido o Nome.
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		
		/*
		 * A Session não guarda a senha por tanto vou selecionar o usuário no 
		 * banco para não anular a senha do mesmo.
		 */
		usuarioLogado = new UsuarioDAO().localizarPorId(usuarioLogado.getId());
		
		if(usuarioLogado != null) {
			
			JSONObject objtoDadosAtualisar = new JSONObject(jsonUsuarioAtualisar);
		
			usuarioLogado.setNome(objtoDadosAtualisar.getString("Nome"));
			
			if(new UsuarioDAO().marge(usuarioLogado)) {
				
				/*
				 * Os dados foram atualisados com sucesso, vou atualisar o Dados da
				 * Session e em seguida retornar mensagem de sucesso!
				 */
				session.setAttribute("usuario", usuarioLogado);
				return new Gson().toJson("Dados do usuário atualizados com sucesso!");
				
			} else {
			
				return new Gson().toJson("Houve um erro ao tentar realizar o update dos dados do usuário!");
				
			}
			
		} else {
			
			return new Gson().toJson("Erro ao obter o usuário da Session atual!");
			
		}		
		
	}
	
	/**
	 * Metodo responsavel por atualisar a senha do usuário que está logado.
	 * 
	 * @param session Sessão atual.
	 * @param dadosAtualizarSenha Dados para atualização da senha
	 * @return Menssagem sobre o status da operação.
	 */
	@RequestMapping(value="/Perfil/UpdatePass", method=RequestMethod.PUT)
	@ResponseBody
	public String atualizarSenha(HttpSession session,@RequestBody String dadosAtualizarSenha) {
		
		// Obtendo o usuário que está logado pela Session para atualizar a senha
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

		/*
		 * A Session não guarda a senha por tanto é necessário obter os dados
		 * do Banco de Dados pelo Id armazenado na session.
		 */
		usuarioLogado = new UsuarioDAO().localizarPorId(usuarioLogado.getId());
		
		
		//Transformando os dados recebidos em um objeto JSON
		JSONObject jsonDadosAtualizarSenha = new JSONObject(dadosAtualizarSenha);
		
		String senhaAntigaDigitada = new Encriptacao().toMD5(jsonDadosAtualizarSenha.getString("senhaAntiga"));
		
		//Checando se a senha antiga e senha nova conferem.
		if(senhaAntigaDigitada.equals(usuarioLogado.getSenha())) {
			
			String novaSenha = jsonDadosAtualizarSenha.getString("novaSenha");
			String confirmacaoSenha = jsonDadosAtualizarSenha.getString("confirmacaoSenha");
			
			//Verificando se a senha e sua confirmação estão corretas
			if(novaSenha.equals(confirmacaoSenha)) {
				
				//As senhas conferem podemos realisar a atualização da senha
				usuarioLogado.setSenha(new Encriptacao().toMD5(jsonDadosAtualizarSenha.getString("novaSenha")));
				if(new UsuarioDAO().marge(usuarioLogado)) {
					
					return new Gson().toJson("Senha atualizada com sucesso!");
					
				} else {
					
					return new Gson().toJson("Erro ao tentar atualizar a senha no banco de dados!");
					
				}
															
			} else {
				
				return new Gson().toJson("A nova senha e sua confirmação não conferem! Atualização de senha não autorizada!");
				
			}																
			
		} else {
			
			return new Gson().toJson("A senha antiga digitada não confere! Atualização de senha não autorizada.");
			
		}
		
	}
		
}
