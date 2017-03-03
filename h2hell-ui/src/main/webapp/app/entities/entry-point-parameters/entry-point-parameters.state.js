(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('entry-point-parameters', {
            parent: 'entity',
            url: '/entry-point-parameters?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.entryPointParameters.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entry-point-parameters/entry-point-parameters.html',
                    controller: 'EntryPointParametersController',
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
                    $translatePartialLoader.addPart('entryPointParameters');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('entry-point-parameters-detail', {
            parent: 'entry-point-parameters',
            url: '/entry-point-parameters/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.entryPointParameters.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entry-point-parameters/entry-point-parameters-detail.html',
                    controller: 'EntryPointParametersDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('entryPointParameters');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EntryPointParameters', function($stateParams, EntryPointParameters) {
                    return EntryPointParameters.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'entry-point-parameters',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('entry-point-parameters-detail.edit', {
            parent: 'entry-point-parameters-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-parameters/entry-point-parameters-dialog.html',
                    controller: 'EntryPointParametersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EntryPointParameters', function(EntryPointParameters) {
                            return EntryPointParameters.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entry-point-parameters.new', {
            parent: 'entry-point-parameters',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-parameters/entry-point-parameters-dialog.html',
                    controller: 'EntryPointParametersDialogController',
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
                    $state.go('entry-point-parameters', null, { reload: 'entry-point-parameters' });
                }, function() {
                    $state.go('entry-point-parameters');
                });
            }]
        })
        .state('entry-point-parameters.edit', {
            parent: 'entry-point-parameters',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-parameters/entry-point-parameters-dialog.html',
                    controller: 'EntryPointParametersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EntryPointParameters', function(EntryPointParameters) {
                            return EntryPointParameters.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entry-point-parameters', null, { reload: 'entry-point-parameters' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entry-point-parameters.delete', {
            parent: 'entry-point-parameters',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-parameters/entry-point-parameters-delete-dialog.html',
                    controller: 'EntryPointParametersDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EntryPointParameters', function(EntryPointParameters) {
                            return EntryPointParameters.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entry-point-parameters', null, { reload: 'entry-point-parameters' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
