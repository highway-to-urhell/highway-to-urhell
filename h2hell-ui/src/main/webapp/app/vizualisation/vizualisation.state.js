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
            url: '/path/{id}',
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
                entity: ['$stateParams', 'Vizualisation', function($stateParams, Vizualisation) {
                    return Vizualisation.getPath({id : $stateParams.id}).$promise;
                }],
            }
        })
        .state('vizualisation-breaker', {
            parent: 'vizualisation',
            url: '/breaker/{id}',
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
                    return Vizualisation.getBreaker().$promise;
                }],
            }
        })
        .state('vizualisation-analysis', {
            parent: 'vizualisation',
            url: '/analysis/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.vizualisation-analysis.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/vizualisation/vizualisation-analysis.html',
                    controller: 'VizualisationAnalysisController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('vizualisation');
                    return $translate.refresh();
                }],
                analysis: ['$stateParams', 'Vizualisation', function($stateParams, Vizualisation) {
                    return Vizualisation.getPath({id : $stateParams.id}).$promise;
                }],
            }
        })
        .state('vizualisation-source', {
            parent: 'vizualisation',
            url: '/source/{id}/{path}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.vizualisation-source.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/vizualisation/vizualisation-source.html',
                    controller: 'VizualisationSourceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('vizualisation');
                    return $translate.refresh();
                }],
                source: ['$stateParams', 'Vizualisation', function($stateParams, Vizualisation) {
                    return Vizualisation.getSource({id : $stateParams.id, path : $stateParams.path}).$promise;
                }],
            }
        });
    }
})();
