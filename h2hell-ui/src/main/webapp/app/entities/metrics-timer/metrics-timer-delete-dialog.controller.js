(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('MetricsTimerDeleteController',MetricsTimerDeleteController);

    MetricsTimerDeleteController.$inject = ['$uibModalInstance', 'entity', 'MetricsTimer'];

    function MetricsTimerDeleteController($uibModalInstance, entity, MetricsTimer) {
        var vm = this;

        vm.metricsTimer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MetricsTimer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
