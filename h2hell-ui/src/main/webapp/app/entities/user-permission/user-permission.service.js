(function() {
    'use strict';
    angular
        .module('h2HellUiApp')
        .factory('UserPermission', UserPermission);

    UserPermission.$inject = ['$resource'];

    function UserPermission ($resource) {
        var resourceUrl =  'api/user-permissions/:id';

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
