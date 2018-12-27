package br.com.controleFinanceiro.model.DAOs.interfaces;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import br.com.controleFinanceiro.model.entitys.Contas;

/**
 * Essa interface é um JPARepository, já tras porntas as funções 
 * básicas de um banco de dados e tem que ser injetada por @Autowired onde for 
 * usada.
 * 
 * @author swb_thiago
 *
 */
@Component
@Transactional
public interface IContasDAO extends JpaRepository<Contas, Integer> {
	
	//Query para Selecionar todas as contas de um usuário
    @Query("FROM Contas WHERE usuario_id = :usuario_id")
    List<Contas> allContasByUser(@Param("usuario_id") int usuario_id);
	

}
