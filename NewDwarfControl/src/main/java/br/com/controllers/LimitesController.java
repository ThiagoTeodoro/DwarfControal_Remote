package br.com.controllers;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.apache.bcel.internal.generic.LMUL;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

import br.com.dao.LimitesDAO;
import br.com.entitys.Limites;

@Controller
public class LimitesController {

	/**
	 * Metodo responsável por  obter o limite do usuário que 
	 * está logado.
	 * 
	 * @param session Sessão atual
	 * @return Limite do usuário logado ou uma msg de erro.
	 */
	@RequestMapping(value="/Limite", method=RequestMethod.GET)
	@ResponseBody
	public String getLimiteUsuariLogado(HttpSession session) {
		Limites limite = new Limites();
		
		limite = new LimitesDAO(session).getLimiteUsuarioLogado(session);
		
		if(limite == null) {
			
			return new Gson().toJson("Erro ao obter o limite do usuário selecioado.");
			
		} else {
			
			limite.getUsuario().setSenha("");
			return new Gson().toJson(limite);
			
		}
	}
	
	/**
	 * Metodo responsável por atualizar o limite do usuário logado
	 * 
	 * @param session Sessão do limite atual.
	 * @param dadosLimite dados para atualizar
	 * @return Messagem com o status do processo.
	 */
	@RequestMapping(value="/Limite", method=RequestMethod.PUT)
	@ResponseBody
	public String updateLimiteUsuariLogado(HttpSession session, @RequestBody String dadosLimite) {
		Limites limite = new Limites();
		
		limite = new LimitesDAO(session).getLimiteUsuarioLogado(session);
		
		if(limite == null) {
			
			return new Gson().toJson("Erro ao obter o limite do usuário selecioado.");
			
		} else {
			
			//Atualizando Limite do usuário
			JSONObject jsonDadosNovoLimite = new JSONObject(dadosLimite);	
			
			/*
			 * O Float precisa ser tratado, primeiro eu tenho que remover todos os pontos 
			 * da String, após isso trocar as virgulas por pontos.
			 *
			 */			
			String novoValor = jsonDadosNovoLimite.getString("Valor");					
			novoValor = novoValor.replace(".", "");
			novoValor = novoValor.replace(",", ".");
			
			//Agora podemos atribuir.
			limite.setValor(Float.parseFloat(novoValor));
			
			if(new LimitesDAO(session).marge(limite)) {
				
				return new Gson().toJson("Limite atualizado com sucesso!");
				
			} else  {
				
				return new Gson().toJson("Erro ao atualizar limite no banco de dados!");
				
			}
			
		}
	}
	
}
