(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('AnalysisDialogController', AnalysisDialogController);

    AnalysisDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Analysis', 'Event', 'EntryPoint', 'Application'];

    function AnalysisDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Analysis, Event, EntryPoint, Application) {
        var vm = this;

        vm.analysis = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.events = Event.query();
        vm.entrypoints = EntryPoint.query();
        vm.applications = Application.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.analysis.id !== null) {
                Analysis.update(vm.analysis, onSaveSuccess, onSaveError);
            } else {
                Analysis.save(vm.analysis, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('h2HellUiApp:analysisUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCreation = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
