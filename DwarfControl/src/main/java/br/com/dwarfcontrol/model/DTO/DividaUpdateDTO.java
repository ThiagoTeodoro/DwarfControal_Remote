package br.com.dwarfcontrol.model.DTO;

import br.com.dwarfcontrol.model.entitys.QuandoPagar;
import br.com.dwarfcontrol.model.entitys.Usuario;
import br.com.dwarfcontrol.model.utilities.DateFunctions;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DividaUpdateDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("data")
    private String data;

    @JsonProperty("valor")
    private float valor;

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("usuario")
    private Usuario usuario;


    public String getDescricao() {
        return descricao;
    }

    public float getValor() {
        return valor;
    }

    public boolean isStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return "DividaUpdateDTO{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", data='" + data + '\'' +
                ", valor=" + valor +
                ", status=" + status +
                ", usuario=" + usuario +
                '}';
    }
}
