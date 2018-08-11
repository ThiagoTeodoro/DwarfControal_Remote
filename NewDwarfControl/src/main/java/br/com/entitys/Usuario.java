package br.com.entitys;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/*
 * Classe Entidade Usuário
 * 
 */

@Entity
@Table(name = "tb_usuarios")
@JsonPropertyOrder({
	"Id",
	"Email",
	"Senha",
	"Nome",
	"Nivel",
	"Data_Hora_Criacao"
})
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Incremento no Banco
	@JsonProperty("Id")
	private Long Id;
	
	@Column(nullable=false, unique=true)
	@JsonProperty("Email")
	private String Email;
	
	@Column(nullable=false)
	@JsonProperty("Senha")
	private String Senha;
	
	@Column(nullable=false)
	@JsonProperty("Nome")
	private String Nome;
	
	@Column(nullable=false)
	@JsonProperty("Nivel")
	private String Nivel;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	@JsonProperty("Data_Hora_Criacao")
	private Calendar DataHora_Criacao;	
	
	/**
	 * Construtor da Classe
	 */
	public Usuario() {

	
	}

	/*
	 * Getters Setters
	 */
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getSenha() {
		return Senha;
	}

	public void setSenha(String senha) {
		Senha = senha;
	}

	public String getNivel() {
		return Nivel;
	}

	public void setNivel(String nivel) {
		Nivel = nivel;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public Calendar getDataHora_Criacao() {
		return DataHora_Criacao;
	}

	public void setDataHora_Criacao(Calendar dataHora_Criacao) {
		DataHora_Criacao = dataHora_Criacao;
	}
	
	@Override
	public String toString(){
		
		SimpleDateFormat Formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		return "Id : [" + this.Id + "] Email : [" + this.Email + "] "
				+ "Senha : [" + this.Senha + "] Nome : [" + this.Nome + 
				"] Nivel : [" + this.Nivel + "] Data Hora Criação : [" +
				Formatador.format(this.DataHora_Criacao.getTime()) + "]";
		
	}
	
	
	
}
