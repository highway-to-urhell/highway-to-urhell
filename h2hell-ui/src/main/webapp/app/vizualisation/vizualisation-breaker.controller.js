(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('VizualisationBreakerController', VizualisationBreakerController);

    VizualisationBreakerController.$inject = ['$scope', '$state', 'analysis'];

    function VizualisationBreakerController ($scope, $state, analysis) {
        var vm = this;
        vm.analysis = analysis;
    }
})();
