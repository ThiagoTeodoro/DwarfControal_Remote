package br.com.controleFinanceiro.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LancamentoDTO {

	@JsonProperty("descricao")
	private String descricao;

	@JsonProperty("valor")
	private Double valor;

	@JsonProperty("data")
	private String data;

	@JsonProperty("id_conta")
	private int id_conta;
	
	@JsonProperty("operacao")
	private String operacao;
	
	@JsonProperty("idDivida")
	private int idDivida;

	// Getters and Setters

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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getId_conta() {
		return id_conta;
	}

	public void setId_conta(int id_conta) {
		this.id_conta = id_conta;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}	
	
	public int getIdDivida() {
		return idDivida;
	}

	public void setIdDivida(int idDivida) {
		this.idDivida = idDivida;
	}

	@Override
	public String toString() {
		
		return String.format("Conta : [%s], Descrição [%s], Data [%s], Valor [%s], Operação [%s], idDivida [%s].",
							  this.id_conta,
							  this.descricao,
							  this.data,
							  this.valor,
							  this.operacao,
							  this.idDivida);
		
	}

}
