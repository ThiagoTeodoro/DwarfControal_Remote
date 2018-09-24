package br.com.dwarfcontrol.controller;

import br.com.dwarfcontrol.controller.interfaces.IQuandoPagarController;
import br.com.dwarfcontrol.model.DTO.DividaDTO;
import br.com.dwarfcontrol.model.DTO.DividaUpdateDTO;
import br.com.dwarfcontrol.model.entitys.QuandoPagar;
import br.com.dwarfcontrol.model.services.QuandoPagarImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.List;

@RestController
@RequestMapping("/sistema")
@CrossOrigin // Essa notação é para permitir requisições fora do mesmo HOST de excução do servidor.
public class QuandoPagarControllerImpl implements IQuandoPagarController {

    @Autowired
    private QuandoPagarImpl quandoPagarService;

    /***
     * Serviço exposto para obter o uma lista das dividas um determinado mês/Ano
     * retornamos apenas a lista do usuário que fez a requisição ou seja
     * o usuário logado
     *
     * @param request
     * @param mesAno pattern [yyyy-MM]
     * @return
     */
    @Override
    @RequestMapping(
            value = "/dividasMes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<QuandoPagar> divadasMes(@RequestBody String mesAno, ServletRequest request){

        return  quandoPagarService.dividasMes(mesAno, request);

    }

    /***
     * Serviço exposto para obter o total de gastos de um determinado mês/Ano
     * retornamos apenas do usuário que fez a requisição ou seja
     * o usuário logado
     *
     * @param request
     * @param mesAno pattern [yyyy-MM]
     * @return
     */
    @Override
    @RequestMapping(
            value = "/totalMes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Float totalMes(@RequestBody String mesAno, ServletRequest request){

        return  quandoPagarService.totalGastosMes(mesAno, request);

    }

    /**
     * Método responsável liquidar uma divida caso o usuário logado seja
     * dono da mesma divida.
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    @RequestMapping(
            value = "/liquidarDivida",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> liquidarDivida(@RequestBody int id, ServletRequest request){

        return  quandoPagarService.liquidarDivida(id, request);

    }

    /**
     * Serviço responsável por cadastrar uma nova dívida no banco de dados.
     * (A divida será cadastrada para o usuário logado (Token))
     */
    @RequestMapping(
            value = "/salvarDivida",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<QuandoPagar> salvarDivida(@RequestBody DividaDTO novaDivada, ServletRequest request) {

        //Chamando Serviço competente
        return this.quandoPagarService.salvarDivida(novaDivada, request);

    }

    /**
     * Serviço responsável por deletar uma divida.
     *
     * (Só deletamos dívidas que pertencem alquele mesmo usuário(Dono) da dívida)
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/deletarDivida",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<Boolean> deletarDivida(@RequestBody int id, ServletRequest request) {

        //Realizando Chamada ao serviço competente.
        return this.quandoPagarService.deletarDivida(id, request);

    }

    /**
     * Serviço responsável por devolver uma única dívida ao front-end solicitante.
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/divida/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<QuandoPagar> getDivida(@PathVariable("id") int id, ServletRequest request) {

        //Realizando chamada ao serviço competente
        return this.quandoPagarService.getDivida(id, request);

    }

    /**
     * Serviço responsável por realizar update da Divida no BackEnd
     *
     * @param dividaUpdate
     * @param request
     * @return
     */
    @RequestMapping(
            value = "/divida/update",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<Boolean> updateDivida(@RequestBody DividaUpdateDTO dividaUpdate, ServletRequest request) {

        return this.quandoPagarService.updateDivida(dividaUpdate, request);
    }

}
