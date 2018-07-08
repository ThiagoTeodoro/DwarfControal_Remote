package br.com.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;

import org.hibernate.query.Query;

import br.com.connectionfactory.EntityManagerUtil;
import br.com.entitys.Limites;
import br.com.entitys.Usuario;

public class LimitesDAO extends GenericDAO<Limites> implements Serializable {

	/**
	 * Toda classe Serializable necessita de SerialVersionID
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Metodo contrutor da Classe inicializando os parametros Obrigatórios
	 */
	public LimitesDAO(HttpSession session) {
		super();
		ordem = "id";
		classePersistente = Limites.class;
		
		/*
		 * Verificando se não existe nenhum limite para o usuário 
		 * se não tiver devemos criar um inicial sempre!
		 */
		
		if(this.getLimiteUsuarioLogado(session) == null) {
			
			System.out.println("O usuário atual não possui um limite cadastrando o Limite Inicial do mesmo!");
			
			//Gerando Limite Inicial!
			Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
			
			/*
			 * Como o usuário vem vazio para o campo senha preciso selecionar ele 
			 * direto do banco pelo Id a Session
			 */
			usuarioLogado = new UsuarioDAO().localizarPorId(usuarioLogado.getId());
			
			//Preenchendo dados do Limite inicial!
			Limites limite = new Limites();
			limite.setValor(Float.parseFloat("0"));
			limite.setUsuario(usuarioLogado);
			
			if(this.persist(limite)) {
				
				System.out.println("Limite inicial cadastro com sucesso!");
				
			} else {
				
				System.out.println("Erro ao cadastrar o limite inicial!");
				
			}
			
		}
		
	}
	
	
	/**
	 * Metodo responsável por retornar o limite do usuário que está logado!
	 * 
	 * @param session Sessão atual
	 * @return Limite do usuário ou null caso o mesmo não seja encontrado
	 */
	public Limites getLimiteUsuarioLogado(HttpSession session) {
		
		try {
			Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
			
			//Como a session não armazena a senha temos que selecionar o usuário direto do banco de dados
			usuarioLogado = new UsuarioDAO().localizarPorId(usuarioLogado.getId());
			
			
			EntityManager em = EntityManagerUtil.getConnection();
			
			String hql = " FROM " + classePersistente.getName() + " WHERE Usuario = :usuario";
			Query query = (Query) em.createQuery(hql);
			query.setParameter("usuario", usuarioLogado);				
			Limites retorno = (Limites) query.getSingleResult();
			
			//Fechando Conexão com o banco 
			em.close();
			
			return retorno;
			
		} catch(NoResultException ex) {
			
			System.out.println("Não existe nenhum limite para este usuário!");
			return null;
			
		}
		
	}
}
