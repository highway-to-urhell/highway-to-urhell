(function () {
    'use strict';

    angular
        .module('h2HellUiApp')
        .factory('Vizualisation', Vizualisation);

    Vizualisation.$inject = ['$resource'];

    function Vizualisation ($resource) {
        var service = $resource('api/vizualisation/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'getPath': {
                method: 'GET'
            },
            'getBreaker': {
                method: 'GET'
            },
            'getAnalysis': {
                method: 'GET'
            }
        });

        return service;
    }
})();
