package br.com.controleFinanceiro.model.entitys;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.controleFinanceiro.commons_methods.datemanipulator.DateManipulatorImpl;




@Entity
@Table(name="tb_lancamentos")
public class Lancamentos {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false)
	private String descricao;
	
	@Column(nullable=false)
	private Double valor;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Calendar data;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Contas conta;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Usuarios usuario;

	//Getters and Setters
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public Contas getConta() {
		return conta;
	}

	public void setConta(Contas conta) {
		this.conta = conta;
	}
	
	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}	

	@Override
	public String toString() {
		
		return String.format("Id : [%s], Descrição : [%s], Valor : [%s], Data : [%s], Conta : [%s], Usuário : [%s]",
							  this.id,
							  this.descricao,
							  this.valor,
							  new DateManipulatorImpl().dateToString(this.data.getTime(), "dd/MM/yyyy"),
							  this.conta.toString(),
							  this.usuario.getEmail()							  
							  );
		
	}

	
	
	
}
