package br.com.controleFinanceiro.model.DAOs.interfaces;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.controleFinanceiro.model.entitys.Usuarios;

public interface IUsuarioDAO extends JpaRepository<Usuarios, Integer> {
	
	//Método responsável por pesquisar e encontrar um Usuario por email na base de dados.
	@Query("FROM Usuarios WHERE email = :email")
	Usuarios findByEmail(@Param("email") String email);

}
