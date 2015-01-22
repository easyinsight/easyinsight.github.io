var easyInsight = angular.module("easyInsight", ["eiAccounts", "eiDataSources", "eiConnections", 'eiScheduling', 'ui.bootstrap', 'ngRoute', 'route-segment', 'view-segment', 'cgBusy', 'eiFilters']);

easyInsight.config(["$routeProvider", "$locationProvider", "$routeSegmentProvider", function ($routeProvider, $locationProvider, $routeSegmentProvider) {

    $locationProvider.html5Mode(true);
    $routeSegmentProvider.when("/missing", "missing").
        segment("missing", {
            templateUrl: '/angular_templates/missing.template.html',
            controller: "MissingFileController"
        });
    $routeProvider.otherwise({ redirectTo: "/missing" });
}])

easyInsight.factory('PageInfo', function() {
    var title = '';
    return {
        title: function() {
            if(title.length == 0)
                return "Easy Insight";
            return "Easy Insight - " + title;
        },
        setTitle: function(value) {
            title = value;
        }
    }
})

easyInsight.controller('MissingFileController', ["PageInfo", function(PageInfo) {
    PageInfo.setTitle("Not Found");
}]);

easyInsight.controller("globalSearchController", ["$scope", "$http", "$timeout", function($scope, $http, $timeout) {
    $scope.search = {
        "text": ""
    };

    $scope.iconFactory = function(result) {
        if(result.type == "dashboard") {
            return "glyphicon-list-alt";
        } else if (result.type == "report") {
            return "glyphicon-th-list"
        }
        else
            return "glyphicon-book";
    };

    $scope.$watch("search.text", function(newVal, oldVal, scope) {
        if(scope.type_delay != null) {
            $timeout.cancel(scope.type_delay);
        }

        scope.type_delay = $timeout(function() {
            scope.results = [];
            if(newVal.length > 0) {
                var searchText = "/app/search?query=" + encodeURIComponent(newVal);
                scope.loading = $http.get(searchText);
                scope.loading.then(function(d) {
                    if(d.config.url == searchText)
                        $scope.results = d.data.results;
                })
            }
        }, 250
        );
    });
}]);

easyInsight.config(["$httpProvider", function($httpProvider) {
    $httpProvider.interceptors.push(["$q", function($q) {
        return {
            'request': function(config) {
                config.headers["X-REQUESTED-WITH"] = "XmlHttpRequest";
                return config;
            }
        }
    }])
}])

easyInsight.run(["$rootScope", "$http", "$location", "PageInfo", "$q", "$window",
    function ($rootScope, $http, $location, PageInfo, $q, $window) {
        $rootScope.user = {
            "username": "..."
        };
        $rootScope.filters = [{"name": "blah", "value": 35, "type": "rolling"}, {"name": "blah2", "type": "single", "value": 26}];
        var user_defer = $q.defer();
    $rootScope.user_promise = user_defer.promise;

    $http.get("/app/userInfo.json").success(function (d, r) {
        if (r == 401)
            window.location = "/app/login.jsp";
        else {
            $rootScope.bookmarks = d.bookmarks;
            $rootScope.user = d.user;
            user_defer.resolve($rootScope.user);
            $rootScope.news_alert = d.news_alert;
        }
    }).error(function () {
        window.location = "/app/login.jsp";
    });

    $rootScope.numKeys = function(obj) {
        if(!obj) return 0;
        return Object.keys(obj).length;
    }
    $rootScope.PageInfo = PageInfo;
    $rootScope.mobile = function() {
        return navigator && navigator.userAgent && navigator.userAgent.match(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i);
    }

        $rootScope.getOffset = function() {
            return new Date().getTimezoneOffset();
        }

        $rootScope.isSearchEnabled = function() {
            return $window.location.host == "localhost" || $window.location.host == "j8staging.easy-insight.com";
        }

}]);

easyInsight.directive("passwordVerify", function() {
   return {
      require: "ngModel",
      scope: {
        passwordVerify: '='
      },
      link: function(scope, element, attrs, ctrl) {
        scope.$watch(function() {
            var combined;
            if (scope.passwordVerify || ctrl.$viewValue) {
               combined = scope.passwordVerify + '_' + ctrl.$viewValue;
            }
            return combined;
        }, function(value) {
            if(scope.passwordVerify && !ctrl.$viewValue) {
                ctrl.$setValidity("passwordVerify", false);
            }
            if (value) {
                ctrl.$parsers.unshift(function(viewValue) {
                    var origin = scope.passwordVerify;
                    if (origin !== viewValue) {
                        ctrl.$setValidity("passwordVerify", false);
                        return undefined;
                    } else {
                        ctrl.$setValidity("passwordVerify", true);
                        return viewValue;
                    }
                });
            }
        });
     }
   };
});

easyInsight.value('cgBusyDefaults',{
    templateUrl: '/angular_templates/angular-busy.html'
});