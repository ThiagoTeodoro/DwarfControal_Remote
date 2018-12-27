package br.com.controleFinanceiro.model.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.controleFinanceiro.commons_methods.datemanipulator.DateManipulatorImpl;
import br.com.controleFinanceiro.commons_methods.datemanipulator.DatePatterns;
import br.com.controleFinanceiro.model.DAOs.CustomLancamentosDAO;
import br.com.controleFinanceiro.model.DAOs.interfaces.IContasDAO;
import br.com.controleFinanceiro.model.DAOs.interfaces.IDividasDAO;
import br.com.controleFinanceiro.model.DAOs.interfaces.ILancamentoDAO;
import br.com.controleFinanceiro.model.DTO.LancamentoDTO;
import br.com.controleFinanceiro.model.DTO.ResumoDTO;
import br.com.controleFinanceiro.model.entitys.Contas;
import br.com.controleFinanceiro.model.entitys.Dividas;
import br.com.controleFinanceiro.model.entitys.Lancamentos;
import br.com.controleFinanceiro.model.entitys.Usuarios;
import br.com.controleFinanceiro.model.services.interfaces.ILancamentosService;


@Service
@Transactional
public class LancamentosService implements ILancamentosService{
	
	@Autowired
	private IContasDAO contasDAO;
	
	@Autowired
	private ILancamentoDAO lancamentoDAO;
	
	@Autowired
	private CustomLancamentosDAO customLancamentoDAO;
	
	@Autowired
	private AutenticationService autenticationService;
	
	@Autowired
	private IDividasDAO dividaDAO;
	
	
	//Serviço de Log
	private Logger logger = LoggerFactory.getLogger(LancamentosService.class);

	/**
	 * Serviço para gravação de novos lançamentos no Banco de Dados.
	 * 
	 * @param lancamentoDTO
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Lancamentos> novoLancamento(LancamentoDTO lancamentoDTO, HttpServletRequest request) {
		
		//Selecionando Conta para gravar junto ao lançamento
		Contas conta = this.contasDAO.findById(lancamentoDTO.getId_conta()).get();
		Usuarios usuarioRequesicao = this.autenticationService.getUsuarioRequisicao(request);
		
		if(conta != null) {
			
			//Verificando se essa conta pertence ao usuário que está logado(TOKEN)
			if(conta.getUsuario().getId() == usuarioRequesicao.getId()) {
				
				//Gerando Lançamento para gravação
				Lancamentos novoLancamento = new Lancamentos();
				novoLancamento.setConta(conta);			
				novoLancamento.setDescricao(lancamentoDTO.getDescricao());
				novoLancamento.setUsuario(usuarioRequesicao);
				
				Calendar dataLancamento = Calendar.getInstance();
				dataLancamento.setTime(new DateManipulatorImpl().stringToDate(lancamentoDTO.getData(), DatePatterns.SQL));
				novoLancamento.setData(dataLancamento);
				
				//Verificando qual o tipo de lançamento
				if(lancamentoDTO.getOperacao().equals("RECEITA")) {
					
					novoLancamento.setValor(lancamentoDTO.getValor());
					
				}
				
				if(lancamentoDTO.getOperacao().equals("DESPESA")) {
					
					novoLancamento.setValor((lancamentoDTO.getValor() * -1));
					
				}
		
				//Tetando inserir Lançamento no Banco de dados.
				Lancamentos lancamento = this.lancamentoDAO.save(novoLancamento);
				
				if(lancamento != null) {
										
					return new ResponseEntity<Lancamentos>(lancamento, HttpStatus.OK);
					
				} else {
					
					this.logger.info("Houve um erro ao tentar inserir o lançamento no Banco de Dados!");
					return new ResponseEntity<Lancamentos>(HttpStatus.EXPECTATION_FAILED);
					
				}
							
			} else {
				
				this.logger.info("A Conta para a qual está tentando cadastrar o lançamento não pertence ao usuário logado(Token)!");
				return new ResponseEntity<Lancamentos>(HttpStatus.FORBIDDEN);
				
			}
			
			
		} else {
			
			this.logger.info(String.format("Não foi possivel localizar a Conta de Id : [%s] !", lancamentoDTO.getId_conta()));
			return new ResponseEntity<Lancamentos>(HttpStatus.EXPECTATION_FAILED);
			
		}
		
	}

	/**
	 * Serviço para obter a lista de lançamento de receitas e despesas e um determinado usuário (TOKEN)
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<List<Lancamentos>> getLancamentos(HttpServletRequest request) {
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		List<Lancamentos> todosLancamentosUsuario = this.lancamentoDAO.getLancamentosPorUsuario(usuarioRequisicao.getId());
		
		return new ResponseEntity<List<Lancamentos>>(todosLancamentosUsuario, HttpStatus.OK);
		
	}

	/**
	 * Serviço para obter o somátorio (Resultado) total de todos os lançamentos de um determinado usuário
	 * sejam eles de Receita ou Despesas.
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Double> getTotalLancamentos(HttpServletRequest request) {
		
		this.logger.info("Método getTotalLancamentos() acionado!");
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		double somatorioLancamentos = this.customLancamentoDAO.getTotalLancamentosPorUsuario(usuarioRequisicao.getId());
		
		this.logger.info(String.format("Somátorio total obtido com sucesso : [%s]", somatorioLancamentos));
		
		return new ResponseEntity<Double>(somatorioLancamentos, HttpStatus.OK);
		
		
	}

	/**
	 * Serviço para obter o resumo das receitas por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<List<ResumoDTO>> getResumoReceitas(HttpServletRequest request) {

		this.logger.info("Método getResumoReceitas() acionado!");
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		List<ResumoDTO> resumoReceitas = this.customLancamentoDAO.getResumoReceita(usuarioRequisicao.getId());
		
		this.logger.info(String.format("Resumo de Receitas Gerado. Total de [%s] entradas.", resumoReceitas.size()));
		
		return new ResponseEntity<List<ResumoDTO>>(resumoReceitas, HttpStatus.OK);
	
	}
	

	/**
	 * Serviço responsável por obter o somátorio total das receitas de um determinado usuário.
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Double> getSomatorioReceitas(HttpServletRequest request) {
		
		this.logger.info("Serviço getSomatorioReceitas acionado...");
		
		double somatorioReceitas = 0.00;
		
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		
		somatorioReceitas = this.customLancamentoDAO.getTotalResumoReceitas(usuarioLogado.getId());
		
		this.logger.info(String.format("Somátorio total de Receitas obtidos com sucesso, somátorio : [%s]", somatorioReceitas));
		
		return new ResponseEntity<Double>(somatorioReceitas, HttpStatus.OK);
	
	}
	
	/**
	 * Serviço para obter o resumo das despesas por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<List<ResumoDTO>> getResumoDespesas(HttpServletRequest request) {

		this.logger.info("Método getResumoDespesas() acionado!");
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		List<ResumoDTO> resumoDespesas = this.customLancamentoDAO.getResumoDespesas(usuarioRequisicao.getId());
		
		this.logger.info(String.format("Resumo de Despesas Gerado. Total de [%s] entradas.", resumoDespesas.size()));
		
		return new ResponseEntity<List<ResumoDTO>>(resumoDespesas, HttpStatus.OK);
	
	}
	

	/**
	 * Serviço responsável por obter o somátorio total das despesas de um determinado usuário.
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Double> getSomatorioDespesas(HttpServletRequest request) {
		
		this.logger.info("Serviço getSomatorioDespesas acionado...");
		
		double somatorioDespesas = 0.00;
		
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		
		somatorioDespesas = this.customLancamentoDAO.getTotalResumoDespesa(usuarioLogado.getId());
		
		this.logger.info(String.format("Somátorio total de Despesas obtidos com sucesso, somátorio : [%s]", somatorioDespesas));
		
		return new ResponseEntity<Double>(somatorioDespesas, HttpStatus.OK);
	
	}

	
	/**
	 * Serviço para obter o resumo de Resultado por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<List<ResumoDTO>> getResumoResultado(HttpServletRequest request) {
		
		this.logger.info("Método getResumoResultado() acionado!");
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		List<ResumoDTO> resumoResultado = this.customLancamentoDAO.getResumoResultado(usuarioRequisicao.getId());
		
		this.logger.info(String.format("Resumo de Resultados Gerado. Total de [%s] entradas.", resumoResultado.size()));
		
		return new ResponseEntity<List<ResumoDTO>>(resumoResultado, HttpStatus.OK);
		
	}
	
	
	/**
	 * Serviço para obter o resumo de Resultado por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<List<Lancamentos>> getReceitas(HttpServletRequest request) {
		
		this.logger.info("Método getReceitas() acionado!");
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		List<Lancamentos> lancamentosReceitas = this.lancamentoDAO.getLancamentosReceitasPorUsuario(usuarioRequisicao.getId());
		
		if(lancamentosReceitas.size() != 0 && lancamentosReceitas != null) {
			
			this.logger.info(String.format("Retornando [%s] lançamentos de Receita.", lancamentosReceitas.size()));
			return new ResponseEntity<List<Lancamentos>>(lancamentosReceitas, HttpStatus.OK);
			
			
		} else {
			
			this.logger.info("Não foram encontrados lançamentos de Receitas para este usuário.");
			return new ResponseEntity<List<Lancamentos>>(new ArrayList<Lancamentos>(), HttpStatus.OK);
			
		}
				
	}
	
	
	/**
	 * Serviço para obter as despesas por conta de um usuário
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<List<Lancamentos>> getDespesas(HttpServletRequest request) {
		
		this.logger.info("Método getDespesas() acionado!");
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		List<Lancamentos> lancamentosDespesas = this.lancamentoDAO.getLancamentosDespesasPorUsuario(usuarioRequisicao.getId());
		
		if(lancamentosDespesas.size() != 0 && lancamentosDespesas != null) {
			
			this.logger.info(String.format("Retornando [%s] lançamentos de Despesas.", lancamentosDespesas.size()));
			return new ResponseEntity<List<Lancamentos>>(lancamentosDespesas, HttpStatus.OK);
			
			
		} else {
			
			this.logger.info("Não foram encontrados lançamentos de Despesas para este usuário.");
			return new ResponseEntity<List<Lancamentos>>(new ArrayList<Lancamentos>(), HttpStatus.OK);
			
		}
			
	
	}

	
	/**
	 * Serviço para exclusão de lançamentos, o lançamento só será excluido
	 * caso ele pertença ao usuário logado (Token) requisição. 
	 * 
	 * @param request
	 * @param idLancamento
	 * @return
	 */
	@Override
	public ResponseEntity<Boolean> excluirLancamento(HttpServletRequest request, int idLancamento) {
		
		this.logger.info("Método excluirLancamento() acionado...");

		// Selecionando Lancamento para exclusão
		Lancamentos lancamentoExcluir = this.lancamentoDAO.findById(idLancamento).get();

		// Selecionando Usuário Logado (Token)
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);

		// Verificando se o Lançamento pertecente ao usuário logado (Token)
		if (usuarioLogado.getId() == lancamentoExcluir.getUsuario().getId()) {

			// OK podemos resalizar a exclusão
			
			/*
			 * Temos que verificar se não existe nenhuma divida cadastrada, 
			 * com o lançamento que será excluido, pois se tiver temos
			 * que setar essa divida com null. 
			 * Como o relacionamento é um para 1 se houver será apenas
			 * 1 divida então vamos verificar se houver setaremos null.
			 */
			Dividas dividaDoLancamento = this.dividaDAO.dividaByIdLancamento(lancamentoExcluir.getId());
						
			if(dividaDoLancamento != null) {
				
				dividaDoLancamento.setLancamento(null);
				this.dividaDAO.save(dividaDoLancamento);
				
			}
			
			this.lancamentoDAO.delete(lancamentoExcluir);
			
			this.logger.info(String.format("Lancamento [%s] excluido come sucesso.", lancamentoExcluir.getId()));

			return new ResponseEntity<Boolean>(true, HttpStatus.OK);

		} else {

			// O lançamento não pertece ao usuário não podemos realizar a exclusão
			this.logger.info(String.format("Não foi possivel excluir o lançamento [%s] por que este não pertence ao usuário da requisição (Token).", lancamentoExcluir.getId()));
			return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);

		}

	}

	
	/**
	 * Serviço para realizar o update de um determinado lançamento caso esse lançamento
	 * pertença ao usuário da requisição(TOKEN)
	 * 
	 * @param request
	 * @param lancamentoUpdate
	 * @return
	 */
	@Override
	public Lancamentos updateLancamento(HttpServletRequest request, Lancamentos lancamentoUpdate) {
		
		this.logger.info("Método updateLancamento() acionado... ");
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		//Verificando se o lançamento a ser editado pertence ao usuário da requisicao
		if(usuarioRequisicao.getId() == lancamentoUpdate.getUsuario().getId()) {
			
			Lancamentos lancamentoAtualizado = this.lancamentoDAO.save(lancamentoUpdate);
			
			if(lancamentoAtualizado != null) {
				
				this.logger.info(String.format("Lançamento atualizado com sucesso! O Lançamento foi atualizado para [%s]", lancamentoAtualizado.toString()));
				return lancamentoAtualizado;
				
			} else {
				
				this.logger.info(String.format("Houve um erro ao tentar atualizar o lançamento :[%s]!", lancamentoUpdate.toString()));
				return null;
						
			}
			
		} else {
			
			this.logger.info("O Lançamento não pertence ao usuário da requisição, operação não autorizada");
			return null;
			
		}

	}

	/**
	 * Serviço para informar o numero de lançamentos pertecente a uma determianda conta.
	 * 
	 * @param conta
	 * @return
	 */
	@Override
	public int qtdLancamentosConta(Contas conta) {
		
		return this.lancamentoDAO.getLancamentosConta(conta.getId()).size();
		
	}
	
	
	
	
	
	
}
