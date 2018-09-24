package br.com.dwarfcontrol.model.services;

import br.com.dwarfcontrol.model.DAO.IQuandoPagarDAO;
import br.com.dwarfcontrol.model.DAO.IUsuarioDAO;
import br.com.dwarfcontrol.model.DTO.DividaDTO;
import br.com.dwarfcontrol.model.DTO.DividaUpdateDTO;
import br.com.dwarfcontrol.model.entitys.QuandoPagar;
import br.com.dwarfcontrol.model.entitys.Usuario;
import br.com.dwarfcontrol.model.services.interfaces.IQuandoPagarService;
import br.com.dwarfcontrol.model.utilities.DateFunctions;
import org.omg.IOP.TAG_ALTERNATE_IIOP_ADDRESS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import java.util.List;

@Service
public class QuandoPagarImpl implements IQuandoPagarService {

    @Autowired
    IQuandoPagarDAO quandoPagarDAO;

    @Autowired
    DateFunctions operacoesData;

    @Autowired
    AuthenticatorServiceImpl authenticatorService;

    @Autowired
    IUsuarioDAO usuarioDAO;

    Logger logger = LoggerFactory.getLogger(QuandoPagar.class);

    /***
     * Mètodo responsável por obter a somatoria total de gastos de um determinado mês/ano
     * enviado como parametro, retornamos apenas do usuário que fez a requisição ou seja
     * o usuário logado
     *
     * @param  request
     * @param mesAno pattern [yyyy-MM]
     * @return
     */
    @Override
    public Float totalGastosMes(String mesAno, ServletRequest request) {

        /*
            Aki eu não preciso verificar nada o Filter já vai checar se o Token é valido e esse
            metodo, pode ser acessado tanto por adms quantos por usuários comuns.
         */

        String emailLogado =  authenticatorService.getEmailToken(authenticatorService.getValueFromKeyServletRequest("Authorization", request));

        //Recuperando O Usuário que está logado para pegar o Id
        Usuario usuarioLogado = usuarioDAO.findByEmail(emailLogado);

        int mes = Integer.parseInt(operacoesData.convertDateString(mesAno, "yyyy-MM", "MM"));
        int ano = Integer.parseInt(operacoesData.convertDateString(mesAno, "yyyy-MM", "yyyy"));
        int id = usuarioLogado.getId();

        return this.quandoPagarDAO.totalGastosMes(mes, ano, id);

    }

    /**
     * Método responsavel por obter uma lista das dividas de um mês/Ano do usuário que está logado(Token)
     *
     * @param mesAno
     * @param request
     * @return
     */
    @Override
    public List<QuandoPagar> dividasMes(String mesAno, ServletRequest request) {

        /*
            Aki eu não preciso verificar nada o Filter já vai checar se o Token é valido e esse
            metodo, pode ser acessado tanto por adms quantos por usuários comuns.
         */

        String emailLogado =  authenticatorService.getEmailToken(authenticatorService.getValueFromKeyServletRequest("Authorization", request));

        //Recuperando O Usuário que está logado para pegar o Id
        Usuario usuarioLogado = usuarioDAO.findByEmail(emailLogado);

        int mes = Integer.parseInt(this.operacoesData.convertDateString(mesAno, "yyyy-MM", "MM"));
        int ano = Integer.parseInt(this.operacoesData.convertDateString(mesAno, "yyyy-MM", "yyyy"));
        int id = usuarioLogado.getId();

        List<QuandoPagar> listaQuandoPagar = this.quandoPagarDAO.quandoPagarMes(mes, ano, id);

        //Removendo a senha do usuário, não mandamos isso para o front-end
        for(QuandoPagar objeto : listaQuandoPagar){

            //Se o documento estiver vencido e não estiver pago então mando vencido igual true por padrão já vem false
            //então não preciso tratar o o else
            if(this.operacoesData.verificavencimento(objeto.getData().getTime()) && objeto.isStatus() != true){

                objeto.setVencido(true);

            }

            objeto.getUsuario().setSenha("");

        }

        return listaQuandoPagar;

    }

    /**
     * Método respnsável por liquidar uma divida no banco de dados.
     *
     * Só é possivel liquidar dividas, do usuário a qual a divida pertence.
     * Então só sera liquidada a divada que pertencer ao solicitante logado(Token)
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Boolean> liquidarDivida(int id, ServletRequest request) {

        //Recuperando o Email do Token logado enviado na requisição
        String emailLogado =  authenticatorService.getEmailToken(
                authenticatorService.getValueFromKeyServletRequest("Authorization", request));

        //Recuperando a Divida a ser liquidada
        QuandoPagar divida = this.quandoPagarDAO.findById(Long.parseLong(String.valueOf(id))).get();

        if(divida != null){

            //Verificando se o usuário da dívida é o mesmo do e-mail logado (Token) se sim faremos a liquidação
            if(divida.getUsuario().getEmail().equals(emailLogado)){

                //Liquidando divida
                divida.setStatus(true);

                //Gravando no banco de dados.
                if(this.quandoPagarDAO.save(divida) != null){

                    String msg = "Divida liquidada com sucesso!";

                    System.out.println(msg);

                    return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);

                } else {

                    String msg = "Ouve um erro ao tentar gravar a liquidação da divida no banco de dados! Retornando " +
                                 "erro 417, Exception Failed! ";

                    System.err.println(msg);

                    return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);
                }

            } else {

                String msg = String.format("O Usuário solicitante : [%s] não é o mesmo da dívida : [%s]" +
                                           " por tanto não pode realizar essa operação, retornando erro :" +
                                           " 403, Forbidden.", emailLogado, divida.getUsuario().getEmail());

                System.err.println(msg);

                return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);

            }

        } else {

            System.err.println("Nenhuma dívida com esse Id foi localizada! Retornando erro 204, (No Content)");
            return new ResponseEntity<Boolean>(HttpStatus.NO_CONTENT);

        }

    }

    /**
     * Método responsável por salvar uma divida no banco de dados
     *
     * @param novaDivida
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<QuandoPagar> salvarDivida(DividaDTO novaDivida, ServletRequest request) {

        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);
        String emailLogado = this.authenticatorService.getEmailToken(token);

        //Recuperando Usuário
        Usuario usuarioLogado = this.usuarioDAO.findByEmail(emailLogado);

        if(usuarioLogado != null){

            QuandoPagar quandoPagar = novaDivida.toQuandoPagar(usuarioLogado);

            //Tentando Salvar quando Pagar.
            QuandoPagar quandoPagarSalvo = this.quandoPagarDAO.save(quandoPagar);

            if(quandoPagarSalvo != null){

                return new ResponseEntity<QuandoPagar>(quandoPagarSalvo, HttpStatus.OK);

            } else {

                //Houve um erro ao tentar salvar o QuandoPagar
                return new ResponseEntity<QuandoPagar>(HttpStatus.EXPECTATION_FAILED);

            }

        } else {

            //Não encontramos o usuário da requisição!
            return new ResponseEntity<QuandoPagar>(HttpStatus.FAILED_DEPENDENCY);

        }

    }

    /**
     * Método responsável por apagar uma divida no banco de dados.
     *
     * Só deletamos dívidas que pertencem ao mesmo usuário.
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Boolean> deletarDivida(int id, ServletRequest request) {

        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);
        String emailLogado = this.authenticatorService.getEmailToken(token);

        //Recuperando Divida que foi encaminhada para exclusão
        QuandoPagar dividaExcluir = this.quandoPagarDAO.findById(Long.parseLong(String.valueOf(id))).get();

        //Verificando se a dívida foi encontrada
        if(dividaExcluir != null){

            //Verificando se a Divida pertence ao mesmo usuário
            if(emailLogado.equals(dividaExcluir.getUsuario().getEmail())){

                //Realizando Exclusão da Dívida
                this.quandoPagarDAO.delete(dividaExcluir);

                //Retornando resposta ao Front-End
                return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);

            } else {

                this.logger.error(String.format("O usuário logado e dívida solicita para exclução não coicidem." +
                                                " Só podemos excluir dívidas do usuário solicitante. Email " +
                                                " Usuário Logado [%s], Email Usuário da Dívida [%s].", emailLogado,
                                                dividaExcluir.getUsuario().getEmail()));

                return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);

            }

        } else {

            this.logger.error(String.format("A divida de id : [%s], não existe no nosso banco de dados!", id));
            return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);

        }

    }

    /**
     * Método responsável por recuperar uma única dívida, do banco de dados, caso a dívida pertença
     * ao solicitante (Token "Logado")
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<QuandoPagar> getDivida(int id, ServletRequest request) {

        //Recuperando Email do Token "Logado"
        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);
        String emailLogado = this.authenticatorService.getEmailToken(token);

        //Recuperando Divida
        QuandoPagar divida = this.quandoPagarDAO.findById(Long.parseLong(String.valueOf(id))).get();

        if(divida != null){

            //Verificando se a Divida existe no nosso Banco de Dados
            if(divida.getUsuario().getEmail().equals(emailLogado)){

                //Não retornamos senha para o Front-End por tanto estamos removendo ela aki para remover
                divida.getUsuario().setSenha("");

                //Tudo certo retornando a Divida Solicita
                return new ResponseEntity<QuandoPagar>(divida, HttpStatus.OK);

            } else {

                this.logger.error(String.format("O usuário logado e dívida solicita não coicidem." +
                                                " Só podemos devolver dívidas do usuário solicitante. Email " +
                                                " Usuário Logado [%s], Email Usuário da Dívida [%s].", emailLogado,
                                                divida.getUsuario().getEmail()));

                return new ResponseEntity<QuandoPagar>(HttpStatus.FORBIDDEN);


            }

        } else {

            //Não encontramos nenhuma dívida com esse id.
            this.logger.error(String.format("A divida de id : [%s], não existe no nosso banco de dados!", id));
            return new ResponseEntity<QuandoPagar>(HttpStatus.EXPECTATION_FAILED);
        }

    }

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
    @Override
    public ResponseEntity<Boolean> updateDivida(DividaUpdateDTO quandoPagar, ServletRequest request) {


        //Recuperando Email do Token "Logado"
        String token = this.authenticatorService.getValueFromKeyServletRequest("Authorization", request);
        String emailLogado = this.authenticatorService.getEmailToken(token);

        //Recuperando Divida
        QuandoPagar dividaBancoDeDados = this.quandoPagarDAO.findById(Long.parseLong(String.valueOf(quandoPagar.getId()))).get();

        if(dividaBancoDeDados != null){

            //Verificando se a Divida existe no nosso Banco de Dados
            if(dividaBancoDeDados.getUsuario().getEmail().equals(emailLogado)){

                //Atulizando a divida do Banco de Dados com os Dados recebidos passiveis de alteração
                dividaBancoDeDados.setData(this.operacoesData.dateToCalendar(this.operacoesData.convertDateStringToDate(quandoPagar.getData(), "yyyy-MM-dd")));
                dividaBancoDeDados.setValor(quandoPagar.getValor());
                dividaBancoDeDados.setDescricao(quandoPagar.getDescricao());
                dividaBancoDeDados.setStatus(quandoPagar.isStatus());
                //Id não precisa atulizar

                //Realizando Update
                if(this.quandoPagarDAO.save(dividaBancoDeDados) != null){

                    //Deu Certo
                    return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);

                } else {

                    //Houve um erro ao tentar ralizar o Update no Banco de Dados
                    this.logger.error(String.format("Houve um erro ao tentar ralizar o Update no Banco de Dados"));
                    return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);

                }

            } else {

                this.logger.error(String.format("O usuário logado e dívida solicita não coicidem." +
                                " Só podemos devolver dívidas do usuário solicitante. Email " +
                                " Usuário Logado [%s], Email Usuário da Dívida [%s].", emailLogado,
                        dividaBancoDeDados.getUsuario().getEmail()));

                return new ResponseEntity<Boolean>(HttpStatus.FORBIDDEN);


            }

        } else {

            //Não encontramos nenhuma dívida com esse id.
            this.logger.error(String.format("A divida de id : [%s], não existe no nosso banco de dados!", quandoPagar.getId()));
            return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);
        }


    }
}
