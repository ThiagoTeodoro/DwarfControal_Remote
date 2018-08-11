angular.module('Dwarf_Control').controller('lancamentosController', function($scope, contasService, lancamentosService){

	//Preenchimento dos campos da data com a data de hj.
	$scope.requestLancamentos = {};
	$scope.requestLancamentos.dataInicial =  new Date();
	$scope.requestLancamentos.dataFinal =  new Date();


	$scope.menssagem = {};
	$scope.saldoAnterior = 0;
	$scope.saldoTotal = 0;
	$scope.exclusaoLancamento = false;
	$scope.editarLancamento = {};

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

	//Carregando Contas - To usando Funlções já pontra no Serviço do Java uploadCSV
	$scope.contasUsuario();

	//Método responsável por obter um saldo anterior no Back-End conforme request.
	$scope.obterSaldoAnterior = function(request){

		$scope.exibirCarregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		lancamentosService.obterSaldoAnterior(request).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			//Ação  da consulta.
			$scope.saldoAnterior = response.data;

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});

	}

	//Método responsável por obter um saldo Total do Periodo no Back-End conforme request.
	$scope.obterSaldoTotal = function(request){

		$scope.exibirCarregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		lancamentosService.obterSaldoTotalPeriodo(request).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			//Ação  da consulta.
			$scope.saldoTotal = response.data;

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});

	}

	//Obter lancamentos por data incial final e conta
	$scope.obterLancamentos = function(request){

		$scope.exibirCarregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		lancamentosService.obterLancamentos(request).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			//Ação  da consulta.
			$scope.listaLancamentos = response.data;

			//Logo após está ação preciso obter o Saldo Anterior e o Saldo Total
			$scope.obterSaldoAnterior(request);
			$scope.obterSaldoTotal(request);


		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});

	};



	//Função para realizar a deleção do lanaçamento no Back-End
	$scope.deleteLancamento = function(id){

		$scope.exibirCarregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		lancamentosService.deleteLancamento(id).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			//Ação  da consulta.
			$scope.exclusaoLancamento = response.data;

			if($scope.exclusaoLancamento == true){

				//Logo após deleção preciso atualizar o grid vou usar o que está setado no bind lá no Front-End
				$scope.obterLancamentos($scope.requestLancamentos);
				$scope.obterSaldoAnterior($scope.requestLancamentos);
				$scope.obterSaldoTotal($scope.requestLancamentos);

			} else {
				//Ação caso Erro
				$scope.exibirCarregando = false;
				$scope.menssagem.erro = true;
				$scope.menssagem.descricao = "Erro ao tentar excluir lanaçamento!";
			}

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});
	};



	//Método responsável por cadastrar novos lançamentos
	$scope.cadastrarLancamento = function(novoLancamento){

		//Escondendo caixas de menssagem e exibindo carregando
		$scope.exibirCarregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		lancamentosService.novoLancamento(novoLancamento).then(function mySuccess(response) {
			
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.resultadoNovoLancamento = response.data;

			if($scope.resultadoNovoLancamento){

				$scope.menssagem.sucesso = true;
				$scope.menssagem.descricao = "Lançamento cadastrado com sucesso!"

				//Limpando campo, de cadastro
				delete $scope.novoLancamento;

				//Atualizando a lista exibida para exibir no mesmo intervalo e conta do lançamento cadastrado
				$scope.requestLancamentos.conta = novoLancamento.conta;
				$scope.requestLancamentos.dataInicial = novoLancamento.data;
				$scope.requestLancamentos.dataFinal = novoLancamento.data;

				$scope.obterLancamentos($scope.requestLancamentos);
				$scope.obterSaldoAnterior($scope.requestLancamentos);
				$scope.obterSaldoTotal($scope.requestLancamentos);


			}
			
		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});


	};


	//Método responsável por obter o lançamento desejado para edição e preencher a tela de edição
	$scope.obterLancamento = function(id){

		$scope.exibirCarregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		lancamentosService.obterLancamento(id).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.editarLancamento.id = response.data.id;
			$scope.editarLancamento.conta = response.data.conta;
			$scope.editarLancamento.valor = response.data.Valor;
			$scope.editarLancamento.descricao = response.data.descricao;

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});

	};


	//Metodo responsável por atualizar o lancamento e exibir
	$scope.atualizarLancamento = function(lancamento){

		$scope.exibirCarregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		lancamentosService.updateLancamento(lancamento).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;


			if(response.data){

				//Atualizando a lista exibida para exibir no mesmo intervalo e conta do lançamento cadastrado
				$scope.requestLancamentos.conta = lancamento.conta;
				$scope.requestLancamentos.dataInicial = lancamento.data;
				$scope.requestLancamentos.dataFinal = lancamento.data;

				$scope.obterLancamentos($scope.requestLancamentos);
				$scope.obterSaldoAnterior($scope.requestLancamentos);
				$scope.obterSaldoTotal($scope.requestLancamentos);


			}

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});

	};
	
});
