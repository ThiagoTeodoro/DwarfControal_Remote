package br.com.controleFinanceiro.model.DTO;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.controleFinanceiro.model.entitys.Contas;

@JsonPropertyOrder({
	"id",
	"dataVencimento",
	"descricao",
	"valor",
	"lancarLancamento",
	"operacao"
})
public class DividaDTO {
	
	@JsonProperty("id")
	private int id;
	
	@JsonProperty("dataVencimento")
	private Calendar dataVencimento;
	
	@JsonProperty("descricao")
	private String descricao;
	
	@JsonProperty("valor")
	private Double valor;
	
	@JsonProperty("lancarLancamento")
	private Boolean lancarLancamento;

	@JsonProperty("operacao")
	private String operacao;
	
	@JsonProperty("conta")
	private Contas conta;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Calendar dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Boolean getLancarLancamento() {
		return lancarLancamento;
	}

	public void setLancarLancamento(Boolean lancarLancamento) {
		this.lancarLancamento = lancarLancamento;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public Contas getConta() {
		return conta;
	}

	public void setConta(Contas conta) {
		this.conta = conta;
	}

	@Override
	public String toString() {
		return "DividaDTO [id=" + id + ", dataVencimento=" + dataVencimento + ", descricao=" + descricao + ", valor="
				+ valor + ", lancarLancamento=" + lancarLancamento + ", operacao=" + operacao + ", conta=" + conta.toString()
				+ "]";
	}
	
	
	
}
