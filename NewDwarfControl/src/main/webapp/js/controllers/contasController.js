angular.module('Dwarf_Control').controller('contasController', function($scope, contasService){

	$scope.tiposConta = ["Corrente", "Poupança", "Investimento"];
	$scope.menssagem = {};

	/*
	 * Metodo responsável por enviar para o BackEnd uma solicitação de
	 * cadastro de uma nova conta, enviado a conta. 
	 */
	$scope.cadastrarConta = function(novaConta){

		//Exibindo carregando e fechando menssagens anteriores.
		$scope.carregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		contasService.salvarConta(novaConta).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.carregando = false;
			$scope.menssagem = response.data;
			delete $scope.newConta;

			//Atualizando Exibição da Lista
			$scope.obterContas();

		}, function myError(response) {
			//Ação caso Erro
			$scope.carregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no servidor!";
			
		});

	};



	/*
	 * Metodo responsável por obter as contas cadastradas para este usuário
	 * do back-end.
	 */
	$scope.obterContas = function(){

		//Exibindo carregando e fechando menssagens anteriores.
		$scope.carregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		contasService.getContas().then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.carregando = false;
			

			if(response.data == null){

				$scope.menssagem.erro = true;
				$scope.menssagem.descricao = "Essa consulta não trouxe retorno, aconteceu um problema, ou você não possui nenhuma conta cadastrada ainda.";

			} else {

				$scope.listaContas = response.data;

			}

		}, function myError(response) {
			//Ação caso Erro
			$scope.carregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no servidor!";
			
		});

	};

	//Chamando preenchimento da Lista de Contas
	$scope.obterContas();


	//Metedo responsável por obter uma conta e preencher a janela modal de edição
	$scope.obterConta = function(Id){

		//Exibindo carregando e fechando menssagens anteriores.
		$scope.carregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		contasService.getConta(Id).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.carregando = false;
			$scope.menssagem = response.data;
			$scope.editarConta = response.data;

		}, function myError(response) {
			//Ação caso Erro
			$scope.carregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no servidor!";
			
		});

	};


	//Metedo responsável por solicitar a atualização de uma conta ao BackEnd
	$scope.updateConta = function(conta){

		//Exibindo carregando e fechando menssagens anteriores.
		$scope.carregando = true;
		$scope.menssagem.erro = false;
		$scope.menssagem.sucesso = false;

		contasService.updateConta(conta).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.carregando = false;
			$scope.menssagem = response.data;
			$scope.obterContas(); //Atualizando Lista			

		}, function myError(response) {
			//Ação caso Erro
			$scope.carregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no servidor!";
			
		});

	};

});