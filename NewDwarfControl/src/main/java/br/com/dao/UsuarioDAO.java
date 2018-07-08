package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.hibernate.query.Query;

import br.com.connectionfactory.EntityManagerUtil;
import br.com.entitys.Usuario;

public class UsuarioDAO extends GenericDAO<Usuario> implements Serializable {

	/**
	 * Toda classe Serializable necessita de SerialVersionID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor passando Parametros padrão
	 */
	public UsuarioDAO() {

		// Setando Parametros "Globais" para GenericDAO
		super();
		ordem = "id";
		classePersistente = Usuario.class;

	}
	
	
	//Metodos especificos deste DAO
	
	/**
	 * Metodo que seleciona um usuário pelo email enviado.
	 * 
	 * @param email
	 * @return Usuario ou null
	 */
	public Usuario getUsuarioPorEmail(String email) {
		
		try {				
			EntityManager em = EntityManagerUtil.getConnection();
			String hql = "FROM " + classePersistente.getName() +  " WHERE Email = :Email";
			Query query = (Query) em.createQuery(hql);
			query.setParameter("Email", email);
			
			//Vamos obter os dados aqui com SingleResult afinal essa consulta deve trazer apenas 1 resultado
			Usuario usuario = (Usuario) query.getSingleResult();
			//Fechando Conexão
			em.close();
			return usuario;
			
		} catch(NoResultException ex){			
			
			//Não houve resultado na consulta retornando null
			System.out.println("### Yellow Alert! Não houve resultado na Consulta getUsuarioPorEmail(). Exception [ " + ex.getMessage() + " ]" );
			return null;
			
		}
		
	}
	
	
	/**
	 * Método que realizada a pesquisa de usuários no sistema. 
	 * 
	 * @param Email a ser pesquisado
	 * @param Nome a ser pesquisado
	 * @param Nivel a ser pesquisado
	 * @return Lista com os usuários encontrados
	 */
	public List<Usuario> pesquisarPorEmailNomeNivel(String Email, String Nome, String Nivel){
		
		try{			
			EntityManager em = EntityManagerUtil.getConnection();
			//Aqui eu não preciso me preocupar com de qual é usuário pertece então posso
			//fazer 1=1 para validar o WHERE mais se eu tivesse que preocupar com 
			//de quem é o lançamento por exemplo eu validaria obritáriamente o 
			//id=idUsuarioLogado para não ter problema.
			String hql = "FROM " + classePersistente.getName() + " WHERE 1=1";
					
			//Logica do Filtro Opicional
			if(! Email.equals("")){
				hql += " and Email like :Email";
			}
			
			if(! Nome.equals("")){
				hql += " and Nome like :Nome";
			}
			
			if(! Nivel.equals("")){
				hql += " and Nivel = :Nivel";
			}
						
			//Contruido Query
			Query query = (Query) em.createQuery(hql);
			
			//Ifs novamente para atribuir os parametros
			if(! Email.equals("")){
				query.setParameter("Email", "%" + Email + "%");
			}
			
			if(! Nome.equals("")){
				query.setParameter("Nome", "%" + Nome + "%");
			}
			
			if(! Nivel.equals("")){
				query.setParameter("Nivel", Nivel);
			}
			
			//Executando Query
			List<Usuario> retorno = query.getResultList();
			
			//Fechando CONEXÂO!
			em.close();
						
			return retorno;
		
		} catch (NoResultException NoResult){
			
			System.out.println("Nenhum usuário foi encontrado! [ " + NoResult.getMessage() + " ]");
			return null;
			
		}
		
	}
	
	
	/**
	 * Esse método de pesquisa, diferente do anterior, recebe um unico campo
	 * e realisa a pesquisa com OR no sql
	 * 
	 * @param pesquisa
	 * @return List<Usuario> pesquisados, ou NULL
	 */
	public List<Usuario> pesquisarNomeEmailNivelCampoUnico(String pesquisa){
		
		try {
			
			EntityManager em = EntityManagerUtil.getConnection();
			
			String hql = "FROM " + classePersistente.getName() + " WHERE Nome LIKE :nome OR Email LIKE :email OR Nivel LIKE :nivel";
			Query query = (Query) em.createQuery(hql);
			query.setParameter("nome", "%" + pesquisa + "%");
			query.setParameter("email", "%" + pesquisa + "%");
			query.setParameter("nivel", "%" + pesquisa + "%");

			List<Usuario> retorno = query.getResultList(); 
			
			//Fechando Conexão, nunca esqueça de fechar a conexão
			em.close();
			return retorno;
			
		}catch (Exception ex) {
			
			System.out.println("### Red Alert! Ouve um erro ao tentar realizar a pesquisa no Banco de Dados! Exception [" + ex.getMessage() + " ]" );
			return null;
		}
		
	}
	

}
