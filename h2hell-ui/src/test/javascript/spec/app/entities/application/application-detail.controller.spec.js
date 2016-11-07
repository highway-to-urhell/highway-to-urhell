'use strict';

describe('Controller Tests', function() {

    describe('Application Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockApplication, MockAnalysis, MockUserPermission;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockApplication = jasmine.createSpy('MockApplication');
            MockAnalysis = jasmine.createSpy('MockAnalysis');
            MockUserPermission = jasmine.createSpy('MockUserPermission');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
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
