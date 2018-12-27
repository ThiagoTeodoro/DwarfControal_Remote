package br.com.controleFinanceiro.model.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.controleFinanceiro.commons_methods.datemanipulator.DateManipulatorImpl;
import br.com.controleFinanceiro.model.DAOs.CustomDividasDAO;
import br.com.controleFinanceiro.model.DAOs.interfaces.IContasDAO;
import br.com.controleFinanceiro.model.DAOs.interfaces.IDividasDAO;
import br.com.controleFinanceiro.model.DAOs.interfaces.ILancamentoDAO;
import br.com.controleFinanceiro.model.DTO.DividaDTO;
import br.com.controleFinanceiro.model.entitys.Contas;
import br.com.controleFinanceiro.model.entitys.Dividas;
import br.com.controleFinanceiro.model.entitys.Lancamentos;
import br.com.controleFinanceiro.model.entitys.Usuarios;
import br.com.controleFinanceiro.model.services.interfaces.IDividasService;


@Service
public class DividasService implements IDividasService {

	@Autowired
	private AutenticationService autenticationService;
	
	@Autowired
	private IDividasDAO dividasDAO;
	
	@Autowired
	private ILancamentoDAO lancamentosDAO;

	@Autowired
	private IContasDAO contasDAO;
	
	@Autowired
	private CustomDividasDAO customDividasDAO;
	
	private Logger logger = LoggerFactory.getLogger(DividasService.class);
	
	
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
	@Override
	public Dividas cadastrarDivida(DividaDTO divida, HttpServletRequest request) {
		
		//Recuperando Usuário Logado
		Usuarios usuarioLogado = autenticationService.getUsuarioRequisicao(request);
		
		//Realiazando Cadastro da Divida sem o Vinculo com o Lançamento caso seja escolhido essa opção
		Dividas dividas = new Dividas();
		dividas.fillFromDividasDTO(divida);		
		dividas.setUsuario(usuarioLogado);
		
		Dividas dividaCadastrada = this.dividasDAO.save(dividas);
		
		//Verificando se foi solicitado a replicação da Divida como Lançamento
		if(divida.getLancarLancamento()) {
			
			Lancamentos lancamento = new Lancamentos();
			lancamento.setConta(dividaCadastrada.getConta());
			lancamento.setData(dividaCadastrada.getDataVencimento());
			lancamento.setDescricao(dividaCadastrada.getDescricao());
			lancamento.setUsuario(dividaCadastrada.getUsuario());
			lancamento.setValor((dividaCadastrada.getValor() * -1));
			
			Lancamentos lancamentoCadastrado = this.lancamentosDAO.save(lancamento);
			
			dividaCadastrada.setLancamento(lancamentoCadastrado);
			this.dividasDAO.save(dividaCadastrada);
			
		}
		
		return dividaCadastrada;
		
	}

	
	/**
	 * Serviços para selecionar todas as dividas cadastradas de um determinado usuário
	 * logado
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public List<Dividas> getDividas(String anoMes, HttpServletRequest request) {
		
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		
		String[] aux = anoMes.split("-");
		
		List<Dividas> dividasUsuario = this.dividasDAO.allDividasByUser(usuarioLogado.getId(), Integer.parseInt(aux[1]), Integer.parseInt(aux[0]));
		
		
		for(int i = 0; i < dividasUsuario.size(); i ++) {
			
			if(dividasUsuario.get(i).isLiquidado() == false) {
				
				dividasUsuario.get(i).setVencido(new DateManipulatorImpl().verificaVencimento(new Date(), dividasUsuario.get(i).getDataVencimento().getTime()));
			
			}
				
		}
		
		return dividasUsuario;
				
	}


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
	@Override
	public boolean liquidarDivida(int idContaOrigem, int idDivida, HttpServletRequest request) {
		
		//Obtendo usuário da requisição
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		
		//Tentando Recuperar a Divida
		Dividas dividaLiquidar = this.dividasDAO.findById(idDivida).get();
		
		if(dividaLiquidar != null) {
			
			dividaLiquidar.setLiquidado(true);						
			
			/*
			 * Se existir lançamento de referência teremos que executar o procedimento de 
			 * neutralização da despesa e saida na conta de origem.
			 */
			if(dividaLiquidar.getLancamento() != null) {
				
				Lancamentos lancamentoReferencia = this.lancamentosDAO.findById(dividaLiquidar.getLancamento().getId()).get();
				
				//Gerando lançamento de neutralização da despesa
				Lancamentos lancamentoNeutralizacaoDespesa = new Lancamentos();
				lancamentoNeutralizacaoDespesa.setConta(lancamentoReferencia.getConta());
				lancamentoNeutralizacaoDespesa.setData(Calendar.getInstance());
				lancamentoNeutralizacaoDespesa.setDescricao("[Liquidação]" + lancamentoReferencia.getDescricao());
				lancamentoNeutralizacaoDespesa.setUsuario(usuarioLogado);
				lancamentoNeutralizacaoDespesa.setValor((lancamentoReferencia.getValor() * -1));
				
				Contas contaSaidaDinheiro = this.contasDAO.findById(idContaOrigem).get();
				
				Lancamentos lancamentoSaidaDinheiro = new Lancamentos();
				lancamentoSaidaDinheiro.setConta(contaSaidaDinheiro);
				lancamentoSaidaDinheiro.setData(Calendar.getInstance());
				lancamentoSaidaDinheiro.setDescricao("[Liquidação]" + lancamentoReferencia.getDescricao());
				lancamentoSaidaDinheiro.setUsuario(usuarioLogado);
				lancamentoSaidaDinheiro.setValor(lancamentoReferencia.getValor());
				
				this.dividasDAO.save(dividaLiquidar);	
				this.lancamentosDAO.save(lancamentoNeutralizacaoDespesa);
				this.lancamentosDAO.save(lancamentoSaidaDinheiro);
				
				return true;
				
			}
			
			//Salvo aqui se não houver lançamento de Referência
			this.dividasDAO.save(dividaLiquidar);
			
			return true;
			
		}
		
		return false;
		
	}


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
	@Override
	public boolean excluirDivida(int idDivida, HttpServletRequest request) {
		
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		
		Dividas dividaExcluir = this.dividasDAO.findById(idDivida).get();
		
		//Verificando se a divida pertence ao usuário logaodo(Token)
		if(dividaExcluir.getUsuario().getId() == usuarioLogado.getId()) {
			
			this.logger.info("Essa divida, pertence ao usuário, continuando operação...");
			
			if(dividaExcluir.getLancamento() != null) {
				
				Lancamentos lancamentoExcluir = this.lancamentosDAO.findById(dividaExcluir.getLancamento().getId()).get();
				
				this.lancamentosDAO.delete(lancamentoExcluir);
				this.dividasDAO.delete(dividaExcluir);
				
				this.logger.info("Divida excluida com sucesso, COM a presença de lançamento de despesa replicado!");
				
				return true;
				
				
			} else {
				
				this.dividasDAO.delete(dividaExcluir);
				
				this.logger.info("Divida excluida com sucesso, SEM a presença de lançamento de despesa replicado!");
				
				return true;
				
				
			}			
			
		} else {
			
			this.logger.info("Essa divida não pertence ao usuário que está logado, operação não autorizada!");
			return false;
			
		}
		
		
	}

	
	/**
	 * Serviço para realizar o update de uma divida, caso o usuário 
	 * da divida seja o mesmo logado (Token), se edisitir um lançamento
	 * de referência o mesmo também será atualizado.
	 * 
	 * @return
	 */
	@Override
	public boolean updateDivida(Dividas divida, HttpServletRequest request) {
		
		this.logger.info("Método updateDivida() acionado...");
		
		//Como eu não consegui fazer o Front-End alterar todo o objeto conta eu vou
		//que selecionar ele aqui e ir alterando tanto ele quanto a divida só os 
		//campos permitidos.
		
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		Dividas dividaEditar = this.dividasDAO.findById(divida.getId()).get();
		
		
		if(dividaEditar != null) {
			

			if(usuarioLogado.getId() == dividaEditar.getUsuario().getId()) {
				
				//Selecionando a conta enviada no banco de dados para alteração
				Contas contaEnviada = this.contasDAO.findById(divida.getConta().getId()).get();
				
				//Alterando campos permitidos 
				dividaEditar.setConta(contaEnviada);
				dividaEditar.setDataVencimento(divida.getDataVencimento());
				dividaEditar.setDescricao(divida.getDescricao());
				dividaEditar.setValor(divida.getValor());
				
				//Verificando a existencia de lançamento referenciado como despesa
				
				if(dividaEditar.getLancamento() != null) {
					
					this.logger.info("Essa divida possui um lançamento replicado, atualizando lançamento replicado de despesa");
					
					Lancamentos lancamentoEditar = dividaEditar.getLancamento();
					lancamentoEditar.setConta(contaEnviada);
					lancamentoEditar.setData(divida.getDataVencimento());
					lancamentoEditar.setDescricao(divida.getDescricao());
					
					if(divida.getValor() > 0) {
						lancamentoEditar.setValor((divida.getValor() * -1));
					}
					
					
					this.lancamentosDAO.save(lancamentoEditar);
					
				}
				
				//Atualizando Divida
				if(this.dividasDAO.save(dividaEditar) != null) {
				
					this.logger.info("Divida atualizada com sucesso!");
					return true;
			
				} else {
					
					this.logger.error("Houve um erro ao tentar atualizar a divida!");
					return false;
					
				}					
					
			} else {
				
				this.logger.error("Essa divida não pertence ao usuário logado, permissão negada!");
				return false;
				
			}		
			
		} else {
			
			this.logger.error("Essa divida não existe na nossa base de dados!");
			return false;
			
		}
		
	}


	/**
	 * Serviço para realizar o somatório das dividas de um mesAno filtrando
	 * por usuário.
	 * 
	 * @param mes
	 * @param ano
	 * @param request
	 * @return
	 */
	@Override
	public double getSomatorioMes(String mes, String ano, HttpServletRequest request) {
					
		return this.customDividasDAO.getSomatorioDividasMes(mes, ano, this.autenticationService.getUsuarioRequisicao(request).getId());
		
	}

	
	
}


