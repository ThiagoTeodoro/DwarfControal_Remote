package br.com.dwarfcontrol;

import br.com.dwarfcontrol.authentication.FilterToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DwarfControlApplication extends SpringBootServletInitializer {

	//É Obrigatorio a Injeção do vilter por @Autowired por que você injeta coisa lá nele se não da pau!
	@Autowired
	FilterToken filterToken;

	//Registrando e aplicando Filtro para Validação por Token JWT
	@Bean
	public FilterRegistrationBean filterJwt(){

		FilterRegistrationBean frb = new FilterRegistrationBean();
		frb.setFilter(filterToken);

		//URL's Monitoradas

		frb.addUrlPatterns("/sistema/*");

		return frb;
	}

	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder application) {
		return application.sources (DwarfControlApplication.class);
	}


	public static void main(String[] args) {
		SpringApplication.run(DwarfControlApplication.class, args);
	}
}
