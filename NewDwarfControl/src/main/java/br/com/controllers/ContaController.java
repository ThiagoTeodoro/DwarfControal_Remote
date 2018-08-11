package br.com.controllers;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.com.dao.ContaDAO;
import br.com.entitys.Conta;
import br.com.entitys.Usuario;
import br.com.objetos.ObjetoMenssagemFrontEnd;

@Controller
public class ContaController {

	/**
	 * Metodo responsável por salvar uma Conta no Banco de Dados
	 * 
	 * @param contaJSON dados para cadastrar a conta vindo do FrontEnd
	 * @param session sessão do usuário logado
	 * @return JSON com o msg de sucesso ou erro da operação
	 */
	@RequestMapping(value="/Conta", method=RequestMethod.POST)
	@ResponseBody
	public String saveConta(@RequestBody String contaJSON, HttpSession session) {
		
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		JSONObject objContaJSON = new JSONObject(contaJSON);
		
		Conta novaConta = new Conta();
		novaConta.setDescricao(objContaJSON.getString("Descricao"));
		novaConta.setTipo(objContaJSON.getString("Tipo"));
		novaConta.setUsuario(usuarioLogado);
		
		if(new ContaDAO().persist(novaConta)) {
			
			ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
			msg.setDescricao("Nova conta cadastrada com sucesso!");
			msg.setSucesso(true);
			
			return msg.toJSON();
			
		} else {
			
			ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
			msg.setDescricao("Erro ao tentar gravar a nova conta!");
			msg.setErro(true);
			
			return msg.toJSON();
			
		}
				
	}
	
	/**
	 * Metodo responsável por obter e retornar todas as contas do
	 * usuário que está logado 
	 *
	 * @param session sessão so usuário que está logado
	 * @return List com todas as contas do usuário ou null caso não haja lista.
	 */
	@RequestMapping(value="Contas", method=RequestMethod.GET)
	@ResponseBody
	public String obterContas(HttpSession session) {
		
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		return new Gson().toJson(new ContaDAO().obterListaContasUsuario(usuarioLogado));
				
	}
	
	
	@RequestMapping(value="/Conta/{id}")
	@ResponseBody
	public String getConta(HttpSession session, @PathVariable("id") long id) {
	
		Conta conta = new ContaDAO().localizarPorId(id);
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		
		if(conta != null) {
		
			//Verificando se a conta solicitada pertência ao usuário que está logado
			if(conta.getUsuario().getId() == usuarioLogado.getId()) {
				
				return conta.toJSON();
				
			} else {
				
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				msg.setErro(true);
				msg.setDescricao("A contá solicitada, não pertece ao usuário logado, solicitação não autorizada!");
				
				return msg.toJSON();
			}
		
		} else {
			
			ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
			msg.setErro(true);
			msg.setDescricao("Não foi localizada nenhuma conta com esse Id no banco de dados.");
			
			return msg.toJSON();
		}
			
	}
	
	@RequestMapping(value="/Conta", method= RequestMethod.PUT)
	@ResponseBody
	public String updateConta(@RequestBody String contaJSONFrontEnd, HttpSession session) {
		
		JSONObject objJsonConta = new JSONObject(contaJSONFrontEnd);
		Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
		Conta contaUpdate = new Conta();			
				
		JSONObject objJsonUsuarioContaUpdate = new JSONObject(objJsonConta.get("Usuario").toString());		
		long IdUsuarioContaUpdate = Long.parseLong(objJsonUsuarioContaUpdate.get("Id").toString());
		
		if(IdUsuarioContaUpdate == usuarioLogado.getId()) {
			
			contaUpdate.setId(Long.parseLong(objJsonConta.get("Id").toString()));
			contaUpdate.setDescricao(objJsonConta.get("Descricao").toString());
			contaUpdate.setTipo(objJsonConta.get("Tipo").toString());
			contaUpdate.setUsuario(usuarioLogado);
						
			if(new ContaDAO().marge(contaUpdate)) {
				
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				msg.setSucesso(true);
				msg.setDescricao("Conta atualizada com sucesso!");
				
				return msg.toJSON();
											
			} else {
				
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				msg.setErro(true);
				msg.setDescricao("Ocorreu um erro ao tentar realizar o Update da conta no Banco de Dados!");
				
				return msg.toJSON();
								
			}
			
		} else {
			
			ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
			msg.setErro(true);
			msg.setDescricao("A conta que está tentando ser atualizada, não pertence ao usuário logado, operação não autorizada!");
			
			return msg.toJSON();
			
		}		
		
	}
	
	
}
