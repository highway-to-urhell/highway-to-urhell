(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointPerfDeleteController',EntryPointPerfDeleteController);

    EntryPointPerfDeleteController.$inject = ['$uibModalInstance', 'entity', 'EntryPointPerf'];

    function EntryPointPerfDeleteController($uibModalInstance, entity, EntryPointPerf) {
        var vm = this;

        vm.entryPointPerf = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EntryPointPerf.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
