package br.com.dwarfcontrol.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeSenhaDTO {

    @JsonProperty("senhaAntiga")
    private String senhaAntiga;

    @JsonProperty("senhaNova")
    private String senhaNova;

    @JsonProperty("senhaRepeti")
    private String senhaRepeti;

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public String getSenhaRepeti() {
        return senhaRepeti;
    }
}
