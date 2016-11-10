(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('entry-point-perf', {
            parent: 'entity',
            url: '/entry-point-perf?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.entryPointPerf.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entry-point-perf/entry-point-perfs.html',
                    controller: 'EntryPointPerfController',
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
                    $translatePartialLoader.addPart('entryPointPerf');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('entry-point-perf-detail', {
            parent: 'entity',
            url: '/entry-point-perf/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.entryPointPerf.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entry-point-perf/entry-point-perf-detail.html',
                    controller: 'EntryPointPerfDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('entryPointPerf');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EntryPointPerf', function($stateParams, EntryPointPerf) {
                    return EntryPointPerf.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'entry-point-perf',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('entry-point-perf-detail.edit', {
            parent: 'entry-point-perf-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-perf/entry-point-perf-dialog.html',
                    controller: 'EntryPointPerfDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EntryPointPerf', function(EntryPointPerf) {
                            return EntryPointPerf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entry-point-perf.new', {
            parent: 'entry-point-perf',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-perf/entry-point-perf-dialog.html',
                    controller: 'EntryPointPerfDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateIncoming: null,
                                parameters: null,
                                parametersContentType: null,
                                timeExec: null,
                                cpuLoadSystem: null,
                                cpuLoadProcess: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('entry-point-perf', null, { reload: 'entry-point-perf' });
                }, function() {
                    $state.go('entry-point-perf');
                });
            }]
        })
        .state('entry-point-perf.edit', {
            parent: 'entry-point-perf',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-perf/entry-point-perf-dialog.html',
                    controller: 'EntryPointPerfDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EntryPointPerf', function(EntryPointPerf) {
                            return EntryPointPerf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entry-point-perf', null, { reload: 'entry-point-perf' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entry-point-perf.delete', {
            parent: 'entry-point-perf',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entry-point-perf/entry-point-perf-delete-dialog.html',
                    controller: 'EntryPointPerfDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EntryPointPerf', function(EntryPointPerf) {
                            return EntryPointPerf.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entry-point-perf', null, { reload: 'entry-point-perf' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
