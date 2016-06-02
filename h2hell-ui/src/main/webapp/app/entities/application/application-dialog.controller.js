(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('ApplicationDialogController', ApplicationDialogController);

    ApplicationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Application', 'Analysis', 'UserPermission'];

    function ApplicationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Application, Analysis, UserPermission) {
        var vm = this;

        vm.application = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.analyses = Analysis.query();
        vm.userpermissions = UserPermission.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.application.id !== null) {
                Application.update(vm.application, onSaveSuccess, onSaveError);
            } else {
                Application.save(vm.application, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('h2HellUiApp:applicationUpdate', result);
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
