angular.module('Dwarf_Control').controller('quandoPagarController', function($scope, quandoPagarService, $filter){

	//O Campo é um month ele mesmo se encarrega de transformar em YYYY-MM
	$scope.filtroMes =  new Date();


	//Opções de Status de QuandoPagar
	$scope.opcoesQuitacao = [
		{"descricao" : "Pago", "quitacao" : true},
		{"descricao" : "Devendo", "quitacao" : false}
	];

	$scope.getDataFiltroFormatada = function(){

		var mes = $scope.filtroMes.getMonth() + 1; // Tem que somar mais pq aqui Janeiro é 0
		var ano = $scope.filtroMes.getFullYear();

		if(mes < 10){
			mes = '0' + mes;
		}

		return ano + '-' + mes;
	};

	//Inicializando objeto
	$scope.menssagem = {};

	$scope.novoQuandoPagar = function(novoQuandoPagar){

		$scope.exibirCarregando = true;
		$scope.menssagem.exibir = false;

		quandoPagarService.newQuandoPagar(novoQuandoPagar).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.menssagem = response.data;	

			//Lipando campos
			delete $scope.newQuandoPagar;		

			//Atualiznado Lista
			$scope.getQuandoPagar($scope.getDataFiltroFormatada());

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("Erro interno no Servidor ao tentar cadastrar novo Quando Pagar");			
			
		});
	};

	$scope.getTotalQuantoPagar = function(mesAno){

		$scope.exibirCarregando = true;
		$scope.menssagem.exibir = false;

		quandoPagarService.getTotalQuantoPagar(mesAno).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.totalQuantoPagarMes = response.data;

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("Erro interno no Servidor ao tentar obter o total de Quando Pagar");			
			
		});

	};

	$scope.getPorcentagem = function(mesAno){

		$scope.exibirCarregando = true;
		$scope.menssagem.exibir = false;

		quandoPagarService.getPorcentagem(mesAno).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.PorcetagemQuandoPagarDespesa = response.data;

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("Erro interno no Servidor ao tentar obter o porcentagem de Quando Pagar em relação ao Limite.");			
			
		});

	};


	$scope.getQuandoPagar = function(mesAno){

		$scope.exibirCarregando = true;
		$scope.menssagem.exibir = false;

		quandoPagarService.getQuandoPagar(mesAno).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.listaQuandoPagar = response.data;	
			$scope.menssagem = response.data;

			//Atualiznado Valor de Quando Pagar
			$scope.getTotalQuantoPagar(mesAno);

			//Atualizando Porcentagem
			$scope.getPorcentagem(mesAno);

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("Erro interno no Servidor ao tentar obter os Quando Pagar");			
			
		});
	};

	//Chamando função para obter os dados já cadastrados
	$scope.getQuandoPagar($scope.getDataFiltroFormatada());


	/*
     * Metodo responsável por fazer a chamada ao back-end 
     * para atualização do Status, sinalizando assim 
     * a o pagamento do Quando Pagar
     */
	$scope.atualizarStatus = function(Id){

		$scope.exibirCarregando = true;
		$scope.menssagem.exibir = false;

		quandoPagarService.updateStatusQuandoPagar(Id).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.menssagem = response.data;

			//Atualizando Lista de Exibição
			$scope.getQuandoPagar($scope.getDataFiltroFormatada());

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("Erro interno no Servidor ao tentar atualizar status do Quando Pagar");			
			
		});
	};


	//Metodo responsável por preencher os campos do quando pagar a editar.
	$scope.getQuandoPagarEditar = function(Id){

		$scope.exibirCarregando = true;

		quandoPagarService.getQuandoPagarUnico(Id).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;
			$scope.quandoPagarEditar = response.data;
			
			/*
				A Data e o Status eu não estou preenchendo por que
				os componentes do HTML eu não consegui fazer funcionar
				para exibição
			*/	
			$scope.quandoPagarEditar.Data = "";	
			$scope.quandoPagarEditar.Status = "";

			$scope.menssagem = response.data;

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("Erro interno no Servidor ao tentar obter o Quando Pagar a editar!");			
			
		});

	};


	//Metodo responsável por solicitar o Update ao back-end do Quando Pagar editado.
	$scope.updateQuandoPagarEditar = function(quandoPagar){

		$scope.exibirCarregando = true;

		quandoPagarService.updateQuandoPagarUnico(quandoPagar).then(function mySuccess(response) {
			//Ação Caso Sucesso
			$scope.exibirCarregando = false;			
			$scope.menssagem = response.data;

			//Atualizando Lista de Exibição
			$scope.getQuandoPagar($scope.getDataFiltroFormatada());

		}, function myError(response) {
			//Ação caso Erro
			$scope.exibirCarregando = false;
			console.log("Erro interno no Servidor ao tentar obter atualizar o Quando Pagar editado!");			
			
		});
		
	};

});