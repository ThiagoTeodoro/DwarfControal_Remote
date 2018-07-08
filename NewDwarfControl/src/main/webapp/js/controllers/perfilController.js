angular.module('Dwarf_Control').controller('perfilController', function($scope, perfilService){


	$scope.getPerfilLogado = function(){

		$scope.exibirCarregando = true;

		perfilService.getPerfilLogado().then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.perfilUsuario = response.data;

		}, function myError(response) {
			//Ação caso Erro
			console.log("Erro ao tentar obter perfil do usuário logado!");			

		});
	};

	//Chamando a função para carregar dados do Perfil.
	$scope.getPerfilLogado();


	$scope.updatePerfilLogado = function(dadosAtualizar){

		$scope.exibirCarregando = true;
		$scope.exibirMenssagem = false;

		perfilService.updatePerfilLogado(dadosAtualizar).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;	
			$scope.exibirMenssagem = true;		
			$scope.dadosMenssagem = response.data;
			//Atualizando Informações após atualização
			$scope.getPerfilLogado();
			$scope.ObeterUsuarioLogado(); //Esse metodo é do controller interoController.

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirMenssagem = true;		
			$scope.dadosMenssagem = "Erro de processamento interno no Servidor!";

		});
	};

	$scope.updatePass = function(dadosNewSenha){

		$scope.exibirCarregando = true;
		$scope.exibirMenssagem = false;

		perfilService.updatePass(dadosNewSenha).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;	
			$scope.exibirMenssagem = true;		
			$scope.dadosMenssagem = response.data;	
			delete $scope.mudarSenha; //Apagando Campos Preenchidos		

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirMenssagem = true;		
			$scope.dadosMenssagem = "Erro de processamento interno no Servidor!";
			delete $scope.mudarSenha; //Apagando Campos Preenchidos

		});
	};


});