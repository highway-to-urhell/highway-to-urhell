'use strict';

describe('Controller Tests', function() {

    describe('UserPermission Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserPermission, MockApplication, MockTeam;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserPermission = jasmine.createSpy('MockUserPermission');
            MockApplication = jasmine.createSpy('MockApplication');
            MockTeam = jasmine.createSpy('MockTeam');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserPermission': MockUserPermission,
                'Application': MockApplication,
                'Team': MockTeam
            };
            createController = function() {
                $injector.get('$controller')("UserPermissionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'h2HellUiApp:userPermissionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
