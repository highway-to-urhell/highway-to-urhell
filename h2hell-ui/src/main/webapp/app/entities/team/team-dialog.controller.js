(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('TeamDialogController', TeamDialogController);

    TeamDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Team', 'User', 'UserPermission'];

    function TeamDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Team, User, UserPermission) {
        var vm = this;
        vm.team = entity;
        vm.users = User.query();
        vm.userpermissions = UserPermission.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('h2HellUiApp:teamUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.team.id !== null) {
                Team.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Team.save(vm.team, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
