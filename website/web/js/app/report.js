(function(){
    var eiReporting = angular.module("eiReporting", ["eiFilters"]);

    eiReporting.controller("eiReportBase", [function() {

    }]);

    eiReporting.controller("eiReportView", ["$scope", "$routeParams", "$http", function($scope, $routeParams, $http) {
        $http.get("/app/html/report/" + $routeParams.id + ".json").then(function(c) {
            $scope.report = c.data;
        });
    }]);

    eiReporting.config(["$routeSegmentProvider", function($routeSegmentProvider) {
        $routeSegmentProvider.when("/report/:id", "report.view").segment(
            "report", {
                templateUrl: "/angular_templates/reports/report_base.template.html",
                controller: "eiReportBase"
            }).within().
            segment("view", {
                templateUrl: "/angular_templates/reports/report.template.html",
                controller: "eiReportView",
                depends: ["id"]
            })
    }]);
}());