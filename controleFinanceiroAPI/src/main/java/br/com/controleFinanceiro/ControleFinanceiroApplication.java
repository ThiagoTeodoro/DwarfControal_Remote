package br.com.controleFinanceiro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import br.com.controleFinanceiro.autentication.FilterToken;

@SpringBootApplication
public class ControleFinanceiroApplication {

	// É Obrigatorio a Injeção do vilter por @Autowired por que você injeta coisa lá nele se não da pau!
	@Autowired
	FilterToken filterToken;

	// Registrando e aplicando Filtro para Validação por Token JWT
	@Bean
	public FilterRegistrationBean filterJwt() {

		FilterRegistrationBean frb = new FilterRegistrationBean();
		frb.setFilter(filterToken);

		// URL's Monitoradas
		frb.addUrlPatterns("/api/*");

		return frb;
	}

	//Start da Aplicação
	public static void main(String[] args) {

		SpringApplication.run(ControleFinanceiroApplication.class, args);

	}

}
