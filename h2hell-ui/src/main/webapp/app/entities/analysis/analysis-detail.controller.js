(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('AnalysisDetailController', AnalysisDetailController);

    AnalysisDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Analysis', 'Event', 'EntryPoint', 'Application'];

    function AnalysisDetailController($scope, $rootScope, $stateParams, previousState, entity, Analysis, Event, EntryPoint, Application) {
        var vm = this;

        vm.analysis = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('h2HellUiApp:analysisUpdate', function(event, result) {
            vm.analysis = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
