(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('PerformanceGraphController', PerformanceGraphController);

    PerformanceGraphController.$inject = ['$scope', '$state', 'analysis'];

    function PerformanceGraphController ($scope, $state, analysis) {
        var vm = this;
        vm.analysis = analysis;
    }
})();
