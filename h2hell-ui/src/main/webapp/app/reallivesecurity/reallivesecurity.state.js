(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('reallivesecurity', {
            parent: 'app',
            url: '/reallivesecurity',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/reallivesecurity/reallivesecurity.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('reallivesecurity');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
