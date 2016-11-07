(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('UserPermissionDialogController', UserPermissionDialogController);

    UserPermissionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserPermission', 'Application', 'Team'];

    function UserPermissionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserPermission, Application, Team) {
        var vm = this;

        vm.userPermission = entity;
        vm.clear = clear;
        vm.save = save;
        vm.applications = Application.query();
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userPermission.id !== null) {
                UserPermission.update(vm.userPermission, onSaveSuccess, onSaveError);
            } else {
                UserPermission.save(vm.userPermission, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('h2HellUiApp:userPermissionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
