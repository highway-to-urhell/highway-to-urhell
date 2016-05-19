'use strict';

describe('Controller Tests', function() {

    describe('EntryPointParameters Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEntryPointParameters, MockMetricsTimer, MockEntryPoint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEntryPointParameters = jasmine.createSpy('MockEntryPointParameters');
            MockMetricsTimer = jasmine.createSpy('MockMetricsTimer');
            MockEntryPoint = jasmine.createSpy('MockEntryPoint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EntryPointParameters': MockEntryPointParameters,
                'MetricsTimer': MockMetricsTimer,
                'EntryPoint': MockEntryPoint
            };
            createController = function() {
                $injector.get('$controller')("EntryPointParametersDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'h2HellUiApp:entryPointParametersUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
