(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointCallDetailController', EntryPointCallDetailController);

    EntryPointCallDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'EntryPointCall', 'EntryPoint'];

    function EntryPointCallDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, EntryPointCall, EntryPoint) {
        var vm = this;

        vm.entryPointCall = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('h2HellUiApp:entryPointCallUpdate', function(event, result) {
            vm.entryPointCall = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
