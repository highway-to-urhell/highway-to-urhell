(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('TeamDialogController', TeamDialogController);

    TeamDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Team', 'User', 'UserPermission'];

    function TeamDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Team, User, UserPermission) {
        var vm = this;

        vm.team = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.userpermissions = UserPermission.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.team.id !== null) {
                Team.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Team.save(vm.team, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('h2HellUiApp:teamUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
