package br.com.dwarfcontrol.model.DAO;

import br.com.dwarfcontrol.model.entitys.QuandoPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IQuandoPagarDAO extends JpaRepository<QuandoPagar, Long> {

    @Query("SELECT SUM(valor) FROM QuandoPagar WHERE MONTH(data) >= :mes AND MONTH(data) <= :mes AND YEAR(data) = :ano AND usuario_id = :idUsuario")
    Float totalGastosMes(@Param("mes") int mes, @Param("ano") int ano, @Param("idUsuario") int id);

    @Query("FROM QuandoPagar WHERE MONTH(data) >= :mes AND MONTH(data) <= :mes AND YEAR(data) = :ano AND usuario_id = :idUsuario ORDER BY data")
    List<QuandoPagar> quandoPagarMes(@Param("mes") int mes, @Param("ano") int ano, @Param("idUsuario") int id);

}
