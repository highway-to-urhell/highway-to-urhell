(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('EntryPointDialogController', EntryPointDialogController);

    EntryPointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EntryPoint', 'MetricsTimer', 'EntryPointParameters', 'Analysis'];

    function EntryPointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EntryPoint, MetricsTimer, EntryPointParameters, Analysis) {
        var vm = this;

        vm.entryPoint = entity;
        vm.clear = clear;
        vm.save = save;
        vm.metricstimers = MetricsTimer.query();
        vm.entrypointparameters = EntryPointParameters.query();
        vm.analyses = Analysis.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.entryPoint.id !== null) {
                EntryPoint.update(vm.entryPoint, onSaveSuccess, onSaveError);
            } else {
                EntryPoint.save(vm.entryPoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('h2HellUiApp:entryPointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
