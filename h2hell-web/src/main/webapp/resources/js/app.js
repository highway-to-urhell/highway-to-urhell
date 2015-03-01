'use strict';

var AngularSpringApp = {};

var App = angular.module('thunderWebApp', []);

// Declare app level module which depends on filters, and services
App.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/thunderstat/:token', {
        templateUrl: 'thunderstat/layout.html',
        controller: 'ThunderStatController'
    });
    $routeProvider.when('/breaker', {
        templateUrl: 'breaker/layout.html',
        controller: 'BreakerController'
    });
    $routeProvider.when('/thundersource/:token/:pathClassMethodName', {
        templateUrl: 'thundersource/layout.html',
        controller: 'ThundersourceController'
    });
    $routeProvider.when('/appReference', {
        templateUrl: 'appReference/layout.html',
        controller: 'ThunderAppReferenceController'
    });
    $routeProvider.otherwise({redirectTo: '/appReference'});
}]);
