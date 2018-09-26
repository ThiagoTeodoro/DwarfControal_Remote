package br.com.dwarfcontrol.controller;

import br.com.dwarfcontrol.controller.interfaces.ILimiteController;
import br.com.dwarfcontrol.model.entitys.Limite;
import br.com.dwarfcontrol.model.services.LimiteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;

@RestController
@RequestMapping("/sistema")
@CrossOrigin
public class LimiteControllerImpl implements ILimiteController {

    @Autowired
    LimiteServiceImpl limiteService;

    /**
     * Serviço responsável por consultar o limite do usuário, caso o limite não existe
     * o serviço cria um limite zerado para o usuário, em seguida o devolve.
     *
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/consultar/limite",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<Limite> consultarLimiteUsuarioLogado(ServletRequest request) {

        return limiteService.consultarLimiteUsuarioLogado(request);

    }

    /**
     * Serviço responsável por atulizar o valor do limite do usuário logado no banco de dados.
     *
     * @param valor
     * @param request
     */
    @RequestMapping(
            value = "/update/limite",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<Boolean> updateLimiteUsuarioLogado(@RequestBody Double valor, ServletRequest request) {

        return limiteService.updateLimiteUsuarioLogado(valor, request);

    }

    /**
     * Serviço responsável por calcular quantos porcentos do limite já foi "Utilizado" em relação
     * aos gastos de QuandoPagar por mês do usuário Logado
     *
     * @param mesAno  pattern [yyyy-MM]
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/consultar/porcentagemLimite",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<Double> getPercentagemLimite(@RequestBody String mesAno, ServletRequest request) {

        return limiteService.getPercentagemLimite(mesAno, request);

    }
}
