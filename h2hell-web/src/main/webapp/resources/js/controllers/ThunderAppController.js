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

    $scope.addAppThunder = function(newAppName) {
        $http.post('api/ThunderApp/createThunderApp/' + newAppName).success(function() {
            $scope.fetchAppList();
        });
        $scope.newAppName = '';
    };
    
    $scope.launchAnalysis = function(token) {
        $http.post('api/ThunderApp/launchAnalysis/' + token).success(function(message) {
            $scope.messageConfig=message;
        });
    };
    
    $scope.fetchAppList();
};