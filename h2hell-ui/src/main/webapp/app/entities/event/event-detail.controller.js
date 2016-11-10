(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Event', 'Analysis'];

    function EventDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Event, Analysis) {
        var vm = this;

        vm.event = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('h2HellUiApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
