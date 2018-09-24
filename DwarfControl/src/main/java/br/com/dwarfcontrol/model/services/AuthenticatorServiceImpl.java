package br.com.dwarfcontrol.model.services;

import br.com.dwarfcontrol.authentication.ManagerToken;
import br.com.dwarfcontrol.model.DTO.AuthenticationDTO;
import br.com.dwarfcontrol.model.DAO.IUsuarioDAO;
import br.com.dwarfcontrol.model.DTO.LoginDTO;
import br.com.dwarfcontrol.model.DTO.StatusDTO;
import br.com.dwarfcontrol.model.entitys.Usuario;
import br.com.dwarfcontrol.model.enums.NivelUsuario;
import br.com.dwarfcontrol.model.services.interfaces.IAuthenticatorService;
import br.com.dwarfcontrol.model.utilities.EncryptionFunctions;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Service
public class AuthenticatorServiceImpl implements IAuthenticatorService {

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private ManagerToken managerToken;

    /***
     * Método responsável por checar se o usuário que está
     * tentando logar é valido, ou seja se ele existe no banco
     * de dados e possui a mesma senha enviada.
     * E se sim retornar o Token de acesso para utilização da
     * API
     *
     * @param loginDTO
     * @return
     */
    @Override
    public ResponseEntity<AuthenticationDTO> obterToken(LoginDTO loginDTO) {

        //Validando Campos
        if(loginDTO.validationFields()) {

            //Criptografando a senha para comparação lá no banco
            String senhaCriptografada =  new EncryptionFunctions().toMD5(loginDTO.getSenha());

            //Tentando recuperar usuário do banco de dados
            Usuario usuarioTentandoLogin = usuarioDAO.findByEmail(loginDTO.getEmail(),senhaCriptografada);

            //Verificando se o email e senha enviados existe no banco de Dados.
            if (usuarioTentandoLogin != null) {

                //Verificando se o usuário está ativo no Banco de Dados
                if(usuarioTentandoLogin.getAtivo()) {

                    String token = managerToken.generateToken(usuarioTentandoLogin.getEmail(), 3600);
                    AuthenticationDTO authenticationDTO = new AuthenticationDTO();
                    authenticationDTO.setToken(token);
                    authenticationDTO.setTempoExpiraco(3600);

                    return new ResponseEntity<AuthenticationDTO>(authenticationDTO, HttpStatus.OK);

                } else {

                    //Retornando NOT_ACCEPTABLE aki, o Front-End vai entender que o usuário foi desativado
                    return new ResponseEntity<AuthenticationDTO>(HttpStatus.NOT_ACCEPTABLE);

                }

            } else {

                return new ResponseEntity<AuthenticationDTO>(HttpStatus.UNAUTHORIZED);

            }

        } else {

            return new ResponseEntity<AuthenticationDTO>(HttpStatus.UNAUTHORIZED);

        }

    }

    /***
     * Método responsável por checar se um Token é valido ou não.
     *
     * @param token
     * @return
     */
    @Override
    public ResponseEntity<StatusDTO> checarToken(String token) {

        Claims recoveryToken = managerToken.checkToken(token);
        StatusDTO statusDTO = new StatusDTO(false);

        if(recoveryToken != null){

            //Retornando que o Token é valido
            statusDTO.setStatus(true);
            return new ResponseEntity<StatusDTO>(statusDTO, HttpStatus.OK);

        } else {

            //Retornando que o Token é invalido
            return new ResponseEntity<StatusDTO>(statusDTO, HttpStatus.OK);

        }

    }

    /**
     * Método responsável por checar se um Token é de um Administrador ou
     * não.
     *
     * @param token
     * @return
     */
    @Override
    public ResponseEntity<Boolean> checkAdmin(String token) {

        Claims tokenDecompiler = new ManagerToken().checkToken(token);

        if(tokenDecompiler == null){

            //Token é invalido ou não foi enviado, algo aconteceu por tanto vamos barrar a continuiedade do processo.
            return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.UNAUTHORIZED);

        } else {

            //O Token existe, hora de ver se ele é de um Administrador ou não.
            Usuario usuario = this.usuarioDAO.findByEmail(tokenDecompiler.getIssuer());

            if(usuario != null){

                if(usuario.getNivel() == NivelUsuario.Administrador.value){

                    //Usuário é um Administrador retornando True
                    return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);

                } else {

                    //Não é um Administrador retornando False
                    return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.OK);

                }

            } else {

                //Não achamos esse usuário desse token não tem algo errado, matando processo.
                return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.UNAUTHORIZED);

            }
        }
    }

    /**
     * Método responsável por retornar o email que está gravado no Token
     *
     * @param token
     * @return
     */
    @Override
    public String getEmailToken(String token) {

        if(token == null || token.equals("")){

            return null;

        } else {

            Claims tokenDecompiler = new ManagerToken().checkToken(token);

            if(tokenDecompiler == null){

                //Token Invalido!
                return  null;

            } else {

                //Retornando o email gravado no Token
                return tokenDecompiler.getIssuer();

            }

        }

    }


    /***
     * Método responsável por recuperar um valor de um Key
     * em uma requisição HTTP. (ServeletRequest)
     *
     * @param nameKey Nome da chave na requisição que guarda o valor
     * @param request Requisição de onde vamos tirar o valor
     * @return
     */
    public String getValueFromKeyServletRequest(String nameKey, ServletRequest request){

        //Exemplo de como recuperar o nome do quem está logado
        HttpServletRequest requestCapturada = (HttpServletRequest) request;

        //Recuperando do HEADER o valor da Chave Authorization
        String valueKey = requestCapturada.getHeader(nameKey);

        return valueKey;

    }

}
