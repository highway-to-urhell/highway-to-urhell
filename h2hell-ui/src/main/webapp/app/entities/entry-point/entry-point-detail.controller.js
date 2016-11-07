(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointDetailController', EntryPointDetailController);

    EntryPointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'EntryPoint', 'EntryPointPerf', 'EntryPointCall', 'Analysis'];

    function EntryPointDetailController($scope, $rootScope, $stateParams, entity, EntryPoint, EntryPointPerf, EntryPointCall, Analysis) {
        var vm = this;

        vm.entryPoint = entity;

        var unsubscribe = $rootScope.$on('h2HellUiApp:entryPointUpdate', function(event, result) {
            vm.entryPoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
