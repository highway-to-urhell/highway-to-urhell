(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EventDeleteController',EventDeleteController);

    EventDeleteController.$inject = ['$uibModalInstance', 'entity', 'Event'];

    function EventDeleteController($uibModalInstance, entity, Event) {
        var vm = this;
        vm.event = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Event.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
