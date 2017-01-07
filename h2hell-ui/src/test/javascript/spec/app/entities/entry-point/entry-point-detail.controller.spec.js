'use strict';

describe('Controller Tests', function() {

    describe('EntryPoint Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEntryPoint, MockEntryPointPerf, MockEntryPointCall, MockAnalysis;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEntryPoint = jasmine.createSpy('MockEntryPoint');
            MockEntryPointPerf = jasmine.createSpy('MockEntryPointPerf');
            MockEntryPointCall = jasmine.createSpy('MockEntryPointCall');
            MockAnalysis = jasmine.createSpy('MockAnalysis');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EntryPoint': MockEntryPoint,
                'EntryPointPerf': MockEntryPointPerf,
                'EntryPointCall': MockEntryPointCall,
                'Analysis': MockAnalysis
            };
            createController = function() {
                $injector.get('$controller')("EntryPointDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'h2HellUiApp:entryPointUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
