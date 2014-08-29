(function () {
    var eiDataSources = angular.module("eiDataSources", ['route-segment', 'view-segment', 'cgBusy', 'ui.bootstrap', 'ui.keypress']);

    eiDataSources.controller("homeBaseController", ["$scope", "$http", function ($scope, $http) {
        $http.get("/app/recentActions.json").then(function (d) {
            $scope.actions = d.data.actions;
        })
    }]);

    eiDataSources.controller("dataSourceListController", ["$scope", "$http", "PageInfo", "$filter", "$modal", function ($scope, $http, PageInfo, $filter, $modal) {
        PageInfo.setTitle("Data Sources");
        $scope.load = $http.get("/app/dataSources.json");
        $scope.load.then(function (d) {
            $scope.data_sources = d.data.data_sources;
        });
        $http.get("/app/html/dataSourceTags.json").then(function (d) {
            $scope.tags = d.data.tags;
        });
        $scope.toggleTag = function (tag) {
            tag.enabled = !tag.enabled;
        }

        $scope.delete_selected = function () {
            var to_delete = $filter("tagged")($scope.data_sources, $scope.tags).filter(function (e, i, l) {
                return e.selected;
            })
            if (to_delete.length > 0) {

                $scope.to_delete = to_delete;
                var m = $modal.open({
                    templateUrl: "/angular_templates/data_sources/delete_dialog.template.html",
                    scope: $scope,
                    controller: "deleteSelectedController"
                });
                m.result.then(function (r) {
                    $scope.data_sources = $scope.data_sources.filter(function (e, i, l) {
                        return !e.selected;
                    });
                });
                m.result.finally(function(r) {
                    delete $scope.to_delete;
                })
            }
        }



        $scope.remove_ds_tag = function (data_source, tag) {
            remove_tag(data_source, tag, "/app/dataSources/" + data_source.url_key + "/tags/" + tag.id, $http);
        }

        $scope.add_ds_tag = function (data_source, tag, model) {
            add_tag(data_source, tag, model, "/app/dataSources/" + data_source.url_key + "/tags", $http);
        }
    }]);

    eiDataSources.controller("deleteSelectedController", ["$scope", "$http", function ($scope, $http) {
        $scope.confirmDelete = function () {
            var v = { "data_sources": $scope.to_delete.map(function (e, i, l) {
                return e.url_key
            }) };
            $scope.deleting = $http.post("/app/dataSources.json", JSON.stringify(v));
            $scope.deleting.then(function (c) {
                $scope.$close();
            })
        }
    }])

    eiDataSources.controller("deleteSelectedReportsController", ["$scope", "$http", function ($scope, $http) {
            $scope.confirmDelete = function () {
                var v = { "reports": $scope.to_delete.map(function (e, i, l) {
                    return {"url_key": e.url_key, "type": e.type }
                }) };

                $scope.deleting = $http.post("/app/dataSources/" + $scope.data_source.url_key + "/reports.json", JSON.stringify(v));
                $scope.deleting.then(function (c) {
                    $scope.$close();
                })
            }
        }])

    var remove_tag = function (data_source, tag, url, $http) {
        var d = $http.delete(url);
        d.then(function (c) {
            data_source.tags.splice(data_source.tags.indexOf(tag), 1);
        })
    }

    var add_tag = function (data_source, tag, model, url, $http) {
        if (!model || !model.text || model.text.length == 0)
            return;
        var d = $http.post(url, JSON.stringify({name: tag ? tag.name : model.text}));
        if (tag)
            data_source.tags.splice(data_source.tags.length, 0, tag);
        else
            d.then(function (c) {
                data_source.tags.splice(data_source.tags.length, 0, c.data);
            })
        model.text = "";

    };

    eiDataSources.controller("reportsListController", ["$scope", "$http", "$routeParams", "$interval", "PageInfo", "$modal",
        "$filter",
        function ($scope, $http, $routeParams, $interval, PageInfo, $modal, $filter) {
            $scope.load = $http.get("/app/dataSources/" + $routeParams.id + "/reports.json");

            $scope.load.then(function (d) {
                $scope.data_source = d.data.data_source;
                PageInfo.setTitle($scope.data_source.name)
                $scope.reports = d.data.reports;
                $scope.folders = d.data.folders;
                $scope.current_folder = $scope.folders[1];
            });

            $scope.refresh_data_source = function () {
                $scope.refresh_status_line = "Starting the refresh...";
                $http.get("/app/refreshDataSource?urlKey=" + $scope.data_source.url_key).then(function (d) {
                    if (d.data && d.data.callDataID) {
                        $scope.refresh_interval = $interval(function () {
                            $http.get("/app/refreshStatus?callDataID=" + d.data.callDataID).then(function (a) {
                                if (a.data.status == 4) {
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

            $scope.$on("$destroy", function () {
                if ($scope.refresh_interval) {
                    $interval.cancel($scope.refresh_interval);
                    $scope.refresh_interval = null;
                }
            })

            $scope.switch_folder = function (folder) {
                $scope.current_folder = folder;
                $scope.folder_dropdown.status = false;
            }
            $scope.folder_dropdown = {status: false};

            $http.get("/app/html/reportTags.json").then(function (d) {
                $scope.tags = d.data.tags;
            });

            $scope.tagsEnabled = function () {
                return $scope.tags && $scope.tags.filter(function (e, i, l) {
                    return e.enabled;
                }).length > 0;
            }

            $scope.toggleTag = function (tag) {
                tag.enabled = !tag.enabled;
            };

            $scope.add_report_tag = function (data_source, tag, model) {
                add_tag(data_source, tag, model, "/app/reports/" + data_source.url_key + "/tags", $http)
            };

            $scope.remove_report_tag = function (data_source, tag) {
                remove_tag(data_source, tag, "/app/reports/" + data_source.url_key + "/tags/" + tag.id, $http);
            }

            $scope.delete_selected = function() {
                var to_delete = $filter("tagged")($scope.reports, $scope.tags, $scope.current_folder.id).filter(function (e, i, l) {
                    return e.selected;
                })
                if (to_delete.length > 0) {

                    $scope.to_delete = to_delete;
                    var m = $modal.open({
                        templateUrl: "/angular_templates/data_sources/delete_reports_dialog.template.html",
                        scope: $scope,
                        controller: "deleteSelectedReportsController"
                    });
                    m.result.then(function (r) {
                        $scope.reports = $scope.reports.filter(function (e, i, l) {
                            return !e.selected;
                        });

                    });
                    m.result.finally(function(r) {
                        delete $scope.to_delete;
                    })
                }
            }

        }]);

    eiDataSources.filter("tagged", function () {
        return function (input, tags, folder) {
            if (!input || !tags)
                return input;
            var v = tags.filter(function (e, i, l) {
                return e.enabled;
            })
            if (v.length > 0) {
                return input.filter(function (e, i, l) {
                    var i, j;
                    for (i = 0; i < v.length; i++) {
                        for (j = 0; j < e.tags.length; j++) {
                            if (v[i].id == e.tags[j].id)
                                return true;
                        }
                    }
                    return false;
                })

            } else if (folder != null) {
                return input.filter(function (e, i, l) {
                    return e.folder == folder;
                });
            } else {
                return input;
            }


        }
    });

    eiDataSources.filter("not_tagged", function () {
        return function (input, data_source_tags) {
            if (!input || !data_source_tags)
                return input;
            return input.filter(function (e, i, l) {
                return !data_source_tags.some(function (ee, ii, ll) {
                    return e.id == ee.id;
                })
            })
        }
    })

    eiDataSources.config(["$routeSegmentProvider", function ($routeSegmentProvider) {
        $routeSegmentProvider.when("/home", "two_column.data_sources").
            when("/", "two_column.data_sources").
            when("/data_sources/:id", "two_column.reports").
            segment("two_column", {
                templateUrl: "/angular_templates/data_sources/home_base.template.html",
                controller: "homeBaseController"
            }).
            within().
            segment("data_sources", {
                templateUrl: "/angular_templates/data_sources/data_sources.template.html",
                controller: "dataSourceListController"
            }).
            segment("reports", {
                templateUrl: "/angular_templates/data_sources/reports.template.html",
                controller: "reportsListController",
                depends: ["id"]
            })
    }])

    String.prototype.capitalize = function () {
        return this.charAt(0).toUpperCase() + this.slice(1);
    }
})();