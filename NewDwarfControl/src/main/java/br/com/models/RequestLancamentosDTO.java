package br.com.models;


import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import br.com.entitys.Conta;
@JsonPropertyOrder({
	"conta",
	"dataInicial",
	"dataFinal"
})
public class RequestLancamentosDTO {

	@JsonProperty("conta")
	private Conta conta;
	
	@JsonProperty("dataInicial")
	private String dataInicial;
	
	@JsonProperty("dataFinal")
	private String dataFinal;

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	@Override
	public String toString() {
		return String.format("Conta : [%s], Data Inicial : [%s], Data Final : [%s]", 
							 this.conta,
							 this.dataInicial,
							 this.dataFinal);
	}
}
