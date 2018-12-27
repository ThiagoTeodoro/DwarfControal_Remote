package br.com.controleFinanceiro.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"token",
	"tempoSegundos"
})
public class TokenDTO {
	
	@JsonProperty("token")
	private String token;
	
	@JsonProperty("tempoSegundos")
	private int tempoSegundos;
	
	//Getters and Setters

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getTempoSegundos() {
		return tempoSegundos;
	}

	public void setTempoSegundos(int tempoSegundos) {
		this.tempoSegundos = tempoSegundos;
	}

}
