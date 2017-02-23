(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('VizualisationPathController', VizualisationPathController);

    VizualisationPathController.$inject = ['$scope', '$state', 'entity'];

    function VizualisationPathController ($scope, $state, entity) {
        var vm = this;
        vm.entrypoints = entity.entrypoints;
        vm.messageConfig = undefined;
        vm.totalStat = 10;
        vm.totalNoTest = 10;
        vm.totalFalsePositive = 10;
        vm.token = "TOKENNNN";

        function updatePaths() {

        }

        function findAllPaths() {

        }
    }
})();
