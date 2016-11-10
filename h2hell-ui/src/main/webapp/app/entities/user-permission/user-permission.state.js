(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-permission', {
            parent: 'entity',
            url: '/user-permission',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.userPermission.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-permission/user-permissions.html',
                    controller: 'UserPermissionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userPermission');
                    $translatePartialLoader.addPart('permission');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-permission-detail', {
            parent: 'entity',
            url: '/user-permission/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'h2HellUiApp.userPermission.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-permission/user-permission-detail.html',
                    controller: 'UserPermissionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userPermission');
                    $translatePartialLoader.addPart('permission');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserPermission', function($stateParams, UserPermission) {
                    return UserPermission.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-permission',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-permission-detail.edit', {
            parent: 'user-permission-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-permission/user-permission-dialog.html',
                    controller: 'UserPermissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserPermission', function(UserPermission) {
                            return UserPermission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-permission.new', {
            parent: 'user-permission',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-permission/user-permission-dialog.html',
                    controller: 'UserPermissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                permission: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-permission', null, { reload: 'user-permission' });
                }, function() {
                    $state.go('user-permission');
                });
            }]
        })
        .state('user-permission.edit', {
            parent: 'user-permission',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-permission/user-permission-dialog.html',
                    controller: 'UserPermissionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserPermission', function(UserPermission) {
                            return UserPermission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-permission', null, { reload: 'user-permission' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-permission.delete', {
            parent: 'user-permission',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-permission/user-permission-delete-dialog.html',
                    controller: 'UserPermissionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserPermission', function(UserPermission) {
                            return UserPermission.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-permission', null, { reload: 'user-permission' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
