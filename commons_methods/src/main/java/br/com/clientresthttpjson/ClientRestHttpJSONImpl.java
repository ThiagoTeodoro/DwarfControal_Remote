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
 * Classe respons�vel por disponibilizar um Client
 * de comunica��o GET e POST por JSON via BODY no 
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
	
	//Classe respons�vel por iguinorar a cadeia de valida��o de certificado Java.
	private IguinoreHttpsJava iguinoreHttpsJava;


	/**
	 * Construtor da Classe
	 * 
	 * @param baseUrl
	 *            Base da URL que o cliente ir� se conectar.
	 * @param autenticateHeader
	 *            O servidor a conectar exige autentica��o em Header?
	 * @param keyAutentication
	 *            Nome da key para autentica��o se autenticateHeader true
	 * @param valueKeyAutentication
	 *            Valor da key para atentica��o se autenticateHeader true
	 * @param iguinorarCadeiaHttpsJava
	 *            O cliente deve iguinorar a cadeia de valida��o HTTPS do Java?
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
	 * M�todo para realizar um requisi��o HTTP/HTTPS do tipo GET.
	 * 
	 * --> application/json
	 * 
	 * @param finalUrl
	 *            Final da URL de conex�o.
	 * @return
	 */
	public String httpGET(String finalUrl) {
		
		try {
			
			//Construindo URL
			String urlFinal = this.baseUrl + finalUrl;
			URL url = new URL(urlFinal);
			
			//Criando Objeto de Conex�o
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			
			//Caso exista Header de Autentica��o fazendos a inclus�o
			if(this.autenticateHeader) {
				
				con.setRequestProperty(keyAutentication, valueKeyAutentication);
				
			}
			
			//Realizando Requisi��o HTTP
			if(con.getResponseCode() != 200) {
				
				logger.error(String.format("Falha na requisi��o HTTP! C�digo HTTP : [%s], Retorno [%s].", con.getResponseCode(), con.getResponseMessage()));
				return "";
				
			} else {
				
				logger.debug("Conex�o realixada com sucesso!");
				
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
			
			logger.error(String.format("Erro de forma��o da URL de conex�o com o servi�o! Mensagem : [%s]", e.getMessage()));
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
	 * M�todo para realizar um requisi��o HTTP/HTTPS do tipo POST.
	 * 
	 * --> application/json
	 * 
	 * @param finalUrl
	 *            Final da URL de conex�o
	 * @param jsonToSend
	 *            Json que ser� enviado para o Url/End_Point
	 * @return
	 */
	public String httpPOST(String finalUrl, String jsonToSend) {
		
		try {

			// Construindo URL
			String urlFinal = this.baseUrl + finalUrl;
			URL url = new URL(urlFinal);

			// Criando Objeto de Conex�o
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");

			// Caso exista Header de Autentica��o fazendos a inclus�o
			if (this.autenticateHeader) {

				con.setRequestProperty(keyAutentication, valueKeyAutentication);

			}

			// Noculando o JSON na requisi��o
			PrintStream printStream = new PrintStream(con.getOutputStream());
			printStream.println(jsonToSend); // seta o que voce vai enviar

			con.connect();

			// Verificando Conex�o
			if (con.getResponseCode() != 200) {

				logger.error("Falha : HTTP codigo do erro : " + con.getResponseCode());
				return "";

			} else {

				logger.info("Conex�o realizada com sucesso!");

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

			logger.error("Erro na forma��o da URL de conex�o com o WebSerivce : " + e.getMessage());
			return "";

		} catch (IOException e) {

			logger.error("Ocorreu um erro de I/O : " + e.getMessage());
			return "";

		}

	}

}
	

