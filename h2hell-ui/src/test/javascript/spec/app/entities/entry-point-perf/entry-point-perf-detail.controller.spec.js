'use strict';

describe('Controller Tests', function() {

    describe('EntryPointPerf Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEntryPointPerf, MockEntryPoint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEntryPointPerf = jasmine.createSpy('MockEntryPointPerf');
            MockEntryPoint = jasmine.createSpy('MockEntryPoint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EntryPointPerf': MockEntryPointPerf,
                'EntryPoint': MockEntryPoint
            };
            createController = function() {
                $injector.get('$controller')("EntryPointPerfDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'h2HellUiApp:entryPointPerfUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
