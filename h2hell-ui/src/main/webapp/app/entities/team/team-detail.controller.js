(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Team', 'UserPermission', 'User'];

    function TeamDetailController($scope, $rootScope, $stateParams, previousState, entity, Team, UserPermission, User) {
        var vm = this;

        vm.team = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('h2HellUiApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
