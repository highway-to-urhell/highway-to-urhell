'use strict';

describe('Controller Tests', function() {

    describe('EntryPointCall Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEntryPointCall, MockEntryPoint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEntryPointCall = jasmine.createSpy('MockEntryPointCall');
            MockEntryPoint = jasmine.createSpy('MockEntryPoint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EntryPointCall': MockEntryPointCall,
                'EntryPoint': MockEntryPoint
            };
            createController = function() {
                $injector.get('$controller')("EntryPointCallDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'h2HellUiApp:entryPointCallUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
