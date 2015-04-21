'use strict';

/**
 * ThunderAdminController.js
 * @constructor
 */
var ThunderAdminController = function($scope, $http) {
    $scope.findAllApp = function() {
        $http.get('api/ThunderApp/findAllThunderApp').success(function(appListResult) {
        	$scope.appList = appListResult;
        });
    };
    $scope.purgeStatByToken = function(token,appName) {
    	$scope.messageStatus='Purge stat for '+appName;
        $http.post('api/ThunderAdmin/purgeStatByToken/'+token).success(function(){
            $scope.messageStatus='Purge done !';
        });
    };
    $scope.deleteThunderAppByToken = function(token,appName) {
    	$scope.messageStatus='Delete all data for '+appName+' (stat, log and application) ';
        $http.post('api/ThunderAdmin/deleteThunderAppByToken/'+token).success(function(){
            $scope.messageStatus='Purge done !';
            $scope.findAllApp();
        });
    };
    $scope.findAllApp();
    $scope.messageStatus=null;
    
    $("#menu li").removeClass("active");
    $("#menu_settings").addClass("active");
    
};