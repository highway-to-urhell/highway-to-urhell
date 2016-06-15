(function() {
    'use strict';
    angular
        .module('h2HellUiApp')
        .factory('EntryPointCall', EntryPointCall);

    EntryPointCall.$inject = ['$resource', 'DateUtils'];

    function EntryPointCall ($resource, DateUtils) {
        var resourceUrl =  'api/entry-point-calls/:id';

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
