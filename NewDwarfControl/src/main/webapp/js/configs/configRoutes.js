angular.module('Dwarf_Control').config(['$routeProvider',function($routeProvider) {

	$routeProvider
	.when('/Perfil', {		
		templateUrl: 'views/interno/perfil/perfil.html',
		controller: 'perfilController'
	})
	.when('/AdministrarUsuarios', {		
		templateUrl: 'views/interno/administrarUsuarios/administrarUsuarios.html',
		controller: 'administrarUsuariosController'		
	})
	.when('/Limites', {		
		templateUrl: 'views/interno/limites/limites.html',
		controller: 'limiteController'		
	})
	.when('/QuandoPagar', {		
		templateUrl: 'views/interno/quandoPagar/quandoPagar.html',
		controller: 'quandoPagarController'		
	})
	.when('/Contas', {		
		templateUrl: 'views/interno/contas/contas.html',
		controller: 'contasController'		
	})
	.when('/UploadCSV', {		
		templateUrl: 'views/interno/uploadcsv/uploadcsv.html',
		controller: 'uploadcsvController'		
	})
	.when('/Lancamentos', {		
		templateUrl: 'views/interno/lancamentos/lancamentos.html',
		controller: 'lancamentosController'		
	})
	.when('/Relatorios', {		
		templateUrl: 'views/interno/relatorios/relatorios.html',
		controller: 'relatoriosController'		
	})



	//Caso a rota não seja valida volta lá pro Login
	.otherwise({
         redirectTo: '/'
    })
	;
}]);
