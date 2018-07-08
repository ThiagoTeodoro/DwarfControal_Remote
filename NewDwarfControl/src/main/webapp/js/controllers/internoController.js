angular.module('Dwarf_Control').controller('internoController', function($scope, internoService){

	$scope.ObeterUsuarioLogado = function(){


		internoService.getUsuarioLogado($scope.Usuario).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.UsuarioLogado = response.data;


		}, function myError(response) {
			//Ação caso Erro
			console.log("ERRO! AO OBTER USUARIO!");			
			
		});
	};


	//Chamando Preenchimento de UsuarioLogado
	$scope.ObeterUsuarioLogado();

});