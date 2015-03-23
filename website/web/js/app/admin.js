(function () {
    var admin = angular.module("eiAdmin", []);
    admin.controller("eiAdminBase", ["$scope", "$rootScope", function($scope, $rootScope) {
    }]);

    admin.controller("eiHealthView", ["$scope", "$http", function($scope, $http) {
        $http.get("/app/admin/health.json").then(function(c) {
            console.log(c.data);
            $scope.health_data = c.data;
        })
    }])
    admin.config(["$routeSegmentProvider", function($routeSegmentProvider) {
        $routeSegmentProvider.when("/admin/health", "admin.health").segment(
            "admin", {
                templateUrl: "/angular_templates/admin/admin_base.template.html",
                controller: "eiAdminBase"
            }).within().
            segment("health", {
                templateUrl: "/angular_templates/admin/health.template.html",
                controller: "eiHealthView"
            })
    }]);
})();