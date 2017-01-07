(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Team', 'User', 'UserPermission'];

    function TeamDetailController($scope, $rootScope, $stateParams, previousState, entity, Team, User, UserPermission) {
        var vm = this;

        vm.team = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('h2HellUiApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
