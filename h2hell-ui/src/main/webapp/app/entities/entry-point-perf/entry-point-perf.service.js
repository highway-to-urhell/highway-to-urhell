(function() {
    'use strict';
    angular
        .module('h2HellUiApp')
        .factory('EntryPointPerf', EntryPointPerf);

    EntryPointPerf.$inject = ['$resource', 'DateUtils'];

    function EntryPointPerf ($resource, DateUtils) {
        var resourceUrl =  'api/entry-point-perfs/:id';

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
