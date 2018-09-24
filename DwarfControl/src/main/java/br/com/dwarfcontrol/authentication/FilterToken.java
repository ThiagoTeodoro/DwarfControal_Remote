package br.com.dwarfcontrol.authentication;

import br.com.dwarfcontrol.model.DAO.IUsuarioDAO;
import br.com.dwarfcontrol.model.entitys.Usuario;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FilterToken extends GenericFilterBean {

    @Autowired
    IUsuarioDAO usuarioDAO;

    /***
     * Método responsável por filtrar as requisições e altorizar ou não a mesma
     * de acordo com Token enviado no Header em 'Authorization'
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("Requisição Recebida!");

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //Verificando Requisição OPTIONS (METODOS CROS)
        if ("OPTIONS".equals(request.getMethod())) {

            System.out.println("Requisição OPTIONS, Iguinorando verificação do Token!");

            //Requisições Options são ignoradas do Filtro! Elas apenas verificam existencia de métodos no EndPoint
            filterChain.doFilter(servletRequest, servletResponse);


        } else {

            //Recuperando do HEADER o valor da Chave Authorization
            String authorization = request.getHeader("Authorization");

            //Verificando se o Header Authorization não está nulo
            if (authorization == null) {

                System.out.println("Requisição Negada! Token de autorização não enviado!");

                //Melhor tratátiva em caso de não autorizado! Ficou bem melhor assim =)
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); // Esse adição é necessária para que o Angular 6 por exemplo não funcione de maneira inesperada em relação ao CROS
                httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Token de autorização não enviado!");

            } else {

                //Verificando se o Token que foi enviado é valido.
                ManagerToken managerToken = new ManagerToken();
                Claims recoveryToken = managerToken.checkToken(authorization);

                if (recoveryToken != null) {

                    //Recuperando o usuário no banco para verificar se ele está ativo ou não
                    Usuario usuarioLogando = usuarioDAO.findByEmail(recoveryToken.getIssuer());

                    //Verificando se o usuário está ativo no Banco de Dados
                    if(usuarioLogando.getAtivo()) {

                        System.out.println("Requisição Autorizada!");

                        //Tudo certo, a requisição está valida e autorizada pelo Token
                        filterChain.doFilter(servletRequest, servletResponse);

                    } else {

                        System.out.println("Requisição Negada! Usuário Desativado!");

                        //Melhor tratátiva em caso de não autorizado! Ficou bem melhor assim =)
                        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); // Esse adição é necessária para que o Angular 6 por exemplo não funcione de maneira inesperada em relação ao CROS
                        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Usuário Desativado!");

                    }

                } else {

                    System.out.println("Requisição Negada! Token Inválido!");

                    //Melhor tratátiva em caso de não autorizado! Ficou bem melhor assim =)
                    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                    httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); // Esse adição é necessária para que o Angular 6 por exemplo não funcione de maneira inesperada em relação ao CROS
                    httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Tokén Inválido");

                }

            }

        }

    }

}
