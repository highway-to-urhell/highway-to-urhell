(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('MetricsTimerDialogController', MetricsTimerDialogController);

    MetricsTimerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MetricsTimer', 'EntryPointParameters'];

    function MetricsTimerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MetricsTimer, EntryPointParameters) {
        var vm = this;
        vm.metricsTimer = entity;
        vm.entrypointparameters = EntryPointParameters.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('h2HellUiApp:metricsTimerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.metricsTimer.id !== null) {
                MetricsTimer.update(vm.metricsTimer, onSaveSuccess, onSaveError);
            } else {
                MetricsTimer.save(vm.metricsTimer, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
