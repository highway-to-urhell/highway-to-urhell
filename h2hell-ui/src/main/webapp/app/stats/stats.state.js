(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('stats', {
            parent: 'app',
            url: '/stats',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/stats/stats.html',
                    controller: 'StatsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('stats');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
