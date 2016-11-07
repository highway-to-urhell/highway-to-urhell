(function() {
    'use strict';
    angular
        .module('h2HellUiApp')
        .factory('EntryPoint', EntryPoint);

    EntryPoint.$inject = ['$resource'];

    function EntryPoint ($resource) {
        var resourceUrl =  'api/entry-points/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
