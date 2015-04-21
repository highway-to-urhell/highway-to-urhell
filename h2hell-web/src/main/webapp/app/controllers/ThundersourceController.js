'use strict';

/**
 * ThundersourceController.js
 * 
 * @constructor
 */
var ThundersourceController = function($scope, $routeParams, $http) {
	$scope.findClassByTokenAndPath = function() {
		var tokenTarget = $routeParams.token;
		var pathTarget = $routeParams.pathClassMethodName;
		$http.get('api/ThunderSource/findSource/' + tokenTarget+'/'+pathTarget)
				.success(function(classData) {
					$scope.data=classData;
				}).error(function(message){
					$scope.messageConfig=message;
				});
	};
	$scope.findClassByTokenAndPath();
};