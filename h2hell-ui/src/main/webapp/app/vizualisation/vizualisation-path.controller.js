(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('VizualisationPathController', VizualisationPathController);

    VizualisationPathController.$inject = ['$scope', '$state', 'entity', 'Vizualisation', '$stateParams'];

    function VizualisationPathController ($scope, $state, entity, Vizualisation, $stateParams) {
        var vm = this;
        vm.entrypoints = entity.entrypoints;
        vm.messageConfig = undefined;
        vm.totalStat = entity.totalStat;
        vm.totalNoTest = entity.totalNoTest;
        vm.totalFalsePositive = entity.totalFalsePositive;
        vm.token = $stateParams.id;
        vm.searchFilter = '';
        vm.sortType = 'pathClassMethodName';
        vm.sortReverse = false;
        vm.Vizualisation = Vizualisation;
        vm.updatePaths = updatePaths;
        vm.findAllPaths = findAllPaths;
        vm.updatePathFalsePositive = updatePathFalsePositive;

        function updatePaths() {
            vm.Vizualisation.updatePaths({id : $stateParams.id});
        }

        function findAllPaths() {
            vm.Vizualisation.findAllPaths({id : $stateParams.id});
        }

        function updatePathFalsePositive(entryPointId, falsePositiveStatus, curEntryPoint) {
            vm.messageConfig = 'Update false positive status ...';
            vm.Vizualisation.updatePathFalsePositive({id : entryPointId, status: falsePositiveStatus}, function(entrypoint) {
                curEntryPoint.falsePositive = entrypoint.falsePositive;
                if(curEntryPoint.falsePositive) {
                    vm.totalFalsePositive++;
                } else {
                    vm.totalFalsePositive--;
                }
                vm.messageConfig = 'Status updated for '
                    + curEntryPoint.pathClassMethodName + ' !';
            });
        }
    }
})();
