package br.com.controleFinanceiro.model.DAOs.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.controleFinanceiro.model.entitys.Dividas;

public interface IDividasDAO extends  JpaRepository<Dividas, Integer>{

	//Query para Selecionar todas as dividas de um usuário
    @Query("FROM Dividas WHERE usuario_id = :usuario_id AND MONTH(dataVencimento) = :mes AND YEAR(dataVencimento) = :ano ")
    List<Dividas> allDividasByUser(@Param("usuario_id") int usuario_id, @Param("mes") int mes, @Param("ano") int ano);
    
    
    //Query para Selecionar uma divida baseada em id de lançamento
    @Query("FROM Dividas WHERE lancamento_id = :lancamento_id ORDER BY dataVencimento")
    Dividas dividaByIdLancamento(@Param("lancamento_id") int lancamento_id);
	
}
