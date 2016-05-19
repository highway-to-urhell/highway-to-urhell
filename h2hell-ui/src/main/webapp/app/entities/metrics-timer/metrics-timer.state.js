(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('metrics-timer', {
            parent: 'entity',
            url: '/metrics-timer?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.metricsTimer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metrics-timer/metrics-timers.html',
                    controller: 'MetricsTimerController',
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
                    $translatePartialLoader.addPart('metricsTimer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('metrics-timer-detail', {
            parent: 'entity',
            url: '/metrics-timer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.metricsTimer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/metrics-timer/metrics-timer-detail.html',
                    controller: 'MetricsTimerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('metricsTimer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MetricsTimer', function($stateParams, MetricsTimer) {
                    return MetricsTimer.get({id : $stateParams.id});
                }]
            }
        })
        .state('metrics-timer.new', {
            parent: 'metrics-timer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metrics-timer/metrics-timer-dialog.html',
                    controller: 'MetricsTimerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                timeExec: null,
                                cpuLoadSystem: null,
                                cpuLoadProcess: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('metrics-timer', null, { reload: true });
                }, function() {
                    $state.go('metrics-timer');
                });
            }]
        })
        .state('metrics-timer.edit', {
            parent: 'metrics-timer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metrics-timer/metrics-timer-dialog.html',
                    controller: 'MetricsTimerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MetricsTimer', function(MetricsTimer) {
                            return MetricsTimer.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('metrics-timer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('metrics-timer.delete', {
            parent: 'metrics-timer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/metrics-timer/metrics-timer-delete-dialog.html',
                    controller: 'MetricsTimerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MetricsTimer', function(MetricsTimer) {
                            return MetricsTimer.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('metrics-timer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
