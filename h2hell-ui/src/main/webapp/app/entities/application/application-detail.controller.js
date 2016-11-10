(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('ApplicationDetailController', ApplicationDetailController);

    ApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Application', 'Analysis', 'UserPermission'];

    function ApplicationDetailController($scope, $rootScope, $stateParams, previousState, entity, Application, Analysis, UserPermission) {
        var vm = this;

        vm.application = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('h2HellUiApp:applicationUpdate', function(event, result) {
            vm.application = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
