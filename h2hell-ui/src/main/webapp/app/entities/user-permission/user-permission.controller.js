(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('UserPermissionController', UserPermissionController);

    UserPermissionController.$inject = ['$scope', '$state', 'UserPermission'];

    function UserPermissionController ($scope, $state, UserPermission) {
        var vm = this;
        
        vm.userPermissions = [];

        loadAll();

        function loadAll() {
            UserPermission.query(function(result) {
                vm.userPermissions = result;
            });
        }
    }
})();
