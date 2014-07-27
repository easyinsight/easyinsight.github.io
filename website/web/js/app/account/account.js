var eiAccounts = angular.module('eiAccounts', ['ui.bootstrap', 'ngRoute', 'route-segment', 'view-segment', 'cgBusy', 'colorpicker.module', 'angularFileUpload']);

eiAccounts.controller('AccountController', function ($scope) {


});

eiAccounts.controller("AccountInfoController", function($scope, $http, PageInfo, $location, $rootScope) {
    if(!$rootScope.user.admin)
        $location.path("/account/profile");
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
})

eiAccounts.controller('UserBaseController', function ($scope, $http) {
    $scope.load = $http.get("/app/json/getUsers").success(function (d, r) {
        $scope.users = d.users;
    })
})

eiAccounts.controller('UsersController', function ($scope, $filter, $http, PageInfo, $location, $rootScope) {
    if(!$rootScope.user.admin)
        $location.path("/account/profile");
    PageInfo.setTitle("Users")
    $scope.deleteSelected = function () {
        var usersToDelete = $filter('filter')($scope.users, {delete: true}, true);
        var usersToDeleteIDs = usersToDelete.map(function (e, i, l) {
            return e.id;
        });
        if (confirm("Are you sure you want to delete the selected set of users?")) {
            $scope.delete_promise = $http.post("/app/html/account/deleteUsers", JSON.stringify({user_ids: usersToDeleteIDs}));
            $scope.delete_promise.then(function (c) {
                if (c.data.success) {
                    for (var ctr = 0; ctr < usersToDelete.length; ctr++) {
                        var user = usersToDelete[ctr];
                        var index = $scope.users.indexOf(user);
                        $scope.users.splice(index, 1);
                    }
                }
            });
        }
    }
})

eiAccounts.controller('UserController', function ($scope, $routeParams, $http, $location, PageInfo, $rootScope) {
    if(!$rootScope.user.admin)
        $location.path("/account/profile");
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
    $scope.new = false;

    $scope.submit = function () {
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

});

eiAccounts.controller('NewDesignerController', function ($scope, $http, $location, PageInfo, $rootScope) {
    if(!$rootScope.user.admin)
        $location.path("/account/profile");
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
    $scope.new = true;
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
});

eiAccounts.controller('NewViewerController', function ($scope, $http, $location, PageInfo, $rootScope) {
    if(!$rootScope.user.admin)
        $location.path("/account/profile");
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
    $scope.new = true;
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
});

eiAccounts.controller("OverviewController", function() {

});

eiAccounts.controller("AccountSettingsController", function($scope, $http) {
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
});

eiAccounts.controller("AccountSkinController", function($scope, $http, $upload) {
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
})

eiAccounts.controller("UserProfileController", function($scope, $rootScope, $http) {
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
});

eiAccounts.directive("eimulticolorform", function() {
    return {
        templateUrl: "/angular_templates/account/directives/multi_color.template.html",
        scope: {
            text: "=text",
            field: "=field"
        }
    };
})

eiAccounts.directive('eicolorform', function() {
    return {
        templateUrl: '/angular_templates/account/directives/color_form.template.html',
        scope: {
            field: "=field",
            text: "=text",
            enabled_field_disabled: "=enabledfielddisabled",
            disabled_field: "=disabledfield",
            enabled_field: "=enabledfield"
        }
    }
});

eiAccounts.config(function ($locationProvider, $routeSegmentProvider) {
    $routeSegmentProvider.when("/account", "account.index.overview").
        when("/account/settings", "account.index.settings").
        when("/account/users", "account.user_base.index").
        when("/account/users/:id", "account.user_base.user").
        when("/account/users/designer/new", "account.user_base.new_designer").
        when("/account/users/viewer/new", "account.user_base.new_viewer").
        when("/account/profile", "account.profile").
        when("/account/skin", "account.index.skin").
        when("/account/report_header", "account.index.report_header").
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
        });
})