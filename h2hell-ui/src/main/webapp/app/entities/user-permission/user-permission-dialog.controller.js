(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('UserPermissionDialogController', UserPermissionDialogController);

    UserPermissionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserPermission', 'Application', 'Team'];

    function UserPermissionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserPermission, Application, Team) {
        var vm = this;
        vm.userPermission = entity;
        vm.applications = Application.query();
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('h2HellUiApp:userPermissionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.userPermission.id !== null) {
                UserPermission.update(vm.userPermission, onSaveSuccess, onSaveError);
            } else {
                UserPermission.save(vm.userPermission, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
