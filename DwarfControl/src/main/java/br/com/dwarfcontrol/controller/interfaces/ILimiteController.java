package br.com.dwarfcontrol.controller.interfaces;

import br.com.dwarfcontrol.model.entitys.Limite;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;

public interface ILimiteController {

    /**
     * Serviço responsável por consultar o limite do usuário, caso o limite não existe
     * o serviço cria um limite zerado para o usuário, em seguida o devolve.
     * @return
     */
    @RequestMapping(
            value = "/consultar/limite",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Limite> consultarLimiteUsuarioLogado(ServletRequest request);

    /**
     *
     * Serviço responsável por atulizar o valor do limite do usuário logado no banco de dados.
     *
     * @param valor
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/update/limite",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Boolean> updateLimiteUsuarioLogado(@RequestBody Double valor, ServletRequest request);


    /**
     *
     * Serviço responsável por calcular quantos porcentos do limite já foi "Utilizado" em relação
     * aos gastos de QuandoPagar por mês do usuário Logado
     *
     * @param mesAno pattern [yyyy-MM]
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/consultar/porcentagemLimite",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Double> getPercentagemLimite(@RequestBody String mesAno, ServletRequest request);

}
