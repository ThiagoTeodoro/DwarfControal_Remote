package br.com.dwarfcontrol.model.services.interfaces;

import br.com.dwarfcontrol.model.DTO.ChangeSenhaDTO;
import br.com.dwarfcontrol.model.entitys.Usuario;
import br.com.dwarfcontrol.model.DTO.UsuarioDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletRequest;
import java.util.List;

public interface IUsuarioService {

    // Esse método somente administradores podem executar,
    // por tanto é necessário repurar o token da request e descobrir se é um administrador.
    ResponseEntity<Usuario> salvarUsuario(UsuarioDTO usuario, ServletRequest request);

    /*
        Esse método somente administradores podem executar
        Responsável por devolver uma lista de usuários cadastrados
     */
    ResponseEntity<List<Usuario>> listarUsuarios(ServletRequest request);

    /*
        Esse método somente administradores podem executar
        Responsável por realizar uma pesquisa nos usuários cadastrados no banco e dados.
     */
    ResponseEntity<List<Usuario>> pesquisarUsuarios(String termo, ServletRequest request);


    /*
        Esse método somente administradores e donos dos proprios dados podem executar
        Responsável por entregar os dados de um unico usuário
     */
    ResponseEntity<Usuario> getUsuario(int id, ServletRequest request);


    /*
        Esse método somente administradore e donos dos proprios dados podem executar
        Responsável por realizar o update dos dados do usuário.
     */
    ResponseEntity<Boolean> updateUsuario(Usuario usuarioUpdate, ServletRequest request);

    /**
     * Esse metodo serve para checar se o email já existe na base de dados
     * caso ele exista mais seja do proprio Usuario que está tentando realizar um
     * update é retornado false, caso não exsita é retornado false, porém se ele
     * existir mais for difirente do usuário solicitante (Enviado como parâmetro)
     * retornamos true.
     */
    Boolean checkEmailExist(Usuario usuario);

    /**
     * Metodo responsável por desativar um usuário na base de dados.
     *
     * Somente usuários administradore poderam usar esse metodo.
     *
     * @param id
     * @return
     */
    ResponseEntity<Boolean> desableUsuario(int id, ServletRequest request);

    /**
     * Metodo responsável por ativar um usuário na base de dados.
     *
     * Somente usuários administradore poderam usar esse metodo.
     *
     * @param id
     * @return
     */
    ResponseEntity<Boolean> enableUsuario(int id, ServletRequest request);

    /**
     * Método que retorna os dados do usuário loogado ao front end sem a presença da senha
     *
     * @param request
     * @return
     */
    ResponseEntity<Usuario> getDadosUsuarioToken(ServletRequest request);


    /**
     * Método responsável por realizar o update do unico campo possivel para
     * o usuário, alterar seus proprios daods, no caso somente o nome.
     *
     * @param newNome
     * @param request
     * @return
     */
    ResponseEntity<Boolean> updateNomeUsuarioTokenLogado(String newNome, ServletRequest request);


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
    ResponseEntity<Boolean> updateSenhaUsuarioTokenLogado(ChangeSenhaDTO changeSenha, ServletRequest request);


}
