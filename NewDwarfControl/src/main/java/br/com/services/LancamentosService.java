package br.com.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import br.com.dao.LancamentoDAO;
import br.com.entitys.Lancamento;
import br.com.entitys.Usuario;
import br.com.models.RequestLancamentosDTO;
import br.com.utilitarios.FuncoesData;

/***
 * Classe responsável por prover os serviços relacionados
 * a lançamentos.
 * 
 * @author Thiago Teodoro
 *
 */
@Service
public class LancamentosService {
		
	private LancamentoDAO lancamentoDAO = new LancamentoDAO();	
	private FuncoesData funcoesData = new FuncoesData();
	
	/**
	 * Método responsável por obter os lançamentos
	 * de um determinado usuário, por periodo e
	 * conta.
	 * 
	 * @return Lista de lançamentos obtidos conforme solicitação.
	 */
	public List<Lancamento> obterLancamentos(RequestLancamentosDTO requestLancamentos){
		
		long idConta;
		String dataInicial;
		String dataFinal;
		
		idConta =  requestLancamentos.getConta().getId();
		dataInicial = funcoesData.converterDataString(requestLancamentos.getDataInicial(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd");
		dataFinal = funcoesData.converterDataString(requestLancamentos.getDataFinal(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd");
		
		List<Lancamento> lista = lancamentoDAO.selecionarPorContaDataInicialEFinal(idConta, dataInicial, dataFinal);
		
		if(lista.size() != 0) {
			/**
			 * For responsável por preencher os saldos unitários da pesquisa.
			 * 
			 * Veja bem se for o primeiro elemento da lista, então saldo 
			 * vai receber exatamente o valor do primeiro lançamento
			 * 
			 * Caso não seja, saldo recebe o valor do saldo anterior
			 * mais o valor do lançamento atual.
			 * 
			 * Essa é a logica implementada.
			 */
			for(int i = 0; i < lista.size(); i++) {
				
				if(i == 0) {
					lista.get(i).setSaldo(this.getSaldoAnterior(requestLancamentos) + lista.get(i).getValor());
				} else {
					lista.get(i).setSaldo(lista.get(i - 1).getSaldo() + lista.get(i).getValor());
				}
				
			}
			
			return lista;
		
		} else {
			
			return new ArrayList<Lancamento>();
		}
				
	}
	
	
	/**
	 * Método responsavel por obter o saldo anterior de um periodo solicitado.
	 * 
	 * @param requestLancamentos
	 * @return
	 */
	public float getSaldoAnterior(RequestLancamentosDTO requestLancamentos) {
		
		long idConta;
		String dataInicial;
		
		idConta =  requestLancamentos.getConta().getId();
		dataInicial = funcoesData.converterDataString(requestLancamentos.getDataInicial(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd");
		
		Float retorno = lancamentoDAO.sumSaldoAnterior(idConta, dataInicial);
		
		if(retorno == null) {
			return 0;
		} else {
			return retorno;
		}
	}
	

	/**
	 * Método responsável por devolver o saldo total até o ultimo dia do 
	 * periodo enviado na Request.
	 * 
	 * @param requestLancamentos
	 * @return saldoTotal
	 */
	public float obterSaldoTotalAteFimPeriodo(RequestLancamentosDTO requestLancamentos) {
				
		FuncoesData funcoesData = new FuncoesData();
		int idConta = Integer.parseInt(String.valueOf(requestLancamentos.getConta().getId()));
		String dataFinal = funcoesData.converterDataString(requestLancamentos.getDataFinal(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd");
				
		float saldoTotal = lancamentoDAO.somarSaldo(idConta, dataFinal);
		
		return saldoTotal;
	}
	
	/***
	 * Método responsável por receber um lançamento, verificar se esse lançamento
	 * pertece ao usuário loagdo, e se pertencer excluir o mesmo.
	 * 
	 * @param id
	 * @return
	 */
	public Boolean excluirLancamento(int id, HttpSession session) {
		
		Boolean statusOperacao = Boolean.FALSE;
		
		Lancamento lancamento = lancamentoDAO.localizarPorId(Long.parseLong(String.valueOf(id)));
		
		if(lancamento != null) {
			
			//Verificando se o lançamento pertece ao usuário logado.
			Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

			if(lancamento.getConta().getUsuario().getId() == usuarioLogado.getId()) {
				
				//Podemos Excluir
				boolean statusDelete = lancamentoDAO.remove(lancamento);
				statusOperacao = statusDelete;
				
			}
			
		}
		
		return statusOperacao;
	}
	
	
	/**
	 * Método responsável por cadastrar um novo lançamento do banco de dados.
	 * 
	 * @param lancamento
	 * @return
	 */
	public Boolean cadastrarLancamento(Lancamento lancamento) {
		
		Boolean statusOperacao = Boolean.FALSE;
		
		//Realizando Cadastro no Banco de Dados
		statusOperacao = lancamentoDAO.persist(lancamento);
		
		return statusOperacao;
		
	}
	
	/**
	 * Método responsável por obter um lançamento no banco de dados baseado no seu ID
	 * e se esse lançamento é do usuário que está logado.
	 * 
	 * @param idLancamento
	 * @param session
	 * @return
	 */
	public Lancamento getLancamento(int idLancamento, HttpSession session) {
		
		Lancamento lancamento = lancamentoDAO.localizarPorId(Long.parseLong(String.valueOf(idLancamento)));
		
		if(lancamento != null) {
			
			//Verificando se o lançamento pertece ao usuário logado.
			Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");

			if(lancamento.getConta().getUsuario().getId() != usuarioLogado.getId()) {
			
				//Não podemos devolver esse lançamento pois não pertece ao usuário solicitente(logado)
				lancamento = null;
				
			} else {
				
				//Não podemos devolver a senha do usuário por tanto vou limpar
				lancamento.getConta().getUsuario().setSenha("");
				
			}
			
		}
		
		return lancamento;
		
	}
	
	/**
	 * Método responsável por atualizar o lançamento do banco de dados.
	 * 
	 * @param lancamento
	 * @return
	 */
	public Boolean updateLancamento(Lancamento lancamento) {
		
		return lancamentoDAO.marge(lancamento);
		
	}
}