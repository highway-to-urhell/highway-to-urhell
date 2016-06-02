(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('MetricsTimerDetailController', MetricsTimerDetailController);

    MetricsTimerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'MetricsTimer', 'EntryPoint'];

    function MetricsTimerDetailController($scope, $rootScope, $stateParams, DataUtils, entity, MetricsTimer, EntryPoint) {
        var vm = this;

        vm.metricsTimer = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('h2HellUiApp:metricsTimerUpdate', function(event, result) {
            vm.metricsTimer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
