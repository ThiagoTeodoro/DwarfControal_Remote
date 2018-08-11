package br.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.models.RequestRelatorioReceitaDespesaDTO;
import br.com.models.ResponseRelatorioReceitaDespesaVO;
import br.com.services.RelatorioService;

@RestController
@RequestMapping("/Relatorios")
public class RelatoriosController {
	
	private static RelatorioService relatorioService = new RelatorioService();

	/***
	 * Método responsável por expor serviço de dados para o relátorio de gráfico
	 * de barras de receitas e despesas por conta e ano
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/RelatorioReceitaDespesaAno", method=RequestMethod.POST)
	private List<ResponseRelatorioReceitaDespesaVO> obterRelatorioDespesaAno(@RequestBody RequestRelatorioReceitaDespesaDTO request){
		
		//Realizando Chamda ao método competente
		return relatorioService.relatorioReceitaDespesaAnualPorConta(request);
		
	}
	
}