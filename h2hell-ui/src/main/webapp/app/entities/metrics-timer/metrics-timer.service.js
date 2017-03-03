(function() {
    'use strict';
    angular
        .module('h2HellUiApp')
        .factory('MetricsTimer', MetricsTimer);

    MetricsTimer.$inject = ['$resource', 'DateUtils'];

    function MetricsTimer ($resource, DateUtils) {
        var resourceUrl =  'api/metrics-timers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateIncoming = DateUtils.convertDateTimeFromServer(data.dateIncoming);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
