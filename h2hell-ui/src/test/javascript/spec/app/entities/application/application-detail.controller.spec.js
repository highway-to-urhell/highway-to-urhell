'use strict';

describe('Controller Tests', function() {

    describe('Application Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockApplication, MockAnalysis, MockUserPermission;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockApplication = jasmine.createSpy('MockApplication');
            MockAnalysis = jasmine.createSpy('MockAnalysis');
            MockUserPermission = jasmine.createSpy('MockUserPermission');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Application': MockApplication,
                'Analysis': MockAnalysis,
                'UserPermission': MockUserPermission
            };
            createController = function() {
                $injector.get('$controller')("ApplicationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'h2HellUiApp:applicationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
