(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('entry-point-call', {
            parent: 'entity',
            url: '/entry-point-call?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.entryPointCall.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entry-point-call/entry-point-calls.html',
                    controller: 'EntryPointCallController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('entryPointCall');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('entry-point-call-detail', {
            parent: 'entity',
            url: '/entry-point-call/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.entryPointCall.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entry-point-call/entry-point-call-detail.html',
                    controller: 'EntryPointCallDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('entryPointCall');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EntryPointCall', function($stateParams, EntryPointCall) {
                    return EntryPointCall.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('entry-point-call.new', {
            parent: 'entry-point-call',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-call/entry-point-call-dialog.html',
                    controller: 'EntryPointCallDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateIncoming: null,
                                parameters: null,
                                parametersContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('entry-point-call', null, { reload: true });
                }, function() {
                    $state.go('entry-point-call');
                });
            }]
        })
        .state('entry-point-call.edit', {
            parent: 'entry-point-call',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-call/entry-point-call-dialog.html',
                    controller: 'EntryPointCallDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EntryPointCall', function(EntryPointCall) {
                            return EntryPointCall.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entry-point-call', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entry-point-call.delete', {
            parent: 'entry-point-call',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-call/entry-point-call-delete-dialog.html',
                    controller: 'EntryPointCallDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EntryPointCall', function(EntryPointCall) {
                            return EntryPointCall.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entry-point-call', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
