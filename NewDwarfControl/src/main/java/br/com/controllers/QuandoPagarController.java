package br.com.controllers;

import java.util.Date;
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
import com.google.gson.JsonObject;

import br.com.dao.LimitesDAO;
import br.com.dao.QuandoPagarDAO;
import br.com.dao.UsuarioDAO;
import br.com.entitys.QuandoPagar;
import br.com.entitys.Usuario;
import br.com.objetos.ObjetoMenssagemFrontEnd;
import br.com.utilitarios.FuncoesData;

@Controller
public class QuandoPagarController {

	
	@RequestMapping(value="/QuandoPagar", method=RequestMethod.POST)
	@ResponseBody
	public String novoQuandoPagar(HttpSession session, @RequestBody String novoQuandoPagar){

		try {					
			
			QuandoPagar quandoPagar = new QuandoPagar();
			quandoPagar.setDescricao(new JSONObject(novoQuandoPagar).getString("Descricao"));
					
			/*
			 * O Front-End já converte as virgula para PONTO então eu não to usando as 
			 * funções numericas por que se usar da pal vem um valor diferente do esperado 
			 * e acaba gravando um valor enorme no banco de dados.
			 * 
			 * Acredito que o Front_End já converte por que eu coloquei campos lá de TYPE
			 * number antes tava Text então como mudou muda o jeito de tratar.
			 */
			quandoPagar.setValor(Float.parseFloat(new JSONObject(novoQuandoPagar).get("Valor").toString()));
	
			quandoPagar.setData(new FuncoesData().dateToCalendar((new FuncoesData().stringToDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", new JSONObject(novoQuandoPagar).getString("Data")))));
						 
			Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
			usuarioLogado = new UsuarioDAO().localizarPorId(usuarioLogado.getId());			 
			quandoPagar.setUsuario(usuarioLogado);			 				 
			 
			if(new QuandoPagarDAO().persist(quandoPagar)) {
				
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				
				msg.setSucesso(true);
				msg.setDescricao("'QuandoPagar' cadastrado com sucesso!");								
				 
				return new Gson().toJson(msg);
				 
			} else {
				 
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				
				msg.setErro(true);
				msg.setDescricao("Erro ao tentar gravar o dado no banco de dados!");
				
				return new Gson().toJson(msg);
				 
			}
			
		}catch (Exception ex) {
			
			ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
			
			msg.setErro(true);
			msg.setDescricao("Erro na execução da função.");
			msg.setExcessao(ex);
			
			return new Gson().toJson(msg);
			
		}		
		
	}
	
	
	/**
	 * Metodo responsável por obter todos os Quando Pagar do usuário que está logado!
	 * 
	 * @param session Sessão atual
	 * @return Lista com os QuandoPagar ou null
	 */
	@RequestMapping(value="/QuandoPagar/{mesAno}", method=RequestMethod.GET)
	@ResponseBody
	public String getQuandoPagarUsuario(HttpSession session,@PathVariable("mesAno") String mesAno){
		
		System.out.println(mesAno);
		
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
				
		List<QuandoPagar> retorno = new QuandoPagarDAO().selectQuandoPagarUsuarioMesAno(usuarioLogado, mesAno);

		if(retorno != null) {
			
			return new Gson().toJson(retorno);
			
		} else {
			
			ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
			
			msg.setErro(true);
			msg.setDescricao("Erro ao obter a lista de 'QuandoPagar' ou não existe quando apagar cadastrado ou houve um erro interno!");			
			
			return new Gson().toJson(msg);
			
		}		
	}
	
	
	/**
	 * Metodo responsável por obter o total dos Quando Pagar do usuário que está logado
	 * conforme mês enviado.
	 * 
	 * @param session Sessão atual
	 * @param mesAno mes e ano do filtro
	 * @return Valor do Total de QuandoPagar do Mês ou 0
	 */
	@RequestMapping(value="/QuandoPagar/Total/{mesAno}", method=RequestMethod.GET)
	@ResponseBody
	public String getTotalQuandoPagarUsuarioMesAno(HttpSession session,@PathVariable("mesAno") String mesAno){
		
		
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
				
		Double retorno = new QuandoPagarDAO().getTotalPrevisto(usuarioLogado, mesAno);

		return new Gson().toJson(retorno);
	}
	
	
	@RequestMapping(value="/QuandoPagar/Porcentagem/{mesAno}", method=RequestMethod.GET)
	@ResponseBody
	public String getPorcetagemLimite(HttpSession session, @PathVariable("mesAno") String mesAno) {
		
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		
		double limite = new LimitesDAO(session).getLimiteUsuarioLogado(session).getValor();
		double totalMes = new QuandoPagarDAO().getTotalPrevisto(usuarioLogado, mesAno);
		
		double porcentagem = (totalMes * 100) / limite;
		
		return new Gson().toJson(porcentagem);
	}
	
	
	@RequestMapping(value="/QuandoPagar/Quitar/{Id}", method=RequestMethod.PUT)
	@ResponseBody
	public String quitaQuandoPagar(@PathVariable("Id") long Id, HttpSession session) {
		
		QuandoPagar quandoPagar = new QuandoPagarDAO().localizarPorId(Id);
		
		//Verificando se o quandoPagar selecionado é do usuário que está  logado.
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		
		if(quandoPagar.getUsuario().getId() == usuarioLogado.getId()) {
			
			/*
			 * O Usuário que está logado é o mesmo do Quando Pagar que está tentando
			 *  atualizar, atualização PERMITIDA
			 */
			quandoPagar.setStatus(true);
			
			if(new QuandoPagarDAO().marge(quandoPagar)) {
				
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				msg.setDescricao("Status do 'Quando Pagar' atualizado no banco de dados com sucesso!");
				msg.setSucesso(true);
				
				return msg.toJSON();
				
			} else {
				
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				msg.setDescricao("Erro ao tentar atualizar Status do 'Quando Pagar' no banco de dados.");
				msg.setErro(true);
			
				return msg.toJSON();
				
			}
				
		} else {
		
			ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
			msg.setDescricao("Erro esse 'Quando Pagar' não pertence ao usuário Logado, Atualização não permitida");
			msg.setErro(true);
		
			return msg.toJSON();			
		
		}
		
	}
	
	
	@RequestMapping(value="/QuandoPagar/QuandoPagar/{Id}", method=RequestMethod.GET)
	@ResponseBody
	public String getQuandoPagar(@PathVariable("Id") long Id, HttpSession session) {
		
		QuandoPagar quandoPagar = new QuandoPagarDAO().localizarPorId(Id);
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		
		//Verificando se o quando Pagar solicitado é realmente do mesmo usuário que está logado
		if(quandoPagar.getUsuario().getId() == usuarioLogado.getId()) {
			
			return quandoPagar.toJSON();			
			
		} else {
			
			ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
			msg.setDescricao("O 'Quando Pagar' solicitado, não pertence ao usuário que está logado. Solicitação não autorizada!");
			msg.setErro(true);
			
			return msg.toJSON();
			
		}
		
	}
	
	@RequestMapping(value="QuandoPagar/QuandoPagar/Update", method=RequestMethod.PUT)
	@ResponseBody
	public String updateQuandoPagar(HttpSession session, @RequestBody String quandoPagarFrontEnd) {
		
		JSONObject objJSON = new  JSONObject(quandoPagarFrontEnd); 
		
		QuandoPagar quandoPagar = new QuandoPagarDAO().localizarPorId(Long.parseLong(objJSON.get("Id").toString()));
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		
		//Verificando se o quando Pagar solicitado é realmente do mesmo usuário que está logado
		if(quandoPagar.getUsuario().getId() == usuarioLogado.getId()) {
			
			//Podemos realizar a Atualização dos campos			
			Date dataRecebida = new FuncoesData().stringToDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", objJSON.getString("Data"));					
			quandoPagar.setData(new FuncoesData().dateToCalendar(dataRecebida));
			quandoPagar.setDescricao(objJSON.getString("Descricao"));					
			quandoPagar.setStatus(new JSONObject(objJSON.get("Status").toString()).getBoolean("quitacao"));
			quandoPagar.setValor(Float.parseFloat(objJSON.get("Valor").toString()));
			
			if(new QuandoPagarDAO().marge(quandoPagar)) {
				
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				msg.setDescricao("'Quando Pagar' atualizado com sucesso!");
				msg.setSucesso(true);
				
				return msg.toJSON();
								
			} else {
				
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				msg.setDescricao("Houve um erro ao tentar atualizar o 'Quando Pagar' no Banco de Dados!");
				msg.setErro(true);
				
				return msg.toJSON();
				
			}
			
		} else {
			
			ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
			msg.setDescricao("O 'Quando Pagar' solicitado, não pertence ao usuário que está logado. Solicitação não autorizada!");
			msg.setErro(true);
			
			return msg.toJSON();
			
		}
		
	}
	
	
}
