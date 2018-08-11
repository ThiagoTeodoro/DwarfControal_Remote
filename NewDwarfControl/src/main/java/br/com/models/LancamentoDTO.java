package br.com.models;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import br.com.entitys.Conta;
import br.com.entitys.Lancamento;
import br.com.utilitarios.FuncoesData;

@JsonPropertyOrder({
	"id",
	"data",
	"descricao",
	"valor",
	"conta"
})
public class LancamentoDTO {
	
	@JsonProperty("id")
	private long id;
	
	@JsonProperty("data")
	private String data;
	
	@JsonProperty("descricao")
	private String descricao;
	
	@JsonProperty("valor")
	private Double valor;

	@JsonProperty("conta")
	private Conta conta;

	//Construtor Padrão
	public LancamentoDTO() {
		
	}
	
	//Getters and Setters
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("Data : [%s], Descrição : [%s], Valor : [%s], Conta : [%s]", this.data, this.descricao, this.valor, this.conta.toString());
	}
	
	/**
	 * Método responsável por converter LacamentoDTO em Lacamento
	 * 
	 * Se o editar for TRUE vamos preencher o ID
	 * @return
	 */
	public Lancamento toLancamento(boolean editar) {
		
		Lancamento lancamento = new Lancamento();
		FuncoesData funcoesData = new FuncoesData();
		
		lancamento.setConta(this.conta);
		lancamento.setDescricao(this.descricao);
		lancamento.setValor(Float.parseFloat(String.valueOf(this.valor)));
		lancamento.setData(funcoesData.dateToCalendar(funcoesData.stringToDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", this.data)));
		
		if(editar) {
			lancamento.setId(this.id);
		}
		
		return lancamento;
		
	}
	
}