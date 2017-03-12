(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('performance', {
            parent: 'app',
            url: '/performance',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/performance/performance.html',
                    controller: 'PerformanceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('performance');
                    return $translate.refresh();
                }],
                analysis: ['$stateParams', 'Vizualisation', function($stateParams, Vizualisation) {
                    return Vizualisation.query().$promise;
                }],
            }
        })
        .state('performance-list', {
            parent: 'performance',
            url: '/list',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/performance/list.html',
                    controller: 'PerformanceListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('performance');
                    return $translate.refresh();
                }],
                analysis: ['$stateParams', 'Vizualisation', function($stateParams, Vizualisation) {
                    return Vizualisation.query().$promise;
                }],
            }
        })
        .state('performance-graph', {
            parent: 'performance',
            url: '/graph',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/performance/graph.html',
                    controller: 'PerformanceGraphController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('performance');
                    return $translate.refresh();
                }],
                analysis: ['$stateParams', 'Vizualisation', function($stateParams, Vizualisation) {
                    return Vizualisation.query().$promise;
                }],
            }
        });
    }
})();
