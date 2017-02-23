(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('VizualisationAnalysisController', VizualisationAnalysisController);

    VizualisationAnalysisController.$inject = ['$scope', '$state', 'analysis'];

    function VizualisationAnalysisController ($scope, $state, analysis) {
        var vm = this;
        vm.analysis = analysis;
    }
})();
