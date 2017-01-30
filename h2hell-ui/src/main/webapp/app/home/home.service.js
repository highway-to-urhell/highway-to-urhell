(function () {
    'use strict';

    angular
        .module('h2HellUiApp')
        .factory('Home', Home);

    Home.$inject = ['$resource'];

    function Home ($resource) {
        var service = $resource('api/home', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });

        return service;
    }
})();
