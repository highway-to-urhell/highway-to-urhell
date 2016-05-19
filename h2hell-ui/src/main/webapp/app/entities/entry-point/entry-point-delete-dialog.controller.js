(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointDeleteController',EntryPointDeleteController);

    EntryPointDeleteController.$inject = ['$uibModalInstance', 'entity', 'EntryPoint'];

    function EntryPointDeleteController($uibModalInstance, entity, EntryPoint) {
        var vm = this;
        vm.entryPoint = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EntryPoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
