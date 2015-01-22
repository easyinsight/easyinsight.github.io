(function() {
    var eiFilters = angular.module("eiFilters", []);

    eiFilters.controller('rollingFilter', ["$scope", function($scope) {
        console.log("ROLLING")
    }]);

    eiFilters.controller('singleFilter', ["$scope", function($scope) {
        console.log("SINGLE")
    }]);

    eiFilters.controller("baseFilter", ["$scope", function($scope) {

    }])



    eiFilters.directive('filterView', [function() {
        return {
            restrict: "E",
            controller: "baseFilter",
            templateUrl: "/angular_templates/filters/base_filter.template.html",
            scope: {
                filter: '=filter'
            }
        };

    }]);

    var URL_MAP = {
        "rolling": "rolling_date.template.html",
        "single": "single_value.template.html"
    };

    eiFilters.directive("specificFilter", ["$http", "$compile", function($http, $compile) {
        return {
            restrict: "E",
            link: function(scope, element, attrs) {
                var url =
                $http.get("/angular_templates/filters/" + URL_MAP[scope.filter.type]).then(
                    function(c) {
                        element.html(c.data).show();
                        $compile(element.contents())(scope);
                    }
                );
            },
            scope: "="
        };
    }])

})();