package br.com.dwarfcontrol.model.DTO;

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

    @Override
    public String toString() {
        return "LoginDTO{" +
                "email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }


    /***
     * Método que valida os campos obrigatórios deste DTO
     * @return
     */
    public Boolean validationFields(){

        if(this.email == null || this.email.equals("") ||
           this.senha == null || this.senha.equals("")
           ){

            return Boolean.FALSE;

        } else {

            return Boolean.TRUE;

        }

    }
}
