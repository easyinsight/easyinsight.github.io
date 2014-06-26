Map = {

    getMap:function (target, params, showLabels, styleProps, filters, drillthroughKey, iframedInUI) {
        return function (data) {
            var sWidth;
            var sHeight;
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
            var svg = d3.select('#d3Div' + target);
            svg.selectAll("*").remove();
            svg.attr("height", height);
            var width = $("#" + target).width() - 50;
            svg.attr("width", width);
            var color = d3.scale.quantize()
                .range(data.colors);

            var mapType = data.map;
            var projection;
            var targetJSON;
            var featureProp;
            var scale;
            if (mapType == "US States") {
                sHeight = height / 480;
                sWidth = width / 735;

                if (sHeight < sWidth) {
                    scale = 1000 * sHeight;
                } else {
                    scale = 1000 * sWidth;
                }
                projection = d3.geo.albersUsa()
                    .scale(scale).translate([width / 2, height / 2]);
                targetJSON = "/js/us-named3.json";

                featureProp = "states";
            } else if (mapType == "World") {
                sHeight = height / 480;
                sWidth = width / 735;
                if (sHeight < sWidth) {
                    scale = 150 * sHeight;
                } else {
                    scale = 150 * sWidth;
                }
                projection = d3.geo.mercator()
                    .scale(scale).
                    translate([width / 2, (height / 2) + (scale * .37)]);
                targetJSON = "/js/maps/world-topo.json";

                featureProp = "countries";
            }

            var path = d3.geo.path().projection(projection);

            var jData = data.regions;
            var lookup = {};
            var valueLookup = {};
            var reverseLookup = {};

            color.domain([
                d3.min(jData, function(d) { return d.scaledValue; }),
                d3.max(jData, function(d) { return d.scaledValue; })
            ]);

            for (var i = 0; i < jData.length; i++) {
                var jD = jData[i];
                lookup[jD.region] = jD.scaledValue;
                valueLookup[jD.region] = jD.value;
                reverseLookup[jD.region] = jD.originalRegion;
            }

            var g = svg.append("g");

            var tooltip = $("#" + target + " .mapTooltip");

            d3.json(targetJSON, function(error, topology) {
                if (error) return console.error(error);

                var feature = topojson.feature(topology, topology.objects[featureProp]);
                var country = g.selectAll(".counties").data(feature.features);

                country.enter().insert("path").attr("class", "counties").attr("d", path).style("fill",
                    function(d) {
                        var value = lookup[d.properties.name];
                        if (value) {
                            return color(value);
                        } else {
                            return data.noDataFill;
                        }
                    }
                );

                country
                    .on("mouseover", function(d, i) {
                        tooltip
                            .toggleClass("mapTooltipHidden", false);
                    })
                    .on("mousemove", function(d,i) {
                        var mouse = d3.mouse(svg.node()).map( function(d) { return parseInt(d); } );
                        var x = mouse[0] + 30;
                        var y = mouse[1] + 30;
                        var value = valueLookup[d.properties.name];

                        if (typeof(value) == "undefined") {
                            value = 0;
                        }
                            tooltip
                                .attr("style", "left:" + x + "px;top:" + y + "px")
                                .html("<b>" + d.properties.name + "</b><br>" + value +"");

                    })
                    .on("mouseout",  function(d,i) {
                        tooltip.toggleClass("mapTooltipHidden", true)
                    });

                if (data["drillthrough"]) {
                    var dtOptions = $.extend(true, {}, data["drillthrough"]);
                    country.on("click", function(d) {
                        var drillthrough = data["drillthrough"];
                        var f = {"reportID": dtOptions["reportID"], "drillthroughID": dtOptions["id"], "embedded": dtOptions["embedded"], "source": dtOptions["source"], "drillthroughKey": drillthroughKey, "filters": filters,
                            "drillthrough_values": {}};
                        f["drillthrough_values"][dtOptions["region"]] = reverseLookup[d.properties.name];
                        drillThrough(f);
                    });
                }

                if (typeof(data.pointDatas) != "undefined") {

                    var pointDatas = data.pointDatas;

                    for (var j = 0; j < pointDatas.length; j++) {
                        var pointData = pointDatas[j];
                        var pointColor = pointData.color;
                        var points = pointData.points;

                        var radiusMin = d3.min(points, function(d) { return d.pointValue; });
                        var radiusMax = d3.max(points, function(d) { return d.pointValue; });
                        var radius = d3.scale.log().range([4, 18]).domain([
                            radiusMin, radiusMax
                        ]);

                        var gradient = svg.append("svg:defs")
                            .append("svg:radialGradient")
                            .attr("id", "gradient" + j)
                            .attr("cx", "50%")
                            .attr("cy", "50%")
                            .attr("r", "50%")
                            .attr("fx", "50%")
                            .attr("fy", "50%");

                        gradient.append("svg:stop")
                            .attr("offset", "0%")
                            .attr("stop-color", pointColor)
                            .attr("stop-opacity", 1);

                        gradient.append("svg:stop")
                            .attr("offset", "20%")
                            .attr("stop-color", pointColor)
                            .attr("stop-opacity", 1);

                        gradient.append("svg:stop")
                            .attr("offset", "40%")
                            .attr("stop-color", pointColor)
                            .attr("stop-opacity", .3);

                        gradient.append("svg:stop")
                            .attr("offset", "100%")
                            .attr("stop-color", pointColor)
                            .attr("stop-opacity", .1);

                        svg.selectAll("circle").data(points).enter().append("circle").
                            attr("cx", function(d) {
                                var cx = projection([d.lon, d.lat]);
                                if (cx != null) {
                                    return cx[0];
                                }
                                return -50000;
                            }).
                            attr("cy", function(d) {
                                var cy = projection([d.lon, d.lat]);
                                if (cy != null) {
                                    return cy[1];
                                }
                                return -50000;
                            }).attr("r", Map.getRadius(radius)).attr("data-legend", Map.getFillName(pointData)).attr("data-legend-color", Map.getFillColor(pointData))
                            .style("fill", "url(#gradient" + j +")");
                    }

                    var legend = svg.append("g")
                        .attr("class","mapLegend")
                        .attr("transform","translate(50,30)")
                        .style("font-size","12px")
                        .call(d3.legend)
                }


            });
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