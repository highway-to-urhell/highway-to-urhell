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
        vm.totalStat = 10;
        vm.totalNoTest = 10;
        vm.totalFalsePositive = 10;
        vm.token = "TOKENNNN";
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
