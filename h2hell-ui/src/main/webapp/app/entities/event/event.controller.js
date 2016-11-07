(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EventController', EventController);

    EventController.$inject = ['$scope', '$state', 'DataUtils', 'Event'];

    function EventController ($scope, $state, DataUtils, Event) {
        var vm = this;
        
        vm.events = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Event.query(function(result) {
                vm.events = result;
            });
        }
    }
})();
