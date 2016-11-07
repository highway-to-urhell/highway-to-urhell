(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointCallDialogController', EntryPointCallDialogController);

    EntryPointCallDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'EntryPointCall', 'EntryPoint'];

    function EntryPointCallDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, EntryPointCall, EntryPoint) {
        var vm = this;

        vm.entryPointCall = entity;
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
            if (vm.entryPointCall.id !== null) {
                EntryPointCall.update(vm.entryPointCall, onSaveSuccess, onSaveError);
            } else {
                EntryPointCall.save(vm.entryPointCall, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('h2HellUiApp:entryPointCallUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateIncoming = false;

        vm.setParameters = function ($file, entryPointCall) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        entryPointCall.parameters = base64Data;
                        entryPointCall.parametersContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
