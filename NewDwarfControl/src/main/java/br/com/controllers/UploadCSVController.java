package br.com.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.hibernate.query.criteria.internal.expression.function.FunctionExpression;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import br.com.dao.ContaDAO;
import br.com.dao.LancamentoDAO;
import br.com.entitys.Conta;
import br.com.entitys.Lancamento;
import br.com.objetos.ObjetoMenssagemFrontEnd;
import br.com.utilitarios.FuncoesData;
import br.com.utilitarios.FuncoesNumericas;


@Controller
public class UploadCSVController {
	
	private String ArquivoFrontEnd;
	
	
	//Lendo e armazenando na classe conteudo recebido do arquivo.
	@RequestMapping(value="UploadCSV", method=RequestMethod.POST)
	@ResponseBody
	public String armazenaArquivoUpload(@RequestBody String ArquivoFrontEnd) {	
		this.ArquivoFrontEnd = ArquivoFrontEnd;
		return "";
	}
		
	//Importando lançamentos
	@RequestMapping(value="UploadCSV/Import", method=RequestMethod.POST)
	@ResponseBody
	public String importarArquivoEnviado(@RequestBody String contaJSON) throws IOException {
	
		JSONObject objContaJSON = new JSONObject(contaJSON);		
		Conta conta = new ContaDAO().localizarPorId(Long.parseLong(objContaJSON.get("Id").toString()));		
		FuncoesData funcoesData = new FuncoesData();
		FuncoesNumericas funcoesNumericas = new FuncoesNumericas();
		LancamentoDAO lancamentoDAO = new LancamentoDAO();
			
			//Lendo Contéudo do Arquivo
			
			StringReader lerString = new StringReader(this.ArquivoFrontEnd);
			
			BufferedReader lerArq = new BufferedReader(lerString);
	
			String linha = lerArq.readLine(); //Lê a primeira linha
			
			//Controle de Erro
			boolean erro = false;
			
			
			/*
			 * Cada linha aqui será quebrada por ";"
			 * Isso resultará nos seguintes pontos por posição,
			 * 
			 * Posição 0 - Data
			 * Posição 1 - Descrição
			 * Posição 2 - Valor
			 */
			while (linha != null) {
				
				String[] dados = linha.split(";");
				Lancamento lancamento = new Lancamento();
				lancamento.setData(funcoesData.dateToCalendar(funcoesData.stringToDate("dd/MM/yyyy",  dados[0])));
				lancamento.setDescricao(dados[1]);
				lancamento.setValor(funcoesNumericas.toFloat(dados[2]));
				lancamento.setConta(conta);				
				
				if(lancamentoDAO.persist(lancamento) != true) {
					erro = true;
				}
				
				linha = lerArq.readLine(); // lê da segunda até a última linha
				dados = null;
				lancamento = null;
			}
			
			if(erro == true) {
				
				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				msg.setErro(true);
				msg.setDescricao("Houve um erro ao tentar inserir os lançamentos!");
				
				//Apagando Dados do aquivo armazenado para não conflitar com futura importação
				this.ArquivoFrontEnd = "";
				
				return new Gson().toJson(msg);
				
			} else {

				ObjetoMenssagemFrontEnd msg = new ObjetoMenssagemFrontEnd();
				msg.setSucesso(true);
				msg.setDescricao("Lançamentos Importados com Sucesso!");
				
				//Apagando Dados do aquivo armazenado para não conflitar com futura importação
				this.ArquivoFrontEnd = "";
				
				return new Gson().toJson(msg);
							
			}
	
	}

}
