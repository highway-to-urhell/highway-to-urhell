(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Team', 'User', 'UserPermission'];

    function TeamDetailController($scope, $rootScope, $stateParams, entity, Team, User, UserPermission) {
        var vm = this;
        vm.team = entity;
        
        var unsubscribe = $rootScope.$on('h2HellUiApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
