(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('TeamDeleteController',TeamDeleteController);

    TeamDeleteController.$inject = ['$uibModalInstance', 'entity', 'Team'];

    function TeamDeleteController($uibModalInstance, entity, Team) {
        var vm = this;

        vm.team = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Team.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
