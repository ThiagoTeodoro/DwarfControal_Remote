angular.module('Dwarf_Control').factory('uploadcsvService', function($http){


	var _uploadCsv = function(file){

		return $http.post("UploadCSV", file);
	};

	var _importCsv = function(Conta){

		return $http.post("UploadCSV/Import", Conta);
	};

	return{
		uploadCsv: _uploadCsv,
		importCsv: _importCsv
	};

});