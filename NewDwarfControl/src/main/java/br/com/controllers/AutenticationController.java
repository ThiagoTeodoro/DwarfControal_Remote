package br.com.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.com.autentication.Autenticator;
import br.com.entitys.Usuario;
import br.com.utilitarios.Encriptacao;

@Controller
public class AutenticationController {

	
	@RequestMapping(value = "/efetuarLogin", method = RequestMethod.POST)	
	public String efetuarLogin(@RequestParam("email") String email, @RequestParam("senha") String senha, HttpSession session){
		
		Autenticator autenticator = new Autenticator();
		Encriptacao encriptacao = new Encriptacao();
		
		if(autenticator.login(email, encriptacao.toMD5(senha), session)) {
			
			//Usuário está autenticado e com sesion, Redire
			return "/views/interno/interno.html";
			
		} else {
			
			//Usuário não foi autenticado voltando para Index
			return "index.html";
			
		}
					
	}
	
	
	@RequestMapping(value = "/Sair", method = RequestMethod.GET)
	public String efetuarLogoff(HttpSession session) throws IOException {
	
		System.out.println("### Blue Alert! Deslogando Usuário!");
		
		//Invalidando Session, muito importante limpar todos os dados antes de invalidar a sessão
		session.invalidate();
		
		//Redirecionando Login
		return "index.html";
		
	}
	
	
	
	@RequestMapping(value = "/getUsuarioLogado", method = RequestMethod.POST)
	@ResponseBody
	public String getUsuarioLogado(HttpSession session) {
		
		//Lipando a Senha
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		usuarioLogado.setSenha("");
		
		return new Gson().toJson(usuarioLogado);
		
	}
	
}
