var easyInsight = angular.module("easyInsight", ["eiAccounts", "eiDataSources", 'ui.bootstrap', 'ngRoute', 'route-segment', 'view-segment', 'cgBusy']);

easyInsight.config(function ($routeProvider, $locationProvider, $routeSegmentProvider) {
    $locationProvider.html5Mode(true);
    $routeSegmentProvider.when("/missing", "missing").
        segment("missing", {
            templateUrl: function() {console.log(arguments); return '/missing.template.html';}
        });
    $routeProvider.otherwise({ redirectTo: "/missing" });
})

easyInsight.controller('MissingFileController', function() {
})

easyInsight.run(function ($rootScope, $http) {
    $rootScope.user = {
        "username": "..."
    };
    $http.get("/app/userInfo.json").success(function (d, r) {
        if (r == 401)
            window.location = "/app/login.jsp";
        else {
            $rootScope.user = d.user;
        }
    }).error(function () {
        window.location = "/app/login.jsp";
    });
});