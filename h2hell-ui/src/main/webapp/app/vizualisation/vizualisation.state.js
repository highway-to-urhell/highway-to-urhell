(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vizualisation', {
            parent: 'app',
            url: '/vizualisation',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/vizualisation/vizualisation.html',
                    controller: 'VizualisationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('vizualisation');
                    return $translate.refresh();
                }],
                analysis: ['$stateParams', 'Vizualisation', function($stateParams, Vizualisation) {
                    return Vizualisation.query().$promise;
                }],
            }
        })
        .state('vizualisation-path', {
            parent: 'vizualisation',
            url: '/vizualisation/path/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.vizualisation-path.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/vizualisation/vizualisation-path.html',
                    controller: 'VizualisationPathController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('vizualisation');
                    return $translate.refresh();
                }],
                analysis: ['$stateParams', 'Vizualisation', function($stateParams, Vizualisation) {
                    return Vizualisation.query().$promise;
                }],
            }
        })
        .state('vizualisation-breaker', {
            parent: 'vizualisation',
            url: '/vizualisation/breaker/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.vizualisation-breaker.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/vizualisation/vizualisation-breaker.html',
                    controller: 'VizualisationBreakerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('vizualisation');
                    return $translate.refresh();
                }],
                analysis: ['$stateParams', 'Vizualisation', function($stateParams, Vizualisation) {
                    return Vizualisation.query().$promise;
                }],
            }
        });
    }
})();
