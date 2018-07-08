package br.com.objetos;

import com.google.gson.Gson;

/**
 * Objeto responsável por padronizar as mensagens de retorno ao Front-End
 * 
 * 
 * @author Thiago Teodoro
 *
 */
public class ObjetoMenssagemFrontEnd {

	private String descricao;
	private boolean sucesso;
	private boolean erro;
	private Exception excessao;
	
	
	public ObjetoMenssagemFrontEnd() {
		
		//Inicializnado Valores
		this.descricao = "";
		this.sucesso = false;
		this.erro = false;
		this.excessao = null;
		
	}
	
	
	//Getters and Setters
	
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public boolean isSucesso() {
		return sucesso;
	}
	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}
	public boolean isErro() {
		return erro;
	}
	public void setErro(boolean erro) {
		this.erro = erro;
	}
	public Exception getExcessao() {
		return excessao;
	}
	public void setExcessao(Exception excessao) {
		this.excessao = excessao;
	}
	
	
	// Metodo que retorna a notação JSON do Objeto em uma String. 
	public String toJSON() {
		return new Gson().toJson(this);
	}
	
}
