(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointParametersDeleteController',EntryPointParametersDeleteController);

    EntryPointParametersDeleteController.$inject = ['$uibModalInstance', 'entity', 'EntryPointParameters'];

    function EntryPointParametersDeleteController($uibModalInstance, entity, EntryPointParameters) {
        var vm = this;
        vm.entryPointParameters = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            EntryPointParameters.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
