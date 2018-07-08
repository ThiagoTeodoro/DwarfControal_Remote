angular.module('Dwarf_Control').factory('administrarUsuariosService', function($http){
	
	var _getUsuarios = function(){
		return $http.get("servicoAdmUsuario");
	};

	var _salvarUsuario = function(newUsuario){
		return $http.post("servicoAdmUsuario", newUsuario);
	}; 

	var _getUsuario = function(Id){
		return $http.get("servicoAdmUsuario/" + Id );
	}

	var _updateUsuario = function(Usuario){
		return $http.put("servicoAdmUsuario", Usuario);
	}

	var _pesquisarUsuarios = function(parametroPesquisa){
		return $http.post("servicoAdmUsuario/Pesquisar", parametroPesquisa);
	}

	return{
		getUsuarios: _getUsuarios,
		salvarUsuario : _salvarUsuario,
		getUsuario: _getUsuario,
		updateUsuario: _updateUsuario,
		pesquisarUsuarios : _pesquisarUsuarios
	};


});