(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('application', {
            parent: 'entity',
            url: '/application',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.application.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application/applications.html',
                    controller: 'ApplicationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('application');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('application-detail', {
            parent: 'entity',
            url: '/application/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.application.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application/application-detail.html',
                    controller: 'ApplicationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('application');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Application', function($stateParams, Application) {
                    return Application.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'application',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('application-detail.edit', {
            parent: 'application-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application/application-dialog.html',
                    controller: 'ApplicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Application', function(Application) {
                            return Application.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application.new', {
            parent: 'application',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application/application-dialog.html',
                    controller: 'ApplicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                token: null,
                                dateCreation: null,
                                urlApp: null,
                                description: null,
                                appType: null,
                                analysed: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('application', null, { reload: 'application' });
                }, function() {
                    $state.go('application');
                });
            }]
        })
        .state('application.edit', {
            parent: 'application',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application/application-dialog.html',
                    controller: 'ApplicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Application', function(Application) {
                            return Application.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application', null, { reload: 'application' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application.delete', {
            parent: 'application',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application/application-delete-dialog.html',
                    controller: 'ApplicationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Application', function(Application) {
                            return Application.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application', null, { reload: 'application' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
