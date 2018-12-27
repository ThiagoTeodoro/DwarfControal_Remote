package br.com.controleFinanceiro.model.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.controleFinanceiro.model.DAOs.interfaces.IUsuarioDAO;
import br.com.controleFinanceiro.model.DTO.ChangeSenhaDTO;
import br.com.controleFinanceiro.model.entitys.Usuarios;
import br.com.controleFinanceiro.model.services.interfaces.IUsuariosService;
import br.com.encryption.EncryptionManipulatorImpl;

@Service
public class UsuariosService implements IUsuariosService{
	
	@Autowired
	private IUsuarioDAO usuarioDAO;
	
	@Autowired
	private AutenticationService autenticationService;
	
	private EncryptionManipulatorImpl encryptionFunctions = new EncryptionManipulatorImpl();
	
	private Logger logger = LoggerFactory.getLogger(UsuariosService.class);

	/**
	 * Método responsável por cadastrar um novo usuário na base de dados.
	 * 
	 * Esse método verifica se o usuário é um administrador, pois apenas 
	 * usuários administradores podem adicionar novos usuários na base 
	 * de dados.
	 * 
	 * @param novoUsuario
	 * @return
	 */
	@Override
	public Usuarios cadastrarUsuario(Usuarios novoUsuario, HttpServletRequest request) {
		
		
		//Veriricando se o usuário que realizou a requisição é um administrador e está ativo no sistema
		Usuarios usuarioRequisicao = autenticationService.getUsuarioRequisicao(request);
		
		if( usuarioRequisicao.isAtivo() && usuarioRequisicao.getNivel() == 1 ) {
			
			if(this.usuarioDAO.findByEmail(novoUsuario.getEmail()) == null) {
	
				logger.info("Iniciando processo de gravação do usuário no banco de dados!");
				
				//Criptografando a senha do usuário
				EncryptionManipulatorImpl encryptionManipulator = new EncryptionManipulatorImpl();
				novoUsuario.setSenha(encryptionManipulator.toMD5(novoUsuario.getSenha()));
				
				//Tentando cadastrar o usuário
				Usuarios usuario = usuarioDAO.save(novoUsuario);
				
				if(usuario != null) {
					
					logger.info(String.format("Usuário salvo no banco de dados com sucesso! Usuaário : [%s]", usuario.toString()));
					return usuario;
					
				} else {
					
					logger.info("Houve um erro ao tentar salvar o novo usuário na banco de dados!");
					return null;
					
				}
				
			} else {
				
				logger.info("Já existe um usuário com esse email em nossa base! Impossivel cadastrar usuário!");
				return null;
				
			}
			
		} else {
			
			logger.info("O usuário da requisição não é um Administrador ou está desativado do sistema!");
			return null;
			
		}
		
		
	}

	
	/**
	 * Serviço para checar se o usuário de uma requisição (Token) é administrador
	 * ou não.
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Boolean> checkAdministrador(HttpServletRequest request) {
		
		this.logger.info("Método checkAdministrador acionado!");
		
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		
		//Checando se conseguimos recuperar o usuário da requição
		if(usuarioLogado != null) {
			
			//Verificando se o usuário é adminsitrador
			if(usuarioLogado.getNivel() == 1) {
				
				this.logger.info("Esse usuário É um Administrador");
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
				
			} else {
				
				this.logger.info("Esse usuário NÃO é um Administrador");
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
			}
			
		} else {
			
			this.logger.error("Não foi possivel recuperar o usuário da requisição!");
			return new ResponseEntity<Boolean>(HttpStatus.UNAUTHORIZED);
			
		}
		
	}


	/** 
	 * Serviço para obter os dados de um usuário logado
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Usuarios> getDadosUsuario(HttpServletRequest request) {
		
		this.logger.info("Método getDadosUsuario() acionado, tentando obter os dados do usuário que está logado...");
		
		Usuarios usuarioLogado = this.autenticationService.getUsuarioRequisicao(request);
		
		if(usuarioLogado != null) {
			
			this.logger.info(String.format("Dados do usuário obtidos com sucesso! Usuario [%s]", usuarioLogado.toString()));
			return new ResponseEntity<Usuarios>(usuarioLogado, HttpStatus.OK);
			
		} else {
			
			this.logger.error("Ocorreu um erro ao tentar obter os dados do usuário logado!");
			return new ResponseEntity<Usuarios>(HttpStatus.EXPECTATION_FAILED);
			
		}		
		
	}

	
	/** 
	 * Serviço para atualizar os dados permitidos do perfil do usuário por enquanto 
	 * estamos atualizando o 
	 * 
	 * Nome
	 * 
	 * @param request
	 * @param nome
	 * @return
	 */
	@Override
	public ResponseEntity<Boolean> updatePerfil(String nome, HttpServletRequest request) {
		
		this.logger.info(String.format("Método updatePerfil() acionado, mudando o nome para [%s]", nome));
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		//Atualizando o Nome
		usuarioRequisicao.setNome(nome);
		
		Usuarios usuarioUpdate = this.usuarioDAO.save(usuarioRequisicao);
		
		if(usuarioUpdate != null) {
			
			this.logger.info(String.format("Dados do usuário alterado com sucesso para [%s]", usuarioUpdate.toString()));
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			
		} else {
			
			this.logger.error("Houve um erro ao tentar atualizar os dados!");
			return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);
			
		}
			
	}


	/**
	 * Serviço para alterar a senha do usuário logado.
	 * 
	 * A atualização da senha só é executada caso a Senha antiga 
	 * seja igual a senha antiga enviada no DTO. É uma forma de 
	 * garantir a segurança.
	 * 
	 * @param changeSenhaDTO
	 * @return
	 */
	@Override
	public ResponseEntity<Boolean> changePassword(ChangeSenhaDTO changeSenhaDTO, HttpServletRequest request) {

		this.logger.info("Método changePassword() acionado! Iniciando processo de alteração de senha...");
		
		Usuarios usuarioRequisicao = this.autenticationService.getUsuarioRequisicao(request);
		
		String md5SenhaAntigaEnviada = this.encryptionFunctions.toMD5(changeSenhaDTO.getSenhaAntiga());
		
		if(md5SenhaAntigaEnviada.equals(usuarioRequisicao.getSenha())) {
			
			if(changeSenhaDTO.getNovaSenha().equals(changeSenhaDTO.getConfirmaSenha())) {
			
				String md5NovaSenha = this.encryptionFunctions.toMD5(changeSenhaDTO.getNovaSenha());
	
				usuarioRequisicao.setSenha(md5NovaSenha);
				
				Usuarios updateUsuario = this.usuarioDAO.save(usuarioRequisicao);
				
				if(updateUsuario != null) {
					
					this.logger.info("Senha atualizada com sucesso!");
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
					
				} else {
					
					this.logger.info("Houve um erro ao tentar atualizar a senha no banco de dados!");
					return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);
					
				}
				
				
			} else {
				
				this.logger.info("A Senha Nova e a sua confirmação são diferentes! Negando operação de atualização de senha.");
				return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);
				
			}
			
		} else {
			
			this.logger.info("A senha antiga não confere com a senha efetivamente cadastrada! Não é possivel realizar a atualização da senha. Permissão Negada");
			return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);
			
		}
		
	}

	
	/**
	 * Retorna a lista de todos os usuários do sistema.
	 * 
	 * Obs: O Hash das senhas é removido por segurança.
	 * 
	 * @return
	 */
	@Override
	public ResponseEntity<List<Usuarios>> listUsuarios(HttpServletRequest request) {
		
		this.logger.info("Método listUsuarios() acionado...");

		Usuarios usuarioRequisiscao = this.autenticationService.getUsuarioRequisicao(request);

		//Verificando se o usuário é ADM
		if(usuarioRequisiscao.getNivel() == 1) {
			
			List<Usuarios> listaUsuario = this.usuarioDAO.findAll();
			
			for(int i = 0; i < listaUsuario.size(); i++) {
				listaUsuario.get(i).setSenha("");				
			}
			
			return new ResponseEntity<List<Usuarios>>(listaUsuario, HttpStatus.OK);
			
		} else {
			
			this.logger.error("O usuário da requisição não é um ADM! Operação Negada.");
			return new ResponseEntity<List<Usuarios>>(HttpStatus.FORBIDDEN);
			
		}

	}


	/**
	 * Método para ativar e desativar usuarios se o o usuário
	 * da requisição for um Administrador.
	 * 
	 * Se o usuário enviado estiver Ativo ele será desativado.
	 * Se estiver inativo será ativado.
	 * 
	 * @param request
	 */
	@Override
	public ResponseEntity<Boolean> ativarDesativarUsuario(HttpServletRequest request, int idUsuarioDesativarAtivar) {

		this.logger.info("Método ativarDesativarUsuario() acionado..."); 
		
		boolean isAdm = this.checkAdministrador(request).getBody();
		
		if(isAdm) {
			
			//Selecionando o usuário que será ativado ou desativado.
			Usuarios usuarioAtivarDesativar = this.usuarioDAO.findById(idUsuarioDesativarAtivar).get();

			if(usuarioAtivarDesativar != null) {
		
				//Se o usuário estiver ativo vamos desativar.
				if(usuarioAtivarDesativar.isAtivo()) {
					
					this.logger.info(String.format("Desativando usuário [%s] ... ", usuarioAtivarDesativar.getEmail()));
					usuarioAtivarDesativar.setAtivo(false);
					
					if (this.usuarioDAO.save(usuarioAtivarDesativar) != null){
						
						this.logger.info(String.format("Usuário [%s] Desativado!", usuarioAtivarDesativar.getEmail()));
						return new ResponseEntity<Boolean>(true, HttpStatus.OK);
						
					} else {
						
						this.logger.error(String.format("Erro ao tentar desativar o usuário [%s].", usuarioAtivarDesativar.getEmail()));
						return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
												
					}
					
				} else {
					
					this.logger.info(String.format("Ativando usuário [%s] ... ", usuarioAtivarDesativar.getEmail()));
					usuarioAtivarDesativar.setAtivo(true);
					
					if (this.usuarioDAO.save(usuarioAtivarDesativar) != null){
						
						this.logger.info(String.format("Usuário [%s] Ativado!", usuarioAtivarDesativar.getEmail()));
						return new ResponseEntity<Boolean>(true, HttpStatus.OK);
						
					} else {
						
						this.logger.error(String.format("Erro ao tentar Ativar o usuário [%s].", usuarioAtivarDesativar.getEmail()));
						return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
												
					}
										
				}				
				
			} else {
				
				this.logger.info("Esse usuário não existe em nossa base de dados!");
				return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
				
			}
				
		} else {
			
			this.logger.warn("O usuário não é um administrador e por tanto não tem permissão para realizar essa operação.");
			return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);
			
		}

	}


	/**
	 * Método para alterar o nivel de um determinado usuário.
	 * 
	 * @param IdUsuario
	 * @param NovoNivel
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Boolean> changeNivelUsuario(int IdUsuario, int NovoNivel, HttpServletRequest request) {

		this.logger.info("Método changeNivelUsuario() acionado...");
		
		boolean isAdm = this.checkAdministrador(request).getBody();
		
		if(isAdm) {
		
			Usuarios usuarioMudarNivel = this.usuarioDAO.findById(IdUsuario).get();

			if(usuarioMudarNivel != null) {
				
				usuarioMudarNivel.setNivel(NovoNivel);
				
				if(this.usuarioDAO.save(usuarioMudarNivel) != null) {

					this.logger.info(String.format("Nivel do usuário [%s], alterado com sucesso!", usuarioMudarNivel.getEmail()));
					return new  ResponseEntity<Boolean>(true, HttpStatus.OK);
					
				} else {
				
					this.logger.error(String.format("Houve um erro ao tentar mudar o nivel do usuário [%s].", usuarioMudarNivel.getEmail()));
					return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
					
				}
								
			} else {
				
				this.logger.warn("Esse usuário não existe em nossa base de dados!");
				return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);				
				
			}						
			
		} else {
			
			this.logger.warn("O usuário não é um administrador e por tanto não tem permissão para realizar essa operação.");
			return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);
			
		}
		
	}


	/**
	 * Serviço para resetar a senha de um determinado usuário para 
	 * 123456 caso o usuário da requisição seja um ADM
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public ResponseEntity<Boolean> resetPasswd(HttpServletRequest request, int idUsuario) {
		
		this.logger.info("Método resetPasswd() acionado...");		
		
		if(this.checkAdministrador(request).getBody()) {
			
			Usuarios usuarioResetSenha = this.usuarioDAO.findById(idUsuario).get();
			
			if(usuarioResetSenha != null) {
			
				String novaSenha = this.encryptionFunctions.toMD5("123456");
				
				usuarioResetSenha.setSenha(novaSenha);
				
				if(this.usuarioDAO.save(usuarioResetSenha) != null) {
					
					this.logger.info(String.format("Senha do usuário [%s] resetada com sucesso!", usuarioResetSenha.getEmail()));
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
					
				} else {
					
					this.logger.error(String.format("Houve um erro ao tentar resetar a senha do usuário usuário [%s].", usuarioResetSenha.getEmail()));
					return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
					
				}
				
			} else {
				
				this.logger.warn("Esse usuário não existe em nossa base de dados!");
				return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
				
			}
			
		} else {
			
			this.logger.warn("O usuário não é um administrador e por tanto não tem permissão para realizar essa operação.");
			return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);
			
		}
		
		
	}
	
}
