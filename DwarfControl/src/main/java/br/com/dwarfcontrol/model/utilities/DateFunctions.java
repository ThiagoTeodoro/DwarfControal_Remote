package br.com.dwarfcontrol.model.utilities;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateFunctions {

    /***
     * Retorna um Date preenchido com data atual completa.
     *
     * @return
     */
    public Date now(){
        return new Date();
    }

    /**
     * Método responsável por realizar conversões de data em String para
     * outros formatos de data também em String.
     *
     * @param data Data a ser convertida
     * @param formatoAtual Formato Atual. Pattern ex: yyyy-MM-dd
     * @param formatoDesejado Formato Desejado Pattern ex: dd/MM/yyyy
     * @return
     */
    public String convertDateString(String data, String formatoAtual, String formatoDesejado){

        try {

            DateFormat formatActual = new SimpleDateFormat(formatoAtual);
            Date date = formatActual.parse(data);

            DateFormat formatWanted = new SimpleDateFormat(formatoDesejado);
            String dataFormatada = formatWanted.format(date);

            return dataFormatada;

        } catch (ParseException e) {

            System.err.println("Erro ao tentar realizar a conversão da data : " + e.getMessage());
            e.printStackTrace();

            return "";

        }

    }

    /**
     * Método para converter uma data String em um objeto Date.
     *
     * @param data
     * @param formatoDaData
     * @return
     */
    public Date convertDateStringToDate(String data, String formatoDaData){

        try {

            DateFormat format = new SimpleDateFormat(formatoDaData);
            Date dataConvertida =  format.parse(data);

            return  dataConvertida;

        } catch (ParseException e) {

            System.err.println("Erro ao converter a data : " + e.getMessage());

            e.printStackTrace();
            return null;

        }

    }

    /**
     * Metodo responsável por verificar se uma conta está vencida ou não.
     * O metodo retorna tru caso a conta esteja vencida ou se ele estiver
     * vencendo naquele mesmo dia também retornará true.
     * Usamos como comparação a data hora da solicitação do método
     * Date()NOW().
     *
     * @param vencimento
     * @return
     */
    public boolean verificavencimento(Date vencimento){

        Date hoje = new Date();

        boolean data;

        if (hoje.before(vencimento)){

            data = false;

        } else if (hoje.after(vencimento)) {

            data = true;

        } else {

            data = false;

        }

        return data;
    }


    /**
     * Método responsável por converter um Date em um Calendar
     * @return
     */
    public Calendar dateToCalendar(Date data){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);

        return calendar;

    }
}
