angular.module('Dwarf_Control').controller('uploadcsvController', function($scope, uploadcsvService, contasService){

	//Inicianlizando Variavel da Mensagem de Erro
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

	//Carregando Contas
	$scope.contasUsuario();


	$scope.enviarArquivo = function(file, conta){

		uploadcsvService.uploadCsv(file).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = true;
			$scope.menssagem.erro = false;
			$scope.menssagem.sucesso = false;

			uploadcsvService.importCsv(conta).then(function mySuccess(response) {
				//Ação Caso Sucesso
				$scope.exibirCarregando = false;
				$scope.menssagem = response.data;				

			}, function myError(response) {
				//Ação caso Erro
				$scope.exibirCarregando = false;
				$scope.menssagem.erro = true;
				$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
				
			});	

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.menssagem.erro = true;
			$scope.menssagem.descricao = "Erro interno de processamento no Servidor!";
			
		});	

	};


});