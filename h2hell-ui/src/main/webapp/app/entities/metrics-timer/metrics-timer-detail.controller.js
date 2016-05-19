(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('MetricsTimerDetailController', MetricsTimerDetailController);

    MetricsTimerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'MetricsTimer', 'EntryPointParameters'];

    function MetricsTimerDetailController($scope, $rootScope, $stateParams, entity, MetricsTimer, EntryPointParameters) {
        var vm = this;
        vm.metricsTimer = entity;
        
        var unsubscribe = $rootScope.$on('h2HellUiApp:metricsTimerUpdate', function(event, result) {
            vm.metricsTimer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
