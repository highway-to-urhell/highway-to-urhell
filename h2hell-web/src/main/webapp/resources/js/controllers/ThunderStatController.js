'use strict';

/**
 * ThunderStatController.js
 * @constructor
 */
var ThunderStatController = function($scope,$routeParams, $http) {
    $scope.findByToken = function() {
    	var tokenTarget = $routeParams.token;
    	$http.post('api/ThunderStat/findThunderStatByToken/'+tokenTarget).success(function(message){
            $scope.ms = message;
        });
    };

    $scope.findAll = function() {
        $http.get('api/ThunderStat/findAllThunderStat').success(function(statListResult) {
        	$scope.statlist = statListResult;
        });
    };
    
    $scope.findToken = function() {
        $http.get('api/ThunderApp/findAllThunderApp').success(function(tokenListResult) {
        	$scope.tokenlist = tokenListResult;
        });
    };
    $scope.updateThunderStatFalsePositive = function(id,status) {
    	var data = id+'/'+status;
    	$http.post('api/ThunderStat/updateThunderStatFalsePositive/'+data).success(function(tokenListResult) {
        	$scope.tokenlist = tokenListResult;
        });
        $scope.findByToken();
        $scope.findToken();
    };
    $scope.findByToken();
};