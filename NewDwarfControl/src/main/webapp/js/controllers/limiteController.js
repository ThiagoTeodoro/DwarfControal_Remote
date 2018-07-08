angular.module('Dwarf_Control').controller('limiteController', function($scope, limiteService, $filter){

	$scope.getLimite = function(){

		$scope.exibirCarregando = true;

		limiteService.getLimite().then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.limite = response.data;
			$scope.limite.Valor = $filter('number')($scope.limite.Valor, 2); // Formatando para 2 casas decimais


		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("Erro interno no Servidor ao tentar obter o limite do usuário logado!");			
			
		});

	};

	//Chamando Função na Chamada do Controller
	$scope.getLimite();


	$scope.updateLimite = function(dadosLimite){

		$scope.exibirCarregando = true;
		$scope.exibirMsg = false;

		limiteService.updateLimite(dadosLimite).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;	
			$scope.exibirMsg = true;					
			$scope.dadosMensagem = response.data;
			$scope.getLimite(); //Atualiznado valor exibido do Limite

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("Erro interno no Servidor ao tentar atualizar o limite do usuário logado!");			
			
		});

	};


});



