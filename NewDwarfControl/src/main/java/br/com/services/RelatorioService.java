package br.com.services;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.models.RequestRelatorioReceitaDespesaDTO;
import br.com.models.ResponseRelatorioReceitaDespesaVO;

@Service
public class RelatorioService {

	/**
	 * Método responsável por chamar os métodos responsáveis por contruir a respose
	 * para o relatório de receita e despesa por ano e conta.
	 * 
	 * @param request
	 * @return
	 */
	public List<ResponseRelatorioReceitaDespesaVO> relatorioReceitaDespesaAnualPorConta(RequestRelatorioReceitaDespesaDTO request){
		
		ResponseRelatorioReceitaDespesaVO relatorioReceitaDespesa = new ResponseRelatorioReceitaDespesaVO();
		int ano = Integer.parseInt(request.getAno());
		int idConta = Integer.parseInt(String.valueOf(request.getConta().getId()));
	
		List<ResponseRelatorioReceitaDespesaVO> retorno = relatorioReceitaDespesa.obterResponseRelatorioReceitaDespesaVO(idConta, ano);

		
		return retorno;
		
	}
	
}