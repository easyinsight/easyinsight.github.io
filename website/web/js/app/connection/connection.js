(function() {
    var eiConnections = angular.module('eiConnections', ['ui.bootstrap', 'ui.keypress', 'ngRoute', 'route-segment', 'view-segment', 'cgBusy', 'angularFileUpload']);


    eiConnections.controller('ConnectionsController', ["$scope", function ($scope) {


    }]);

    eiConnections.controller('UploadBaseController', ["$scope", function ($scope) {
        $scope.uploadData = { "uploadKey": null }

    }]);

    eiConnections.controller('UploadRefreshBaseController', ["$scope", "$routeParams", function ($scope, $routeParams) {
        if (typeof($routeParams.id) != "undefined") {
            $scope.uploadData.data_source_id = $routeParams.id;
        }
    }]);

    eiConnections.controller("FileUploadController", ["$scope", "$http", "$upload", "$location", function($scope, $http, $upload, $location) {
        $scope.onFileSelect = function(files) {
            if(files.length > 0) {
                var file = files[0];
                if(file.size < 1024*1024*10) {
                    $scope.upload = $upload.upload({
                        url: '/app/connection/csvupload',
                        file: file,
                        method: "POST"
                    });

                    $scope.upload.then(function(c) {
                        if(c.data.upload_key) {
                            $scope.uploadData.uploadKey = c.data.upload_key;
                            $location.path("/file/define");
                        }
                    });
                }
            }
        }
    }]);

    eiConnections.controller("UploadRefreshController", ["$scope", "$http", "$upload", "$location", function($scope, $http, $upload, $location) {

        $scope.onFileSelect = function(files) {
            if(files.length > 0) {
                var file = files[0];
                if(file.size < 1024*1024*10) {
                    $scope.upload = $upload.upload({
                        url: '/app/connection/csvupload',
                        file: file,
                        method: "POST"
                    });

                    $scope.upload.then(function(c) {
                        if(c.data.upload_key) {
                            $scope.uploadData.uploadKey = c.data.upload_key;
                            $location.path("/file/update");
                        }
                    });
                }
            }
        }
    }]);

    eiConnections.controller("DefineDataSourceController", ["$scope", "$http", "$upload", "$location", function($scope, $http, $upload, $location) {

        $scope.datasource = {
            "datasourcename": null
        };

        $scope.submit = function () {
            $scope.error = "";
            $scope.success = false;
            var result = { upload_key: $scope.uploadData.uploadKey, data_source_name: $scope.datasource.datasourcename };

            $scope.save = $http.post("/app/html/connection/createFlatFileSource", JSON.stringify(result));
            $scope.save.then(function (c) {
                $scope.success = c.data.success;
                if (c.data.success) {
                    window.location = "/a/home";
                } else {
                    $scope.error = c.data.error;
                }
            });
        }
    }]);

    eiConnections.controller("RefreshDataSourceController", ["$scope", "$routeParams", "$http", "$upload", "$location", function($scope, $routeParams, $http, $upload, $location) {

        $scope.datasource = {
            "operation": 2
        };

        $scope.submit = function () {
            $scope.error = "";
            $scope.success = false;
            var result = { upload_key: $scope.uploadData.uploadKey, data_source_id: $scope.uploadData.data_source_id, operation: $scope.datasource.operation };
            $scope.save = $http.post("/app/html/connection/refreshFlatFileSource", JSON.stringify(result));
            $scope.save.then(function (c) {
                $scope.success = c.data.success;
                if (c.data.success) {
                    window.location = "/a/home";
                } else {
                    $scope.error = c.data.error;
                }
            });
        }
    }]);

    eiConnections.config(["$locationProvider", "$routeSegmentProvider", function ($locationProvider, $routeSegmentProvider) {
        //https://localhost:4443/a/dataSource/JYlcFsbhHEmo/refresh
        $routeSegmentProvider.when("/connection", "file_upload.upload_base.csv_file_upload").
            when("/file/define", "file_upload.upload_base.file_define_upload").
            when("/dataSource/:id/refresh", "file_upload.upload_base.upload_refresh.refresh_file").
            when("/file/update", "file_upload.upload_base.upload_refresh.file_define_refresh").
            segment("file_upload", {
                templateUrl: '/angular_templates/connections/csv_upload.html',
                controller: 'ConnectionsController'
            }).within().
                segment("upload_base", {
                    templateUrl: '/angular_templates/connections/upload_base_template.html',
                    controller: 'UploadBaseController'
                }).within().
                    segment("upload_refresh", {
                        templateUrl: '/angular_templates/connections/upload_refresh_template.html',
                        controller: 'UploadRefreshBaseController',
                        dependencies: ["id"]
                    }).within().
                        segment("refresh_file", {
                            templateUrl: '/angular_templates/connections/csv_file_refresh.html',
                            controller: 'UploadRefreshController'
                        }).
                        segment("file_define_refresh", {
                            templateUrl: '/angular_templates/connections/file_define_refresh.html',
                            controller: 'RefreshDataSourceController'
                        }).
                        up().
                    segment("csv_file_upload", {
                        templateUrl: '/angular_templates/connections/csv_file_upload.html',
                        controller: 'FileUploadController'
                    }).

                    segment("file_define_upload", {
                        templateUrl: '/angular_templates/connections/file_define_upload.html',
                        controller: 'DefineDataSourceController'
                    });
    }])
})();