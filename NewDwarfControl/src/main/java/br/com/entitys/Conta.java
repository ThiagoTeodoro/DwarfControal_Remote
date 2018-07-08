package br.com.entitys;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.gson.Gson;

@Entity
@Table(name="tb_contas")
public class Conta {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long Id;
	
	@Column(nullable=false, columnDefinition="TEXT")
	private String Descricao;
	
	@Column(nullable=false)
	private String Tipo;
	
	//Campo que armazena a qual usuário pertence essa conta.
	@OneToOne
	private Usuario Usuario;
	
	@OneToMany(fetch = FetchType.EAGER) //Esse fecth=FechType.EAGER é obrigatório (Poderia ser outros tipos, mas esse funciona melhor) para esse tipo de realacionamento
	private List<Lancamento> Lancamentos;
	
	//Getters and Setters

	public List<Lancamento> getLancamentos() {
		return Lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		Lancamentos = lancamentos;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getDescricao() {
		return Descricao;
	}

	public void setDescricao(String descricao) {
		Descricao = descricao;
	}

	public String getTipo() {
		return Tipo;
	}

	public void setTipo(String tipo) {
		Tipo = tipo;
	}

	public Usuario getUsuario() {
		return Usuario;
	}

	public void setUsuario(Usuario usuario) {
		Usuario = usuario;
	}
	
	//Metodo responsável por devolver este objeto no formato de Notação JSON
	public String toJSON() {
		return new Gson().toJson(this);
	}
	
}
