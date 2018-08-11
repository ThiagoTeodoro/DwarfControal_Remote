package br.com.entitys;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;


import com.google.gson.Gson;

/**
 * Classe entidade para Lançamentos
 * 
 * @author Thiago Teodoro
 *
 */
@Entity
@Table(name="tb_lancamentos")
@JsonPropertyOrder({
	"id",
	"data",
	"descricao",
	"Valor",
	"conta"
})
public class Lancamento {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty("id")
	private long id;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	@JsonProperty("data")
	private Calendar data;
	
	@Column(nullable=false)
	@JsonProperty("descricao")
	private String descricao;
	
	@Column(nullable=false, precision=10, scale=2)
	@JsonProperty("Valor")
	private float Valor;
	
	@ManyToOne
	@JsonProperty("conta")
	private Conta conta;
	
	@javax.persistence.Transient //Transiente é para dizer que o campo não faz parte do banco de dados
	private float saldo;

	//Getters and Setters
	
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

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public float getValor() {
		return Valor;
	}

	public void setValor(float valor) {
		Valor = valor;
	}	
	
	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	//Metodo que retorna 
	public String toJSON() {
		return new Gson().toJson(this);
	}
	
	
}