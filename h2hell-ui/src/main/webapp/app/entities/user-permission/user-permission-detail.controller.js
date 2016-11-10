(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('UserPermissionDetailController', UserPermissionDetailController);

    UserPermissionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserPermission', 'Application', 'Team'];

    function UserPermissionDetailController($scope, $rootScope, $stateParams, previousState, entity, UserPermission, Application, Team) {
        var vm = this;

        vm.userPermission = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('h2HellUiApp:userPermissionUpdate', function(event, result) {
            vm.userPermission = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
