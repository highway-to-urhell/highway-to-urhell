'use strict';

/**
 * BreakerController.js
 * @constructor
 */
var BreakerController = function($scope, $http) {
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
     
    $scope.findToken();
};