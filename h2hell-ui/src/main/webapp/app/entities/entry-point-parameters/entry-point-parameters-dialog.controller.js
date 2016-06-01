(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointParametersDialogController', EntryPointParametersDialogController);

    EntryPointParametersDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'EntryPointParameters', 'EntryPoint'];

    function EntryPointParametersDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, EntryPointParameters, EntryPoint) {
        var vm = this;
        vm.entryPointParameters = entity;
        vm.entrypoints = EntryPoint.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('h2HellUiApp:entryPointParametersUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.entryPointParameters.id !== null) {
                EntryPointParameters.update(vm.entryPointParameters, onSaveSuccess, onSaveError);
            } else {
                EntryPointParameters.save(vm.entryPointParameters, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
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

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
