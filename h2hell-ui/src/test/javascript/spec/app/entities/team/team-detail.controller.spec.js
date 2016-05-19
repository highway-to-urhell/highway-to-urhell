'use strict';

describe('Controller Tests', function() {

    describe('Team Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTeam, MockUser, MockUserPermission;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTeam = jasmine.createSpy('MockTeam');
            MockUser = jasmine.createSpy('MockUser');
            MockUserPermission = jasmine.createSpy('MockUserPermission');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Team': MockTeam,
                'User': MockUser,
                'UserPermission': MockUserPermission
            };
            createController = function() {
                $injector.get('$controller')("TeamDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'h2HellUiApp:teamUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
