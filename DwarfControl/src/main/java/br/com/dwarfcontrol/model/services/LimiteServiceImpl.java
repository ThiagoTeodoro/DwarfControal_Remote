package br.com.dwarfcontrol.model.services;

import br.com.dwarfcontrol.model.DAO.ILimiteDAO;
import br.com.dwarfcontrol.model.DAO.IUsuarioDAO;
import br.com.dwarfcontrol.model.entitys.Limite;
import br.com.dwarfcontrol.model.entitys.Usuario;
import br.com.dwarfcontrol.model.services.interfaces.ILimiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;

@Service
public class LimiteServiceImpl implements ILimiteService {

    @Autowired
    AuthenticatorServiceImpl authenticatorService;

    @Autowired
    QuandoPagarImpl quandoPagarService;

    @Autowired
    IUsuarioDAO usuarioDAO;

    @Autowired
    ILimiteDAO limiteDAO;

    private Logger logger = LoggerFactory.getLogger(LimiteServiceImpl.class);

    /**
     * Serviço responsável por consultar o limite do usuário que está logado no banco
     * de dados, caso o limite não exista o metodo cria um limite zerado automaticamente
     * pois todos os usuário devem ter um limite.
     * Se criado ou se consultado esse limite é retornado.
     *
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Limite> consultarLimiteUsuarioLogado(ServletRequest request) {

        String token = authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        String emailToken = authenticatorService.getEmailToken(token);

        //Selecionando o Usuário que está logado
        Usuario usuarioLogado = usuarioDAO.findByEmail(emailToken);

        if( usuarioLogado != null){

            //Verificando se o usuário possui um Limite se não vamos cadastrar um zerado para ele e devolver
            Limite limiteUsuarioLogado = limiteDAO.findByIdUsuario(usuarioLogado.getId());

            if(limiteUsuarioLogado != null){

                //Devolvendo Limite
                return new ResponseEntity<Limite>(limiteUsuarioLogado, HttpStatus.OK);

            } else {

                logger.info("Esse usuário não possui limite inicial cadastrando, Cadastrando limite inicial zerado!");

                //Cadastrando um Limite Zerado para o Usuário, todos os usuários devem ter um limite.
                Limite novoLimite = new Limite();
                novoLimite.setUsuario(usuarioLogado);
                novoLimite.setValor(0.00);

                Limite limiteCadastrado = limiteDAO.save(novoLimite);

                if(limiteCadastrado != null){

                    return new ResponseEntity<Limite>(limiteCadastrado, HttpStatus.OK);

                } else {

                    //Não encontramos o usuário
                    logger.error("Erro ao tentar gravar o limite para usuário no banco de dados!");
                    return new ResponseEntity<Limite>(HttpStatus.EXPECTATION_FAILED);

                }

            }

        } else {

            //Não encontramos o usuário
            logger.error("Não foi encontrado nenhum usuário para esse Token de requisição!");
            return new ResponseEntity<Limite>(HttpStatus.NO_CONTENT);

        }

    }

    /**
     * Método responsável por realizar o update do valor do limite no banco de dados
     *
     * @param valor
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Boolean> updateLimiteUsuarioLogado(Double valor, ServletRequest request) {

        String token = authenticatorService.getValueFromKeyServletRequest("Authorization", request);

        String emailToken = authenticatorService.getEmailToken(token);

        //Selecionando o Usuário que está logado
        Usuario usuarioLogado = usuarioDAO.findByEmail(emailToken);

        if( usuarioLogado != null){

            //Verificando se o usuário possui um Limite se não vamos cadastrar um zerado para ele e devolver
            Limite limiteUsuarioLogado = limiteDAO.findByIdUsuario(usuarioLogado.getId());

            if(limiteUsuarioLogado != null){

                //Atulizando Limite
                limiteUsuarioLogado.setValor(valor);

                if(limiteDAO.save(limiteUsuarioLogado) != null){

                    //Limite atualizado
                    return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);

                } else {

                    //Não encontramos o usuário
                    logger.error("Erro ao tentar atualizar limite no banco de dados!");
                    return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);

                }

            } else {

                //Não encontramos o usuário
                logger.error("Limite do usuário não localizado! Impossivel realizar operação de atualização!");
                return new ResponseEntity<Boolean>(HttpStatus.EXPECTATION_FAILED);

            }

        } else {

            //Não encontramos o usuário
            logger.error("Não foi encontrado nenhum usuário para esse Token de requisição!");
            return new ResponseEntity<Boolean>(HttpStatus.NO_CONTENT);

        }

    }

    /**
     * Método responsável por calcular a porcentagem de "uso" do limite em ralação ao
     * usuário logado e mês enviado!
     *
     * @param mesAno
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Double> getPercentagemLimite(String mesAno, ServletRequest request) {

        //Obtendo o Limite do usuário Logado
        Double valorLimite = this.consultarLimiteUsuarioLogado(request).getBody().getValor();

        //Obtendo o Valor total de Quando Pagar do Mês atual
        Double valorGastoTotalQuandoPagarMes = quandoPagarService.totalGastosMes(mesAno, request).doubleValue();

        //Realizando Regra de 3
        Double porcentagem = (valorGastoTotalQuandoPagarMes * 100) / valorLimite;

        return new ResponseEntity<Double>(porcentagem, HttpStatus.OK);

    }
}
