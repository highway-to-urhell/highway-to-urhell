(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('VizualisationController', VizualisationController);

    VizualisationController.$inject = ['$scope', '$state', 'analysis'];

    function VizualisationController ($scope, $state, analysis) {
        var vm = this;
        vm.analysis = analysis;
    }
})();
