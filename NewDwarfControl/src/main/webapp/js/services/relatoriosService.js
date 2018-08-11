angular.module('Dwarf_Control').factory('relatoriosService', function($http){

	var _relatorioReceitaDespesaAnualPorConta = function(request){
		return $http.post("Relatorios/RelatorioReceitaDespesaAno", request);
	}

	return{
		relatorioReceitaDespesaAnualPorConta : _relatorioReceitaDespesaAnualPorConta
	}


});
