'use strict';

/**
 * ThunderHomeReferenceController.js
 * 
 * @constructor
 */
var ThunderHomeReferenceController = function($scope, $routeParams, $http) {
	$http.get('api/ThunderApp/findAllThunderApp').success(function(appzList) {
		draw_Appz(appzList);
	});
	$http.get('api/ThunderApp/findAllType').success(function(appzType) {
		draw_Type(appzType);
	});
	$http.get('api/ThunderApp/findGlobalStat').success(function(msg) {
		$scope.numberApplications=msg.numberApplications;
		$scope.numberEntriesPoint=msg.numberEntriesPoint;
	});

	$("#menu li").removeClass("active");
	$("#menu_home").addClass("active");
};

function draw_Appz(apps) {
	var tabAppz = [];
	var tabAppzEntries = [];
	for (var i = 0; i < apps.length; i++) {
		tabAppz.push(apps[i].nameApp);
		tabAppzEntries.push(apps[i].numberEntryPoints);
	}
	console.log(tabAppz.length);
	console.log(tabAppzEntries.length);

	$('#apps_stat').highcharts({
		chart : {
			type : 'bar'
		},
		title : {
			text : 'Applications tracked by H2H'
		},
		xAxis : {
			categories : tabAppz,
			title : {
				text : null
			}
		},
		yAxis : {
			min : 0,
			title : {
				text : '',
				align : 'high'
			},
			labels : {
				overflow : 'justify'
			}
		},
		plotOptions : {
			bar : {
				dataLabels : {
					enabled : true
				}
			}
		},
		legend : {
			layout : 'vertical',
			align : 'right',
			verticalAlign : 'top',
			x : -40,
			y : 100,
			floating : true,
			borderWidth : 1,
			backgroundColor : '#FFFFFF',
			shadow : true
		},
		credits : {
			enabled : false
		},
		series : [ {
			name : 'Entries Points',
			data : [ 18, 18, 18 ]
		} ]
	});
}
function draw_Type(apps) {
	var tabAppz = [];
	for (var i = 0; i < apps.length; i++) {
		console.log("HERE"+apps[i].typeApplication+apps[i].nb);
		tabAppz.push([apps[i].typeApplication,apps[i].nb]);
	}
	$('#apps_type').highcharts({
		chart : {
			type : 'pie',
			options3d : {
				enabled : true,
				alpha : 45
			}
		},
		title : {
			text : 'Type Applications'
		},
		plotOptions : {
			pie : {
				innerSize : 100,
				depth : 45
			}
		},
		credits : {
			enabled : false
		},
		series : [ {
			name : 'Application',
			data : tabAppz
		} ]
	});
}