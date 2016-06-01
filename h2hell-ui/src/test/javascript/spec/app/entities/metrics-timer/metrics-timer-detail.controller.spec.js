'use strict';

describe('Controller Tests', function() {

    describe('MetricsTimer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMetricsTimer, MockEntryPoint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMetricsTimer = jasmine.createSpy('MockMetricsTimer');
            MockEntryPoint = jasmine.createSpy('MockEntryPoint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MetricsTimer': MockMetricsTimer,
                'EntryPoint': MockEntryPoint
            };
            createController = function() {
                $injector.get('$controller')("MetricsTimerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'h2HellUiApp:metricsTimerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
