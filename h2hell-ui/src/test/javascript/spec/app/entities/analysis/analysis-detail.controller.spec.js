'use strict';

describe('Controller Tests', function() {

    describe('Analysis Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAnalysis, MockEvent, MockEntryPoint, MockApplication;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAnalysis = jasmine.createSpy('MockAnalysis');
            MockEvent = jasmine.createSpy('MockEvent');
            MockEntryPoint = jasmine.createSpy('MockEntryPoint');
            MockApplication = jasmine.createSpy('MockApplication');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Analysis': MockAnalysis,
                'Event': MockEvent,
                'EntryPoint': MockEntryPoint,
                'Application': MockApplication
            };
            createController = function() {
                $injector.get('$controller')("AnalysisDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'h2HellUiApp:analysisUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
