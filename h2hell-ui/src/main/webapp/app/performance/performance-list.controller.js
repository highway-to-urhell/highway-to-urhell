(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('PerformanceListController', PerformanceListController);

    PerformanceListController.$inject = ['$scope', '$state', 'analysis'];

    function PerformanceListController ($scope, $state, analysis) {
        var vm = this;
        vm.analysis = analysis;
    }
})();
