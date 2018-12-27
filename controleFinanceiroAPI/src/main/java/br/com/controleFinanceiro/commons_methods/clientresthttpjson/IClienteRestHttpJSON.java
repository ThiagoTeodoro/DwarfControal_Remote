package br.com.controleFinanceiro.commons_methods.clientresthttpjson;

public interface IClienteRestHttpJSON {

	/**
	 * M�todo para realizar um requisi��o HTTP/HTTPS do tipo GET.
	 * 
	 * --> application/json
	 * 
	 * @param finalUrl
	 *            Final da URL de conex�o.
	 * @return
	 */
	String httpGET(String finalUrl);
	
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
	String httpPOST(String finalUrl, String jsonToSend);

}
