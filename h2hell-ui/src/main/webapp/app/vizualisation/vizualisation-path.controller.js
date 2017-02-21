(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('VizualisationPathController', VizualisationPathController);

    VizualisationPathController.$inject = ['$scope', '$state', 'analysis'];

    function VizualisationPathController ($scope, $state, analysis) {
        var vm = this;
        vm.analysis = analysis;
    }
})();
