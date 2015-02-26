'use strict';

/**
 * ThunderAppController
 * @constructor
 */
var ThunderAppController = function($scope, $http) {
    $scope.fetchAppList = function() {
        $http.get('api/ThunderApp/findAllThunderApp').success(function(appzList){
            $scope.applisthunder = appzList;
        });
    };

    $scope.launchAnalysis = function(token,nameApp) {
    	$scope.messageConfig='Analysis Running for application '+nameApp;
        $http.post('api/ThunderApp/launchAnalysis/' + token).success(function(message) {
        	$scope.messageConfig=message;
        });
    };
    $scope.fetchAppList();
};