package br.com.controleFinanceiro.commons_methods.clientresthttpjson;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;

/**
 * Classe que iguinora a cadeia de valida��o 
 * de certificado Java.
 *
 */
public class IguinoreHttpsJava {

	//Logger da Classe
	public static Logger logger = Logger.getLogger(IguinoreHttpsJava.class);

	
	/**
	 * Metodo que cria uma cadeia SSL para que o Java 
	 * n�o use a nativa da JDK, fazendo com que as valia��es
	 * de SSL fiquem igonoradas. 
	 */
	public void ignorarCertificadosJava() {
		
		// Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        
        try {
        	
	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	 
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	 
	        
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
        } catch (Exception e) {
        	
        	logger.error(String.format(">>> Erro ao tentar criar Cadeia de SSL desabilitando valida��o do Java. Exception : [%s]." + e.getMessage()));
        	
        }
		
	}
	
	
	
}
