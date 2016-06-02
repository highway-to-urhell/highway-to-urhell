(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointParametersDialogController', EntryPointParametersDialogController);

    EntryPointParametersDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'EntryPointParameters', 'EntryPoint'];

    function EntryPointParametersDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, EntryPointParameters, EntryPoint) {
        var vm = this;

        vm.entryPointParameters = entity;
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
            if (vm.entryPointParameters.id !== null) {
                EntryPointParameters.update(vm.entryPointParameters, onSaveSuccess, onSaveError);
            } else {
                EntryPointParameters.save(vm.entryPointParameters, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('h2HellUiApp:entryPointParametersUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateIncoming = false;

        vm.setParameters = function ($file, entryPointParameters) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        entryPointParameters.parameters = base64Data;
                        entryPointParameters.parametersContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
