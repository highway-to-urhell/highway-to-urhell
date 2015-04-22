'use strict';

/**
 * BreakerController.js
 * @constructor
 */
var BreakerController = function($scope, $routeParams, $http) {
    
	
	$scope.findByToken = function(token) {
        $http.post('api/BreakerLog/findBreakerWithToken/'+token).success(function(breakerListResult){
            $scope.breakerlist = breakerListResult;
        });
    };
    
    $scope.findToken = function() {
        $http.get('api/ThunderApp/findAllThunderApp').success(function(tokenListResult) {
        	$scope.tokenlist = tokenListResult;
        });
    };
    
	$scope.loadAppStat = function(token) {
		$http.post('api/ThunderStat/findThunderStatByToken/' + token)
				.success(function(message) {
					$scope.ms = message;
				});
	};
    
    $scope.findByToken($routeParams.token);
    $scope.loadAppStat($routeParams.token);
    
    
    
//    $scope.findToken();
};