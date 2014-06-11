var eiAccounts = angular.module('eiAccounts', ['ui.bootstrap', 'ngRoute', 'route-segment', 'view-segment', 'cgBusy']);

eiAccounts.controller('AccountController', function ($scope, $http) {
    $http.get("/app/account.json").success(function (d, r) {
        $scope.account = d.account;
        $scope.stats = d.stats;
    })
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
    $scope.delete = function (user) {
        if (confirm("Are you sure you want to delete this user?"))
            $scope.delete_promise = $http.get("/app/html/account/delete?userID=" + user.id).then(function (c) {
                if (c.data.success) {
                    var i = $scope.users.indexOf(user);
                    $scope.users.splice(i, 1);
                }
            })
    }
})

eiAccounts.controller('UsersController', function () {
})

eiAccounts.controller('UserController', function ($scope, $routeParams, $http, $location) {
    $scope.load.then(function () {
        var i;
        for (i in $scope.users) {
            var e = $scope.users[i];
            if (typeof(e) != "undefined" && e.id == $routeParams.id) {
                $scope.user = e;
            }
        }
    })

    $scope.new = false;

    $scope.submit = function () {
        $scope.save = $http.post("/app/html/account/createUser", JSON.stringify($scope.user));
        $scope.save.then(function (c) {
            if (c.data.success) {
                var i = $scope.users.indexOf($scope.user);
                if(i != -1) {
                    $scope.users[i] = c.data.user;
                }
                $location.path("/account/users");
            }
        });
    }

});

eiAccounts.controller('NewDesignerController', function ($scope, $http, $location) {
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
        $scope.save = $http.post("/app/html/account/createUser", JSON.stringify($scope.user));
        $scope.save.then(function (c) {
            if (c.data.success) {
                $scope.users.push(c.data.user);
                $location.path("/account/users");
            }
        });
    }
})

eiAccounts.controller('NewViewerController', function ($scope, $http, $location) {
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
        $scope.save = $http.post("/app/html/account/createUser", JSON.stringify($scope.user))
        $scope.save.then(function (c) {
            if (c.data.success) {
                $scope.users.push(c.data.user);
                $location.path("/account/users");
            }
        })
    }
})

eiAccounts.config(function ($routeProvider, $locationProvider, $routeSegmentProvider) {
    $locationProvider.html5Mode(true);
    $routeSegmentProvider.when("/account", "account.index").
        when("/account/users", "account.user_base.index").
        when("/account/users/:id", "account.user_base.user").
        when("/account/users/designer/new", "account.user_base.new_designer").
        when("/account/users/viewer/new", "account.user_base.new_viewer").
        segment("account", {
            templateUrl: '/account/base.template.html',
            controller: 'AccountController'
        }).within().
        segment("index", {
            templateUrl: "/account/index.template.html"
        }).

        segment("user_base", {
            templateUrl: '/account/user_base.template.html',
            controller: 'UserBaseController'
        }).within().
        segment("user", {
            templateUrl: '/account/user.template.html',
            controller: 'UserController',
            dependencies: ["id"]
        }).
        segment("index", {
            templateUrl: '/account/users.template.html',
            controller: "UsersController"
        }).segment("new_designer", {
            templateUrl: '/account/user.template.html',
            controller: 'NewDesignerController'
        }).segment("new_viewer", {
            templateUrl: '/account/user.template.html',
            controller: 'NewViewerController'
        });
})

eiAccounts.run(function ($rootScope, $http) {
    $rootScope.user = {
        "username": "..."
    };
    $http.get("/app/userInfo.json").success(function (d, r) {
        if (r == 401)
            window.location = "/app/login.jsp";
        else {
            $rootScope.user = d.user;
        }
    }).error(function () {
            window.location = "/app/login.jsp";
        });
});