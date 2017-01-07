(function() {
    'use strict';
    angular
        .module('h2HellUiApp')
        .factory('Analysis', Analysis);

    Analysis.$inject = ['$resource', 'DateUtils'];

    function Analysis ($resource, DateUtils) {
        var resourceUrl =  'api/analyses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateCreation = DateUtils.convertDateTimeFromServer(data.dateCreation);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
