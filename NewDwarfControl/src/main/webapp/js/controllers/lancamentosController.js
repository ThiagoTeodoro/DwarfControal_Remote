angular.module('Dwarf_Control').controller('lancamentosController', function($scope, contasService){

	$scope.menssagem = {};

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

});