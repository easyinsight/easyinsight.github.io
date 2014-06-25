var eiDataSources = angular.module("eiDataSources", ['route-segment', 'view-segment', 'cgBusy']);

eiDataSources.controller("homeBaseController", function($scope, $http) {
    $http.get("/app/recentActions.json").then(function(d) {
        $scope.actions = d.data.actions;
    })
})

eiDataSources.controller("dataSourceListController", function($scope, $http, PageInfo) {
    PageInfo.setTitle("Data Sources");
    $scope.load = $http.get("/app/dataSources.json");
    $scope.load.then(function(d) {
        $scope.data_sources = d.data.data_sources;
    });
    $http.get("/app/html/dataSourceTags.json").then(function(d) {
        $scope.tags = d.data.tags;
    });
    $scope.toggleTag = function(tag) {
        tag.enabled = !tag.enabled;
    }
})

eiDataSources.controller("reportsListController", function($scope, $http, $routeParams, $interval, $q, PageInfo) {
    $scope.load = $http.get("/app/dataSources/" + $routeParams.id + "/reports.json")
    $scope.load.then(function(d) {
        $scope.data_source = d.data.data_source;
        PageInfo.setTitle($scope.data_source.name)
        $scope.reports = d.data.reports;
        $scope.folders = d.data.folders;
        $scope.current_folder = 1;
    });

    $scope.refresh_data_source = function() {
        $scope.refresh_status_line = "Starting the refresh...";
        $http.get("/app/refreshDataSource?urlKey=" + $scope.data_source.url_key).then(function(d) {
            if(d.data && d.data.callDataID) {
                $scope.refresh_interval = $interval(function() {
                    $http.get("/app/refreshStatus?callDataID=" + d.data.callDataID).then(function(a) {
                        if(a.data.status == 4) {
                            $scope.refresh_status_line = "";
                            $interval.cancel($scope.refresh_interval);
                            $scope.refresh_interval = null;
                        }
                        $scope.refresh_status_line = a.data.statusMessage;
                    });
                }, 5000);
            }
        });
    }

    $scope.$on("$destroy", function() {
        if($scope.refresh_interval) {
            $interval.cancel($scope.refresh_interval);
            $scope.refresh_interval = null;
        }
    })

    $scope.switch_folder = function(folder) {
        $scope.current_folder = folder;
    }

    $http.get("/app/html/reportTags.json").then(function(d) {
        $scope.tags = d.data.tags;
    });

    $scope.tagsEnabled = function() {
        return $scope.tags && $scope.tags.filter(function(e, i, l) { return e.enabled; }).length > 0;
    }

    $scope.toggleTag = function(tag) {
        tag.enabled = !tag.enabled;
    };
});

eiDataSources.filter("tagged", function() {
    return function(input, tags, folder) {
        if(!input || !tags)
            return input;
        var v = tags.filter(function(e, i, l) {
            return e.enabled;
        })
        if(v.length > 0) {
            return input.filter(function(e, i, l) {
                var i, j;
                for(i = 0;i < v.length;i++) {
                    for(j = 0;j < e.tags.length;j++) {
                        if(v[i].id == e.tags[j].id)
                            return true;
                    }
                }
                return false;
            })

        } else if(folder != null) {
            return input.filter(function(e, i, l) {
                return e.folder == folder;
            });
        } else {
            return input;
        }


    }
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

String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
}