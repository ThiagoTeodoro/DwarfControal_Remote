package br.com.dwarfcontrol.model.services.interfaces;

import br.com.dwarfcontrol.model.DTO.DividaDTO;
import br.com.dwarfcontrol.model.DTO.DividaUpdateDTO;
import br.com.dwarfcontrol.model.entitys.QuandoPagar;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletRequest;
import java.util.List;

public interface IQuandoPagarService {

    // Método responsável por obter a somatária total de gasto de um mês conforme mes enviado de um determinado usuário.
    Float totalGastosMes (String mesAno, ServletRequest request);

    // Método responsável por obterr uma lista das dividas de um dertirmando mes, do usuário que realizou a requisição (Logado
    List<QuandoPagar> dividasMes (String mesAno, ServletRequest request);

    //Método responsável por liquidar uma dívida, só liquidamos dividas, caso o usuário solicitante seja o mesmo da divida.
    ResponseEntity<Boolean> liquidarDivida(int id, ServletRequest request);

    //Método responspavel por cadastrar uma nova divida no banco de dados.
    ResponseEntity<QuandoPagar> salvarDivida(DividaDTO novaDivida, ServletRequest request);

    //Método responsável por deletar uma divida no banco de dados. (Só deletamos dividas que pertença ao memso usuário logado)
    ResponseEntity<Boolean> deletarDivida(int id, ServletRequest request);

    //Método responsável por recuperar uma única dívida por Id no banco de dados. (Só devolvemos dívidas pertencentes ao mesmo solicitante.
    ResponseEntity<QuandoPagar> getDivida(int id, ServletRequest request);

    /**
     * Método responsável por realizar o update de uma divida no banco de dados.
     * Só fazemos o update das dividas os quais o usuário é dono, também temos
     * uma outra preocupação, nós não devolvemos a senha para o Front-End quando
     * o mesmo faz o get para preencher os objetos, visto isso, quando esse objeto
     * chega aki ele chega sem senha e por conta disso não podemos simplismente
     * dar um update no objeto que chegou, nós temos que pelo id recuperar o
     * objeto de divida fazer o de para dos campos permitidos e só então realizar
     * o update! Isso evita falha de segurança!
     *
     * @param quandoPagar
     * @param request
     * @return
     */
    ResponseEntity<Boolean> updateDivida(DividaUpdateDTO quandoPagar, ServletRequest request);
}
