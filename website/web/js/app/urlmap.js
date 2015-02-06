(function () {
    var mapService = angular.module("UrlMapService", []);

    mapService.factory('UrlMap', function () {

        var urlMapService;
        var urlMap = {};
        urlMapService = {
            registerUrls: function (type, map) {
                urlMap[type] = map;
            },
            getUrl: function (type, value) {
                return urlMap[type][value];
            }
        };
        return urlMapService;
    });

    mapService.directive("dynamicTemplate", ["$http", "$compile", "UrlMap", function($http, $compile, UrlMap) {
        return {
            restrict: "E",
            require: "ngModel",
            link: function(scope, element, attrs, ngModel) {
                var type = attrs.type;
                $http.get("/angular_templates/" + type + "/" + UrlMap.getUrl(type, scope[type].type)).then(
                    function(c) {
                        element.html(c.data).show();
                        $compile(element.contents())(scope);
                    }
                );
            },
            scope: "="
        };
    }]);
}());