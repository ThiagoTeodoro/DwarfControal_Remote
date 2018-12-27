package br.com.clientresthttpjson;

public interface IClienteRestHttpJSON {

	/**
	 * Método para realizar um requisição HTTP/HTTPS do tipo GET.
	 * 
	 * --> application/json
	 * 
	 * @param finalUrl
	 *            Final da URL de conexão.
	 * @return
	 */
	String httpGET(String finalUrl);
	
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
	String httpPOST(String finalUrl, String jsonToSend);

}
