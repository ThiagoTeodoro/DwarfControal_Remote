package br.com.dwarfcontrol.model.DAO;

import br.com.dwarfcontrol.model.entitys.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Repository
@Transactional
public interface IUsuarioDAO extends JpaRepository<Usuario, Integer> {

    @Query("FROM Usuario WHERE email = :email AND senha = :senha")//A tabela aki é a Entidade Hibernate por isso Usuario e não tb_usuarios.
    Usuario findByEmail(@Param("email") String email, @Param("senha") String senha);

    @Query("FROM Usuario WHERE email = :email")//A tabela aki é a Entidade Hibernate por isso Usuario e não tb_usuarios.
    Usuario findByEmail(@Param("email") String email);

    @Query("FROM Usuario WHERE nome LIKE :termo OR email like :termo") //A tabela aki é a Entidade Hibernate por isso Usuario e não tb_usuarios.
    List<Usuario> pesquisarUsuarios(@Param("termo") String termo);


}
