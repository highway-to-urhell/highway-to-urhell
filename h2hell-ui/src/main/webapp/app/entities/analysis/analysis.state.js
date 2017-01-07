(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('analysis', {
            parent: 'entity',
            url: '/analysis',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.analysis.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/analysis/analyses.html',
                    controller: 'AnalysisController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('analysis');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('analysis-detail', {
            parent: 'analysis',
            url: '/analysis/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.analysis.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/analysis/analysis-detail.html',
                    controller: 'AnalysisDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('analysis');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Analysis', function($stateParams, Analysis) {
                    return Analysis.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'analysis',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('analysis-detail.edit', {
            parent: 'analysis-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analysis/analysis-dialog.html',
                    controller: 'AnalysisDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Analysis', function(Analysis) {
                            return Analysis.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('analysis.new', {
            parent: 'analysis',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analysis/analysis-dialog.html',
                    controller: 'AnalysisDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateCreation: null,
                                pathSource: null,
                                numberEntryPoints: null,
                                appVersion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('analysis', null, { reload: 'analysis' });
                }, function() {
                    $state.go('analysis');
                });
            }]
        })
        .state('analysis.edit', {
            parent: 'analysis',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analysis/analysis-dialog.html',
                    controller: 'AnalysisDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Analysis', function(Analysis) {
                            return Analysis.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('analysis', null, { reload: 'analysis' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('analysis.delete', {
            parent: 'analysis',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analysis/analysis-delete-dialog.html',
                    controller: 'AnalysisDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Analysis', function(Analysis) {
                            return Analysis.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('analysis', null, { reload: 'analysis' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
