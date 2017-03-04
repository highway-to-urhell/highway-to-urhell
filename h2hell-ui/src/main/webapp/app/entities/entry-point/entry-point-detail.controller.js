(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointDetailController', EntryPointDetailController);

    EntryPointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EntryPoint', 'EntryPointPerf', 'EntryPointCall', 'Analysis'];

    function EntryPointDetailController($scope, $rootScope, $stateParams, previousState, entity, EntryPoint, EntryPointPerf, EntryPointCall, Analysis) {
        var vm = this;

        vm.entryPoint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('h2HellUiApp:entryPointUpdate', function(event, result) {
            vm.entryPoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
