angular.module('Dwarf_Control').factory('contasService', function($http){


	var _salvarConta = function(novaConta) {
		return $http.post("Conta", novaConta)
	}

	var _getContas = function(){
		return $http.get("Contas");
	}

	var _getConta = function(Id){
		return $http.get("Conta/" + Id);
	}

	var _updateConta = function(conta){
		return $http.put("Conta", conta);
	}

	return {
		salvarConta: _salvarConta,
		getContas: _getContas,
		getConta: _getConta,
		updateConta: _updateConta
	}

});