package br.com.controleFinanceiro.model.DAOs.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.controleFinanceiro.model.entitys.Limites;

public interface ILimitesDAO extends JpaRepository<Limites, Integer> {
	
	@Query("FROM Limites WHERE usuario_id = :usuario_id")
	Limites getLimiteByUsuario(@Param("usuario_id") int usuario_id);

}
