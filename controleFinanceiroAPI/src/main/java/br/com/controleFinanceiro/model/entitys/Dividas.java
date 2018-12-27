package br.com.controleFinanceiro.model.entitys;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.controleFinanceiro.model.DTO.DividaDTO;
import br.com.datemanipulator.DateManipulatorImpl;

@Entity
@Table(name="tb_dividas")
public class Dividas {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Calendar dataVencimento;
	
	@Column(nullable=false)
	private String descricao;
	
	@Column(nullable=false)
	private Double valor;
	
	@Column(nullable=false)
	private boolean liquidado;
	
	@Transient
	private boolean vencido;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Contas conta;
	
	@OneToOne
	private Lancamentos lancamento;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Usuarios usuario;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Calendar dataVencimento) {
		this.dataVencimento = dataVencimento;
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

	public Contas getConta() {
		return conta;
	}

	public void setConta(Contas conta) {
		this.conta = conta;
	}

	public Lancamentos getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamentos lancamento) {
		this.lancamento = lancamento;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}

	public boolean isLiquidado() {
		return liquidado;
	}

	public void setLiquidado(boolean liquidado) {
		this.liquidado = liquidado;
	}		

	public boolean isVencido() {
		return vencido;
	}

	public void setVencido(boolean vencido) {
		this.vencido = vencido;
	}

	@Override
	public String toString() {
		return "Dividas [id=" + id + ", dataVencimento=" + new DateManipulatorImpl().dateToString(this.dataVencimento.getTime(), "dd/MM/yyyy") + ", descricao=" + descricao + ", valor="
				+ valor + ", conta=" + conta + ", lancamento=" + lancamento + ", usuario=" + usuario + "]";
	}
	
	
	/**
	 * Método responsável por realizar o preenchimento de um
	 * objeto Divida, apartir de um Objeto dividaDTO
	 */
	public void fillFromDividasDTO(DividaDTO dividaDTO) {
		
		this.descricao = dividaDTO.getDescricao();
		this.conta = dividaDTO.getConta();
		this.dataVencimento = dividaDTO.getDataVencimento();
		this.valor = dividaDTO.getValor();		
		
	}
	
	
}
