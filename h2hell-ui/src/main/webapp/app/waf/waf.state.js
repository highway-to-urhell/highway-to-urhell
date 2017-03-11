(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('waf', {
            parent: 'app',
            url: '/waf',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/waf/waf.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('waf');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
