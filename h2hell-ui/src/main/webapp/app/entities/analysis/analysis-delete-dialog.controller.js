(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('AnalysisDeleteController',AnalysisDeleteController);

    AnalysisDeleteController.$inject = ['$uibModalInstance', 'entity', 'Analysis'];

    function AnalysisDeleteController($uibModalInstance, entity, Analysis) {
        var vm = this;

        vm.analysis = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Analysis.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
