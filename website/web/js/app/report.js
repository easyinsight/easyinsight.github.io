(function(){
    var eiReporting = angular.module("eiReporting", ["eiFilters", "UrlMapService", "angulard3"]);

    eiReporting.controller("eiReportBase", [function() {

    }]);

    eiReporting.controller("eiReportView", ["$scope", "$routeParams", "$http", "$q", "$timeout", function($scope, $routeParams, $http) {
        $http.get("/app/html/report/" + $routeParams.id + ".json?timezoneOffset=" + $scope.getOffset() + "&data=true").then(function(c) {
            $scope.report = c.data.report;
            $scope.data = c.data.data.values;
        });

        $scope.$on("filterChanged", function() {
            var filters = $scope.report.filters.reduce(function(m, e, i, l) {m[e.id] = e; return m;}, {})
            $http.post("/app/html/report/" + $routeParams.id + "/data.json", JSON.stringify({"filters": filters})).then(function(c) {
                $scope.data = c.data.values;
            })
        });

    }]);

    eiReporting.config(["$routeSegmentProvider", function($routeSegmentProvider) {
        $routeSegmentProvider.when("/report/:id", "report.view").segment(
            "report", {
                templateUrl: "/angular_templates/report/report_base.template.html",
                controller: "eiReportBase"
            }).within().
            segment("view", {
                templateUrl: "/angular_templates/report/report.template.html",
                controller: "eiReportView",
                depends: ["id"]
            })
    }]);
}());