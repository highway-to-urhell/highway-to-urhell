(function () {
    'use strict';

    angular
        .module('h2HellUiApp')
        .factory('Performance', Performance);

    Performance.$inject = ['$resource'];

    function Performance ($resource) {
        var service = $resource('api/vizualisation/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'getPath': {
                method: 'GET'
            },
            'getBreaker': {
                method: 'GET',
                url: 'api/vizualisation/findAllPaths/:id'
            },
            'getAnalysis': {
                method: 'GET',
                url: 'api/vizualisation/findAllPaths/:id'
            },
            'getSource': {
                method: 'GET',
                url: 'api/vizualisation/findSource/:id/:path'
            },
            'updatePaths': {
                method: 'POST',
                url: 'api/vizualisation/findAllPaths/:id'
            },
            'findAllPaths': {
                method: 'GET',
                url: 'api/vizualisation/findAllPaths/:id'
            },
            'updatePathFalsePositive': {
                method: 'POST',
                url: 'api/vizualisation/updatePathFalsePositive/:id/:status'
            },
        });

        return service;
    }
})();
