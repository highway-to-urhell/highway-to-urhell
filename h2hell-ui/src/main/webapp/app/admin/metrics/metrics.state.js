(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('jhi-metrics', {
            parent: 'admin',
            url: '/metrics',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'metrics.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/metrics/list.html',
                    controller: 'JhiMetricsMonitoringController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('metrics');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
