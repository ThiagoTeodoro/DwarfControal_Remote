package br.com.dwarfcontrol.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin // Essa notação é para permitir requisições fora do mesmo HOST de excução do servidor.
public class HomeController {

    @RequestMapping(
                     value = "/",
                     method = RequestMethod.GET,
                     produces = MediaType.APPLICATION_JSON_VALUE
                   )
    public String home(){
        return "API REST DWARF CONTROL";
    }

}