package br.com.dwarfcontrol.controller;


import br.com.dwarfcontrol.model.entitys.Usuario;
import br.com.dwarfcontrol.model.DTO.UsuarioDTO;
import br.com.dwarfcontrol.model.services.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.List;

@RestController
@RequestMapping("/sistema")
@CrossOrigin // Essa notação é para permitir requisições fora do mesmo HOST de excução do servidor.
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;


    /**
     * Serviço exposto para o cadastro de Usuários.
     *
     *    >>>Somente Usuários Administradores podem salvar Usuarios<<<
     *
     * @param usuario
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/usuario",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody UsuarioDTO usuario, ServletRequest request){

        return  usuarioService.salvarUsuario(usuario, request);

    }

    /**
     * Serviço responsável por listar os Usuários Cadastrados na base de dados.
     *
     *    >>>Somente Usuários Administradores podem salvar Usuarios<<<
     *
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/listarUsuarios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Usuario>> listarUsuarios(ServletRequest request){

        return usuarioService.listarUsuarios(request);

    }


    /**
     * Serviço responsável por realizar uma pesquisa nos usuários cadastrados no banco de dados
     * e devolver essa pesquisa para o front-end
     *
     *    >>>Somente Usuários Administradores podem realizar esse tipo de pesquisa<<<
     *
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/pesquisarUsuarios/{termo}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Usuario>> pesquisarUsuarios(@PathVariable("termo") String termo, ServletRequest request){

        return this.usuarioService.pesquisarUsuarios(termo, request);

    }


    /**
     * Serviço responsável por entregar os dados e um unico usuário para o frond-end
     *
     *
     *    >>>Somente Usuários Administradores e usuários que solicitam seus proprios dados
     *    podem realizar esse tipo de pesquisa<<<
     *
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/usuario/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Usuario> getUsuario(@PathVariable("id") int id, ServletRequest request){

        return this.usuarioService.getUsuario(id, request);

    }

    /**
     * Método responsável por realizar o update dos dados de um usuário.
     * Esse método recebe os dados de um usuário e baseado no id que está no objeto usuarioUpdate
     * realiza o update dos dados.
     *
     *  >>> Esse método só pode ser executado por administradores, ou pelos donos do proprio usuário ou seja
     *  pela mesmo usuário que está logado os dados dele mesmo <<<
     *
     * @param usuarioUpdate
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/usuario",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> updateUsuario(@RequestBody Usuario usuarioUpdate, ServletRequest request){

        return this.usuarioService.updateUsuario(usuarioUpdate, request);

    }

    /**
     * Serviço responsável por realizar a desativação de um usuário
     *
     *  >>> Esse método só pode ser executado por administradores <<<
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/usuario/desable",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> desableUsuario(@RequestBody int id, ServletRequest request){

        return this.usuarioService.desableUsuario(id, request);

    }


    /**
     * Serviço responsável por realizar a desativação de um usuário
     *
     *  >>> Esse método só pode ser executado por administradores <<<
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/usuario/enable",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> enableUsuario(@RequestBody int id, ServletRequest request){

        return this.usuarioService.enableUsuario(id, request);

    }



}
