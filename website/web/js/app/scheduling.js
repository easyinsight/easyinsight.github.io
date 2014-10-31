(function() {
    var eiScheduling = angular.module("eiScheduling", []);

    eiScheduling.controller("schedulingBaseController", ["$scope", "$http", function($scope, $http) {
        $scope.reload = function() {
            $scope.loading = $http.get("/app/scheduled_tasks.json?offset=" + $scope.getOffset()).then(function (c) {
                $scope.schedules = c.data;
            })
        }
        $scope.reload();
    }])

    eiScheduling.controller("schedulingReportsController", ["$scope", "$modal", function($scope, $modal) {
        $scope.deleteSelected = function() {
        var to_delete = $scope.schedules.reports.filter(function(e, i, l) {
            return e.selected;
        })
        if(to_delete.length > 0) {
            $scope.to_delete = to_delete;
            var m = $modal.open({
                templateUrl: "/angular_templates/scheduling/delete_scheduled_report_dialog.template.html",
                scope: $scope,
                controller: "deleteScheduledDataSourceController"
            });
            m.result.then(function() {
                $scope.schedules.reports = $scope.schedules.reports.filter(function (e, i, l) {
                    return !e.selected;
                });
            });
            m.result.finally(function() {
                delete $scope.to_delete;
            })
        }
    }
    }])

    eiScheduling.controller("deleteScheduledDataSourceController", ["$scope", function($scope) {
        $scope.confirmDelete = function() {
            $scope.$close();
        }
    }])

    eiScheduling.controller("schedulingDataSourceController", ["$scope", "$modal", function($scope, $modal) {
        $scope.deleteSelected = function() {
            var to_delete = $scope.schedules.data_sources.filter(function(e, i, l) {
                return e.selected;
            })
            if(to_delete.length > 0) {
                $scope.to_delete = to_delete;
                var m = $modal.open({
                    templateUrl: "/angular_templates/scheduling/delete_scheduled_data_source_dialog.template.html",
                    scope: $scope,
                    controller: "deleteScheduledDataSourceController"
                });
                m.result.then(function() {
                    $scope.schedules.data_sources = $scope.schedules.data_sources.filter(function (e, i, l) {
                        return !e.selected;
                    });
                });
                m.result.finally(function() {
                    delete $scope.to_delete;
                })
            }
        }
    }])

    eiScheduling.controller("viewDataSourceSchedulingController", ["$scope", "$http", "$routeParams", "$location", function($scope, $http, $routeParams, $location) {
        $scope.loading.then(function() {
            var i;
            for(i = 0;i < $scope.schedules.data_sources.length;i++) {
                if($scope.schedules.data_sources[i].id == $routeParams.id) {
                    $scope.schedule = angular.copy($scope.schedules.data_sources[i]);
                }
            }

        })

        $scope.save_schedule = function() {
            var s = $http.post("/app/scheduled_tasks/" + $scope.schedule.id + ".json?offset=" + $scope.getOffset(), JSON.stringify($scope.schedule));
            s.then(function(d) {
                $scope.reload();
                $location.path("/scheduling/data_sources");

            })
        }
    }])

    eiScheduling.controller("viewDeliverySchedulingController", ["$scope", "$routeParams", "$http", "$location", function($scope, $routeParams, $http, $location) {
        $scope.loading.then(function() {
            var i;
            for(i = 0;i < $scope.schedules.reports.length;i++) {
                if($scope.schedules.reports[i].id == $routeParams.id) {
                    $scope.schedule = angular.copy($scope.schedules.reports[i]);

                }
            }
            $scope.save_schedule = function() {
                var v = $http.post("/app/scheduled_tasks/" + $scope.schedule.id + ".json?offset=" + $scope.getOffset(), JSON.stringify($scope.schedule));
                v.then(function(a) {
                    $scope.reload();
                     $location.path("/scheduling/reports")
                })
            }
        })
    }])

    eiScheduling.controller("viewDeliverySchedulingRecipientsController", ["$scope", "$http", function($scope, $http) {
        $scope.loading.then(function() {
            $scope.all_emails = $scope.schedule.emails.map(function(e, i, l) {return {selected: false, email: e }})
            $scope.load = $http.get("/app/json/getUsers").success(function (d, r) {
                $scope.users = d.users;
            })

            $scope.full_name = function(user) {
                if(typeof(user) != "undefined")
                    return user.first_name + " " + user.last_name + " (" + user.email + ")";
                else
                    return "";
            }

            $scope.add_default = function(list) {
                return [{"id": 0, "first_name": "Easy", "last_name": "Insight", "email": "reports@easy-insight.com"}].concat(list);
            }

            $http.get("/app/json/groups").then(function(d, r) {
                $scope.groups = d.data.groups;
            })

            $scope.addables = function() {
                return [].concat($scope.groups.map(function(e, i, l) { return {label: e.name, type: "group", data: e};})).concat($scope.users.map(function(e, i, l) { return {label: $scope.full_name(e), type: "user", data: e};}));
            };
            $scope.add_selected = function() {
                var value = $scope.added;
                if(typeof(value) == "string") {
                    if(value.match(/\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b/i)) {
                        $scope.all_emails.push({ selected: false, email: $scope.added });
                        $scope.schedule.emails = $scope.all_emails.map(function(e, i, l) { return e.email; })
                        $scope.added = "";
                    }
                } else if(typeof(value) != "undefined") {
                    if(value.type == "group") {
                        $scope.schedule.groups.push(value.data);
                        $scope.added = "";
                    } else if(value.type == "user") {
                        $scope.schedule.users.push(value.data);
                        $scope.added = "";
                    }
                }
            };

            $scope.delete_selected = function() {
                $scope.all_emails = $scope.all_emails.filter(function(e, i, l) { return !e.selected })
                $scope.schedule.emails = $scope.all_emails.map(function(e, i, l) { return e.email; })
                $scope.schedule.groups = $scope.schedule.groups.filter(function(e, i, l) { return !e.selected; })
                $scope.schedule.users = $scope.schedule.users.filter(function(e, i, l) { return !e.selected; })
            }
        })
    }]);

    eiScheduling.controller("viewDeliverySchedulingEmailController", [function() {

    }])

    eiScheduling.controller("viewDeliverySchedulingHistoryController", ["$scope", "$http", function($scope, $http) {
        $scope.loading.then(function() {
            var l = $http.get("/app/deliveries/" + $scope.schedule.id + "/history.json");

             l.then(function(c) { 
                $scope.history = c.data.history;
            })
        })
        $scope.runNow = function() {
            $scope.finished = false;
            $scope.error = false;
            var l = $http.post("/app/deliveries/" + $scope.schedule.id + "/run", JSON.stringify({}));
            l.then(function() {
                $scope.finished = true;
            }, function() {
                $scope.finished = false;
                $scope.error = true;
            })
        }
    }])

    eiScheduling.controller("viewDeliverySchedulingReportController", ["$scope", "$http", function($scope, $http) {
        $scope.ds_load = $http.get("/app/dataSources.json");
        $scope.ds_load.then(function (d) {
            $scope.data_sources = d.data.data_sources;
            if($scope.data_sources.length == 1) {
                $scope.ds_name = $scope.data_sources[0];
                $scope.select_data_source($scope.ds_name);
            }
            $scope.loading.then(function() {
                if(typeof($scope.schedule.data_source_id) != "undefined") {
                    var i;
                    var found = -1;
                    for(i = 0;i < $scope.data_sources.length;i++) {
                        if($scope.data_sources[i].id == $scope.schedule.data_source_id) {
                            found = i;
                        }
                    }
                    if(found != -1) {
                        $scope.ds_name = $scope.data_sources[found];
                        $scope.select_data_source($scope.ds_name);
                        $scope.report_loading.then(function() {

                            if(typeof($scope.schedule.report_id != null))
                            var i;
                            var found = -1;
                            for(i = 0;i < $scope.reports.length;i++) {
                                if($scope.reports[i].id == $scope.schedule.report_id)
                                    found = i;
                            }
                            if(found != -1) {
                                $scope.selected_report = $scope.reports[found];
                                $scope.select_report($scope.selected_report);
                            }
                        });
                    }
                }
            });
        });
        $scope.isSelected = function(val) {
            return val && typeof(val) === "object"
        };
        $scope.select_data_source = function(item) {
            $scope.report_loading = $http.get("/app/dataSources/" + item.url_key + "/reports.json").then(function(d) {
                $scope.reports = d.data.reports;
            });
        };
        $scope.select_report = function(item) {
            $scope.schedule.report_id = item.id;
            $http.get("/app/html/report/" + item.url_key + "/data.json").then(function(d) {
                $scope.configurations = d.data.configurations;
            });
        };
        $scope.default_added = function(a) {
            return [{"name": "Default Configuration", "url_key": "", "id": 0}].concat(a);
        };
    }]);

    eiScheduling.config(["$routeSegmentProvider", function($routeSegmentProvider) {
        $routeSegmentProvider.when("/scheduling", "scheduling.reports").
            when("/scheduling/reports", "scheduling.reports").
            when("/scheduling/data_sources", "scheduling.data_sources").
            when("/scheduling/data_sources/:id", "scheduling.view_data_source").
            when("/scheduling/delivery/:id", "scheduling.view_delivery.scheduling").
            when("/scheduling/delivery/:id/schedule", "scheduling.view_delivery.scheduling").
            when("/scheduling/delivery/:id/report", "scheduling.view_delivery.report_info").
            when("/scheduling/delivery/:id/email", "scheduling.view_delivery.email").
            when("/scheduling/delivery/:id/history", "scheduling.view_delivery.history").
            segment("scheduling", {
                templateUrl: "/angular_templates/scheduling/scheduling_base.template.html",
                controller: "schedulingBaseController"
            }).within().
            segment("reports", {
                templateUrl: "/angular_templates/scheduling/report_delivery_list.template.html",
                controller: "schedulingReportsController"
            }).
            segment("data_sources", {
                templateUrl: "/angular_templates/scheduling/data_source_list.template.html",
                controller: "schedulingDataSourceController"
            }).
            segment("view_data_source", {
                templateUrl: "/angular_templates/scheduling/view_data_source_refresh.template.html",
                controller: "viewDataSourceSchedulingController",
                depends: ["id"]
            }).
            segment("view_delivery", {
                templateUrl: "/angular_templates/scheduling/view_delivery_base.template.html",
                controller: "viewDeliverySchedulingController",
                depends: ["id"]
            }).within().
            segment("scheduling", {
                templateUrl: "/angular_templates/scheduling/view_delivery_schedule.template.html",
                controller: "viewDeliverySchedulingRecipientsController"
            }).
            segment("report_info", {
                templateUrl: "/angular_templates/scheduling/view_delivery_report.template.html",
                controller: "viewDeliverySchedulingReportController"
            }).
            segment("email", {
                templateUrl: "/angular_templates/scheduling/view_delivery_email.template.html",
                controller: "viewDeliverySchedulingEmailController"
            }).segment("history", {
                templateUrl: "/angular_templates/scheduling/report_delivery_history.template.html",
                controller: "viewDeliverySchedulingHistoryController"
            })
    }])
})()