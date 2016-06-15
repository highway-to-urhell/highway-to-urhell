(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointCallDeleteController',EntryPointCallDeleteController);

    EntryPointCallDeleteController.$inject = ['$uibModalInstance', 'entity', 'EntryPointCall'];

    function EntryPointCallDeleteController($uibModalInstance, entity, EntryPointCall) {
        var vm = this;

        vm.entryPointCall = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EntryPointCall.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
