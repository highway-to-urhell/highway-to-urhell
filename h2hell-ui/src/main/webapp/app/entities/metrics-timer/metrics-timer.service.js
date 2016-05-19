(function() {
    'use strict';
    angular
        .module('h2HellUiApp')
        .factory('MetricsTimer', MetricsTimer);

    MetricsTimer.$inject = ['$resource'];

    function MetricsTimer ($resource) {
        var resourceUrl =  'api/metrics-timers/:id';

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
