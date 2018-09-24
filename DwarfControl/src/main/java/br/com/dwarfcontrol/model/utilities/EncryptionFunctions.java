package br.com.dwarfcontrol.model.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.provider.MD5;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionFunctions {

    Logger logger = LoggerFactory.getLogger(EncryptionFunctions.class);

    /***
     * Método responsável por criptografar uma String em SHA256
     *
     * @param value valor a ser criptografado.
     * @return
     */
    public String toMD5(String value){

        try {

            // Adicionando Chave de Segurança do Sistema
            value = value + "Thorin Oakenshield";

            //Realizando Encripitação
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.update(value.getBytes(), 0, value.length());

            return new BigInteger(1, algorithm.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            logger.debug("Erro ao tentar realizar conversão para Hash-SHA256 : " + e.getMessage());

            return null;

        }

    }
}
