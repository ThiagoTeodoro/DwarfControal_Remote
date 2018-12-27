package br.com.controleFinanceiro.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class limiteDTO {
	
	@JsonProperty("valor")
	private double valor;

	public double getValor() {
		return valor;
	}
	
	

}
