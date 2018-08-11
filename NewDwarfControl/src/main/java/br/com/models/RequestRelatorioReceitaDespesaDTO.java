package br.com.models;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import br.com.entitys.Conta;

@JsonPropertyOrder({
	"conta",
	"ano"
})
public class RequestRelatorioReceitaDespesaDTO {
	
	@JsonProperty("conta")
	private Conta conta;
	
	@JsonProperty("ano")
	private String ano;
	
	//Construtor Vazio
	public RequestRelatorioReceitaDespesaDTO() {
		
	}

	//Getters and Setters
	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}	
	
	@Override
	public String toString() {
		return String.format("Conta enviada : [%s], Ano enviado : [%s]", this.conta.getId(), this.ano);
	}

}
