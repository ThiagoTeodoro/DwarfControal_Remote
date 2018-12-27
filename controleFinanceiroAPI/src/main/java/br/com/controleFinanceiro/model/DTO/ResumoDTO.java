package br.com.controleFinanceiro.model.DTO;

public class ResumoDTO {
	
	private String descricaoConta;
	private double valor;
	
	
	public String getDescricaoConta() {
		return descricaoConta;
	}
	
	public void setDescricaoConta(String descricaoConta) {
		this.descricaoConta = descricaoConta;
	}
	
	public double getValor() {
		return valor;
	}
	
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	
	@Override
	public String toString() {
		return String.format("Descrição : [%s], Valor : [%s].", this.descricaoConta, this.valor);
	}
}
