(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('MetricsTimerDialogController', MetricsTimerDialogController);

    MetricsTimerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MetricsTimer', 'EntryPoint'];

    function MetricsTimerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MetricsTimer, EntryPoint) {
        var vm = this;

        vm.metricsTimer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.entrypoints = EntryPoint.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.metricsTimer.id !== null) {
                MetricsTimer.update(vm.metricsTimer, onSaveSuccess, onSaveError);
            } else {
                MetricsTimer.save(vm.metricsTimer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('h2HellUiApp:metricsTimerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

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

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
