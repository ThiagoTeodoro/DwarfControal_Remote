package br.com.controleFinanceiro.model.services;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.controleFinanceiro.model.DAOs.interfaces.ILimitesDAO;
import br.com.controleFinanceiro.model.entitys.Limites;
import br.com.controleFinanceiro.model.entitys.Usuarios;
import br.com.controleFinanceiro.model.services.interfaces.ILimitesService;

@Service
public class LimitesService implements ILimitesService{
	
	@Autowired
	private AutenticationService autenticationService;
	
	@Autowired
	private ILimitesDAO limitesDAO;

	private static Logger logger = LoggerFactory.getLogger(LimitesService.class);
	
	/**
	 * Método que consulta um limite de um determinado usuário
	 * e o devolve.
	 * 
	 *  #Caso o limite não exista na base de dados, será criado
	 *  um automaticamente de valor 0, visto que to usuário deva 
	 *  ter um limite na API.
	 *  
	 * @param request
	 * @return
	 */
	@Override
	public double getLimite(HttpServletRequest request) {
		
		logger.info("Método getLimite() acionado!");
		
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		
		Limites limiteUsuario = this.limitesDAO.getLimiteByUsuario(usuarioLogado.getId());
		
		if(limiteUsuario == null) {
			
			logger.info("Esse usuário ainda não possui limite, gerando de valor zerado...");
			
			Limites novoLimite = new Limites();
			novoLimite.setValor(0);
			novoLimite.setUsuario(usuarioLogado);
			
			limiteUsuario = this.limitesDAO.save(novoLimite);
		}
		
		return limiteUsuario.getValor();
	}

	
	/**
	 * Método responsável por atualizar um limite na base de dados.
	 * 
	 * @param valor
	 * @param request
	 * @return
	 */
	@Override
	public boolean updateLimite(double valor, HttpServletRequest request) {
		
		logger.info("Método updateLimite() acionado!");
		
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		
		Limites limiteUsuario = this.limitesDAO.getLimiteByUsuario(usuarioLogado.getId());
		
		limiteUsuario.setValor(valor);
		
		if(this.limitesDAO.save(limiteUsuario) != null) {
			
			logger.info("Limite autualizado com sucesso!");
			return true;
			
		} else {
			
			logger.info("Problema ao tentar atualizar o limite!");
			return false;
			
		}
				
	}
	

	/**
	 * Método responsável por calcular quanto um valor de referência é
	 * em relação ao limite do usuário logado(TOKEN)
	 * 
	 * @param valorReferencia
	 * @param request
	 * @return
	 */
	@Override
	public double percentagemLimite(double valorReferencia, HttpServletRequest request) {
				
		logger.info("Método percentagemLimite() acionado!");		
		
		//Selecionando o Limite do Usuário atraves do proprio método que já tem a tratativa caso o usuário aindan não possua limite.
		Double limiteUsuarioLogado = this.getLimite(request);
		
		//Realizando a regra de 3
		return (valorReferencia * 100) / limiteUsuarioLogado;
		
	}

}
