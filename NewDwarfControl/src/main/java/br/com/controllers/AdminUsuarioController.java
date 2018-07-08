package br.com.controllers;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.com.autentication.Autenticator;
import br.com.dao.UsuarioDAO;
import br.com.entitys.Usuario;
import br.com.utilitarios.Encriptacao;



/**
 * ATENÇÃO!!!!
 * 
 * Essa classe trata de operações envolvendo acesso restrito a ADMINISTRADOR
 * por tanto, para cada requisição feita em qualquer um dos metodos eu vou 
 * checar PELA SESSION SEMPRE PELA SESSION e se for Administrador eu continuo 
 * o processo, se não eu não continuo. Eu vou checar pela Session, sempre pela
 * SESSION pois lá é seguro coletar as informações
 * 
 * @author Thiago Teodoro
 *
 */
@Controller
public class AdminUsuarioController {
	
	/**
	 * Metodo que retorna uma Lista com todos os usuários Cadastrados no Sistema
	 * 
	 * **NECESÁRIO SER UM ADM LOGADO!!! FAZER ESSA CHECAGEM**
	 * 
	 * @return Lista JSON com todos os usuários cadastrados no sistema ou null caso aconteça algum erro.
	 */
	@RequestMapping(value = "/servicoAdmUsuario", method = RequestMethod.GET)
	@ResponseBody
	public String getUsuariosCadastrados(HttpSession session) {
		
		if(new Autenticator().verificaAdministrador(session)) {
			
			List<Usuario> listaUsuario = new UsuarioDAO().getListaTodos();
			return new Gson().toJson(listaUsuario);				
				
		} else {
			
			return null;
			
		}
			
	}
	
	
	/**
	 * Metodo que cadastra um Usuário no Sistema 
	 * 
	 * **NECESÁRIO SER UM ADM LOGADO!!! FAZER ESSA CHECAGEM**
	 * 
	 * @return true caso tenha sido cadastrado false caso não tenha cadastrado
	 */
	@RequestMapping(value = "/servicoAdmUsuario", method = RequestMethod.POST)
	@ResponseBody
	public boolean saveUsuario(@RequestBody String usuarioJson, HttpSession session) {
		
		if(new Autenticator().verificaAdministrador(session)) {
			
			//Preenchendo Dados
			Usuario novoUsuario = new Usuario();
			JSONObject jsonObject = new JSONObject(usuarioJson);
			novoUsuario.setEmail(jsonObject.getString("Email"));
			novoUsuario.setNome(jsonObject.getString("Nome"));
			novoUsuario.setSenha(new Encriptacao().toMD5(jsonObject.getString("Senha")));
			novoUsuario.setNivel(jsonObject.getString("Nivel"));
			novoUsuario.setDataHora_Criacao(Calendar.getInstance());
			
			if(new UsuarioDAO().persist(novoUsuario)) {
			
				return true;
		
			} else {
			
				return false;
				
			}
		} else {
			
			return false;
			
		}
			
	}
	
	
	
	/**
	 * Metodo que retorna O Usuário do ID Solicitado
	 * 
	 * **NECESÁRIO SER UM ADM LOGADO!!! FAZER ESSA CHECAGEM**
	 * 
	 * @return Ususario solicitado em formato JSON ou null
	 */
	@RequestMapping(value = "/servicoAdmUsuario/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getUsuario(@PathVariable("id") long Id, HttpSession session) {
		
		if(new Autenticator().verificaAdministrador(session)) {
			
			Usuario usuario = new UsuarioDAO().localizarPorId(Id);
			usuario.setSenha("");
			return new Gson().toJson(usuario);				
				
		} else {
			
			return null;
			
		}
			
	}
	
	
	@RequestMapping(value = "/servicoAdmUsuario", method = RequestMethod.PUT)
	@ResponseBody	
	public String updateUsuario(@RequestBody String usuarioJson, HttpSession session) {
		
		try {
		
			if(new Autenticator().verificaAdministrador(session)) {
				
				//Atualizando Dados			
				JSONObject jsonObject = new JSONObject(usuarioJson);
				Usuario updateUsuario = new UsuarioDAO().localizarPorId(jsonObject.getLong("Id"));									
				updateUsuario.setEmail(jsonObject.getString("Email"));
				updateUsuario.setNome(jsonObject.getString("Nome"));			
				updateUsuario.setNivel(jsonObject.getString("Nivel"));
				
				if(new UsuarioDAO().marge(updateUsuario)) {
					
					return new Gson().toJson("Usuário atualizado com sucesso!");
					
				} else {
					
					return new Gson().toJson("### Red Alert! Erro ao tentar realizar o Update no Banco de Dados!");
				}
											
			} else {
				
				return new Gson().toJson("### Yellow Alert! Usuário não autorizado para está operação!");
				
			}
			
		} catch(Exception ex) {
			
			System.out.println("### Red Alert! Ouve um erro na execução do processo. Exception [ " + ex.getMessage() + " ]");
			return new Gson().toJson("### Red Alert! Ouve um erro na execução do processo. Exception [ " + ex.getMessage() + " ]");
			
		}
		
	}
	
	
	@RequestMapping(value = "/servicoAdmUsuario/Pesquisar", method = RequestMethod.POST)
	@ResponseBody
	public String pesquisarUsuario(@RequestBody String parametroPesquisa, HttpSession session) {	
		if (new Autenticator().verificaAdministrador(session)) {
			try {
				return new Gson().toJson(new UsuarioDAO().pesquisarNomeEmailNivelCampoUnico(new JSONObject(parametroPesquisa).getString("dados")));
			} catch (Exception ex) {
				System.out.println("### Red Alert! Erro ao tentar executar o método de pesquisa pesquisarUsuario(). Excepetion [ "	+ ex.getMessage() + " ]");
				return new Gson().toJson("### Red Alert! Erro ao tentar executar o método de pesquisa pesquisarUsuario(). Excepetion [ " + ex.getMessage() + " ]");
			}
		} else {
			return new Gson().toJson("### Yellow Alert! Usuário não autorizado para está operação!");
		}
	}

}
