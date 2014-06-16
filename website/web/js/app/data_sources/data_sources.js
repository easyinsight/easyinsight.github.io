var eiDataSources = angular.module("eiDataSources", ['route-segment', 'view-segment']);

eiDataSources.controller("homeBaseController", function($scope, $http) {
    $http.get("/app/recentActions.json").then(function(d) {
        $scope.actions = d.data.actions;
    })
})

eiDataSources.controller("dataSourceListController", function($scope, $http) {
    $http.get("/app/dataSources.json").then(function(d) {
        $scope.data_sources = d.data.data_sources;
    })
})

eiDataSources.controller("reportsListController", function($scope, $http, $routeParams) {
    console.log($routeParams.id);
    $http.get("/app/reports/" + $routeParams.id + ".json");
})

eiDataSources.config(function ($routeSegmentProvider) {
    $routeSegmentProvider.when("/home", "two_column.data_sources").
        when("/data_sources/:id", "two_column.reports").
        segment("two_column", {
            templateUrl: "/data_sources/home_base.template.html",
            controller: "homeBaseController"
        }).
        within().
        segment("data_sources", {
            templateUrl: "/data_sources/data_sources.template.html",
            controller: "dataSourceListController"
        }).
        segment("reports", {
            templateUrl: "/data_sources/reports.template.html",
            controller: "reportsListController",
            depends: ["id"]
        })
})