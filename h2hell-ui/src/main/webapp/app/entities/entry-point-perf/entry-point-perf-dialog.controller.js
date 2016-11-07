(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointPerfDialogController', EntryPointPerfDialogController);

    EntryPointPerfDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'EntryPointPerf', 'EntryPoint'];

    function EntryPointPerfDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, EntryPointPerf, EntryPoint) {
        var vm = this;

        vm.entryPointPerf = entity;
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
            if (vm.entryPointPerf.id !== null) {
                EntryPointPerf.update(vm.entryPointPerf, onSaveSuccess, onSaveError);
            } else {
                EntryPointPerf.save(vm.entryPointPerf, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('h2HellUiApp:entryPointPerfUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateIncoming = false;

        vm.setParameters = function ($file, entryPointPerf) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        entryPointPerf.parameters = base64Data;
                        entryPointPerf.parametersContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
