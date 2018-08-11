package br.com.models;

import java.util.ArrayList;
import java.util.List;

import br.com.dao.LancamentoDAO;

public class ResponseRelatorioReceitaDespesaVO {
	
	private int numeroMes;
	private String nomeMes;
	private Double valorDespesa;
	private Double valorReceita;
	
	public ResponseRelatorioReceitaDespesaVO() {
		// TODO Auto-generated constructor stub
	};
	
	//Construtor usando campos.
	public ResponseRelatorioReceitaDespesaVO(int numeroMes, String nomeMes, Double valorDespesa, Double valorReceita) {
		
		this.numeroMes = numeroMes;
		this.nomeMes = nomeMes;
		this.valorDespesa = valorDespesa;
		this.valorReceita = valorReceita;
		
	}


	/**
	 * Método responsável por devolver um list<ResponseRelatorioReceitaDespesaVO>
	 * inicializado com os numeros e nomes dos meses.
	 * 
	 * @return
	 */
	private List<ResponseRelatorioReceitaDespesaVO> inicializarListComMesesAno(){
		
		List<ResponseRelatorioReceitaDespesaVO> listaComMesesAno = new ArrayList<ResponseRelatorioReceitaDespesaVO>();
		
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(1, "Janeiro", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(2, "Fevereiro", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(3, "Março", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(4, "Abril", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(5, "Maio", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(6, "Junho", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(7, "Julho", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(8, "Agosto", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(9, "Setembro", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(10, "Outubro", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(11, "Novembro", 0.00, 0.00));
		listaComMesesAno.add(new ResponseRelatorioReceitaDespesaVO(12, "Dezembro", 0.00, 0.00));
		
		return listaComMesesAno;
	
	}
	
	//Método abstraido para alimentação das despesas
	private void alimentarReceitaDespesa(List<TotalPorMesDTO> receitaOrdespesa, List<ResponseRelatorioReceitaDespesaVO> listaAlimentar,boolean receita) {
		
		//Alimentando Dados da Lista Esperada pela VIEW 
		for(int i = 0; i < receitaOrdespesa.size(); i++) {
			
			for(int t = 0; t < listaAlimentar.size(); t++) {
				
				if(receitaOrdespesa.get(i).getMes() == listaAlimentar.get(t).numeroMes) {
					
					//Verificando se se trata de Receita ou Despesa
					if(receita == true) {
						listaAlimentar.get(t).valorReceita = receitaOrdespesa.get(i).getTotalMes();
					}else {
						listaAlimentar.get(t).valorDespesa = receitaOrdespesa.get(i).getTotalMes();
					}					
					
				}
				
			}
			
		}
				
	}
	
	
	/**
	 * Método responsável por entregar uma lista de Receitas e Depesas conforme ano e conta
	 * no padão de objeto exigido pela view do Angular implementda
	 * 
	 * @param idConta id da conta que será calculado a receita e despesa do ano agrupados por mês
	 * @param ano ano que será calculado a receita e despesa agrupados por mês. Pattern "yyyy"
	 * @return
	 */
	public List<ResponseRelatorioReceitaDespesaVO> obterResponseRelatorioReceitaDespesaVO(int idConta, int ano){
		
		List<ResponseRelatorioReceitaDespesaVO> retorno = this.inicializarListComMesesAno();
		
		//Obtendo Receitas e Despesas da Conta e Ano Eviados
		LancamentoDAO lancamentoDAO = new LancamentoDAO();
		List<TotalPorMesDTO> receitasDoAnoPorMes = lancamentoDAO.obterReceitasAnoPorMes(idConta, ano);
		List<TotalPorMesDTO> despesasDoAnoPorMes = lancamentoDAO.obterDespesasAnoPorMes(idConta, ano);
		
		//Alientando retorno conforme os meses encontrados na consulta para despesa e receitas
		alimentarReceitaDespesa(receitasDoAnoPorMes, retorno, true);
		alimentarReceitaDespesa(despesasDoAnoPorMes, retorno, false);
		
		return retorno;
	}
	
	
	@Override
	public String toString() {
		return String.format("Numero do mês : [%s], Nome do mês [%s], Valor Despesa : [%s], Valor Receita [%s].",
								numeroMes,
								nomeMes,
								valorDespesa,
								valorReceita
							);
	}

}
