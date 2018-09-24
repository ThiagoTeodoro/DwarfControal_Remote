package br.com.dwarfcontrol.model.DTO;

import br.com.dwarfcontrol.model.entitys.QuandoPagar;
import br.com.dwarfcontrol.model.entitys.Usuario;
import br.com.dwarfcontrol.model.utilities.DateFunctions;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class DividaDTO {

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("dataVencimento")
    private String dataVencimento;

    @JsonProperty("valor")
    private float valor;

    @JsonProperty("status")
    private boolean status;


    public String getDescricao() {
        return descricao;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public float getValor() {
        return valor;
    }

    public boolean isStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "DividaDTO{" +
                "descricao='" + descricao + '\'' +
                ", dataVencimento='" + dataVencimento + '\'' +
                ", valor=" + valor +
                ", status=" + status +
                '}';
    }


    /**
     * Método responsável por converter a DividaDTO em um QuandoPagar.
     *
     * @return
     */
    public QuandoPagar toQuandoPagar(Usuario usuario){

        QuandoPagar quandoPagar = new QuandoPagar();

        quandoPagar.setDescricao(this.descricao);
        quandoPagar.setStatus(this.status);
        quandoPagar.setValor(this.valor);
        quandoPagar.setData(new DateFunctions().dateToCalendar(new DateFunctions().convertDateStringToDate(this.dataVencimento, "yyyy-MM-dd")));
        quandoPagar.setUsuario(usuario);

        return quandoPagar;

    }
}
