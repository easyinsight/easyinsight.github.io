var seleniumID;

$(function () {
    seleniumID = seleniumJSON.seleniumID;
});

var afterRefresh = function (selector, id) {
    return function () {
        selector.hide();
        if (typeof(id) != "undefined") {
            console.log("before timeout = " + id);
            var svg = document.getElementById("d3Div" + id);
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
        }
    }
};
