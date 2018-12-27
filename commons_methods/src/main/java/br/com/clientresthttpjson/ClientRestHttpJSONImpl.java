package br.com.clientresthttpjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * Classe responsável por disponibilizar um Client
 * de comunicação GET e POST por JSON via BODY no 
 * caso do POST. 
 * 
 */
public class ClientRestHttpJSONImpl implements IClienteRestHttpJSON{
	
	//Atributos da Classe
	private String baseUrl;
	private boolean autenticateHeader;
	private String keyAutentication;
	private String valueKeyAutentication;
	
	//Logger da Classe
	private static Logger logger = Logger.getLogger(ClientRestHttpJSONImpl.class);
	
	//Classe responsável por iguinorar a cadeia de validação de certificado Java.
	private IguinoreHttpsJava iguinoreHttpsJava;


	/**
	 * Construtor da Classe
	 * 
	 * @param baseUrl
	 *            Base da URL que o cliente irá se conectar.
	 * @param autenticateHeader
	 *            O servidor a conectar exige autenticação em Header?
	 * @param keyAutentication
	 *            Nome da key para autenticação se autenticateHeader true
	 * @param valueKeyAutentication
	 *            Valor da key para atenticação se autenticateHeader true
	 * @param iguinorarCadeiaHttpsJava
	 *            O cliente deve iguinorar a cadeia de validação HTTPS do Java?
	 */
	public ClientRestHttpJSONImpl(	String baseUrl,
									boolean autenticateHeader,
									String keyAutentication,
									String valueKeyAutentication,							
									boolean iguinorarCadeiaHttpsJava
						  		  ) {
		
		//Base da Url do servidor a conectar.
		this.baseUrl = baseUrl;
		this.autenticateHeader = autenticateHeader;
		this.iguinoreHttpsJava = new IguinoreHttpsJava();
		
		//Preenchendo a key e o valor da key se valueAutentication for true.
		if(this.autenticateHeader) {
			
			this.keyAutentication = keyAutentication;
			this.valueKeyAutentication = valueKeyAutentication;
						
		}
		
		//Iguinorando cadeia do Java se solicitado pelo utilizador do Cliente
		if(iguinorarCadeiaHttpsJava) {
			
			this.iguinoreHttpsJava.ignorarCertificadosJava();
			
		}
		
	}


	/**
	 * Método para realizar um requisição HTTP/HTTPS do tipo GET.
	 * 
	 * --> application/json
	 * 
	 * @param finalUrl
	 *            Final da URL de conexão.
	 * @return
	 */
	public String httpGET(String finalUrl) {
		
		try {
			
			//Construindo URL
			String urlFinal = this.baseUrl + finalUrl;
			URL url = new URL(urlFinal);
			
			//Criando Objeto de Conexão
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			
			//Caso exista Header de Autenticação fazendos a inclusão
			if(this.autenticateHeader) {
				
				con.setRequestProperty(keyAutentication, valueKeyAutentication);
				
			}
			
			//Realizando Requisição HTTP
			if(con.getResponseCode() != 200) {
				
				logger.error(String.format("Falha na requisição HTTP! Código HTTP : [%s], Retorno [%s].", con.getResponseCode(), con.getResponseMessage()));
				return "";
				
			} else {
				
				logger.debug("Conexão realixada com sucesso!");
				
				BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
				
				String output;
				String retorno = "";
				
				while ((output = br.readLine()) != null) {
					
						retorno = retorno + output;
						
				}

				con.disconnect();
				
				//Retornando Resultado
				return retorno;
				
			}
			
		} catch (MalformedURLException e) {
			
			logger.error(String.format("Erro de formação da URL de conexão com o serviço! Mensagem : [%s]", e.getMessage()));
			e.printStackTrace();
			return "";
			
		} catch (IOException e) {
			
			logger.error(String.format("Ocorreu um erro de I/O! Mensagem : [%s]", e.getMessage()));
			e.printStackTrace();
			return "";
			
		}
		
	}

	/**
	 * 
	 * Método para realizar um requisição HTTP/HTTPS do tipo POST.
	 * 
	 * --> application/json
	 * 
	 * @param finalUrl
	 *            Final da URL de conexão
	 * @param jsonToSend
	 *            Json que será enviado para o Url/End_Point
	 * @return
	 */
	public String httpPOST(String finalUrl, String jsonToSend) {
		
		try {

			// Construindo URL
			String urlFinal = this.baseUrl + finalUrl;
			URL url = new URL(urlFinal);

			// Criando Objeto de Conexão
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");

			// Caso exista Header de Autenticação fazendos a inclusão
			if (this.autenticateHeader) {

				con.setRequestProperty(keyAutentication, valueKeyAutentication);

			}

			// Noculando o JSON na requisição
			PrintStream printStream = new PrintStream(con.getOutputStream());
			printStream.println(jsonToSend); // seta o que voce vai enviar

			con.connect();

			// Verificando Conexão
			if (con.getResponseCode() != 200) {

				logger.error("Falha : HTTP codigo do erro : " + con.getResponseCode());
				return "";

			} else {

				logger.info("Conexão realizada com sucesso!");

				BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

				String output;
				String retorno = "";

				while ((output = br.readLine()) != null) {
					retorno = retorno + output;
				}

				con.disconnect();

				return retorno;
			}
		} catch (MalformedURLException e) {

			logger.error("Erro na formação da URL de conexão com o WebSerivce : " + e.getMessage());
			return "";

		} catch (IOException e) {

			logger.error("Ocorreu um erro de I/O : " + e.getMessage());
			return "";

		}

	}

}
	

