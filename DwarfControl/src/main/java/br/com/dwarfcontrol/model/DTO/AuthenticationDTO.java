package br.com.dwarfcontrol.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "token",
        "tempoExpiracao"
})
public class AuthenticationDTO {

    @JsonProperty("token")
    private String token;

    @JsonProperty("tempoExpiracao")
    private int tempoExpiraco;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTempoExpiraco() {
        return tempoExpiraco;
    }

    public void setTempoExpiraco(int tempoExpiraco) {
        this.tempoExpiraco = tempoExpiraco;
    }

    @Override
    public String toString() {
        return "AuthenticationDTO{" +
                "token='" + token + '\'' +
                ", tempoExpiraco=" + tempoExpiraco +
                '}';
    }
}
