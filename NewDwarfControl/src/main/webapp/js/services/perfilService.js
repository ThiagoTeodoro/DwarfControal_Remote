angular.module('Dwarf_Control').factory('perfilService', function($http){
	
	var _getPerfilLogado = function(){
		return $http.get("Perfil");
	};	

	var _updatePerfilLogado = function(dadosAtualizar){
		return $http.put("Perfil", dadosAtualizar);
	};	

	var _updatePass = function(dadosNewSenha){
		return $http.put("Perfil/UpdatePass", dadosNewSenha);
	};

	return{
		getPerfilLogado: _getPerfilLogado,
		updatePerfilLogado: _updatePerfilLogado,
		updatePass: _updatePass
	};


});