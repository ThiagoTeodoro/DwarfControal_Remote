angular.module('Dwarf_Control').controller('relatoriosController', function($scope, contasService, $location, relatoriosService){

	$scope.listaContas = {};
	$scope.menssagem = {};
	$scope.listaRelatorioReceitaDespesa = {};
	$scope.ocultarFormularios = false;
	$scope.ocultarGraficoReceitaDespesa = true;
	$scope.exibirTabelaReceitaDespesa = false;

	//Recuperando Lista de Contas do Usuário
	$scope.contasUsuario = function(){

		$scope.exibirCarregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		contasService.getContas().then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.listaContas = response.data;

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});
	};

	//Chamando método
	$scope.contasUsuario();

	//Chamada do relatório de despesas e receitas anual
	$scope.relatorioReceitaDespesas = function(request){

		$scope.exibirCarregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;
		$scope.tituloReceitaDespesa = {"Conta": request.conta.Descricao + "-" + request.conta.Tipo, "Ano" : request.ano};

		relatoriosService.relatorioReceitaDespesaAnualPorConta(request).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.listaRelatorioReceitaDespesa = response.data;

			//Gerando gráfico apartir da resposta

			//Declarando nome das barras no eixo Y
			$scope.labels = [];		
			for (i = 0; i < $scope.listaRelatorioReceitaDespesa.length; i++){

				$scope.labels[i] = $scope.listaRelatorioReceitaDespesa[i].nomeMes;

			}

			//Descrição de cada Barra no caso as barras do gráfico mesmo tipo legenda
			$scope.series = ['Despesas', 'Receitas'];

			//Dados da 1º série
			$scope.despesas = [];
			for (i = 0; i < $scope.listaRelatorioReceitaDespesa.length; i++){

				$scope.despesas[i] = $scope.listaRelatorioReceitaDespesa[i].valorDespesa;

			}

			//Dados da 2º série
			$scope.receitas = [];
			for (i = 0; i < $scope.listaRelatorioReceitaDespesa.length; i++){

				$scope.receitas[i] = $scope.listaRelatorioReceitaDespesa[i].valorReceita;

			}


			//Atribuindo os valores das séries para o gráfico
			$scope.data = [
						  	$scope.despesas,
						  	$scope.receitas	
						  ];

			$scope.colors = ['#DC143C', '#4682B4'];

			//Exibindo Gráficos
			$scope.ocultarGraficoReceitaDespesa = false;

			//Após a exibição do gráfico eu quero ocultar os formulários de gráficos
			$scope.ocultarFormularios = true;

			//Exibindo a Tabela de sse gráfico
			$scope.exibirTabelaReceitaDespesa = true;


		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});

	};


	//Botao Voltar
	$scope.voltar = function(){
		$scope.ocultarFormularios = false;

		//Ocultando Tabelas e Graficos caso abertas
		$scope.exibirTabelaReceitaDespesa = false;
		$scope.ocultarGraficoReceitaDespesa = true;
	}




});