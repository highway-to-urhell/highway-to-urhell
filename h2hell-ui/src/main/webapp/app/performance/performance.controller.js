(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('PerformanceController', PerformanceController);

    PerformanceController.$inject = ['$scope', '$state', 'analysis'];

    function PerformanceController ($scope, $state, analysis) {
        var vm = this;
        vm.analysis = analysis;
    }
})();
