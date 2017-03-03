(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointParametersDetailController', EntryPointParametersDetailController);

    EntryPointParametersDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'EntryPointParameters', 'EntryPoint'];

    function EntryPointParametersDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, EntryPointParameters, EntryPoint) {
        var vm = this;

        vm.entryPointParameters = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('h2HellUiApp:entryPointParametersUpdate', function(event, result) {
            vm.entryPointParameters = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
