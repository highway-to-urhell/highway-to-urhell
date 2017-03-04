(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('VizualisationPathController', VizualisationPathController);

    VizualisationPathController.$inject = ['$scope', '$state', 'entity', 'Vizualisation', '$stateParams'];

    function VizualisationPathController ($scope, $state, entity, Vizualisation, $stateParams) {
        var vm = this;
        vm.entrypoints = entity.entrypoints;
        vm.messageConfig = entity.messageConfig;
        vm.totalStat = entity.totalStat;
        vm.totalNoTest = entity.totalNoTest;
        vm.totalFalsePositive = entity.totalFalsePositive;
        vm.token = $stateParams.id;
        vm.Vizualisation = Vizualisation;
        vm.updatePaths = updatePaths;
        vm.findAllPaths = findAllPaths;

        function updatePaths() {
            vm.Vizualisation.updatePaths({id : $stateParams.id});
        }

        function findAllPaths() {
            vm.Vizualisation.findAllPaths({id : $stateParams.id});
        }
    }
})();
