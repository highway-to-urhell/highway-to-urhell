'use strict';

/**
 * ThunderHomeReferenceController.js
 * @constructor
 */
var ThunderHomeReferenceController = function($scope,$routeParams, $http) {
	 $http.get('api/ThunderApp/findAllThunderApp').success(function(appzList){
         console.log("ThunderAppHome initialized", appzList); 
         draw_chart (appzList);
     });
  
    $("#menu li").removeClass("active");
    $("#menu_home").addClass("active");
};


function draw_chart (apps) {
	var convertedAppToArray = [];
	apps.reduce(
			function(prev, curr) {
				convertedAppToArray.push([curr.nameApp, curr.numberEntryPoints]);
			}
			, convertedAppToArray);
	
    $('#sum_up_apps').highcharts({
        chart: {
            type: 'pie',
            options3d: {
                enabled: true,
                alpha: 45
            }
        },
        title: {
            text: 'H2H'
        },
        subtitle: {
            text: 'Applications tracked by H2H'
        },
        plotOptions: {
            pie: {
                innerSize: 100,
                depth: 45
            }
        },
        series: [{
            name: 'Entry points',
            data: convertedAppToArray
        }]
    });
}