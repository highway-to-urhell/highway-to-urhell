'use strict';

describe('Controller Tests', function() {

    describe('EntryPoint Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEntryPoint, MockMetricsTimer, MockEntryPointParameters, MockAnalysis;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEntryPoint = jasmine.createSpy('MockEntryPoint');
            MockMetricsTimer = jasmine.createSpy('MockMetricsTimer');
            MockEntryPointParameters = jasmine.createSpy('MockEntryPointParameters');
            MockAnalysis = jasmine.createSpy('MockAnalysis');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EntryPoint': MockEntryPoint,
                'MetricsTimer': MockMetricsTimer,
                'EntryPointParameters': MockEntryPointParameters,
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
