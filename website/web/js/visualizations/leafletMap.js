LeafletMap = {

    getMap:function (target, params, showLabels, styleProps, filters, drillthroughKey, iframedInUI) {
        return function (data) {
            var sWidth;
            var sHeight;
            var centered;
            $("#" + target + " .reportArea").show();
            $("#" + target + " .noData").hide();
            if (typeof(afterRefresh) != "undefined") {
                if (afterRefresh.length > 0) {
                    afterRefresh($("#" + target + " .loading"))();
                } else {
                    afterRefresh();
                }
            }
            var height = Chart.chartHeightWithIFrame(target, styleProps, iframedInUI);

            $("#d3Div" + target).height(height);

            var map = L.map("d3Div" + target).setView([data["centerLat"], data["centerLong"]], data["defaultZoom"]);

            L.tileLayer(
                'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {

                    maxZoom: data["maxZoom"]
                }).addTo(map);

            var pointDatas = data.pointDatas;

            var addressPoints = [];

            for (var j = 0; j < pointDatas.length; j++) {
                var pointData = pointDatas[j];
                var points = pointData.points;

                for (var k = 0; k < points.length; k++) {
                    var point = points[k];
                    addressPoints.push([point.lat, point.lon, point.pointValue]);
                }
            }

            var options = { radius: data["radius"], blur: data["blur"] };
            L.heatLayer(addressPoints, options).addTo(map);

        }


    },

    getRadius:function(radius) {
        return function(d) {
            return radius(d.pointValue);
        }
    },

    getFillName:function(pointData) {
        return function(d) {
            return pointData.name;
        }
    },

    getFillColor:function(pointData) {
        return function(d) {
            return pointData.color;
        }
    },

    getMove:function(zoom, width, height, g) {
        return function() {
            var t = d3.event.translate;
            var s = d3.event.scale;
            var h = height / 3;

            t[0] = Math.min(width / 2 * (s - 1), Math.max(width / 2 * (1 - s), t[0]));
            t[1] = Math.min(height / 2 * (s - 1) + h * s, Math.max(height / 2 * (1 - s) - h * s, t[1]));

            zoom.translate(t);
            g.style("stroke-width", 1 / s).attr("transform", "translate(" + t + ")scale(" + s + ")");
        }
    }


};