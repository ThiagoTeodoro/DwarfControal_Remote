package br.com.controleFinanceiro.model.services;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.controleFinanceiro.autentication.ManagerToken;
import br.com.controleFinanceiro.commons_methods.encryption.EncryptionManipulatorImpl;
import br.com.controleFinanceiro.model.DAOs.interfaces.IUsuarioDAO;
import br.com.controleFinanceiro.model.DTO.LoginDTO;
import br.com.controleFinanceiro.model.DTO.TokenDTO;
import br.com.controleFinanceiro.model.entitys.Usuarios;
import br.com.controleFinanceiro.model.services.interfaces.IAutenticationService;
import io.jsonwebtoken.Claims;

@Service
public class AutenticationService implements IAutenticationService {
	
	@Autowired
	private IUsuarioDAO usuarioDAO;
	
	@Autowired
	private ManagerToken managerToken;
	
	private Logger logger = LoggerFactory.getLogger(AutenticationService.class);

	private EncryptionManipulatorImpl encryptionManipulator = new EncryptionManipulatorImpl();
	
	/**
	 * Método responsável por recuperar o usuário da requisição
	 * que está sendo feita.
	 * 
	 * Esse método recupera o Token, estrai o email do Token
	 * e retorna os dados de usuário deste email.
	 * @param request
	 * @return
	 */
	@Override
	public Usuarios getUsuarioRequisicao(HttpServletRequest request) {
		
		logger.info("Recuperando Header Authorization detentor do TOKEN...");
		
		//Recuperando o Header Authorization que possui o Token
		String authorization = request.getHeader("Authorization");
		
		//Verificando se Header existe. (Não está nulo)
		if(authorization != null) {
			
			logger.info("Header Authorization Recuperado!");
			
			//Verificando se o Token que foi enviado é valido.
			ManagerToken managerToken = new ManagerToken();
            Claims recoveryToken = managerToken.checkToken(authorization);
            
            logger.info("Verificando se o TOKEN é valido...");
            
            //Verificando se o TOKEN é valido
            if(recoveryToken != null) {
            	
            	logger.info("TOKEN valido!");
            	logger.info("Recuperando Usuário ...");

            	//Estraindo Email do Token
            	String emailRequisicao = recoveryToken.getIssuer();
            	
            	logger.info("Recuperando dados do usuário de e-mail [" + emailRequisicao + "]");
            	
            	Usuarios usuarioRequisicao = usuarioDAO.findByEmail(emailRequisicao);
            	
            	//Verificando se usuário foi recuperado do Banco de Dados
            	if(usuarioRequisicao != null) {
            		
            		logger.info("Dados Recuperados!");
            		return usuarioRequisicao;
            		
            	} else {
            		
                	logger.info("O usuário do e-mail [" + emailRequisicao + "] não existe em nossa base de dados!");
                	return null;
            		
            	}
            	
            } else {
            	
            	logger.info("TOKEN inválido!");
            	return null;
            	
            }
            
		} else {
			
			logger.info("TOKEN não recibo! Header Authorization inexistente ou vázio.");
			return null;
			
		}
		
	}


	/**
	 * Método responsável por gerar um Token para utilização caso
	 * o email e a senha enviados em Login, conferir com o email
	 * e senha do usuário conrrespondente no banco de dados.
	 * 
	 * @param loginDTO
	 * @return
	 */
	@Override
	public ResponseEntity<TokenDTO> getToken(LoginDTO loginDTO) {
		
		logger.info("Iniciando processo de geração de TOKEN...");				
		
		//Tentando Recuperar usuario da base de dados
		Usuarios usuarioDataBase = usuarioDAO.findByEmail(loginDTO.getEmail());
		
		if(usuarioDataBase != null) {
			
			//Conferindo a senha enviada
			loginDTO.setSenha(encryptionManipulator.toMD5(loginDTO.getSenha()));
			
			if(loginDTO.getSenha().equals(usuarioDataBase.getSenha())) {
		
				//Verificando se o usuário está Ativo na Base de Dados
				if(usuarioDataBase.isAtivo()) {
					
					TokenDTO tokenDTO = new TokenDTO();
					tokenDTO.setTempoSegundos(3600);
					tokenDTO.setToken(managerToken.generateToken(usuarioDataBase.getEmail(), tokenDTO.getTempoSegundos()));
					
					logger.info(String.format("TOKEN de acesso gerado com sucesso para o usuário de e-mail [%s]", usuarioDataBase.getEmail()));
					
					return new ResponseEntity<TokenDTO>(tokenDTO, HttpStatus.OK);
					
				} else {
					
					logger.warn(String.format("O usuário [%s] está Desativado!", usuarioDataBase.getEmail()));
					return new ResponseEntity<TokenDTO>(HttpStatus.UNAUTHORIZED);
					
				}
				
			} else {
				
				logger.error("A senha enviada não confere com a senha cadastrada!");
				return new ResponseEntity<TokenDTO>(HttpStatus.UNAUTHORIZED);
				
			}
			
		} else {
			
			logger.error("Este e-mail não está cadastrado na nossa base de dados, impossível gerar o TOKEN.");
			return new ResponseEntity<TokenDTO>(HttpStatus.PRECONDITION_FAILED);
		}
				
	}

	
	/**
	 * Método responsável por checar se um Token está valido
	 * atravéz da JWT
	 * 
	 * @param Token
	 * @return
	 */
	@Override
	public ResponseEntity<Boolean> checkToken(String token) {
		
		logger.info(String.format("Verificando se o TOKEN [%s] está valido...", token));
		
		Claims cadeiaValidaToken = managerToken.checkToken(token);
		
		if(cadeiaValidaToken != null) {
			
			logger.info("TOKEN válido!");
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);		
			
		} else {
			
			logger.error("TOKEN inválido!");
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
			
		}
		
	}

}
