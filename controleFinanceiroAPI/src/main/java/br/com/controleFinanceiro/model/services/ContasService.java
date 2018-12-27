package br.com.controleFinanceiro.model.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.controleFinanceiro.model.DAOs.interfaces.IContasDAO;
import br.com.controleFinanceiro.model.entitys.Contas;
import br.com.controleFinanceiro.model.entitys.Usuarios;
import br.com.controleFinanceiro.model.services.interfaces.IContasService;

@Service
public class ContasService implements IContasService {
	
	@Autowired
	private IContasDAO contasDAO;
	
	@Autowired
	private AutenticationService autenticationService;
	
	@Autowired
	private LancamentosService lancamentoService;

	
	Logger logger = LoggerFactory.getLogger(ContasService.class);
	
	/**
	 * Método que retorna uma lista com todas as contas do usuário
	 * do TOKEN da requisição
	 * 
	 * @return
	 */
	public List<Contas> allContasByToken(HttpServletRequest request){
		
		logger.info("Método allContas acionado!");
		
		Usuarios usuarioRequisicao = autenticationService.getUsuarioRequisicao(request);
		
		if(usuarioRequisicao != null) {
		
			List<Contas> contas = contasDAO.allContasByUser(usuarioRequisicao.getId());					
			
			logger.info(String.format("Retornando [%s] Contas!", contas.size()));
			
			return contas;
			
		} else {
			
			logger.error("Usuário da requisição não encontrado no banco de dados!");
			return null;
			
		}
			
	}

	/**
	 * Método responável por cadastrar uma nova conta do Banco de Dados
	 *  
	 * @param request
	 * @param novaConta
	 * @return
	 */
	@Override
	public ResponseEntity<Contas> novaConta(HttpServletRequest request, String nomeConta) {
		
		logger.info("Método novaConta() acionado!");
		
		Usuarios usuarioRequisicao = autenticationService.getUsuarioRequisicao(request);
		
		if(usuarioRequisicao != null) {

			//Gerando Conta a ser Gravada
			Contas novaConta = new Contas();
			novaConta.setUsuario(usuarioRequisicao);
			novaConta.setDescricao(nomeConta);
			
			Contas contaGravada = this.contasDAO.save(novaConta);
			
			if(contaGravada != null) {
				
				this.logger.info(String.format("Conta [%s] gravada com sucesso!", contaGravada.toString()));
				return new ResponseEntity<>(contaGravada, HttpStatus.OK);
				
			} else {
				
				this.logger.error("Houve um erro ao tentar gravar a nova conta no Banco de dados!");
				return new ResponseEntity<Contas>(HttpStatus.INTERNAL_SERVER_ERROR);
						
			}
			
		} else {
			
			logger.error("Usuário da requisição não encontrado no banco de dados!");
			return new ResponseEntity<Contas>(HttpStatus.UNAUTHORIZED);
			
		}
	}
	
	
	/**
	 * Método para recuperar uma conta do banco de dados, o método só entrega
	 * contas que pertence ao mesmo usuário da requisição, essa distinção é feita
	 * atravéz do TOKEN na request.
	 * 
	 * @param request
	 * @param idConta
	 * @return
	 */
	@Override
	public ResponseEntity<Contas> getConta(HttpServletRequest request, long idConta) {

		this.logger.info("Método getConta acionado!");
		
		Contas contaRecuperada = this.contasDAO.findById(Integer.parseInt(String.valueOf(idConta))).get();
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);		
		
		if(contaRecuperada !=null) {
		
			if(contaRecuperada.getUsuario().getId() == usuarioRequisicao.getId()) {
				
				this.logger.info(String.format("Retornando Conta : [%s]", contaRecuperada.toString()));
				
				return new ResponseEntity<Contas>(contaRecuperada, HttpStatus.OK);
				
			} else {
				
				this.logger.info("O conta requisitida não pertence ao usuário logado (Token)! Operação não autorizada.");
				return new ResponseEntity<Contas>(HttpStatus.FORBIDDEN);
				
			}
			
		} else {
			
			this.logger.info("Conta não encontrada!");
			return new ResponseEntity<Contas>(HttpStatus.PARTIAL_CONTENT);
			
		}
			
	}
	
	
	/**
	 * Método responsável por realizar o Update de uma determinda conta
	 * caso essa conta pertença ao usuário logado(Token). 
	 * 
	 * @param contaUpdate
	 * @param request
	 * @return
	 */
	public ResponseEntity<Contas> updateConta(Contas contaUpdate,HttpServletRequest request){
		
		this.logger.info("Método updateConta acionado!");
		
		//Selecionando a conta antiga no seu estado antigo para comparar com o usuário logado
		Contas oldConta = this.contasDAO.findById(contaUpdate.getId()).get();
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		if(oldConta != null) {
			
			if(oldConta.getUsuario().getId() == usuarioRequisicao.getId()) {
				
				//Atualizando dados da conta - No caso só a Descrição
				oldConta.setDescricao(contaUpdate.getDescricao());
				
				contaUpdate = null;
				contaUpdate = this.contasDAO.save(oldConta);
				
				if(contaUpdate != null) {
					
					this.logger.info("Conta atualizada para : " + contaUpdate.toString());
					return new ResponseEntity<Contas>(contaUpdate, HttpStatus.OK);
					
				} else {
					
					this.logger.info("Houve um erro ao tentar realizar o Update da Conta!");
					return new ResponseEntity<Contas>(HttpStatus.INTERNAL_SERVER_ERROR);
					
				}
				
			} else {
				
				this.logger.info("O conta requisitida não pertence ao usuário logado (Token)! Operação não autorizada.");
				return new ResponseEntity<Contas>(HttpStatus.FORBIDDEN);
				
			}
			
		} else {
			
			this.logger.info("Conta para Update não encontrada no banco de Dados!");
			return new ResponseEntity<Contas>(HttpStatus.PARTIAL_CONTENT);
			
		}
		
	}

	
	/**
	 * Método responsável por deletar a conta caso a conta pertença ao mesmo usuário
	 * da requisição(Token) e a conta não possua nenhum lançamento.
	 * 
	 * @param conta conta a ser excluida
	 * @param request Requisicao HTTP
	 * @return
	 */
	@Override
	public ResponseEntity<Boolean> deleteConta(Contas conta, HttpServletRequest request) {

		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		if(usuarioRequisicao.getId() == conta.getUsuario().getId()) {
			
			int qtdLancamentos = this.lancamentoService.qtdLancamentosConta(conta);
			
			if(qtdLancamentos == 0) {
				
				this.contasDAO.delete(conta);
				
				this.logger.info(String.format("Conta [%s] excluida com sucesso!", conta.toString()));
				
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
				
				
			} else {
				
				this.logger.info(String.format("Essa conta possui [%s] lançamentos, só excluimos contas que não possuem nenhum lançamento!", qtdLancamentos));
				return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);
				
			}
			
		} else {
			
			this.logger.info("Essa conta não pertece ao usuário da requisição!");
			return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);
			
		}
				
	}

}
