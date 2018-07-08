angular.module('Dwarf_Control').factory('quandoPagarService', function($http){
	
	var _newQuandoPagar = function(novoQuandoPagar){
		return $http.post("QuandoPagar", novoQuandoPagar);
	};	

	var _getQuandoPagar = function(mesAno){
		return $http.get("QuandoPagar/" + mesAno);
	};

	var _getTotalQuantoPagar = function(mesAno){
		return $http.get("QuandoPagar/Total/" + mesAno);
	};

	var _getPorcentagem = function(mesAno){
		return $http.get("QuandoPagar/Porcentagem/" + mesAno);
	};

	var _updateStatusQuandoPagar = function(Id){
		return $http.put("QuandoPagar/Quitar/" + Id);
	}

	var _getQuandoPagarUnico = function(Id){
		return $http.get("QuandoPagar/QuandoPagar/" + Id);
	}

	var _updateQuandoPagarUnico = function(quandoPagar){
		return $http.put("QuandoPagar/QuandoPagar/Update", quandoPagar);
	}

	return{
		newQuandoPagar: _newQuandoPagar,
		getQuandoPagar: _getQuandoPagar,	
		getTotalQuantoPagar: _getTotalQuantoPagar,
		getPorcentagem: _getPorcentagem,
		updateStatusQuandoPagar: _updateStatusQuandoPagar,
		getQuandoPagarUnico: _getQuandoPagarUnico,
		updateQuandoPagarUnico: _updateQuandoPagarUnico
	};


});