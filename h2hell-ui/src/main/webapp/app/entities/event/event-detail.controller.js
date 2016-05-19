(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Event', 'Analysis'];

    function EventDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Event, Analysis) {
        var vm = this;
        vm.event = entity;
        
        var unsubscribe = $rootScope.$on('h2HellUiApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
