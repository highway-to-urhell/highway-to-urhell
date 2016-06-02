(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('AnalysisController', AnalysisController);

    AnalysisController.$inject = ['$scope', '$state', 'Analysis'];

    function AnalysisController ($scope, $state, Analysis) {
        var vm = this;
        
        vm.analyses = [];

        loadAll();

        function loadAll() {
            Analysis.query(function(result) {
                vm.analyses = result;
            });
        }
    }
})();
