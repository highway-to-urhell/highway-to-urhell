(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('ApplicationDialogController', ApplicationDialogController);

    ApplicationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Application', 'Analysis', 'UserPermission'];

    function ApplicationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Application, Analysis, UserPermission) {
        var vm = this;
        vm.application = entity;
        vm.analyses = Analysis.query();
        vm.userpermissions = UserPermission.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('h2HellUiApp:applicationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.application.id !== null) {
                Application.update(vm.application, onSaveSuccess, onSaveError);
            } else {
                Application.save(vm.application, onSaveSuccess, onSaveError);
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
