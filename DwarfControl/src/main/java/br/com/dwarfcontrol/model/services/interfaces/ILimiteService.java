package br.com.dwarfcontrol.model.services.interfaces;

import br.com.dwarfcontrol.model.entitys.Limite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.ServletRequest;

public interface ILimiteService {

    /**
     * Serviço responsável por consultar o limite do usuário que está logado no banco
     * de dados, caso o limite não exista o metodo cria um limite zerado automaticamente
     * pois todos os usuário devem ter um limite.
     * Se criado ou se consultado esse limite é retornado.
     *
     * @param request
     * @return
     */
    ResponseEntity<Limite> consultarLimiteUsuarioLogado(ServletRequest request);

    /**
     * Método responsável por realizar o update do valor do limite no banco de dados
     *
     * @param valor
     * @param request
     * @return
     */
    ResponseEntity<Boolean> updateLimiteUsuarioLogado( Double valor, ServletRequest request);


    /**
     * Método responsável por calcular a porcentagem de "uso" do limite em ralação ao
     * usuário logado e mês enviado!
     *
     * @param mesAno
     * @param request
     * @return
     */
    ResponseEntity<Double> getPercentagemLimite(String mesAno, ServletRequest request);

}
