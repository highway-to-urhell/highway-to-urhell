'use strict';

/**
 * ThunderAppReferenceController.js
 * @constructor
 */
var ThunderAppReferenceController = function($scope,$routeParams, $http) {
	$scope.fetchAppList = function() {
        $http.get('api/ThunderApp/findAllThunderApp').success(function(appzList){
            $scope.appReferenceList = appzList;
            
            $http.post('api/ThunderStat/findThunderStatByToken/' + tokenTarget)
			.success(function(message) {
				$scope.ms = message;
			});
            
        });
    };

    $scope.fetchAppList();
  
};