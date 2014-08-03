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
            /*var h = svg.height();
            var w = svg.width();*/

            svgAsDataUri(svg, 1, function(uri) {
                var image = new Image();
                image.src = uri;
                image.onload = function () {
                    var canvas = document.createElement('canvas');
                    canvas.width = image.width;
                    canvas.height = image.height;
                    var context = canvas.getContext('2d');
                    context.drawImage(image, 0, 0);
                    var img = canvas.toDataURL("image/png");
                    console.log(canvas);
                    $.ajax({
                        url: '/app/uploadExportImage?id=' + seleniumJSON["seleniumID"] + "&height=" + image.height + "&width=" + image.width,
                        data: {imgBase64: img},
                        type: "POST"
                    });
                }
            });


            /*canvg("d3Canvas" + id, svg.html());
            var canvas = document.getElementById("d3Canvas" + id);
            var img = canvas.toDataURL("image/png");
            $.ajax({
                url: '/app/uploadExportImage?id=' + seleniumJSON["seleniumID"] + "&height=" + h + "&width=" + w,
                data: {imgBase64: img},
                type: "POST"
            });*/
        }
        /*setTimeout(function () {
            console.log("id = " + id);

        }, 15000);*/
    }
};

var handleFullFilters = function (fullFilters) {

    return function () {
        fullFilters["seleniumID"] = seleniumID;
        return fullFilters;
    }
};

var doctype = '<?xml version="1.0" standalone="no"?><!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">';

function inlineImages(callback) {
    var images = document.querySelectorAll('svg image');
    var left = images.length;
    if (left == 0) {
        callback();
    }
    for (var i = 0; i < images.length; i++) {
        (function(image) {
            if (image.getAttribute('xlink:href')) {
                var href = image.getAttribute('xlink:href').value;
                if (/^http/.test(href) && !(new RegExp('^' + window.location.host).test(href))) {
                    throw new Error("Cannot render embedded images linking to external hosts.");
                }
            }
            var canvas = document.createElement('canvas');
            var ctx = canvas.getContext('2d');
            var img = new Image();
            img.src = image.getAttribute('xlink:href');
            img.onload = function() {
                canvas.width = img.width;
                canvas.height = img.height;
                ctx.drawImage(img, 0, 0);
                image.setAttribute('xlink:href', canvas.toDataURL('image/png'));
                left--;
                if (left == 0) {
                    callback();
                }
            }
        })(images[i]);
    }
}

function styles(dom) {
    var used = "";
    var sheets = document.styleSheets;
    for (var i = 0; i < sheets.length; i++) {
        var rules = sheets[i].cssRules;
        for (var j = 0; j < rules.length; j++) {
            var rule = rules[j];
            if (typeof(rule.style) != "undefined") {
                var elems = dom.querySelectorAll(rule.selectorText);
                if (elems.length > 0) {
                    used += rule.selectorText + " { " + rule.style.cssText + " }\n";
                }
            }
        }
    }

    var s = document.createElement('style');
    s.setAttribute('type', 'text/css');
    s.innerHTML = "<![CDATA[\n" + used + "\n]]>";

    var defs = document.createElement('defs');
    defs.appendChild(s);
    return defs;
}

svgAsDataUri = function(el, scaleFactor, cb) {
    scaleFactor = scaleFactor || 1;

    inlineImages(function() {
        var outer = document.createElement("div");
        var clone = el.cloneNode(true);
        var width = parseInt(clone.getAttribute("width"));
        var height = parseInt(clone.getAttribute("height"));

        var xmlns = "http://www.w3.org/2000/xmlns/";

        clone.setAttribute("version", "1.1");
        clone.setAttributeNS(xmlns, "xmlns", "http://www.w3.org/2000/svg");
        clone.setAttributeNS(xmlns, "xmlns:xlink", "http://www.w3.org/1999/xlink");
        clone.setAttribute("width", width * scaleFactor);
        clone.setAttribute("height", height * scaleFactor);
        clone.setAttribute("viewBox", "0 0 " + width + " " + height);
        outer.appendChild(clone);

        clone.insertBefore(styles(clone), clone.firstChild);

        var svg = doctype + outer.innerHTML;
        var uri = 'data:image/svg+xml;base64,' + window.btoa(unescape(encodeURIComponent(svg)));
        if (cb) {
            cb(uri);
        }
    });
};

/*
(function() {
    var out$ = typeof exports != 'undefined' && exports || this;



    out$.saveSvgAsPng = function(el, name, scaleFactor) {
        out$.svgAsDataUri(el, scaleFactor, function(uri) {
            var image = new Image();
            image.src = uri;
            image.onload = function() {
                var canvas = document.createElement('canvas');
                canvas.width = image.width;
                canvas.height = image.height;
                var context = canvas.getContext('2d');
                context.drawImage(image, 0, 0);

                var a = document.createElement('a');
                a.download = name;
                a.href = canvas.toDataURL('image/png');
                document.body.appendChild(a);
                a.click();
            }
        });
    }
})();*/
