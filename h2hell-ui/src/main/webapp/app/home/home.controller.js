(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Home'];

    function HomeController ($scope, Principal, LoginService, $state, Home) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                vm.entity = Home.get();
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
