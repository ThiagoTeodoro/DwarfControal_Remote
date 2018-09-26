package br.com.dwarfcontrol.model.services;

import br.com.dwarfcontrol.model.DAO.IUsuarioDAO;
import br.com.dwarfcontrol.model.DTO.ChangeSenhaDTO;
import br.com.dwarfcontrol.model.entitys.Usuario;
import br.com.dwarfcontrol.model.DTO.UsuarioDTO;
import br.com.dwarfcontrol.model.services.interfaces.IUsuarioService;
import br.com.dwarfcontrol.model.utilities.EncryptionFunctions;
import jdk.management.resource.NotifyingMeter;
import jdk.nashorn.internal.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.validation.constraints.Email;
import java.util.List;


@Component
@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private AuthenticatorServiceImpl authenticatorService;

    private EncryptionFunctions encryptionFunctions = new EncryptionFunctions();
    private Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);


    /**
     * Método responsável por salvar um novo usuário no Banco de Dados
     * @param usuario
     * @return
     */
    @Override
    public ResponseEntity<Usuario> salvarUsuario(UsuarioDTO usuario, ServletRequest request) {

        Usuario usuarioCheck = new Usuario();
        usuarioCheck.setId(0);
        usuarioCheck.setNome(usuario.getNome());
        usuarioCheck.setEmail(usuario.getEmail());
        usuarioCheck.setNivel(usuario.getNivel());
        usuarioCheck.setSenha(usuario.getSenha());

        if(this.checkEmailExist(usuarioCheck) == false) {

            //Recuperando Token da Requisição
            String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);

            //Verificando se o usuário é um Administrador
            if (this.authenticatorService.checkAdmin(token).getBody() == true) {

                //A Senha deve vir descriptografada para o cadastro temos que criptografar
                usuario.setSenha(encryptionFunctions.toMD5(usuario.getSenha()));

                Usuario cadastro = usuarioDAO.save(usuario.toUsuario());

                if (cadastro != null) {

                    return new ResponseEntity<Usuario>(cadastro, HttpStatus.OK);

                } else {

                    return new ResponseEntity<Usuario>(HttpStatus.INTERNAL_SERVER_ERROR);

                }

            } else {

                //Esse usuário não tem permissão para cadastrar usuário pois não é um Administrador.
                System.err.println("Esse usuário não é um Administrador por tanto não pode cadastrar novos usuários!");
                return new ResponseEntity<Usuario>(HttpStatus.FORBIDDEN);

            }

        } else {

            //O Email já existe na base de dados requisião não aceitavel.
            return new ResponseEntity<Usuario>(HttpStatus.NOT_ACCEPTABLE);

        }
    }

    /**
     * Método responsável por entregar uma lista de Usuários com todos os
     * Usuários cadastrados na aplicação.
     *
     *     >>>Somente Administradores<<<
     *
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<List<Usuario>> listarUsuarios(ServletRequest request) {

        //Recuperando Token da requisição para verificar se o usuário é ou não um administrador.
        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        if(this.authenticatorService.checkAdmin(token).getBody() == Boolean.TRUE){

            List<Usuario> usuarios = this.usuarioDAO.findAll();

            /*
                Nesse select estamos trazendo inclusive as Senhas, não queremos
                entregar para o Front-End as senhas, por tanto vamos limpalas.
             */
            for(Usuario usuario: usuarios){

                //Tirando Todas as Senhas da lista
                usuario.setSenha("");

            }

            //Retornando Lista de Usuários do Banco de Dados
            return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);

        } else {

            //Esse usuário não é um Administrador, devolvendo erro para "Não autorizado"
            System.err.println("Esse usuário não é um Administrador por tanto não pode obter a lista de usuários cadastrados!");
            return new ResponseEntity<List<Usuario>>(HttpStatus.FORBIDDEN);

        }
    }

    /**
     * Método responsável por ralizar uma pesquisa no Banco de dados nos usuários
     * cadastrados. E devolver uma lista com o resultado da pesquisa
     *
     * @param termo
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<List<Usuario>> pesquisarUsuarios(String termo, ServletRequest request) {

        //Recuperando Token da requisição para verificar se o usuário é ou não um administrador.
        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        if(this.authenticatorService.checkAdmin(token).getBody() == Boolean.TRUE){

            //Realizando Pesquisa
            List<Usuario> listaPesquisada = this.usuarioDAO.pesquisarUsuarios( "%" + termo + "%");

            //Devolvendo Pesquisa
            return new ResponseEntity<List<Usuario>>(listaPesquisada, HttpStatus.OK);

        } else {

            //Esse usuário não é um Administrador, devolvendo erro para "Não autorizado"
            System.err.println("Esse usuário não é um Administrador por tanto não pode realizar pesquisas de Usuários!");
            return new ResponseEntity<List<Usuario>>(HttpStatus.FORBIDDEN);

        }


    }


    /**
     * Método responsável por obter os dados de um unico usuário e devolver ao back end.
     * Esse dado só é devolvido caso o usuário seja um administardor ou seja dono dos proprios dados
     * ou seja, os dados são do proprio usuario logado (Token)
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Usuario> getUsuario(int id, ServletRequest request) {

        //Recuperando Token da requisição para verificar se o usuário é ou não um administrador.
        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        if(this.authenticatorService.checkAdmin(token).getBody() == Boolean.TRUE) {

            if(this.usuarioDAO.findById(id).isPresent()) {

                Usuario usuario = this.usuarioDAO.findById(id).get();

                //Apagando a Senha, nós não devolvemos senha para o Front-End
                usuario.setSenha("");

                return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);

            } else {

                //O id solicitado não existe no banco de dados
                return new ResponseEntity<Usuario>(HttpStatus.NO_CONTENT);

            }

        } else {

            String email = this.authenticatorService.getEmailToken(token);

            //Selecionando o Id enviado para descobrir se o mesmo é do usuário que está logado (token)
            //Esse is presente é que pode ser que o usuário nem exista no banco de dados então primeiro verificamos se
            //ele existe para depois guardar e comparar.
            if(this.usuarioDAO.findById(id).isPresent()) {

                Usuario usuario = this.usuarioDAO.findById(id).get();

                if (usuario.getEmail().equals(email)) {

                    //Apagando a Senha, nós não devolvemos senha para o Front-End
                    usuario.setSenha("");

                    return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);

                } else {

                    return new ResponseEntity<Usuario>(HttpStatus.FORBIDDEN);

                }

            } else {

                //O id solicitado não existe no banco de dados
                return new ResponseEntity<Usuario>(HttpStatus.NO_CONTENT);

            }

        }

    }

    /**
     * Método responsável por realizar opdate no banco de dados de um usuario.
     *
     * >>> Só podem realizar update usuários administradores, ou donos dos proprios dados <<<
     *
     * @param usuarioUpdate
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Boolean> updateUsuario(Usuario usuarioUpdate, ServletRequest request) {

        //Preciso verificar se o email que está tentando ser atulizado ja não existe em OUTRO usuário
        if(this.checkEmailExist(usuarioUpdate) == false) {

            //Recuperando Token da requisição para verificar se o usuário é ou não um administrador.
            String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);

            if (this.authenticatorService.checkAdmin(token).getBody() == Boolean.TRUE) {

                //Realizando o update e devolvendo resultado
                boolean status = this.exeUpdateUsuario(usuarioUpdate);

                return new ResponseEntity<Boolean>(status, HttpStatus.OK);

            } else {

                String email = this.authenticatorService.getEmailToken(token);

                //Selecionando o Id enviado para descobrir se o mesmo é do usuário que está logado (token)
                //Esse is presente é que pode ser que o usuário nem exista no banco de dados então primeiro verificamos se
                //ele existe para depois guardar e comparar.
                if (this.usuarioDAO.findById(usuarioUpdate.getId()).isPresent()) {

                    Usuario usuario = this.usuarioDAO.findById(usuarioUpdate.getId()).get();

                    if (usuario.getEmail().equals(email)) {

                        //Realizando o update e devolvendo resultado
                        boolean status = this.exeUpdateUsuario(usuarioUpdate);

                        return new ResponseEntity<Boolean>(status, HttpStatus.OK);


                    } else {

                        return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);

                    }

                } else {

                    //O usuarioUpdate.id solicitado não existe no banco de dados
                    return new ResponseEntity<Boolean>(HttpStatus.NO_CONTENT);

                }

            }

        } else {

            //O Email já existe, requisição não aceitavel.
            return new ResponseEntity<Boolean>(HttpStatus.NOT_ACCEPTABLE);

        }

    }

    /**
     * Esse metodo serve para checar se o email já existe na base de dados
     * caso ele exista mais seja do proprio Usuario que está tentando realizar um
     * update é retornado false, caso não exsita é retornado false, porém se ele
     * existir mais for difirente do usuário solicitante (Enviado como parâmetro)
     * retornamos true.
     */
    @Override
    public Boolean checkEmailExist(Usuario usuario) {

        if(this.usuarioDAO.findByEmail(usuario.getEmail()) != null){

            Usuario usuarioEncontrado = this.usuarioDAO.findByEmail(usuario.getEmail());

            if(usuarioEncontrado.getId() == usuario.getId()){

                //Existe mais é dele mesmo, então tudo bem.
                return false;

            } else {

                //Existe mais não é dele então não pode.
                return true;

            }

        } else {

            //Não existe
            return false;

        }

    }

    /**
     * Metodo responsavel por desativar Usuário da base dados.
     *
     * Esse metodo só pode ser usado por Administradores
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<Boolean> desableUsuario(int id, ServletRequest request) {

        //Recuperando Token da requisição para verificar se o usuário é ou não um administrador.
        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        if (this.authenticatorService.checkAdmin(token).getBody() == Boolean.TRUE) {


            Usuario usuarioDesabilitar = this.usuarioDAO.findById(id).get();

            if (usuarioDesabilitar != null) {

                usuarioDesabilitar.setAtivo(false);

                if (this.usuarioDAO.save(usuarioDesabilitar) != null) {

                    return new ResponseEntity<Boolean>(true, HttpStatus.OK);

                } else {

                    //Houve um erro ao tentar desabilitar o usuário
                    return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);

                }

            } else {

                return new ResponseEntity<Boolean>(HttpStatus.NO_CONTENT);

            }

        } else {

            //Só usuário administradores podem executar essa ação
            return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);

        }

    }

    /**
     * Metodo responsavel por ativar Usuário da base dados.
     *
     * Esse metodo só pode ser usado por Administradores
     *
     * @param id
     * @return
     */
    @Override
    public ResponseEntity<Boolean> enableUsuario(int id, ServletRequest request) {

        //Recuperando Token da requisição para verificar se o usuário é ou não um administrador.
        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        if (this.authenticatorService.checkAdmin(token).getBody() == Boolean.TRUE) {

            Usuario usuarioAtivar = this.usuarioDAO.findById(id).get();

            if (usuarioAtivar != null) {

                usuarioAtivar.setAtivo(true);

                if (this.usuarioDAO.save(usuarioAtivar) != null) {

                    return new ResponseEntity<Boolean>(true, HttpStatus.OK);

                } else {

                    //Houve um erro ao tentar desabilitar o usuário
                    return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);

                }

            } else {

                return new ResponseEntity<Boolean>(HttpStatus.NO_CONTENT);

            }

        } else {

            //Só usuário administradores podem executar essa ação
            return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);

        }

    }

    /**
     * Método que retorna os dados do usuário loogado ao front end sem a presença da senha
     *
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Usuario> getDadosUsuarioToken(ServletRequest request) {

        //Recuperando Token da requisição para verificar para encontrar o usuário
        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        //Recuperando Email do Token
        String emailLogado = this.authenticatorService.getEmailToken(token);

        Usuario usuarioLogado = this.usuarioDAO.findByEmail(emailLogado);

        if(usuarioLogado != null){

            //Removendo a Senha
            usuarioLogado.setSenha("");

            return new ResponseEntity<Usuario>(usuarioLogado, HttpStatus.OK);

        } else {

            //Não encontramos o usuário
            logger.error("Não foi encontrado nenhum usuário para esse Token de requisição!");
            return new ResponseEntity<Usuario>(HttpStatus.NO_CONTENT);

        }

    }

    /**
     * Método responsável por realizar o update do unico campo possivel para
     * o usuário, alterar seus proprios daods, no caso somente o nome.
     *
     * @param newNome
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Boolean> updateNomeUsuarioTokenLogado(String newNome, ServletRequest request) {

        String token = authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        String emailToken = authenticatorService.getEmailToken(token);

        Usuario usuarioLogado = usuarioDAO.findByEmail(emailToken);

        if(usuarioLogado != null){

            //Atulizando o Campo permitido que é o nome
            usuarioLogado.setNome(newNome);

            //Atuliznado usuarioLogado no Banco de Dados
            if(usuarioDAO.save(usuarioLogado) != null){

                return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);

            } else {

                //Não encontramos o usuário
                logger.error("Erro ao tentar atualizar os dados do Usuário no Banco de dados!");
                return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);

            }

        } else {

            //Não encontramos o usuário
            logger.error("Não foi encontrado nenhum usuário para esse Token de requisição!");
            return new ResponseEntity<Boolean>(HttpStatus.NO_CONTENT);

        }

    }

    /**
     * Serviço para troca de senha.
     *
     * Verfica se a senha antiga confere com a atual no banco.
     * Verifica se a nova senha e sua repetição são iguais
     * se tudo isso for valido ele realiza alteração da senha.
     *
     * @param changeSenha
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Boolean> updateSenhaUsuarioTokenLogado(ChangeSenhaDTO changeSenha, ServletRequest request) {

        String token = authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        String emailToken = authenticatorService.getEmailToken(token);

        Usuario usuarioLogado = usuarioDAO.findByEmail(emailToken);

        if(usuarioLogado != null){

            //Criptografando senha antiga para ver se tá igual
            String senhaAntiga = new EncryptionFunctions().toMD5(changeSenha.getSenhaAntiga());

            if(senhaAntiga.equals(usuarioLogado.getSenha())){

                if(changeSenha.getSenhaNova().equals(changeSenha.getSenhaRepeti())){

                    //Tudo certo podemos tentar atualizar a senha no Banco de Dados
                    String senhaNovaMd5 = new EncryptionFunctions().toMD5(changeSenha.getSenhaNova());
                    usuarioLogado.setSenha(senhaNovaMd5);

                    if(usuarioDAO.save(usuarioLogado) != null){

                        //Senha gravada!
                        return new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.OK);

                    } else {

                        logger.error("Houve um erro ao tentar gravar a nova senha no Banco de Dados");
                        return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);

                    }

                } else {

                    logger.error("A senha e a sua repetição não conferem!");
                    return new ResponseEntity<Boolean>(HttpStatus.PRECONDITION_FAILED);

                }

            } else {

                logger.error("A senha antiga e a senha cadastrada no banco de dados não conferem!");
                return new ResponseEntity<Boolean>(HttpStatus.PRECONDITION_FAILED);

            }

        } else {

            //Não encontramos o usuário
            logger.error("Não foi encontrado nenhum usuário para esse Token de requisição!");
            return new ResponseEntity<Boolean>(HttpStatus.NO_CONTENT);

        }


    }


    /**
     * Método auxiliar que apenas realiza o update do usuário no banco diretamente.
     * Concentrando o processo de update para facilitar DEBUG
     *
     * @param usuario
     * @return
     */
    private boolean exeUpdateUsuario(Usuario usuario){

        //Selecionando o objeto no banco de dados para realizar o update.
        Usuario usuarioUpdate = this.usuarioDAO.findById(usuario.getId()).get();

        //Atualizando atributos
        usuarioUpdate.setNome(usuario.getNome());
        usuarioUpdate.setEmail(usuario.getEmail());
        //Não alteramos a senha nesse método, existe um método para alterar a senha e um para resetar a senha
        //alterar a senha só será permitido pelo proprio usuário dono dos dados, o reset é permitido por adm's apenas.
        usuarioUpdate.setNivel(usuario.getNivel());
        //Também não mecho no ativo e desativado existem metodos para isso aki

        //Quando realizamos um SAVE em um objeto que já possui ID o sistema executa um update
        if(this.usuarioDAO.save(usuarioUpdate) != null){
            return true;
        } else {
            return false;
        }

    }

}
