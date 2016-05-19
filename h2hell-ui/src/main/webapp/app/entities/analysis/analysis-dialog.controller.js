(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('AnalysisDialogController', AnalysisDialogController);

    AnalysisDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Analysis', 'Event', 'EntryPoint', 'Application'];

    function AnalysisDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Analysis, Event, EntryPoint, Application) {
        var vm = this;
        vm.analysis = entity;
        vm.events = Event.query();
        vm.entrypoints = EntryPoint.query();
        vm.applications = Application.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('h2HellUiApp:analysisUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.analysis.id !== null) {
                Analysis.update(vm.analysis, onSaveSuccess, onSaveError);
            } else {
                Analysis.save(vm.analysis, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.dateCreation = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
