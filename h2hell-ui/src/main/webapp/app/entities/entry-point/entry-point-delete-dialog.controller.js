(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointDeleteController',EntryPointDeleteController);

    EntryPointDeleteController.$inject = ['$uibModalInstance', 'entity', 'EntryPoint'];

    function EntryPointDeleteController($uibModalInstance, entity, EntryPoint) {
        var vm = this;

        vm.entryPoint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EntryPoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
