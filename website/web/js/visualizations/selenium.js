$.holdReady(true);
Modernizr.load({ complete: function () {
    $.holdReady(false);
}});

var seleniumID;
var reportType;

$(function () {
    seleniumID = seleniumJSON.seleniumID;
    reportType = seleniumJSON.reportType;
});

var afterRefresh = function (selector, id) {
    return function () {
        selector.hide();
        if (typeof(id) != "undefined") {
            $.ajax({
                url: "/app/done",
                contentType: "application/json; charset=UTF-8",
                type: "GET"
            });
        }
    }
};
