package br.com.controleFinanceiro.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class liquidarDTO {
	
	@JsonProperty("contaOrigemDinheiro")
	private int contaOrigemDinheiro;
	
	@JsonProperty("idDivida")
	private int idDivida;

	public int getContaOrigemDinheiro() {
		return contaOrigemDinheiro;
	}

	public void setContaOrigemDinheiro(int contaOrigemDinheiro) {
		this.contaOrigemDinheiro = contaOrigemDinheiro;
	}

	public int getIdDivida() {
		return idDivida;
	}

	public void setIdDivida(int idDivida) {
		this.idDivida = idDivida;
	}
		

}
