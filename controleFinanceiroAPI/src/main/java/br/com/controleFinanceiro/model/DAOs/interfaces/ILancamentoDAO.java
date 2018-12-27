package br.com.controleFinanceiro.model.DAOs.interfaces;

import java.util.List;

import javax.transaction.Transactional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import br.com.controleFinanceiro.model.entitys.Lancamentos;

@Transactional
@Service
public interface ILancamentoDAO extends JpaRepository<Lancamentos, Integer> {
	
	/**
	 * Serviço para obter todas as receitas de um determinado usuário.
	 * 
	 * @param usuario_id
	 * @return
	 */
	@Query("FROM Lancamentos WHERE usuario_id = :usuario_id AND valor > 0 ORDER BY data")
	List<Lancamentos> getLancamentosReceitasPorUsuario(@Param("usuario_id") int usuario_id);
	
	/**
	 * Serviço para obter todas as despesas de um determinado usuário.
	 * 
	 * @param usuario_id
	 * @return
	 */
	@Query("FROM Lancamentos WHERE usuario_id = :usuario_id AND valor < 0")
	List<Lancamentos> getLancamentosDespesasPorUsuario(@Param("usuario_id") int usuario_id);
	
	/**
	 * Serviço para obter todos os lançamentos de um determinado usuário.
	 * 
	 * @param usuario_id
	 * @return
	 */
	@Query("FROM Lancamentos WHERE usuario_id = :usuario_id")
	List<Lancamentos> getLancamentosPorUsuario(@Param("usuario_id") int usuario_id);

	/**
	 * Serviço para informar a quantidade de lançamentos
	 * que uma conta possui.
	 * 
	 * @param conta_id
	 * @return
	 */
	@Query("FROM Lancamentos WHERE conta_id = :conta_id")
	List<Lancamentos> getLancamentosConta(@Param("conta_id") int conta_id);
	
}
