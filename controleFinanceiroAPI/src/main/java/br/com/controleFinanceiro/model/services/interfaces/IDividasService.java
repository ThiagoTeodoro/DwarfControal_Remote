package br.com.controleFinanceiro.model.services.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;

import br.com.controleFinanceiro.model.DTO.DividaDTO;
import br.com.controleFinanceiro.model.entitys.Dividas;

public interface IDividasService {
	
	/**
	 * Método que cadastra dividas no banco de dados.
	 * 
	 * Se a propriedade lancarLancamento estiver marcada 
	 * como true, nós vamos chamar o cadastro de lançamentos
	 * passar os dados gerar o lançamento conforme operação
	 * e atualizar os campos de referencia da divida com o 
	 * id do lançamento de do lançamento com o id da divida.
	 * 
	 * @param divida
	 * @return
	 */
	Dividas cadastrarDivida(DividaDTO divida, HttpServletRequest request);
	
	
	/**
	 * Serviços para selecionar todas as dividas de um determinado ano e mes
	 * cadastradas de um determinado usuário
	 * logado
	 * 
	 * @param request
	 * @return
	 */
	List<Dividas> getDividas(String anoMes, HttpServletRequest request);
	
	
	/**
	 * Serviço para realizar a liquidação da divida.
	 * 
	 * Se a divida tiver um lançamento de espelho (Despesa)
	 * iremos realizar um lançamento de receita na mesma conta com 
	 * a descrição [Liquidação] + Descrição do Lançamento.
	 * Em seguida vamos no idConta enviado que é o idConta origem 
	 * do dinheiro, e vamos gerar uma despeça com a mesma descrição
	 * para diminiuir assim a conta que deu horigem a esse pagamento.
	 * 
	 * @param idContaOrigem
	 * @param idDividad
	 * @return
	 */
	boolean liquidarDivida(int idContaOrigem, int idDivida, HttpServletRequest request);
	
	/**
	 * Serviço para exclusão de dividas. 
	 * 
	 * O serviço exclui dividas que pertença ao mesmo usuário logado(Token) 
	 * e caso a divida possua um lançamento vinculado esse lançamento também
	 * é excluido.
	 * 
	 * @param idDivida
	 * @param request
	 * @return
	 */
	boolean excluirDivida(int idDivida, HttpServletRequest request);
	
	/**
	 * Serviço para realizar o update de uma divida, caso o usuário 
	 * da divida seja o mesmo logado (Token), se edisitir um lançamento
	 * de referência o mesmo também será atualizado.
	 * 
	 * @return
	 */
	boolean updateDivida(Dividas divida, HttpServletRequest request);
	
	/**
	 * Serviço para realizar o somatório das dividas de um mesAno filtrando
	 * por usuário.
	 * 
	 * @param mes
	 * @param ano
	 * @param request
	 * @return
	 */
	double getSomatorioMes(String mes, String ano, HttpServletRequest request);

}
