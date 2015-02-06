(function() {
    var eiFilters = angular.module("eiFilters", ["UrlMapService"]);

    eiFilters.controller('rollingFilter', ["$scope", function($scope) {
        // TODO: IMPLEMENT THE REST
    }]);

    eiFilters.controller('singleFilter', ["$scope", function($scope) {
        // TODO: IMPLEMENTATION
    }]);

    eiFilters.controller("baseFilter", ["$scope", function($scope) {

    }]);

    eiFilters.controller("patternMatchFilterController", ["$scope", function($scope) {
        $scope.$watch('filter.pattern', function(oldValue, newValue) {
            if(oldValue != newValue)
                $scope.$emit("filterChanged");
        });
    }]);

    eiFilters.directive('filterView', [function() {
        return {
            restrict: "E",
            controller: "baseFilter",
            templateUrl: "/angular_templates/filter/base_filter.template.html",
            scope: {
                filter: '=filter'
            }
        };

    }]);

    var URL_MAP = {
        "rolling": "rolling_date.template.html",
        "single": "single_value.template.html",
        "pattern_filter": "pattern_filter.template.html"
    };

    eiFilters.run(["UrlMap", function(UrlMap) {
        UrlMap.registerUrls("filter", URL_MAP);
    }])
})();