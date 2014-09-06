(function() {
    var eiAccounts = angular.module('eiAccounts', ['ui.bootstrap', 'ui.keypress', 'ngRoute', 'route-segment', 'view-segment', 'cgBusy', 'colorpicker.module', 'angularFileUpload']);


    eiAccounts.controller('AccountController', ["$scope", function ($scope) {


    }]);

eiAccounts.controller("AccountInfoController", ["$scope", "$http", "PageInfo", "$location",
    "$rootScope",
    function($scope, $http, PageInfo, $location, $rootScope) {
    $rootScope.user_promise.then(function() {
        if(!$rootScope.user.admin)
            $location.path("/account/profile");
    });
    PageInfo.setTitle("Account Info");
    $http.get("/app/account.json").success(function (d, r) {
        $scope.account = d.account;
        $scope.stats = d.stats;
    });
    $scope.planName = function () {
        switch ($scope.account.account_type) {
            case "basic":
                return "Basic Plan";
            case "plus":
                return "Plus Plan";
            case "professional":
                return "Professional Plan";
            case "enterprise":
                return "Enterprise";
            case "administrator":
                return "Administrator"
            default:
                return "Unknown";
        }
    }
    $scope.isEnterprise = function () {
        if (typeof($scope.account) == "undefined")
            return false;

        return $scope.account.account_type == "enterprise" || $scope.account.account_type == "premium";
    }
}])

eiAccounts.controller('UserBaseController', ["$scope", "$http", function ($scope, $http) {
    $scope.load = $http.get("/app/json/getUsers").success(function (d, r) {
        $scope.users = d.users;
    })
}])

eiAccounts.controller('UsersController', ["$scope", "$filter", "$http", "PageInfo", "$location", "$rootScope", "$modal",
    function ($scope, $filter, $http, PageInfo, $location, $rootScope, $modal) {
        $rootScope.user_promise.then(function() {
            if(!$rootScope.user.admin)
                $location.path("/account/profile");
        });
        PageInfo.setTitle("Users")
        $scope.deleteSelected = function () {
            var usersToDelete = $filter('filter')($scope.users, {"delete": true}, true);

            $scope.to_delete = usersToDelete;
            var m = $modal.open({
                templateUrl: "/angular_templates/account/delete_users_dialog.template.html",
                scope: $scope,
                controller: "deleteUsersController"
            });
            m.result.then(function (r) {
                for (var ctr = 0; ctr < usersToDelete.length; ctr++) {
                    var user = usersToDelete[ctr];
                    var index = $scope.users.indexOf(user);
                    $scope.users.splice(index, 1);
                }
            });
            m.result.finally(function(r) {
                delete $scope.to_delete;
            })


        }
}])

    eiAccounts.controller("deleteUsersController", ["$scope", "$http", function($scope, $http) {
        $scope.confirmDelete = function() {
            var usersToDeleteIDs = $scope.to_delete.map(function (e, i, l) {
                return e.id;
            });

            $scope.delete_promise = $http.post("/app/html/account/deleteUsers", JSON.stringify({user_ids: usersToDeleteIDs}));
            $scope.delete_promise.then(function (c) {

                $scope.$close();
            });
        }
    }])

eiAccounts.controller('UserController', ["$scope", "$routeParams", "$http", "$location", "PageInfo", "$rootScope",
    function ($scope, $routeParams, $http, $location, PageInfo, $rootScope) {
    $rootScope.user_promise.then(function() {
        if(!$rootScope.user.admin)
            $location.path("/account/profile");
    });
    $scope.load.then(function () {
        var i;
        for (i in $scope.users) {
            var e = $scope.users[i];
            if (typeof(e) != "undefined" && e.id == $routeParams.id) {
                $scope.user = e;
            }
        }
    })
    PageInfo.setTitle($scope.user.first_name + " " + $scope.user.last_name);
    $scope['new'] = false;

    $scope['submit'] = function () {
        $scope.error = "";
        $scope.success = false;
        $scope.save = $http.post("/app/html/account/createUser", JSON.stringify($scope.user));

        $scope.save.then(function (c) {
            $scope.success = c.data.success;
            if (c.data.success) {
                var i = $scope.users.indexOf($scope.user);
                if (i != -1) {
                    $scope.users[i] = c.data.user;
                }
                $location.path("/account/users");
            } else {
                $scope.error = c.data.error;
            }
        });
    }

}]);

eiAccounts.controller('NewDesignerController', ["$scope", "$http", "$location", "PageInfo", "$rootScope",
    function ($scope, $http, $location, PageInfo, $rootScope) {
        $rootScope.user_promise.then(function() {
            if(!$rootScope.user.admin)
                $location.path("/account/profile");
        });
    PageInfo.setTitle("New Designer");
    $scope.user = {
        "email": null,
        "first_name": null,
        "title": null,
        "last_name": null,
        "username": null,
        "all_reports": false,
        "newsletter": false,
        "invoice_recipient": false,
        "designer": true,
        "admin": false,
        "change_password": false
    };
    $scope['new'] = true;
    $scope.submit = function () {
        $scope.error = "";
        $scope.success = false;
        $scope.save = $http.post("/app/html/account/createUser", JSON.stringify($scope.user));
        $scope.save.then(function (c) {
            $scope.success = c.data.success;
            if (c.data.success) {
                $scope.users.push(c.data.user);
                $location.path("/account/users");
            } else {
                $scope.error = c.data.error;
            }
        });
    }
}]);

eiAccounts.controller('NewViewerController', ["$scope", "$http", "$location", "PageInfo", "$rootScope",
    function ($scope, $http, $location, PageInfo, $rootScope) {
    $rootScope.user_promise.then(function() {
        if(!$rootScope.user.admin)
            $location.path("/account/profile");
    });
    PageInfo.setTitle("New Viewer");
    $scope.user = {
        "email": null,
        "first_name": null,
        "title": null,
        "last_name": null,
        "username": null,
        "all_reports": false,
        "newsletter": false,
        "invoice_recipient": false,
        "designer": false,
        "admin": false,
        "change_password": false
    };
    $scope['new'] = true;
    $scope.submit = function () {
        $scope.error = "";
        $scope.success = false;
        $scope.save = $http.post("/app/html/account/createUser", JSON.stringify($scope.user))
        $scope.save.then(function (c) {
            $scope.success = c.data.success;
            if (c.data.success) {
                $scope.users.push(c.data.user);
                $location.path("/account/users");
            } else {
                $scope.error = c.data.error;
            }
        })
    }
}]);

eiAccounts.controller("OverviewController", [function() {

}]);

eiAccounts.controller("AccountSettingsController", ["$scope", "$http", function($scope, $http) {
    $scope.loading = $http.get("/app/account_settings.json");
    $scope.loading.then(function(c) {
        $scope.settings = c.data.settings;
    })

    $scope.submit = function() {
        $scope.saved = false;
        $scope.loading = $http.post("/app/account_settings.json", JSON.stringify($scope.settings));
        $scope.loading.then(function(c) {
            if(c.data.success) {
                $scope.settings = c.data.settings;
                $scope.saved = true;
            }
        })
    }
}]);

eiAccounts.controller("AccountSkinController", ["$scope", "$http", "$upload", function($scope, $http, $upload) {
    $scope.loading = $http.get("/app/account_skin.json");
    $scope.loading.then(function(c) {
        $scope.skin = c.data.skin;
    })

    $http.get("/app/images").then(function(c) {
        $scope.images = c.data.images
    })
    $scope.submit= function() {

        $scope.saving = $http.post("/app/account_skin.json", JSON.stringify($scope.skin));
        $scope.saving.then(function(c) {

            $scope.skinForm.$setPristine();
        })
    }
    $scope.onFileSelect = function(files) {
        if(!$scope.skin.report_header)
            return;
        if(files.length > 0) {
            var file = files[0];
            if(file.size < 1024*1024*10 && file.type.match(/^image\//)) {
                $scope.upload = $upload.upload({
                    url: '/app/images',
                    file: file,
                    method: "POST"
                });

                $scope.upload.then(function(c) {
                    if(c.data.image) {
                        $scope.images.push(c.data.image);
                    }
                });
            }
        }
    }
}]);

eiAccounts.controller("UserProfileController", ["$scope", "$rootScope", "$http", function($scope, $rootScope, $http) {
    $scope.user = angular.copy($rootScope.user);
    $scope.submit = function () {
        $scope.error = "";
        $scope.success = false;
        $scope.save = $http.post("/app/html/account/createUser", JSON.stringify($scope.user));
        $scope.save.then(function (c) {
            $scope.success = c.data.success;
            if (c.data.success) {
                $rootScope.user = c.data.user;
                $scope.user.old_password = "";
                $scope.user.new_password = "";
            } else {
                $scope.error = c.data.error;
                $scope.user.old_password = "";
                $scope.user.new_password = "";
            }
        });
    }
}]);

eiAccounts.controller("GroupBaseController", ["$scope", "$http", function($scope, $http) {
    $scope.loading = $http.get("/app/json/groups");
    $scope.loading.then(function(c) {
        $scope.groups = c.data.groups;
    })
}]);

eiAccounts.controller("GroupIndexController", ["$scope", function($scope) {

}]);

    eiAccounts.filter("not_in_group", function() {
        return function(input, group) {
            if(!input) return input;
            return input.filter(function(e, i, l) {
                if(group == null)
                    return true;
                else {
                    return !group.some(function(ee, ii, ll) {
                        return e.id == ee.id;
                    });

                }
            })
        }
    })

eiAccounts.controller("GroupInfoController", ["$scope", "$http", "$routeParams", "$filter", "$rootScope", function($scope, $http, $routeParams, $filter) {
    $scope.group_loading = $http.get("/app/groups/" + $routeParams.id + ".json");
    $scope.group_loading.then(function(c) {
        $scope.group = c.data.group;
    })
    $scope.user_load = $http.get("/app/json/getUsers").success(function (d, r) {
        $scope.users = d.users;
    })

    $scope.removeSelectedUsers = function() {
        $scope.group.users = $scope.group.users.filter(function(e, i, l) {
            return !e.selected;
        })
    }

    $scope.submit = function() {
        $scope.submit_promise = $http.post("/app/groups/" + $routeParams.id + ".json", JSON.stringify($scope.group));
        $scope.submit_promise.success(function(d) {})

    }

    $scope.addSelectedUsers = function() {

        var v = $filter("not_in_group")($scope.users, $scope.group.users).filter(function(e, i, l) {
            return e.selected;
        }).map(function(e, i, l) {
                e.selected = false;
                return {id: e.id,
                    name: e.name,
                    role: "Viewer",
                    user_name: e.username
                }
            });
        $scope.group.users = $scope.group.users.concat(v);
    }

}])

eiAccounts.directive("eimulticolorform", function() {
    return {
        templateUrl: "/angular_templates/account/directives/multi_color.template.html",
        scope: {
            text: "=text",
            field: "=field"
        }
    };
});



eiAccounts.directive('eicolorform', function() {
    return {
        templateUrl: '/angular_templates/account/directives/color_form.template.html',
        scope: {
            field: "=field",
            text: "=text",
            enabled_field_disabled: "=enabledfielddisabled",
            disabled_field: "=disabledfield",
            enabled_field: "=enabledfield",
            skin: "=skin"
        }
    }
});

eiAccounts.controller("quickLinksController", ["$scope", "PageInfo", "$rootScope", "$http", "$modal", function($scope, PageInfo, $rootScope, $http, $modal) {
    $rootScope.user_promise.then(function() {
        if(!$rootScope.user.admin)
            $location.path("/account/profile");
    });
    PageInfo.setTitle("Top Reports and Dashboards");
    $http.get("/app/html/dataSourceTags.json").then(function (d) {
        $scope.ds_tags = d.data.tags;
    });

    $http.get("/app/html/reportTags.json").then(function (d) {
        $scope.report_tags = d.data.tags;
    });

    $scope.ds_load = $http.get("/app/dataSources.json");
    $scope.ds_load.then(function (d) {
        $scope.data_sources = d.data.data_sources;
        if($scope.data_sources.length == 1) {
            $scope.ds_name = $scope.data_sources[0];
            $scope.select_data_source($scope.ds_name);
        }
    });

    $scope.select_data_source = function(item) {
        $http.get("/app/dataSources/" + item.url_key + "/reports.json").then(function(d) {
            $scope.reports = d.data.reports;
        });
    }

    $scope.isSelected = function(val) {
        return val && typeof(val) === "object"
    }

    $scope.addReport = function() {
        var out = {"bookmarks": [{"url_key": $scope.selected_report.url_key, "type": $scope.selected_report.type }]}
        $scope.saving = $http.post("/app/userInfo.json", JSON.stringify(out))
        $scope.saving.then(function(d) {
            $rootScope.bookmarks.push(d.data);
            $scope.selected_report = null;
            $scope.ds_name = null;
        })
    }

    $scope.deleteSelected = function() {

        var toDelete = $rootScope.bookmarks.filter(function(e, i, l) {
            return e.selected;
        })
        if (toDelete.length > 0) {
            $scope.to_delete = toDelete;
            var m = $modal.open({
                templateUrl: "/angular_templates/account/delete_quick_links_dialog.template.html",
                scope: $scope,
                controller: "deleteSelectedBookmarksController"
            });
            m.result.then(function (r) {
                $rootScope.bookmarks = $rootScope.bookmarks.filter(function (e, i, l) {
                    return !e.selected;
                });
            });
            m.result.finally(function(r) {
                delete $scope.to_delete;
            })
        }

    }

}]);

    eiAccounts.filter("not_bookmarked", function() {
        return function (input, bookmarks) {
            return input.filter(function(e, i, l) {
                return !bookmarks.some(function(ee, ii, ll) {
                    return ee.url_key == e.url_key && ee.type == e.type;
                })
            })
        }
    })

    eiAccounts.controller("deleteSelectedBookmarksController", ["$http", "$scope", function($http, $scope) {
        $scope.confirmDelete = function() {
            var output = {"bookmarks": $scope.to_delete.map(function(e, i, l) {
                return {"url_key": e.url_key, "type": e.type };
            }) };
            $scope.saving = $http.delete("/app/userInfo.json", {data: JSON.stringify(output)});
            $scope.saving.then(function (c) {
                $scope.$close();
            })
        }
    }])

eiAccounts.config(["$locationProvider", "$routeSegmentProvider", function ($locationProvider, $routeSegmentProvider) {
    $routeSegmentProvider.when("/account", "account.index.overview").
        when("/account/settings", "account.index.settings").
        when("/account/users", "account.user_base.index").
        when("/account/users/:id", "account.user_base.user").
        when("/account/users/designer/new", "account.user_base.new_designer").
        when("/account/users/viewer/new", "account.user_base.new_viewer").
        when("/account/profile", "account.profile").
        when("/account/skin", "account.index.skin").
        when("/account/report_header", "account.index.report_header").
        when("/account/groups", "account.group.index").
        when("/account/groups/:id", "account.group.group_info").
        when("/account/quick_links", "account.index.quick_links").
        segment("account", {
            templateUrl: '/angular_templates/account/base.template.html',
            controller: 'AccountController'
        }).within().
        segment("index", {
            templateUrl: "/angular_templates/account/index.template.html",
            controller: 'AccountInfoController'
        }).within().
        segment("overview", {
            templateUrl: "/angular_templates/account/overview.template.html",
            controller: "OverviewController"
        }).
        segment("settings", {
            templateUrl: "/angular_templates/account/settings.template.html",
            controller: "AccountSettingsController"
        }).
        segment("skin", {
            templateUrl: "/angular_templates/account/skin.template.html",
            controller: "AccountSkinController"
        }).
        segment("report_header", {
            templateUrl: "/angular_templates/account/report_header.template.html",
            controller: "AccountSkinController"
        }).
        segment("quick_links", {
            templateUrl: "/angular_templates/account/quick_links.template.html",
            controller: "quickLinksController"
        }).
        up().
        segment("profile", {
            templateUrl: "/angular_templates/account/profile.template.html",
            controller: "UserProfileController"
        }).
        segment("user_base", {
            templateUrl: '/angular_templates/account/user_base.template.html',
            controller: 'UserBaseController'
        }).within().
            segment("user", {
                templateUrl: '/angular_templates/account/user.template.html',
                controller: 'UserController',
                dependencies: ["id"]
            }).
            segment("index", {
                templateUrl: '/angular_templates/account/users.template.html',
                controller: "UsersController"
            }).segment("new_designer", {
                templateUrl: '/angular_templates/account/user.template.html',
                controller: 'NewDesignerController'
            }).segment("new_viewer", {
                templateUrl: '/angular_templates/account/user.template.html',
                controller: 'NewViewerController'
            }).up().
        segment("group", {
            templateUrl: '/angular_templates/account/group_base.template.html',
            controller: 'GroupBaseController'
        }).within().
        segment("index", {
            templateUrl: '/angular_templates/account/groups.template.html',
            controller: 'GroupIndexController'
        }).
        segment("group_info", {
            templateUrl: '/angular_templates/account/group.template.html',
            controller: 'GroupInfoController',
            dependencies: ["id"]
        });
    ;
}])

})();