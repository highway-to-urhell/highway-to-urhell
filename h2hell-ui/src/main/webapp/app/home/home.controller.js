(function() {
    'use strict';

    angular
        .module('h2HellUiApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, Principal, LoginService, $state) {
    $scope.numberApplications = 3;
    $scope.numberEntriesPoint = 50;
        $scope.optionsAppStats = {
            chart: {
                type: 'multiBarHorizontalChart',
                margin : {
                    left: 100
                },
                x: function(d){return d.label;},
                y: function(d){return d.value;},
                //yErr: function(d){ return [-Math.abs(d.value * Math.random() * 0.3), Math.abs(d.value * Math.random() * 0.3)] },
                showControls: false,
                showValues: true,
                duration: 500,
                valueFormat: function(d){
                    return d3.format('d')(d);
                },
                xAxis: {
                    showMaxMin: false
                },
                yAxis: {
                    axisLabel: 'Values',
                    tickFormat: function(d){
                        return d3.format('1d')(d);
                    }
                }
            },
             title: {
                 enable: true,
                 text: 'Entrypoints by application'
             },
        };

        $scope.dataAppStats = [
            {
                "key": "Entrypoints",
                "color": "#d62728",
                "values": [
                    {
                        "label" : "StrutsDemo" ,
                        "value" : 69
                    } ,
                    {
                        "label" : "ExpressJS" ,
                        "value" : 12
                    } ,
                    {
                        "label" : "Symphony" ,
                        "value" : 42
                    } ,
                ]
            },

        ];

        $scope.optionsAppTypes = {
            chart: {
                type: 'pieChart',
                height: 300,
                x: function(d){return d.key;},
                y: function(d){return d.y;},
                showLabels: true,
                duration: 500,
                labelThreshold: 0.01,
                labelSunbeamLayout: true,
                valueFormat: function(d){
                    return d3.format('d')(d);
                },

            },
            title: {
                enable: true,
                text: 'Applications types'
            },
        };

        $scope.dataAppTypes = [
            {
                key: "Java",
                y: 12
            },
            {
                key: "Php",
                y: 2
            },
            {
                key: "NodeJS",
                y: 6
            },
        ];


    }
})();
