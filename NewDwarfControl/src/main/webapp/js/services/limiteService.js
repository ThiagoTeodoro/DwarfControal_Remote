angular.module('Dwarf_Control').factory('limiteService', function($http){
	
	var _getLimite = function(){
		return $http.get("Limite");
	};	


	var _updateLimite = function(dadosLimite){
		return $http.put("Limite", dadosLimite);
	};	


	return{
		getLimite: _getLimite,
		updateLimite: _updateLimite
	};


});