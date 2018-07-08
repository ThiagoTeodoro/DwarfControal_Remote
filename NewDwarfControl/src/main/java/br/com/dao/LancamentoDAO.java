package br.com.dao;

import java.io.Serializable;

import br.com.entitys.Lancamento;

public class LancamentoDAO extends GenericDAO<Lancamento> implements Serializable{

	
	/**
	 * Obrigatório
	 */
	private static final long serialVersionUID = 1L;

	//Definindo Comportamento da Classe
	public LancamentoDAO() {
		super();
		ordem = "Id";
		classePersistente = Lancamento.class;
	}
	
}
