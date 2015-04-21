'use strict';

/**
 * ThunderStatController.js
 * 
 * @constructor
 */
var ThunderStatController = function($scope, $routeParams, $http) {
	$scope.findByToken = function() {
		var tokenTarget = $routeParams.token;
		$http.post('api/ThunderStat/findThunderStatByToken/' + tokenTarget)
				.success(function(message) {
					$scope.ms = message;
				});
	};
	
	$scope.launchAnalysis = function(token) {
	    	$scope.messageConfig='Analysis Running for application ';
	        $http.post('api/ThunderApp/launchAnalysis/' + token).success(function(message) {
	        	$scope.messageConfig=message;
	        });
	    };

	$scope.updateThunderStatFalsePositive = function(id, status, methodInput) {
		$scope.messageConfig = 'Update status ...';
		var data = id + '/' + status;
		$http.post('api/ThunderStat/updateThunderStatFalsePositive/' + data)
				.success(
						function(tokenListResult) {
							$scope.tokenlist = tokenListResult;
							$scope.findByToken();
							$scope.messageConfig = 'Status updated for '
									+ methodInput + ' !';
						});

	};

	$scope.set_color = function(falsePositive, count) {
		// alert(falsePositive);
		if (falsePositive == true) {
			return {
				color : "green"
			};
		} 
	};
	$scope.updateToken = function() {
		$scope.messageConfig = 'Update ...';
		var tokenTarget = $routeParams.token;
		$http.post('api/ThunderStat/findThunderStatByToken/' + tokenTarget)
				.success(function(message) {
					$scope.ms = message;
					$scope.messageConfig = null;
				});
	};
	$scope.findByToken();
};