$.holdReady(true);
Modernizr.load({ complete: function () {
    $.holdReady(false);
}});

var seleniumID;

$(function () {
    seleniumID = seleniumJSON.seleniumID;
});

var afterRefresh = function (selector, id) {
    return function () {
        selector.hide();
        if (typeof(id) != "undefined") {
            setTimeout(function () {
                console.log("before timeout = d3Div" + id);
                var svg = $("#d3Div" + id);
                var h = svg.height();
                var w = svg.width();


                canvg("d3Canvas" + id, svg.html());
                var canvas = document.getElementById("d3Canvas" + id);
                var img = canvas.toDataURL("image/png");
                $.ajax({
                    url: '/app/uploadExportImage?id=' + seleniumJSON["seleniumID"] + "&height=" + h + "&width=" + w,
                    data: {imgBase64: img},
                    type: "POST"
                });
            }, 5000);
        }
    }
};
