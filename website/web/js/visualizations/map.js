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
            var color;


            var mapType = data.map;

            var projection;
            var targetJSON;
            var featureProp;
            var scale;
            if (mapType == "US States") {
                sHeight = height / 490;
                sWidth = width / 755;

                if (sHeight < sWidth) {
                    scale = 1000 * sHeight;
                } else {
                    scale = 1000 * sWidth;
                }
                projection = d3.geo.albers()
                    .scale(scale).translate([width / 2, (height / 2) + 10]);
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
            } else if (mapType == "TN") {
                var baseScale = 8000;
                sHeight = height / 480;
                sWidth = width / 735;
                if (sHeight < sWidth) {
                    scale = baseScale * sHeight;
                } else {
                    scale = baseScale * sWidth;
                }
                projection = d3.geo.albers().scale(1)
                    .translate([0, 0]).rotate([86.7489, 0])
                    .center([0, 35.7449]);
                targetJSON = "/js/maps/tn.json";
                featureProp = "tnzip";
            }


            var path = d3.geo.path().projection(projection);

            var regionFill = false;
            var lookup = {};
            var valueLookup = {};
            var reverseLookup = {};
            if (typeof(data.regions) != "undefined") {
                color = d3.scale.quantize()
                    .range(data.colors);
                regionFill = true;
                var jData = data.regions;




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
            }







            var g = svg.append("g");

            var tooltip = $("#" + target + " .mapTooltip");

            console.log("loading " + targetJSON);
            d3.json(targetJSON, function(error, topology) {
                if (error) return console.error(error);

                var feature = topojson.feature(topology, topology.objects[featureProp]);
                var country = g.selectAll(".counties").data(feature.features);

                if (regionFill) {


                    if (mapType == "TN") {
                        var boundsSet = data["bounds_set"];
                        var targetData;
                        if (typeof(boundsSet) != "undefined") {
                            var selected = d3.set(boundsSet);

                            targetData = topojson.merge(topology, topology.objects.tncounties.geometries.filter(function (d) {
                                return selected.has(d.properties.name)
                            }));
                            var h = height - 20;
                            var b = path.bounds(targetData),
                                s = .95 / Math.max((b[1][0] - b[0][0]) / width, (b[1][1] - b[0][1]) / h),
                                t = [(width - s * (b[1][0] + b[0][0])) / 2, ((h - s * (b[1][1] + b[0][1])) / 2) + 30];

                            projection
                                .scale(s)
                                .translate(t);
                        } else {
                            targetData = topology.objects.tncounties;
                        }

                        svg.insert("path", ".graticule").datum(topojson.mesh(topology, topology.objects.tncounties, function (a, b) {
                            return a !== b;
                        })).style("fill", "none").style("stroke", "#AAAAAA").attr("d", path);

                        svg.append("path").datum(targetData).style("fill", "none").style("stroke-width", "3px").style("stroke", "#000000").attr("d", path);
                    }

                    country.enter().append("path").attr("class", "counties").attr("d", path).style("fill",
                        function (d) {
                            var value = lookup[d.properties.name];
                            if (value) {
                                return color(value);
                            } else {
                                return data.noDataFill;
                            }
                        }
                    );
                } else {
                    country.enter().append("path").attr("class", "counties").attr("d", path).style("fill",
                        function (d) {
                            var value = lookup[d.properties.name];
                            if (value) {
                                return color(value);
                            } else {
                                return data.noDataFill;
                            }
                        }
                    );
                }

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

                        var radius;
                        if (mapType == "TN") {
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
                                }).attr("r", 6).attr("data-legend", Map.getFillName(pointData)).attr("data-legend-color", Map.getFillColor(pointData))
                                .style("fill", pointColor);
                        } else {
                            var radiusMin = d3.min(points, function(d) { return d.pointValue; });
                            var radiusMax = d3.max(points, function(d) { return d.pointValue; });
                            radius = d3.scale.log().range([4, 18]).domain([
                                radiusMin, radiusMax
                            ]);
                            var gradient = svg.append("svg:defs")
                                .append("svg:radialGradient")
                                .attr("id", "gradient" + j)
                                .attr("cx", "50%")
                                .attr("cy", "50%")
                                .attr("r", "30%")
                                .attr("fx", "50%")
                                .attr("fy", "50%");

                            gradient.append("svg:stop")
                                .attr("offset", "0%")
                                .attr("stop-color", pointColor)
                                .attr("stop-opacity", 1);

                            gradient.append("svg:stop")
                                .attr("offset", "30%")
                                .attr("stop-color", pointColor)
                                .attr("stop-opacity", 1);

                            gradient.append("svg:stop")
                                .attr("offset", "60%")
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
                    }

                    if (data.pointDatas.length > 1) {
                        var legend = svg.append("g")
                            .attr("class", "mapLegend")
                            .attr("transform", "translate(50,50)")
                            .style("font-size", "12px")
                            .call(d3.legend)
                    }
                }

                if (regionFill) {
                    var xMin = d3.min(jData, function (d) {
                        return d.scaledValue;
                    });
                    var xMax = d3.max(jData, function (d) {
                        return d.scaledValue;
                    });
                    var xActualMin = d3.min(jData, function (d) {
                        return d.value;
                    });
                    var xActualMax = d3.max(jData, function (d) {
                        return d.value;
                    });
                    var x = d3.scale.linear()
                        .domain([xMin,
                            xMax])
                        .range([0, 240]);

                    svg.append("rect").attr("height", 25).attr("width", width + 30).style("fill", "#FFFFFF");
                    svg.selectAll("rect")
                        .data(color.range().map(function (c) {
                            var d = color.invertExtent(c);
                            if (d[0] == null) d[0] = x.domain()[0];
                            if (d[1] == null) d[1] = x.domain()[1];
                            return d;
                        }))
                        .enter().append("rect")
                        .attr("height", 8)
                        .attr("x", function (d) {
                            return x(d[0]);
                        })
                        .attr("width", function (d) {
                            return x(d[1]) - x(d[0]);
                        })
                        .style("fill", function (d) {
                            return color(d[0]);
                        });
                    svg.append("text").attr("y", 20).attr("x", 10).text(xActualMin);
                    svg.append("text").attr("y", 20).attr("x", 230).text(xActualMax);
                }

                if(typeof(afterRefresh) != "undefined") {
                    if (afterRefresh.length > 1) {
                        afterRefresh($("#" + target + " .loading"), target)();
                    } else if(afterRefresh.length > 0) {
                        afterRefresh($("#" + target + " .loading"))();
                    } else {
                        afterRefresh();
                    }
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