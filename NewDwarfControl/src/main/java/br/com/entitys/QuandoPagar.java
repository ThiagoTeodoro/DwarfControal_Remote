package br.com.entitys;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.Gson;

@Entity
@Table(name="tb_quandopagar")
public class QuandoPagar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@OneToOne
	private Usuario Usuario;
	
	@Column(nullable=false, length=500)
	private String Descricao;
	
	@Column(nullable=false, precision=10, scale=2)
	private float Valor;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Calendar Data;
	
	@Column(nullable=false)
	private boolean Status;

	//Getters and Setters
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Usuario getUsuario() {
		return Usuario;
	}

	public void setUsuario(Usuario usuario) {
		Usuario = usuario;
	}

	public String getDescricao() {
		return Descricao;
	}

	public void setDescricao(String descricao) {
		Descricao = descricao;
	}

	public float getValor() {
		return Valor;
	}

	public void setValor(float valor) {
		Valor = valor;
	}

	public Calendar getData() {
		return Data;
	}

	public void setData(Calendar data) {
		Data = data;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}
	
	//Metodo responsável por retornar a notação String JSON deste objeto. 
	public String toJSON() {
		return new Gson().toJson(this);
	}
	
}
