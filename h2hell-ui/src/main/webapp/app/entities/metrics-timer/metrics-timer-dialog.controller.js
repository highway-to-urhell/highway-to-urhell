(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('MetricsTimerDialogController', MetricsTimerDialogController);

    MetricsTimerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MetricsTimer', 'EntryPoint'];

    function MetricsTimerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MetricsTimer, EntryPoint) {
        var vm = this;
        vm.metricsTimer = entity;
        vm.entrypoints = EntryPoint.query();

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

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateIncoming = false;

        vm.setParameters = function ($file, metricsTimer) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        metricsTimer.parameters = base64Data;
                        metricsTimer.parametersContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
