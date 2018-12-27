package br.com.controleFinanceiro.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"senhaAntiga",
	"novaSenha",
	"confirmaSenha"
})
public class ChangeSenhaDTO {
	
	@JsonProperty("senhaAntiga")
	private String senhaAntiga;
	
	@JsonProperty("novaSenha")
	private String novaSenha;
	
	@JsonProperty("confirmaSenha")
	private String confirmaSenha;

	//Getters and Setters
	
	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}
	
	@Override
	public String toString() {
		
		return String.format(
							  "Senha Antiga [%s], Nova Senha [%s], Confirmacao Nova Senha [%s]", 
							  this.senhaAntiga,
							  this.novaSenha,
							  this.confirmaSenha
							 ); 
		
	}

}
