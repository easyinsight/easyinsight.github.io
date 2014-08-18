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
            setTimeout(function () {

                var img;
                var h;
                var w;
                if (reportType == "svg") {
                    var svg = $("#d3Div" + id);
                    h = svg.height();
                    w = svg.width();


                    canvg("d3Canvas" + id, svg.html());
                    var canvas = document.getElementById("d3Canvas" + id);
                    img = canvas.toDataURL("image/png");
                } else if (reportType == "gauge") {
                    var c = $("#gauge"+id);
                    var gaugeCanvas = document.getElementById("gauge" + id);
                    h = c.height();
                    w = c.height();
                    img = gaugeCanvas.toDataURL("image/png");
                } else if (reportType == "diagram") {
                    var dCanvas = $("#" + id + " canvas");
                    //var diagramCanvas = document.getElementsByClassName("diagramCanvas").item(0);
                    h = dCanvas.height();
                    w = dCanvas.height();
                    img = dCanvas.toDataURL("image/png");
                }
                $.ajax({
                    url: '/app/uploadExportImage?id=' + seleniumJSON["seleniumID"] + "&height=" + h + "&width=" + w,
                    data: {imgBase64: img},
                    type: "POST"
                });
            }, 1000);
        }
    }
};
