(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointPerfDetailController', EntryPointPerfDetailController);

    EntryPointPerfDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'EntryPointPerf', 'EntryPoint'];

    function EntryPointPerfDetailController($scope, $rootScope, $stateParams, DataUtils, entity, EntryPointPerf, EntryPoint) {
        var vm = this;

        vm.entryPointPerf = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('h2HellUiApp:entryPointPerfUpdate', function(event, result) {
            vm.entryPointPerf = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
