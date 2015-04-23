'use strict';

var AngularSpringApp = {};

var App = angular.module('thunderWebApp', []);

// Declare app level module which depends on filters, and services
App.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/thunderstat/:token', {
        templateUrl: 'app/views/thunderstat.html',
        controller: 'ThunderStatController'
    });
    $routeProvider.when('/breaker/:token', {
        templateUrl: 'app/views/breaker.html',
        controller: 'BreakerController'
    });
    $routeProvider.when('/thundersource/:token/:pathClassMethodName', {
        templateUrl: 'app/views/thundersource.html',
        controller: 'ThundersourceController'
    });
    $routeProvider.when('/adminh2h', {
        templateUrl: 'app/views/adminh2h.html',
        controller: 'ThunderAdminController'
    });
    $routeProvider.when('/apps', {
        templateUrl: 'app/views/app.html',
        controller: 'ThunderAppReferenceController'
    });
    $routeProvider.when('/home', {
        templateUrl: 'app/views/home.html',
        controller: 'ThunderHomeReferenceController'
    });
    $routeProvider.otherwise({redirectTo: '/home'});
}]);
