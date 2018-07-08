package br.com.autentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Esse metodo é quem autoriza ou não a navegação em paginas que exigem login
 * aqui a ideia é pedir autorização para tudo e liberar apenas que você quer
 * para isso funcionar é necessári fazer o registo do Interceptor lá no
 * aplication_context.xml atraves do seguinte
 * 
 *  	<mvc:interceptors>
          <bean class= "br.com.autentication.AutorizadorInterceptor" />
      	</mvc:interceptors>
 * 
 * Esse caminho obviamente muta conforme seu projeto
 * 
 * @author Thiago Teodoro
 *
 */
public class AutorizadorInterceptor extends HandlerInterceptorAdapter {	
	
	private String UrlBase = "https://dwarfcontrol.herokuapp.com/NewDwarfControl/";

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller) throws Exception {	
		
		String uri = request.getRequestURI();
            
        //Autorizando URI de Login e das Inportações e aquelas que não precisam estar logadas
        //Note que alguns eu liberei para endsWith ou seja termina com... e outros eu liberei
        //por contuedo ou seja contem determinda coisa.
		//NÃO PRECISA PREOCUPAR COM AS ROTAS DO ANGULAR, ANGULAR é no ANGULAR esquece o resto
        if( uri.endsWith("index.html") || // URL NORMAL  primeira entrada        	
        	uri.endsWith("efetuarLogin") || //URL que chega o Login para tentar logar               
        	uri.contains("bibliotecas") || //Pacote de Bibliotecas necessário para Angular JQUERY ETC... isso munda conforme projeto
            uri.contains("css") || //Pacote com os CSS
			uri.contains("img") || //Pacote com as Imgs
			uri.contains("js")){ //Pacote con os JS
            	
            return true;
                
        } else if(request.getSession().getAttribute("usuario") != null) { //lá eu setei usuário poderia ser qualquer coisa
                
        	//Tem sessão aberta, BLZ deixa passar
           	return true;
            	
        } else {
            	
           	//O usuário  não está logado e nem mesmo é uma URI autorizada retornando ele para LOGIN
	        response.sendRedirect(this.UrlBase + "index.html");	        
	        
	        return false;
	            
        }
 
    }

}
	

