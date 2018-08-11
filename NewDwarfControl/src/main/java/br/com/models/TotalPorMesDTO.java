package br.com.models;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({
	"mes",
	"totalMes"
})
public class TotalPorMesDTO {
	
	@JsonProperty("mes")
	private int mes;
	
	@JsonProperty("totalMes")
	private double totalMes;
	
	/**
	 * Construtor vazio.
	 */
	public TotalPorMesDTO() {
		
	}
	
	/**
	 * Construtor por Item
	 */
	public TotalPorMesDTO(int mes, double totalMes) {
		super();
		this.mes = mes;
		this.totalMes = totalMes;
	}

	//Getters and Setters
	
	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public double getTotalMes() {
		return totalMes;
	}

	public void setTotalMes(double totalMes) {
		this.totalMes = totalMes;
	}	

	@Override
	public String toString() {
		return String.format("Mês : [%s], Total do Mês : [%s].", 
							  this.mes,
							  this.totalMes
							);
	}
}
