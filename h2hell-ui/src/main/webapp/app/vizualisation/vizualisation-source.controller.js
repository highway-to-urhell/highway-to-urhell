(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('VizualisationSourceController', VizualisationSourceController);

    VizualisationSourceController.$inject = ['$scope', '$state', 'source', '$stateParams'];

    function VizualisationSourceController ($scope, $state, source, $stateParams) {
        var vm = this;
        vm.messageConfig = undefined;
        vm.token = $stateParams.id;
        vm.path = $stateParams.path;
        vm.data = source.data;

    }
})();
