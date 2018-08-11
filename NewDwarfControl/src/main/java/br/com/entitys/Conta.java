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

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.google.gson.Gson;

@Entity
@Table(name="tb_contas")
@JsonPropertyOrder({
	"Id",
	"Descricao",
	"Tipo",
	"Usuario",
	"Lancamentos"
})
public class Conta {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty("Id")
	private Long Id;
	
	@Column(nullable=false, columnDefinition="TEXT")
	@JsonProperty("Descricao")
	private String Descricao;
	
	@Column(nullable=false)
	@JsonProperty("Tipo")
	private String Tipo;
	
	//Campo que armazena a qual usuário pertence essa conta.
	@OneToOne
	@JsonProperty("Usuario")
	private Usuario Usuario;
	
	@OneToMany(fetch = FetchType.EAGER) //Esse fecth=FechType.EAGER é obrigatório (Poderia ser outros tipos, mas esse funciona melhor) para esse tipo de realacionamento
	@JsonProperty("Lancamentos")
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
	
	@Override
	public String toString() {
		return String.format("Id : [%s], Descrição [%s], Tipo : [%s], Usuário : [%s], Lancamento (Array com [%s] dados).",
							 this.Id,
							 this.Descricao,
							 this.Tipo,
							 this.Usuario.getNome(),
							 this.Lancamentos.size());
	}
	
}
