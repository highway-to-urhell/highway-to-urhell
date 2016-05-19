(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('entry-point', {
            parent: 'entity',
            url: '/entry-point',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.entryPoint.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entry-point/entry-points.html',
                    controller: 'EntryPointController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('entryPoint');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('entry-point-detail', {
            parent: 'entity',
            url: '/entry-point/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.entryPoint.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entry-point/entry-point-detail.html',
                    controller: 'EntryPointDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('entryPoint');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EntryPoint', function($stateParams, EntryPoint) {
                    return EntryPoint.get({id : $stateParams.id});
                }]
            }
        })
        .state('entry-point.new', {
            parent: 'entry-point',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point/entry-point-dialog.html',
                    controller: 'EntryPointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                pathClassMethodName: null,
                                count: null,
                                falsePositive: null,
                                uri: null,
                                httpmethod: null,
                                audit: null,
                                averageTime: null,
                                checkLaunch: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('entry-point', null, { reload: true });
                }, function() {
                    $state.go('entry-point');
                });
            }]
        })
        .state('entry-point.edit', {
            parent: 'entry-point',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point/entry-point-dialog.html',
                    controller: 'EntryPointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EntryPoint', function(EntryPoint) {
                            return EntryPoint.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('entry-point', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entry-point.delete', {
            parent: 'entry-point',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point/entry-point-delete-dialog.html',
                    controller: 'EntryPointDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EntryPoint', function(EntryPoint) {
                            return EntryPoint.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('entry-point', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
