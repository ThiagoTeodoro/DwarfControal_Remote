package br.com.controleFinanceiro.model.entitys;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="tb_usuarios")
public class Usuarios {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false)
	private String nome;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	@Column(nullable=false)
	private String senha;
	
	@Column(nullable=false)
	private int nivel;
	
	@Column(nullable=false)
	private boolean ativo;
	
	@OneToMany(fetch=FetchType.LAZY)	
	private List<Contas> contas;
	
	@OneToMany(fetch=FetchType.LAZY)
	private List<Lancamentos> lancamentos;
	
	@OneToMany(fetch=FetchType.LAZY)
	private List<Dividas> dividas;
		
	//Getters and Setters
	
	public List<Lancamentos> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamentos> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Contas> getContas() {
		return contas;
	}

	public void setContas(List<Contas> contas) {
		this.contas = contas;
	}
	
	
	@Override
	public String toString() {
		return String.format("Id : [%s], Nome : [%s], Email : [%s], Senha : [%s], Nivel : [%s], Ativo : [%s] ", 
							 this.id,
							 this.nome,
							 this.email,
							 this.senha,
							 this.nivel,
							 this.ativo);
	}
}
