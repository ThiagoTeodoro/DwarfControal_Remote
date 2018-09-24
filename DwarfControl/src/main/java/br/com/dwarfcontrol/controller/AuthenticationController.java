package br.com.dwarfcontrol.controller;

import br.com.dwarfcontrol.model.DTO.AuthenticationDTO;
import br.com.dwarfcontrol.model.DTO.LoginDTO;
import br.com.dwarfcontrol.model.DTO.StatusDTO;
import br.com.dwarfcontrol.model.DTO.TokenDTO;
import br.com.dwarfcontrol.model.services.AuthenticatorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;

@RestController
@RequestMapping("/authenticator")
@CrossOrigin // Essa notação é para permitir requisições fora do mesmo HOST de excução do servidor.
public class AuthenticationController {

    @Autowired
    AuthenticatorServiceImpl authenticatorService;

    /***
     * Serviço esposto para obter um Token (Logar)
     *
     * @param loginDTO
     * @return
     * @throws ServletException
     */
    @RequestMapping(
                     value = "/token",
                     method = RequestMethod.POST,
                     produces = MediaType.APPLICATION_JSON_VALUE
                   )
    public ResponseEntity<AuthenticationDTO> obterToken(@RequestBody LoginDTO loginDTO)throws ServletException {

        return authenticatorService.obterToken(loginDTO);

    }


    /***
     * Serviço exposto para checar se um Token é valido ou não.
     *
     * @param token
     * @return
     */
    @RequestMapping(
            value = "/checkToken",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<StatusDTO> checkToken(@RequestBody String token){

        //Relizando Chamada ao Serviço Competente para checagem do Token
        return authenticatorService.checarToken(token);

    }


    /**
     * Método responsável por verificar se um usuário é ou não Administrador
     * de sistema para que possamos exibir ou não ou realizar uma ação
     * ou não no sitema
     *
     * @return
     */
    @RequestMapping(
            value = "/checkAdmin",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> checkAdministrador(@RequestBody TokenDTO token){

        return authenticatorService.checkAdmin(token.getToken());

    }




}
