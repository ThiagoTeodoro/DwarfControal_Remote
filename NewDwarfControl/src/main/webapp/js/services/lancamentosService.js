angular.module('Dwarf_Control').factory('lancamentosService', function($http){
	
	var _obterLancamentos = function(request){
		return $http.post("get/lancamentos", request);
	};	

	var _obterSaldoAnterior = function(request){
		return $http.post("get/saldoAnterior", request);
	};

	var _obterSaldoTotalPeriodo = function(request){
		return $http.post("get/saldoTotalPeriodo", request);
	};

	var _deleteLancamento = function(id){
		return $http.delete("delete/lancamento/" + id);
	};

	var _novoLancamento = function(request){
		return $http.post("new/lancamento", request);
	};

	var _obterLancamento = function(id){
		return $http.get("get/lancamento/" + id);
	}

	var _updateLancamento = function(lancamento){
		return $http.post("update/lancamento", lancamento);
	}

	return{
		obterLancamentos: _obterLancamentos,
		obterSaldoAnterior : _obterSaldoAnterior,
		obterSaldoTotalPeriodo : _obterSaldoTotalPeriodo,
		deleteLancamento : _deleteLancamento,
		novoLancamento : _novoLancamento,
		obterLancamento : _obterLancamento,
		updateLancamento : _updateLancamento
	};


});