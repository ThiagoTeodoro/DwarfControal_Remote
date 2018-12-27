package br.com.controleFinanceiro.model.entitys;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="tb_contas")
public class Contas {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false)
	private String descricao;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Usuarios usuario;
	
	@OneToMany(fetch=FetchType.LAZY)
	private List<Lancamentos> lancamentos;
	
	@OneToMany(fetch=FetchType.LAZY)
	private List<Dividas> dividas;

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

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}
		
	public List<Dividas> getDividas() {
		return dividas;
	}

	public void setDividas(List<Dividas> dividas) {
		this.dividas = dividas;
	}

	@Override
	public String toString() {
		
		return String.format("Id : [%s], Descrição [%s], Usuarios [%s].",
							  this.id,
							  this.descricao,
							  this.usuario.getId()
							);
	}

}
