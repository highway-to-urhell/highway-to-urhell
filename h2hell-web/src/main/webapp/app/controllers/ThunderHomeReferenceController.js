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

var h2hGraphColors =  ["#EA0037", "#FF5600", "#B40097", "#53DF00", "#980023", "#A63800", "#750062", "#369100", "#F56E8D", "#FFA273", "#60D4AE", "#9CEF6C"];


function draw_Appz(apps) {
	var tabAppz = [];
	var tabAppzEntries = [];
	
	for (var i = 0; i < apps.length; i++) {
		tabAppz.push(apps[i].nameApp);
		tabAppzEntries.push(
				{
					'y' : Number(apps[i].numberEntryPoints),
					'token' : apps[i].token
				});
		console.log(Number(apps[i].numberEntryPoints));
		console.log(tabAppz);
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
				text : 'PPPP',
				align : 'high'
			},
			labels : {
				overflow : 'justify'
			}
		},
		plotOptions: {
			column: {
				colorByPoint: true
			},
			bar : {
				dataLabels : {
					enabled : true
				},
				colorByPoint : true,
				colors : h2hGraphColors
			},
            series: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function () {
                            location.href = "#/thunderstat/" + this.options.token;
                        }
                    }
                }
            }
        },
		legend : {
			layout : 'vertical',
			align : 'right',
			verticalAlign : 'top',
			x : 0,
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
			name : 'Entry Points',
			data : tabAppzEntries
		} ]
	});
}
function draw_Type(apps) {
	var tabAppz = [];
	for (var i = 0; i < apps.length; i++) {
		tabAppz.push([apps[i].typeApplication,apps[i].nb]);
	}
	$('#apps_type').highcharts({
		chart : {
			type : 'pie',
			options3d : {
				enabled : true,
				alpha : 45
			},
			colors : h2hGraphColors
		},
		title : {
			text : 'Application types'
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