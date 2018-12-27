package br.com.controleFinanceiro.autentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import br.com.controleFinanceiro.model.DAOs.interfaces.IUsuarioDAO;
import br.com.controleFinanceiro.model.entitys.Usuarios;
import io.jsonwebtoken.Claims;

@Component
public class FilterToken extends GenericFilterBean {
	
	@Autowired
    IUsuarioDAO usuarioDAO;
	
	Logger logger = LoggerFactory.getLogger(FilterToken.class);

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

        logger.info("Requisição Recebida!");

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //Verificando Requisição OPTIONS (METODOS CROS)
        if ("OPTIONS".equals(request.getMethod())) {

        	logger.info("Requisição OPTIONS, Iguinorando verificação do Token!");

            //Requisições Options são ignoradas do Filtro! Elas apenas verificam existencia de métodos no EndPoint
            filterChain.doFilter(servletRequest, servletResponse);


        } else {

            //Recuperando do HEADER o valor da Chave Authorization
            String authorization = request.getHeader("Authorization");

            //Verificando se o Header Authorization não está nulo
            if (authorization == null) {

                logger.error("Requisição Negada! Token de autorização não enviado!");

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
                    Usuarios usuarioLogando = usuarioDAO.findByEmail(recoveryToken.getIssuer());

                    //Verificando se o usuário está ativo no Banco de Dados
                    if(usuarioLogando.isAtivo()) {

                    	logger.info("Requisição Autorizada!");

                        //Tudo certo, a requisição está valida e autorizada pelo Token
                        filterChain.doFilter(servletRequest, servletResponse);

                    } else {

                    	logger.info("Requisição Negada! Usuário Desativado!");

                        //Melhor tratátiva em caso de não autorizado! Ficou bem melhor assim =)
                        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); // Esse adição é necessária para que o Angular 6 por exemplo não funcione de maneira inesperada em relação ao CROS
                        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Usuário Desativado!");

                    }

                } else {

                    logger.error("Requisição Negada! Token Inválido!");

                    //Melhor tratátiva em caso de não autorizado! Ficou bem melhor assim =)
                    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                    httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); // Esse adição é necessária para que o Angular 6 por exemplo não funcione de maneira inesperada em relação ao CROS
                    httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Tokén Inválido");

                }

            }

        }

    }



}
