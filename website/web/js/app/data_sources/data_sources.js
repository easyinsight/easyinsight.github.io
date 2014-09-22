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

    eiDataSources.filter("not_already_selected", function() {
        return function(input, selected_list) {
            return input.filter(function(e, i, l) {
                return !selected_list.some(function(ee, ii, ll) {
                        return e == ee;
                    }
                )
            });
        }
    })

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

    eiDataSources.filter("remove_other_source", function() {
        return function(input, data_source) {
            var output = input.slice(0);
            if(data_source) {
                var i;
                var index = -1;
                for(i = 0;i < output.length;i++) {
                    if(output[i].url_key == data_source.url_key)
                        index = i;
                }
                if(index != -1)
                    output.splice(index, 1);
            }
            return output;
        }


    })

    eiDataSources.controller("addDataSourceController", ["$scope", "$http", "$location", "$modal", function($scope, $http, $location, $modal) {
        $scope.loadingSources = $http.get("/app/dataSources.json");
        $scope.loadingSources.then(function(c) {
            $scope.all_data_sources = c.data.data_sources.map(function(e, i, l) {
                e.selected = $scope.composite.data_sources.some(function(ee, ii, ll) {
                    return ee.url_key == e.url_key;
                });

                return e;
            }).filter(function(e, i, l) {
                return e.type != "composite";
            });

        });
        $scope.add_join = function() {
            var flagged = [];
            var a = $scope.all_data_sources.filter(function(e, i, l) {
                var aa = e.selected;
                if(!aa && $scope.composite.data_sources.some(function(ee, ii, ll) {
                    return ee.url_key == e.url_key
                })) {
                    flagged.push(e);
                }
                return aa;
            })
            var flagged_joins = $scope.composite.joins.filter(function(e, i, l) {
                return flagged.some(function(ee, ii, ll) {
                    return e.source_ds.url_key == ee.url_key || e.target_ds.url_key == ee.url_key;
                })
            })
            var i;
            if(flagged_joins.length == 0) {
                $scope.composite.data_sources = a;
                for(i = 0;i < $scope.all_data_sources.length;i++) {
                    $scope.all_data_sources[i].selected = false;
                }
                $location.path("/composite/new")
            } else {
                $scope.to_delete = flagged;
                var m = $modal.open({
                    templateUrl: "/angular_templates/data_sources/composite/delete_data_sources.template.html",
                    scope: $scope,
                    controller: "compositeRemoveSelectedDataSourcesController"
                })
                m.result.then(function() {
                    $scope.composite.data_sources = a;
                    $location.path("/composite/new");
                })

                m.result.finally(function() {
                    delete $scope.to_delete;
                })
            }
        }
    }])

    eiDataSources.controller("compositeRemoveSelectedDataSourcesController", ["$scope", function($scope) {
        $scope.delete_joins = $scope.composite.joins.filter(function(e, i, l) {
            return $scope.to_delete.some(function(ee, ii, ll) {
                return e.source_ds.url_key == ee.url_key || e.target_ds.url_key == ee.url_key;
            });
        })
        $scope.confirmDelete = function() {
            $scope.composite.data_sources = $scope.composite.data_sources.filter(function(e,i,l) {
                return !e.selected;
            });
            $scope.composite.joins = $scope.composite.joins.filter(function(e, i, l) {
                return !$scope.delete_joins.some(function(ee, ii, ll) {
                    return ee == e;
                })
            })
            $scope.$close();
        }
    }])

    eiDataSources.controller("compositeRemovaSelectedJoinsController", ["$scope", function($scope) {
            $scope.confirmDelete = function() {
                $scope.composite.joins = $scope.composite.joins.filter(function(e,i,l) {
                    return !e.selected;
                });
                $scope.$close();
            }
        }])

    eiDataSources.controller("combineDifferentSourcesController", ["$scope", "$modal", "$http", function($scope, $modal, $http) {
        $scope.removeSelectedDS = function() {
            $scope.to_delete = $scope.composite.data_sources.filter(function(e, i, l) {
                return e.selected;
            })
            if($scope.to_delete.length == 0)
                return;
            var m = $modal.open({
                templateUrl: "/angular_templates/data_sources/composite/delete_data_sources.template.html",
                scope: $scope,
                controller: "compositeRemoveSelectedDataSourcesController"
            })
            m.result.finally(function() {
                delete $scope.to_delete;
            })
        }
        $scope.removeSelectedJoins = function() {
            $scope.to_delete = $scope.composite.joins.filter(function(e, i, l) {
                return e.selected;
            })
            if($scope.to_delete.length == 0)
                return;
            var m = $modal.open({
                templateUrl: "/angular_templates/data_sources/composite/delete_joins.template.html",
                scope: $scope,
                controller: "compositeRemovaSelectedJoinsController"
            })
            m.result.finally(function(){
                delete $scope.to_delete;
            })
        }
        $scope.save_composite = function() {
            $scope.saving = $http.post("/app/html/composite", JSON.stringify($scope.composite));

        }
    }])

    eiDataSources.controller("combineDataSourcesSplashController", function() {

    })

    eiDataSources.controller("combineDifferentSourcesBaseController", ["$scope", "$http", "$rootScope", "$location", function($scope, $http, $rootScope, $location) {
        $rootScope.user_promise.then(function(u) {
            if(!u.designer) {
                $location.path("/missing");
            }
        });

        $scope.composite = {"data_sources": [], "joins": []}

    }]);

    eiDataSources.controller("joinEditController", ["$scope", "$http", "$location", "$routeParams",
        function($scope, $http, $location, $routeParams) {
            if($routeParams.id == "new")
                $scope.join = {
                    "source_cardinality": "one",
                    "target_cardinality": "one",
                    "outer_join": false
                }
            else
                $scope.join = angular.copy($scope.composite.joins[$routeParams.id])
        $scope.select_target_data_source = function (item, model, label) {
            $scope.loading_target_fields = $http.get("/app/html/reports/" + item.url_key + "/fields.json");
            $scope.loading_target_fields.then(function(c) {
                $scope.target_fields = c.data;
            })
        }
        $scope.select_source_data_source = function (item, model, label) {
            $scope.loading_source_fields = $http.get("/app/html/reports/" + item.url_key + "/fields.json");
            $scope.loading_source_fields.then(function(c) {
                $scope.source_fields = c.data;
            })
        }
        $scope.add_join = function() {
            if(!$scope.joinForm.$valid)
                return;
            if($routeParams.id == "new")
                $scope.composite.joins.push($scope.join);
            else
                $scope.composite.joins[$routeParams.id] = $scope.join;
            $location.path("/composite/new");
        }
    }])

    eiDataSources.config(["$routeSegmentProvider", function ($routeSegmentProvider) {
        $routeSegmentProvider.when("/home", "two_column.data_sources").
            when("/", "two_column.data_sources").
            when("/data_sources/:id", "two_column.reports").
            when("/combine_sources", "combine_sources").
            when("/composite/new", "combine_different_sources.new_source").
            when("/composite/new/joins/:id", "combine_different_sources.edit_join").
            when("/composite/new/data_sources", "combine_different_sources.edit_data_sources").
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
            }).up().
            segment("combine_sources", {
                templateUrl: "/angular_templates/data_sources/combine_data_sources_splash.template.html",
                controller: "combineDataSourcesSplashController"
            }).
            segment("combine_different_sources", {
                templateUrl: "/angular_templates/data_sources/composite_base.template.html",
                controller: "combineDifferentSourcesBaseController"
            }).
            within().
            segment("new_source", {
                templateUrl: "/angular_templates/data_sources/combine_different_sources.template.html",
                controller: "combineDifferentSourcesController"
            }).
            segment("edit_join", {
                templateUrl: "/angular_templates/data_sources/join_sources.template.html",
                controller: "joinEditController",
                depends: ["id"]
            }).
            segment("edit_data_sources", {
                templateUrl: "/angular_templates/data_sources/composite/add_data_sources.template.html",
                controller: "addDataSourceController"
            })
    }])

    String.prototype.capitalize = function () {
        return this.charAt(0).toUpperCase() + this.slice(1);
    }
})();