package br.com.controleFinanceiro.autentication;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class ManagerToken {

	private String secretFrase = "Milhoessssss"; //Não pode conter acentuação.

	Logger logger = LoggerFactory.getLogger(ManagerToken.class);

	    /***
	     * Método responsável por gerar o TOKEN para validar
	     * sessões de acesso à API.
	     *
	     * @param email email do usuário que está gerando o token para recuperação posterior.
	     * @param tempoExpirar tempo que esse Token ficará valido para acesso à aplicação. (Tempo em Segundos)
	     * @return Token
	     */
	    public String generateToken(String email, int tempoExpirar){

	        //Data e hora de geração do Token
	        Date agora = new Date();

	        //Setando Tempo de Expiração em Segundos
	        Calendar expiracao = Calendar.getInstance();
	        expiracao.add(Calendar.SECOND, tempoExpirar);

	        //Gerando Token
	        String token = Jwts.builder()
	                           .setIssuer(email)
	                           .signWith(SignatureAlgorithm.HS512, secretFrase)
	                           .setIssuedAt(agora)
	                           .setExpiration(expiracao.getTime())
	                           .compact();

	        return token;

	    }

	    /***
	     * Método responsável por verificar se um Token é valido ou não.
	     *
	     * (Utilize o retorno desse método .getIssuer() para recuperar o usuário do Token)
	     * (Exemplo : recovery.getIssuer())
	     *
	     * @param token
	     * @return
	     */
	    public Claims checkToken(String token){

	        try {

	            Claims tokenRecovery = Jwts.parser()
	                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretFrase))
	                    .parseClaimsJws(token)
	                    .getBody();

	            return tokenRecovery;

	        } catch (Exception e) {

	            logger.debug(String.format("Problema na validação do Token : [%s]", e.getMessage()));

	            return null;

	        }

	    }	
	
	
}
