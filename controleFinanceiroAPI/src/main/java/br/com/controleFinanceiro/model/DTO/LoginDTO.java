package br.com.controleFinanceiro.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"email",
	"senha"
})
public class LoginDTO {
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("senha")
	private String senha;

	//Getters and Setters
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
