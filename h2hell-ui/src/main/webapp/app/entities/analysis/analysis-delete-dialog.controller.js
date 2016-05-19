(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('AnalysisDeleteController',AnalysisDeleteController);

    AnalysisDeleteController.$inject = ['$uibModalInstance', 'entity', 'Analysis'];

    function AnalysisDeleteController($uibModalInstance, entity, Analysis) {
        var vm = this;
        vm.analysis = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Analysis.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
