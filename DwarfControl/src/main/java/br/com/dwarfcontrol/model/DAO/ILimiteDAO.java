package br.com.dwarfcontrol.model.DAO;

import br.com.dwarfcontrol.model.entitys.Limite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ILimiteDAO extends JpaRepository<Limite, Integer> {

    //Query para Selecionar um Limite por Id de Usuário no Banco de Dados
    @Query("FROM Limite WHERE usuario_id = :usuario_id")
    Limite findByIdUsuario(@Param("usuario_id") int usuario_id);

}
