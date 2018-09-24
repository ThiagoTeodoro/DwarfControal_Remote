package br.com.dwarfcontrol.controller.interfaces;

import br.com.dwarfcontrol.model.DTO.DividaDTO;
import br.com.dwarfcontrol.model.DTO.DividaUpdateDTO;
import br.com.dwarfcontrol.model.entitys.QuandoPagar;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.print.attribute.standard.Media;
import javax.servlet.ServletRequest;
import java.util.List;

public interface IQuandoPagarController {

    //Serviço  para obter as dividas de um mês conforme mês enviado, só devolvemos a divída do usuário logado (Token)
    @RequestMapping(
            value = "/dividasMes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<QuandoPagar> divadasMes(@RequestBody String mesAno, ServletRequest request);

    /**
     * Serviço  para obter o total das dividas de um mês conforme mês enviado, só devolvemos o total das divída
     * do usuário logado (Token)
     */
    @RequestMapping(
            value = "/totalMes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    Float totalMes(@RequestBody String mesAno, ServletRequest request);

    /**
     * Serviço para liquidar a divida conforme id solicitado, só liquidamos dividas do mesmo usuário logado (Token)
     * dono da divida.
     */
    @RequestMapping(
            value = "/liquidarDivida",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Boolean> liquidarDivida(@RequestBody int id, ServletRequest request);


    /**
     * Serviço responsável por cadastrar uma nova dívida no banco de dados.
     * (A divida será cadastrada para o usuário logado (Token))
     */
    @RequestMapping(
            value = "/salvarDivida",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<QuandoPagar> salvarDivida(@RequestBody DividaDTO novaDivada, ServletRequest request);


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
    ResponseEntity<Boolean> deletarDivida(@RequestBody int id, ServletRequest request);


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
    ResponseEntity<QuandoPagar> getDivida(int id, ServletRequest request);

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
    ResponseEntity<Boolean> updateDivida(DividaUpdateDTO dividaUpdate, ServletRequest request);
}
