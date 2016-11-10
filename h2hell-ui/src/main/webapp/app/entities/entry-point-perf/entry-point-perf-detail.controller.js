(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointPerfDetailController', EntryPointPerfDetailController);

    EntryPointPerfDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'EntryPointPerf', 'EntryPoint'];

    function EntryPointPerfDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, EntryPointPerf, EntryPoint) {
        var vm = this;

        vm.entryPointPerf = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('h2HellUiApp:entryPointPerfUpdate', function(event, result) {
            vm.entryPointPerf = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
