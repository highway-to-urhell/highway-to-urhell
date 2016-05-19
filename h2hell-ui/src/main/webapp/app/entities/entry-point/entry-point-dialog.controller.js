(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointDialogController', EntryPointDialogController);

    EntryPointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EntryPoint', 'EntryPointParameters', 'Analysis'];

    function EntryPointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EntryPoint, EntryPointParameters, Analysis) {
        var vm = this;
        vm.entryPoint = entity;
        vm.entrypointparameters = EntryPointParameters.query();
        vm.analyses = Analysis.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('h2HellUiApp:entryPointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.entryPoint.id !== null) {
                EntryPoint.update(vm.entryPoint, onSaveSuccess, onSaveError);
            } else {
                EntryPoint.save(vm.entryPoint, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
