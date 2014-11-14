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

    eiScheduling.controller("deleteScheduledDataSourceController", ["$scope", "$http", function($scope, $http) {
        $scope.confirmDelete = function() {
            var l = $http.delete("/app/scheduled_tasks.json", {data: JSON.stringify({
                "schedules": $scope.to_delete.map(function(e, i, l) { return e.id; })
                }
            )  });
            l.then(function() {
                $scope.$close();
            })
        }
    }])

    eiScheduling.controller("schedulingDataSourceController", ["$scope", "$modal", "$http", function($scope, $modal, $http) {

        $http.get("/app/scheduling/missing_refreshes.json").then(function(c) {
            $scope.refreshables = c.data.refreshables;
        })

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
        console.log($routeParams);
        $scope.loading.then(function() {

            if($routeParams.id == "new") {
                $scope.schedule = {
                    data_source: $routeParams.data_source_id,
                    schedule_type: {
                        offset: $scope.getOffset(),
                        type: "daily",
                        hour: 0,
                        minute: 0
                    },
                    interval_number: 0,
                    interval_units: 0,
                    id: "new",
                    type: "data_source",
                    description: "New Refresh"
                }
            } else {
                var i;
                for (i = 0; i < $scope.schedules.data_sources.length; i++) {
                    if ($scope.schedules.data_sources[i].id == $routeParams.id) {
                        $scope.schedule = angular.copy($scope.schedules.data_sources[i]);
                    }
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
            $scope.is_new = $routeParams.id == "new";
            console.log($location.path())
            if($scope.is_new) {
                $scope.schedule = {
                    emails: [],
                    groups: [],
                    users: [],
                    id: "new",
                    type: $location.path().match(/scheduling\/delivery/i) ? "report" : "general",
                    schedule_type: {
                        offset: $scope.getOffset(),
                        type: "daily",
                        hour: 0,
                        minute: 0
                    },
                    label: "",
                    delivery_info: [],
                    report_id: 0,
                    sender: 0,
                    timezone_offset: $scope.getOffset(),
                    offset: $scope.getOffset(),
                    configuration_id: 0,
                    report_format: "excel2007",
                    delivery_label: "",
                    subject: "",
                    body: ""
                }
            } else {
                var i;
                for (i = 0; i < $scope.schedules.reports.length; i++) {
                    if ($scope.schedules.reports[i].id == $routeParams.id) {
                        $scope.schedule = angular.copy($scope.schedules.reports[i]);
                    }
                }
            }

            $scope.errors = [];

            $scope.to_schedule = function() {
                $scope.errors = [];
                if($scope.schedule.type == "report" && $scope.schedule.report_id == 0) {
                    $scope.errors.push("There must be a report selected.");
                } else if ($scope.schedule.type == "general" && $scope.schedule.delivery_info.length == 0) {
                    $scope.errors.push("There must be a report selected.");
                } else {
                    $location.path("/scheduling/" + ($scope.schedule.type == 'general' ? 'multi_delivery' : 'delivery') + "/" + $routeParams.id + "/schedule")
                }
            };
            $scope.to_email = function() {
                $scope.errors = [];
                if($scope.schedule.emails.length == 0  && $scope.schedule.users.length == 0 && $scope.schedule.groups.length == 0 ) {
                    $scope.errors.push("There must be at least one recipient to the delivery.");
                } else {
                    $location.path("/scheduling/" + ($scope.schedule.type == 'general' ? 'multi_delivery' : 'delivery') + "/" + $routeParams.id + "/email");
                }
            };

            $scope.save_schedule = function() {
                $scope.errors = [];
                var change = true;
                var endLocation = null;
                if($scope.schedule.type == "report" && $scope.schedule.report_id == 0) {
                    $scope.errors.push("There must be a report selected.");
                    if($location.path().match(/\/report$/i))
                        change = false;
                    if(endLocation != null)
                        endLocation = "/scheduling/" + ($scope.schedule.type == 'general' ? 'multi_delivery' : 'delivery') + "/" + $routeParams.id + "/report";
                }
                if($scope.schedule.type == "general" && $scope.schedule.delivery_info.length == 0) {
                    $scope.errors.push("There must be a report selected.");
                    if($location.path().match(/\/report$/i))
                        change = false;
                    if(endLocation != null)
                        endLocation = "/scheduling/delivery/" + $routeParams.id + "/report";
                }
                if($scope.schedule.emails.length == 0  && $scope.schedule.users.length == 0 && $scope.schedule.groups.length == 0 ) {
                    $scope.errors.push("There must be at least one recipient to the delivery.");
                    if($location.path().match(/\/schedule$/i))
                        change = false;
                    endLocation = "/scheduling/" + ($scope.schedule.type == 'general' ? 'multi_delivery' : 'delivery') + "/" + $routeParams.id + "/schedule";
                }
                if($scope.schedule.subject.trim().length == 0) {
                    $scope.errors.push("You must add a subject to the email.");
                    if($location.path().match(/\/email$/i))
                        change = false;
                    if(endLocation != null)
                        endLocation = "/scheduling/" + ($scope.schedule.type == 'general' ? 'multi_delivery' : 'delivery') + "/" + $routeParams.id + "/email";
                }
                if($scope.errors.length == 0) {
                    var v = $http.post("/app/scheduled_tasks/" + $scope.schedule.id + ".json?offset=" + $scope.getOffset(), JSON.stringify($scope.schedule));
                    v.then(function (a) {
                        $scope.reload();
                        $location.path("/scheduling/reports");
                    })
                } else if(change) {
                    $location.path(endLocation);
                }
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
            var l;
            l = $http.get("/app/deliveries/" + $scope.schedule.id + "/history.json");

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
            $scope.checkPDF = function() {
                if($scope.schedule.report_format == "pdf") {
                    if(typeof($scope.schedule.delivery_info) == "undefined") {
                        $scope.schedule.delivery_info = {
                            show_header: false,
                            orientation: "landscape"
                        }
                    }
                } else {
                    if(typeof($scope.schedule.delivery_info) != "undefined") {
                        delete $scope.schedule.delivery_info;
                    }
                }
            }
        });
        $scope.isSelected = function(val) {
            return val && typeof(val) === "object"
        };
        $scope.select_data_source = function(item) {
            $scope.report_loading = $http.get("/app/dataSources/" + item.url_key + "/reports.json").then(function(d) {
                $scope.reports = d.data.reports.filter(function(e, i, l) { return e.type == "report" });
            });
        };
        $scope.select_report = function(item) {
            $scope.schedule.report_id = item.id;
            $http.get("/app/html/" + item.type + "/" + item.url_key + "/data.json").then(function(d) {
                $scope.configurations = d.data.configurations;
            });
        };
        $scope.default_added = function(a) {
            return [{"name": "Default Configuration", "url_key": "", "id": 0}].concat(a);
        };
    }]);

    eiScheduling.controller("multiReportDeliverySelectionController", ["$scope", "$http", function($scope, $http) {
        $scope.ds_load = $http.get("/app/dataSources.json");
        $scope.ds_load.then(function (d) {
            $scope.data_sources = d.data.data_sources;
        });
        $scope.select_data_source = function(item) {
            $scope.report_loading = $http.get("/app/dataSources/" + item.url_key + "/reports.json").then(function(d) {
                $scope.reports = d.data.reports;
            });
        };
        $scope.add_report = function() {
            if($scope.isSelected($scope.selected_report)) {
                var v = {
                    format: $scope.selected_report == 'report' ? "excel2007" : "pdf",
                    id: $scope.selected_report.id,
                    type: $scope.selected_report.type,
                    send_if_no_data: true,
                    name: $scope.selected_report.name
                }
                if(v.format == "pdf") {
                    v.delivery_info = {
                        show_header: false,
                        orientation: "Landscape"
                    }
                }
                $scope.schedule.delivery_info.push(v);
            }
        }
        $scope.isSelected = function(val) {
            return val && typeof(val) === "object"
        };

        $scope.delete_selected = function() {
            $scope.schedule.delivery_info = $scope.schedule.delivery_info.filter(function(e, i, l) { return !e.selected; })
        }


        $scope.select_delivery = function(item) {
            $scope.selected_delivery = item;
            $http.get("/app/html/" + item.type + "/" + item.url_key + "/data.json").then(function(d) {
                $scope.schedule_configurations = d.data.configurations;
            });
        }

        $scope.default_added = function(a) {
            return [{"name": "Default Configuration", "url_key": "", "id": 0}].concat(a);
        };

        $scope.checkPDF = function() {
            if($scope.selected_delivery.format == "pdf") {
                if(typeof($scope.selected_delivery.delivery_info) == "undefined") {
                    $scope.selected_delivery.delivery_info = {
                        show_header: false,
                        orientation: "Landscape"
                    }
                }
            } else {
                if(typeof($scope.selected_delivery.delivery_info) != "undefined") {
                    delete $scope.selected_delivery.delivery_info;
                }
            }
        }

    }])

    eiScheduling.config(["$routeSegmentProvider", function($routeSegmentProvider) {
        $routeSegmentProvider.when("/scheduling", "scheduling.reports").
            when("/scheduling/reports", "scheduling.reports").
            when("/scheduling/data_sources", "scheduling.data_sources").
            when("/scheduling/data_sources/:id/:data_source_id", "scheduling.view_data_source").
            when("/scheduling/data_sources/:id", "scheduling.view_data_source").
            when("/scheduling/delivery/:id", "scheduling.view_delivery.reports").
            when("/scheduling/delivery/:id/schedule", "scheduling.view_delivery.times").
            when("/scheduling/delivery/:id/report", "scheduling.view_delivery.reports").
            when("/scheduling/delivery/:id/email", "scheduling.view_delivery.email").
            when("/scheduling/delivery/:id/history", "scheduling.view_delivery.history").
            when("/scheduling/multi_delivery/:id", "scheduling.view_multi_delivery.reports").
            when("/scheduling/multi_delivery/:id/schedule", "scheduling.view_multi_delivery.times").
            when("/scheduling/multi_delivery/:id/email", "scheduling.view_multi_delivery.email").
            when("/scheduling/multi_delivery/:id/history", "scheduling.view_multi_delivery.history").
            when("/scheduling/multi_delivery/:id/report", "scheduling.view_multi_delivery.reports").
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
            segment("times", {
                templateUrl: "/angular_templates/scheduling/view_delivery_schedule.template.html",
                controller: "viewDeliverySchedulingRecipientsController"
            }).
            segment("reports", {
                templateUrl: "/angular_templates/scheduling/view_delivery_report.template.html",
                controller: "viewDeliverySchedulingReportController"
            }).
            segment("email", {
                templateUrl: "/angular_templates/scheduling/view_delivery_email.template.html",
                controller: "viewDeliverySchedulingEmailController"
            }).segment("history", {
                templateUrl: "/angular_templates/scheduling/report_delivery_history.template.html",
                controller: "viewDeliverySchedulingHistoryController"
            }).up().
            segment("view_multi_delivery", {
                templateUrl: "/angular_templates/scheduling/report_multi_delivery.template.html",
                controller: "viewDeliverySchedulingController"
            }).within().
            segment("times", {
                templateUrl: "/angular_templates/scheduling/view_delivery_schedule.template.html",
                controller: "viewDeliverySchedulingRecipientsController"
            }).segment("email",  {
                templateUrl: "/angular_templates/scheduling/view_delivery_email.template.html",
                controller: "viewDeliverySchedulingEmailController"
            }).segment("history", {
                templateUrl: "/angular_templates/scheduling/report_delivery_history.template.html",
                controller: "viewDeliverySchedulingHistoryController"
            }).segment("reports", {
                templateUrl: "/angular_templates/scheduling/multi_report_delivery_selection.template.html",
                controller: "multiReportDeliverySelectionController"
            });

    }])
})()