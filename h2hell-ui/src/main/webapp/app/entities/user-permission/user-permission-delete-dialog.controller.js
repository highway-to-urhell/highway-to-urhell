(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('UserPermissionDeleteController',UserPermissionDeleteController);

    UserPermissionDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserPermission'];

    function UserPermissionDeleteController($uibModalInstance, entity, UserPermission) {
        var vm = this;

        vm.userPermission = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserPermission.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
