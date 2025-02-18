'use strict';

/* controllers */
angular.module('app.controllers', []).controller('FileUploadController', function ($scope, $http,spinnerService) {

    var spinner = spinnerService.bodySpinner;

    $scope.document = {};
    $scope.currentImageId = -1;
    $scope.getImageUrl = function (path, imageid) {
        return path + imageid + ".png";
    };

    $scope.uploadFile = function () {

        var formData = new FormData();
        formData.append("file", file.files[0]);
        spinnerService.spinOnPage();
        $http({
            method: 'POST',
            url: '/image-search/back-end/image-catalog-operations/addImage',
            headers: { 'Content-Type': undefined},
            data: formData,
            transformRequest: function (data, headersGetterFunction) {
                return data; // do nothing! FormData is very good!
            }
        }).success(function (data, status) {
                $http({
                    method: 'GET',
                    url: data

                }).success(function (data, status) {
                    $scope.currentImageId = data.currentImageId;
                    $scope.similarities = data.similarities;
                    $scope.abosoluteUrlThumbs = data.abosoluteUrlThumbs;
                    $scope.abosoluteUrlOriginals = data.abosoluteUrlOriginals;
                    spinnerService.stopSpinOnPage();

                }).error(function (data, status) {
                    spinnerService.stopSpinOnPage();
                    alert("You better put it in pop-up :) with friendly message... Error ... " + status + "    " + data);
                });


            })
            .error(function (data, status) {
                spinnerService.stopSpinOnPage();
                alert("You better put it in pop-up :) with friendly message... Error ... " + status + "    " + data);
            });
    };
});