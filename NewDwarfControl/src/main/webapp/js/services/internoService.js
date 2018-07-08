angular.module('Dwarf_Control').factory('internoService', function($http){
	
	var _getUsuarioLogado = function(){
		return $http.post("getUsuarioLogado");
	};

	return{
		getUsuarioLogado: _getUsuarioLogado
	};


});