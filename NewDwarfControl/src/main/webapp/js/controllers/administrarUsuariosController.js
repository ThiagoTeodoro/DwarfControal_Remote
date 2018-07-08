angular.module('Dwarf_Control').controller('administrarUsuariosController', function($scope, administrarUsuariosService){

	//Array com os Níveis
	$scope.niveisUsuario = ['ADMINISTRADOR', 'USUARIO'];

	//Incializando Objeto para evitar erros
	$scope.parametroPesquisa = {};


	$scope.ObterUsuarios = function(){

		$scope.exibirCarregando = true;

		administrarUsuariosService.getUsuarios().then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.listaUsuarios = response.data;

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("### RED ALERT! Erro ao obter lista de Usuários");			
			
		});

	};

	//Chamando função para exibir usuários na chamada do controler.
	$scope.ObterUsuarios();


	$scope.salvarUsuario = function(newUsuario){

		$scope.exibirCarregando = true;

		administrarUsuariosService.salvarUsuario(newUsuario).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			
			if(response.data == true){
				$scope.ObterUsuarios();
				delete $scope.newUsuario;
			}

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("### RED ALERT! Erro ao cadastrar novo Usuário");			
			
		});

	};


	$scope.getUsuario = function(Id){

		$scope.exibirCarregando = true;

		administrarUsuariosService.getUsuario(Id).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.usuarioEditar = response.data;

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("### RED ALERT! Erro ao consultar Usuário para edição.");			
			
		});

	};

	$scope.updateUsuario = function(Usuario){

		$scope.exibirCarregando = true;
		$scope.boxMsgSucessAdmUsuario = false;
		$scope.boxMsgErroAdmUsuario = false;

		administrarUsuariosService.updateUsuario(Usuario).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.boxMsgSucessAdmUsuario = true;
			$scope.msgSucessAdmUsuario = response.data;
			$scope.ObterUsuarios();

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.boxMsgErroAdmUsuario = true;
			$scope.msgErroAdmUsuario = response.data;
			
		});


	};


	$scope.pesquisarUsuarios = function(parametroPesquisa){
		$scope.exibirCarregando = true;
		$scope.boxMsgErroAdmUsuario = false;
		$scope.boxMsgSucessAdmUsuario = false;
		administrarUsuariosService.pesquisarUsuarios(parametroPesquisa).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;			
			$scope.listaUsuarios = response.data;
		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			$scope.boxMsgErroAdmUsuario = true;
			$scope.msgErroAdmUsuario = "Erro ao obter os dados!";			
		});
	};
	
});