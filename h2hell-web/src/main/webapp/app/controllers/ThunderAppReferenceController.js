'use strict';

/**
 * ThunderAppReferenceController.js
 * @constructor
 */
var ThunderAppReferenceController = function($scope,$routeParams, $http) {
	$scope.fetchAppList = function() {
        $http.get('api/ThunderApp/findAllThunderApp').success(function(appzList){
            $scope.appReferenceList = appzList;
        });
    };

    $scope.fetchAppList();
  
    $("#menu li").removeClass("active");
    $("#menu_home").addClass("active");
    
};