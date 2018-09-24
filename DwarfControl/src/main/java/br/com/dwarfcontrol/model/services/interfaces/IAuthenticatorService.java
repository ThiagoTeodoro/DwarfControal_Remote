package br.com.dwarfcontrol.model.services.interfaces;

import br.com.dwarfcontrol.model.DTO.AuthenticationDTO;
import br.com.dwarfcontrol.model.DTO.LoginDTO;
import br.com.dwarfcontrol.model.DTO.StatusDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletException;

public interface IAuthenticatorService {

    //Método responsável por entregar um Token para o Cliente.
    ResponseEntity<AuthenticationDTO> obterToken(LoginDTO loginDTO) throws ServletException;

    //Método responsável por checar se um Token é valido
    ResponseEntity<StatusDTO> checarToken(String token);

    //Método resposável por checar se um usuário(TOKEN) é de um administrador ou não
    ResponseEntity<Boolean> checkAdmin(String token);

    //Método responsável por recuperar o email de um Token
    String getEmailToken(String token);

}
