Chart = {



    getD3StackedColumnChart:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {
                        var height = Chart.chartHeight(target, styleProps);

                        var rWidth = $("#d3Div" + target).width();

                        var s1 = data["values"];
                        var maxLabelSize = d3.max(s1, function(d) {
                            return d3.max(d.values, function(e) { return e.x.length } );
                        });
                        var factorForRotate = rWidth / 30;
                        var needStagger = maxLabelSize > (rWidth / 50);
                        var useRotate = maxLabelSize > factorForRotate;
                        var charLimit = useRotate ? 15 : 0;

                        var chart = nv.models.multiBarChart()
                            .height(height)
                            .reduceXTicks(false)
                            .showControls(false)
                            .stacked(true)
                            .staggerLabels(!useRotate && needStagger)
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .showYAxis(true)        //Show the y-axis
                            .showXAxis(true)        //Show the x-axis
                            .margin({top: 20, right: 40, bottom: useRotate ? 130 : (needStagger ? 60 : 50), left: 80});

                        var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
                        if (customWidth > -1) {
                            chart.width(customWidth);
                        }

                        if (data["drillthrough"]) {
                            var dtOptions = $.extend(true, {}, data["drillthrough"]);
                            chart.multibar.dispatch.on("elementClick", function(e) {
                                var drillthrough = data["drillthrough"];
                                var f = {"reportID": dtOptions["reportID"], "drillthroughID": dtOptions["id"], "embedded": dtOptions["embedded"], "source": dtOptions["source"], "drillthroughKey": drillthroughKey, "filters": filters,
                                    "drillthrough_values": {}};
                                f["drillthrough_values"][dtOptions["xaxis"]] = e.point.x;
                                f["drillthrough_values"][drillthrough["stack"]] = e.series.key;
                                drillThrough(f);
                            });
                        }

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 50, -70, charLimit);
                        Chart.assignAxisMinMaxValues(chart, data, true);


                        if (useRotate) {
                            chart.xAxis.rotateLabels(-45);
                        }

                        d3.select('#d3Div' + target)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target, styleProps);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    getD3StackedBarChart:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {
                        var height = Chart.chartHeight(target, styleProps);

                        var s1 = data["values"];

                        var maxLen = 0;
                        for (var l = 0; l < s1.length; l++) {
                            var lenVals = s1[l].values;
                            for (var k = 0; k < lenVals.length; k++) {
                                var x = lenVals[k].x;
                                if (x.length > maxLen) {
                                    maxLen = x.length;
                                }
                            }
                        }

                        var leftNeeded = maxLen * 16;
                        if (leftNeeded > 150) {
                            leftNeeded = 150;
                        }

                        var chart = nv.models.multiBarHorizontalChart()
                            .x(function(d) {
                                if (d.x.length > 15) {
                                    return d.x.substring(0, 15) + "...";
                                } else {
                                    return d.x;
                                }
                            })
                            .height(height)
                            .showControls(false)
                            .stacked(true)
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .margin({top: 20, right: 40, bottom: 60, left: leftNeeded});

                        var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
                        if (customWidth > -1) {
                            chart.width(customWidth);
                        }

                        var floatingY = data["floatingY"];
                        if (floatingY) {
                            var vals = s1[0].values;
                            var minX = d3.min(vals, function (d) {
                                return d.xMin;
                            });
                            if (data["dateAxis"]) {
                                chart.cumulativeDateAxis(true);
                                var maxX = data["maxY"];
                                chart.minX(function(d) {
                                    return d.xMin;
                                }).forceY([minX, maxX]);
                            } else {
                                chart.minX(function(d) {
                                    return d.xMin;
                                }).forceY([minX]);
                            }
                        }

                        if (data["drillthrough"]) {
                            var dtOptions = $.extend(true, {}, data["drillthrough"]);
                            chart.multibar.dispatch.on("elementClick", function(e) {
                                var drillthrough = data["drillthrough"];
                                var f = {"reportID": dtOptions["reportID"], "drillthroughID": dtOptions["id"], "embedded": dtOptions["embedded"], "source": dtOptions["source"], "drillthroughKey": drillthroughKey, "filters": filters,
                                    "drillthrough_values": {}};
                                f["drillthrough_values"][dtOptions["xaxis"]] = e.point.x;
                                f["drillthrough_values"][drillthrough["stack"]] = e.series.key;
                                drillThrough(f);
                            });
                        }

                        if (data["dateAxis"]) {
                            data["yFormat"].type = "msToDate";
                        }

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, -leftNeeded + 10, 30);
                        Chart.assignAxisMinMaxValues(chart, data, true);

                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target, styleProps);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    getBulletChartCallback:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {

                        var s1 = data["values"][0];

                        var title = s1.title;
                        var titleLength = title.length;
                        if (titleLength > 15) {
                            title = title.substring(0, 13) + "...";
                            titleLength = title.length;
                        }
                        var leftMargin = titleLength * 10;


                        var chart = nv.models.bulletChart()
                            .margin({top: 20, right: 20, bottom: 20, left: leftMargin});

                        d3.select('#d3Div' + target)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target, styleProps);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    getD3PieChartCallback:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {

                        var height = Chart.chartHeight(target, styleProps);

                        var s1 = data["values"];

                        var colors = [];


                        for (var i = 0; i < s1.length; i++) {
                            var point = s1[i];
                            var c = point.color;
                            colors.push(point.color);
                        }

                        var labelType = data["pieLabelStyle"];
                        var pieLabelType;
                        if (labelType == "Label") {
                            pieLabelType = "key";
                        } else if (labelType == "Percentage") {
                            pieLabelType = "percent";
                        } else if (labelType == "Value with Percentage") {
                            pieLabelType = "custom";
                        } else {
                            pieLabelType = "value";
                        }

                        var showLegend = data["showLegend"];

                        var chart = nv.models.pieChart()
                            //.width(width)
                            .x(function(d) { return d.label })
                            .y(function(d) { return d.value })
                            .showLabels(true)
                            .color(colors)
                            .height(height)
                            .showLegend(showLegend)
                            .valueFormat(Chart.createFormat(data["yFormat"]))
                            .labelThreshold(0.02)
                            .labelType(pieLabelType)
                            /*.donut(true)          //Turn on Donut mode. Makes pie chart look tasty!
                            .donutRatio(0.35)*/
                            .margin({top: 20, right: 20, bottom: 20, left: 20})
                            .tooltipContent(function(key, x, e, graph) {
                                return '<h3>' + key + '</h3>' +
                                    '<p><b>' +  x + '</b></p>' +
                                    '<h4><b>' + e.point.percent + '%</b> of <b>' + e.point.total + '</b></h4>';
                            });
                        var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
                        if (customWidth > -1) {
                            chart.width(customWidth);
                        }

                        if (data["donut"]) {
                            chart.donut(true).donutRatio(data["donutRatio"]);
                        }

                        if (data["drillthrough"]) {
                            var dtOptions = $.extend(true, {}, data["drillthrough"]);
                            chart.pie.dispatch.on("elementClick", function(e) {
                                var drillthrough = data["drillthrough"];
                                var f = {"reportID": dtOptions["reportID"], "drillthroughID": dtOptions["id"], "embedded": dtOptions["embedded"], "source": dtOptions["source"], "drillthroughKey": drillthroughKey, "filters": filters,
                                    "drillthrough_values": {}};
                                f["drillthrough_values"][dtOptions["xaxis"]] = e.label;
                                drillThrough(f);
                            });
                        }



                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target, styleProps);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    getD3ColumnChartCallback:function (target, params, showLabels, styleProps, filters, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {
                        var height = Chart.chartHeight(target, styleProps);
                        var chart;

                        var s1 = data["values"];

                        var maxLabelSize = d3.max(s1, function(d) {
                            return d3.max(d.values, function(e) { return e.x.length } );
                        });
                        var rWidth = $("#d3Div" + target).width();

                        var factorForRotate = rWidth / 30;
                        var useRotate = maxLabelSize > factorForRotate;
                        var needStagger = maxLabelSize > (rWidth / 50);
                        var charLimit = useRotate ? 15 : 0;

                        if (data["oneMeasure"]) {
                            var colors = [];


                            for (var i = 0; i < s1.length; i++) {
                                var point = s1[i];
                                var c = point.color;
                                colors.push(point.color);
                            }
                            chart = nv.models.discreteBarChart()
                                //.width(width)
                                .height(height)
                                .color(colors)
                                .staggerLabels(!useRotate && needStagger)
                                .transitionDuration(350)  //how fast do you want the lines to transition?
                                .tooltipContent(function(key, x, y, e, graph) {
                                    return '<b>' + x + '</b>' +
                                        '<p>' +  y + '</p>'
                                })
                                .showYAxis(true)        //Show the y-axis
                                .showXAxis(true)        //Show the x-axis
                                .margin({top: 20, right: 40, bottom: useRotate ? 120 : (needStagger ? 60 : 60), left: 85});
                            var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
                            if (customWidth > -1) {
                                chart.width(customWidth);
                            }
                            if (data["valueLabel"]) {
                                chart.showValues(true);
                                if (data["yFormat"]) {
                                    chart.valueFormat(Chart.createFormat(data["yFormat"]));
                                }
                            }
                            if (data["drillthrough"]) {
                                var dtOptions = $.extend(true, {}, data["drillthrough"]);
                                chart.discretebar.dispatch.on("elementClick", function(e) {
                                    var drillthrough = data["drillthrough"];
                                    var f = {"reportID": dtOptions["reportID"], "drillthroughID": dtOptions["id"], "embedded": dtOptions["embedded"], "source": dtOptions["source"], "drillthroughKey": drillthroughKey, "filters": filters,
                                        "drillthrough_values": {}};
                                    f["drillthrough_values"][dtOptions["xaxis"]] = e.point.x;
                                    drillThrough(f);
                                });
                            }
                            Chart.assignAxisMinMaxValues(chart, data, false);
                        } else {
                            chart = nv.models.multiBarChart()
                            //.width(width)
                            .height(height)
                            .reduceXTicks(false)
                            .showControls(false)
                            .staggerLabels(!useRotate && needStagger)
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .showYAxis(true)        //Show the y-axis
                            .showXAxis(true)        //Show the x-axis
                            .margin({top: 20, right: 40, bottom: useRotate ? 120 : (needStagger ? 60 : 60), left: 80});
                            if (data["drillthrough"]) {
                                var dtOptions = $.extend(true, {}, data["drillthrough"]);
                                if (dtOptions["id"]) {

                                }
                                chart.multibar.dispatch.on("elementClick", function(e) {
                                    var drillthrough = data["drillthrough"];
                                    var f = {"reportID": dtOptions["reportID"], "drillthroughID": dtOptions["id"], "embedded": dtOptions["embedded"], "source": dtOptions["source"], "drillthroughKey": drillthroughKey, "filters": filters,
                                        "drillthrough_values": {}};
                                    f["drillthrough_values"][dtOptions["xaxis"]] = e.point.x;
                                    drillThrough(f);
                                });
                            }
                            var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
                            if (customWidth > -1) {
                                chart.width(customWidth);
                            }
                            Chart.assignAxisMinMaxValues(chart, data, true);
                        }




                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 30, -70, charLimit);

                        if (useRotate) {
                            chart.xAxis.rotateLabels(-45);
                        }


                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target, styleProps);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    getD3BarChartCallback:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {
                        var s1 = data["values"];

                        var maxLen = 0;
                        for (var l = 0; l < s1.length; l++) {
                            var lenVals = s1[l].values;
                            for (var k = 0; k < lenVals.length; k++) {
                                var x = lenVals[k].x;
                                if (x.length > maxLen) {
                                    maxLen = x.length;
                                }
                            }
                        }

                        var leftNeeded = maxLen * 16;
                        if (leftNeeded > 150) {
                            leftNeeded = 150;
                        }

                        var height = Chart.chartHeight(target, styleProps);
                        var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
                        var chart = nv.models.multiBarHorizontalChart()
                            .x(function(d) {
                                if (d.x.length > 15) {
                                    return d.x.substring(0, 15) + "...";
                                } else {
                                    return d.x;
                                }
                            })
                            .height(height)
                            .showControls(false)
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .margin({top: 10, right: 30, bottom: 40, left: leftNeeded});
                        var floatingY = data["floatingY"];
                        if (floatingY) {
                            var vals = s1[0].values;
                            var minX = d3.min(vals, function (d) {
                                return d.minY;
                            });
                            chart.minX(function(d) {
                                return d.minY;
                            }).forceY([minX]);
                        }
                        if (customWidth > -1) {
                            chart.width(customWidth);
                        }
                        if (data["valueLabel"]) {
                            chart.showValues(true);
                            if (data["yFormat"]) {
                                chart.valueFormat(Chart.createFormat(data["yFormat"]));
                            }
                        }
                        if (data["drillthrough"]) {
                            var dtOptions = $.extend(true, {}, data["drillthrough"]);
                            chart.multibar.dispatch.on("elementClick", function(e) {
                                var drillthrough = data["drillthrough"];
                                var f = {"reportID": dtOptions["reportID"], "drillthroughID": dtOptions["id"], "embedded": dtOptions["embedded"], "source": dtOptions["source"], "drillthroughKey": drillthroughKey, "filters": filters,
                                    "drillthrough_values": {}};
                                f["drillthrough_values"][dtOptions["xaxis"]] = e.point.x;
                                drillThrough(f);
                            });
                        }

                        if (data["dateAxis"]) {
                            data["yFormat"].type = "msToDate";
                        }

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, -leftNeeded + 10, 40);
                        Chart.assignAxisMinMaxValues(chart, data, true);



                        d3.select('#d3Div' + target)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target, styleProps);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    canvasHeights:function (target, styleProps) {
        var h = $("#d3Div"+ target).height();
        var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
        var w;
        if (customWidth > -1) {
            w = customWidth;
        } else {
            w = $("#d3Div" + target).width();
        }
        $("#d3Canvas"+target).attr('height', h);
        $("#d3Canvas"+target).attr('width', w);

        if (styleProps != null && styleProps["png"]) {
            var targ = d3.select('#d3Div' + target + " .nv-wrap");
            targ.insert("rect", ":first-child")
                .attr("width", "100%")
                .attr("height", "100%")
                .attr("fill", "#FFFFFF");
            d3.selectAll("svg text").style("font", "normal 12px Arial");
            d3.selectAll(".title").style("font", "bold 14px Arial");
            d3.selectAll(".nv-axislabel").style("font", "bold 14px Arial");
            d3.selectAll(".nvd3 .nv-axis .nv-axisMaxMin text").style("font-weight", "bold");
            d3.selectAll(".nvd3 .nv-discretebar .nv-groups text").style("font-weight", "bold");
            d3.selectAll(".nvd3 .nv-multibarHorizontal .nv-groups text").style("font-weight", "bold");
            d3.selectAll(".nvd3 .nv-multibar .nv-groups rect").style("fill-opacity", 1);
            d3.selectAll(".nvd3 .nv-multibarHorizontal .nv-groups rect").style("fill-opacity", 1);
            d3.selectAll(".nvd3 .nv-discretebar .nv-groups rect").style("fill-opacity", 1);
        }
    },

    chartHeightWithIFrame:function (target, styleProps, iframedInUI) {
        var height;
        var customHeight = styleProps != null ? styleProps["customHeight"] : -1;
        if (customHeight > -1) {

            if (customHeight > 0) {
                height = customHeight;
            } else {
                var verticalMargin = styleProps["verticalMargin"];
                height = $(document).height() - $('#filterRow').height() - $('#reportHeader').height() - verticalMargin;
                if (height < 400) {
                    height = 400;
                }
            }

        } else {
            var raHeight = $('#'+target+'ReportArea').height();
            if (typeof(raHeight) != "undefined" && raHeight > 150) {
                height = raHeight;
            } else {
                height = nv.utils.windowSize().height - $('#filterRow').height() - $('#reportHeader').height() - (iframedInUI ? 100 : 250);
            }
        }
        return height;
    },

    chartHeight:function (target, styleProps) {
        var height;
        //alert("target height =  " + $('#'+target+"").height());
        var customHeight = styleProps != null ? styleProps["customHeight"] : -1;
        if (customHeight > -1) {

            if (customHeight > 0) {
                height = customHeight;
            } else {
                var verticalMargin = styleProps["verticalMargin"];
                height = $(document).height() - $('#filterRow').height() - $('#reportHeader').height() - verticalMargin;
                if (height < 400) {
                    height = 400;
                }
            }

        } else {
            var customPreferredHeight = styleProps != null ? styleProps["preferredHeight"] : -1;
            if (customPreferredHeight > -1) {
                height = customPreferredHeight;
            } else {
                var raHeight = $('#' + target + 'ReportArea').height();
                if (typeof(raHeight) != "undefined" && raHeight > 150) {
                    height = raHeight;
                } else {
                    height = nv.utils.windowSize().height - $('#filterRow').height() - $('#reportHeader').height() - 250;
                }
            }
        }
        return height;
    },

    getD3ScatterCallback:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {

                        var height = Chart.chartHeight(target, styleProps);

                        var chart = nv.models.scatterChart()
                            .height(height)
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .margin({top: 20, right: 40, bottom: 60, left: 80});

                        var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
                        if (customWidth > -1) {
                            chart.width(customWidth);
                        }

                        if (data["point"]) {
                            chart.size(function(d) { return 100 })
                            .sizeRange([100, 100]);
                        }


                        chart.scatter.onlyCircles(false);

                        if (data["drillthrough"]) {
                            var dtOptions = $.extend(true, {}, data["drillthrough"]);
                            if (dtOptions["id"]) {

                            }
                            chart.scatter.dispatch.on("elementClick", function(e) {
                                var drillthrough = data["drillthrough"];
                                var f = {"reportID": dtOptions["reportID"], "drillthroughID": dtOptions["id"], "embedded": dtOptions["embedded"], "source": dtOptions["source"], "drillthroughKey": drillthroughKey, "filters": filters,
                                    "drillthrough_values": {}};
                                f["drillthrough_values"][dtOptions["xaxis"]] = e.key;
                                drillThrough(f);
                            });
                        }

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 50, -70);
                        Chart.assignAxisMinMaxValues(chart, data, true);

                        var s1 = data["values"];

                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target, styleProps);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    assignAxisLabels:function (xAxis, yAxis, data, xLabelDistance, yLabelDistance, limit) {
        xAxis.axisLabel(data["xTitle"]);
        if (typeof(data["xFormat"]) != "undefined") {
            xAxis.tickFormat(Chart.createFormat(data["xFormat"], limit));
        }
        xAxis.axisLabelDistance(xLabelDistance);
        yAxis.axisLabel(data["yTitle"]);
        if (typeof(data["yFormat"]) != "undefined") {
            yAxis.tickFormat(Chart.createFormat(data["yFormat"], limit));
        }
        yAxis.axisLabelDistance(yLabelDistance);
    },

    assignAxisMinMaxValues:function (chart, data, showLegend) {

        if (showLegend) {
            if (data["showLegend"]) {
                chart.showLegend(true);
            } else {
                chart.showLegend(false);
            }
        }

        var xMin = data["xMin"];
        var xMax = data["xMax"];
        if (typeof(xMin) != "undefined" && typeof(xMax) != "undefined") {
            chart.forceX([xMin, xMax]);
        }
        var yMin = data["yMin"];
        var yMax = data["yMax"];
        if (typeof(yMin) != "undefined" && typeof(yMax) != "undefined") {
            chart.forceY([yMin, yMax]);
        }
    },

    createFormat:function (formatInfo, limit) {
        return function(d) {
            if (formatInfo.type == "msToDate") {
                var format = d3.time.format("%m/%d/%Y");
                return format(new Date(d));
            } else if (formatInfo.type == "measure") {
                var precision = formatInfo.precision;
                var numberFormat = formatInfo.numberFormat;
                var numberFormatter = d3.format(",."+precision+"f");
                if (numberFormat == 2) {
                    var currencySymbol = formatInfo.currencySymbol;
                    return currencySymbol + numberFormatter(d);
                } else if (numberFormat == 3) {
                    return numberFormatter(d) + "%";
                } else if (numberFormat == 4) {
                    return Chart.millisecond("m", d);
                } else if (numberFormat == 5) {
                    return Chart.millisecond("s", d);
                } else {

                }
                return numberFormatter(d);
            } else {
                if (limit > 0) {
                    return Chart.truncate(limit, d);
                }
            }
            return d;
        }
    },

    truncate:function(limit, val) {
        if (val.length > limit) {
            return val.substring(0, limit) + "...";
        } else {
            return val;
        }
    },

    millisecond:function(format, val) {
        if(val ==  0)
            return String("");
        var result = "";
        if(format == "s")
            val = val * 1000;
        var unsigned = Math.abs(val);
        var milliseconds, seconds, minutes, hours, days;
        if (unsigned < 60000) {
            seconds = Math.floor(unsigned / 1000);
            milliseconds =  (val % 1000);
            result = seconds + "s:";
            if(format == "ms")
                result = result + milliseconds + "ms";
        } else if (unsigned < (60000 * 60)) {
            minutes = Math.floor(unsigned / 60000);
            seconds = Math.floor(unsigned / 1000) % 60;
            result = minutes + "m: " + seconds + "s";
        } else if (unsigned < (60000 * 60 * 24)) {
            hours = Math.floor(unsigned / (60000 * 60));
            minutes = Math.floor(unsigned % 24);
            result = hours + "h:" + minutes + "m";
        } else {
            days = Math.floor(unsigned / (60000 * 60 * 24));
            hours = Math.floor(unsigned / (60000 * 60) % 24);
            result = days + "d:" + hours + "h";
        }
        if (val < 0) {
            result = "(" + result + ")";
        }
        return String(result);
    },

    getD3LineCallback:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {

                        var s1 = data["values"];

                        var format = d3.time.format("%m/%d/%Y");

                        var minY = null;
                        var maxY = null;
                        for (var i = 0; i < s1.length; i++) {
                            var keyVals = s1[i];
                            for (var j = 0; j < keyVals.values.length; j++) {
                                var row = keyVals.values[j];
                                row.x = format.parse(row.x);
                                if (minY == null || minY > row.y) {
                                    minY = row.y;
                                }
                                if (maxY == null || row.y > maxY) {
                                    maxY = row.y;
                                }
                            }
                        }

                        var height = Chart.chartHeight(target, styleProps);

                        var chart = nv.models.lineChart()
                            //.width(width)
                            .height(height)
                            .useInteractiveGuideline(true)  //We want nice looking tooltips and a guideline!
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .showLegend(true)       //Show the legend, allowing users to turn on/off line series.
                            .showYAxis(true)        //Show the y-axis
                            .showXAxis(true)        //Show the x-axis
                            .margin({top: 20, right: 40, bottom: 40, left: 80});

                        var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
                        if (customWidth > -1) {
                            chart.width(customWidth);
                        }

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 40, -65);
                        Chart.assignAxisMinMaxValues(chart, data);

                        var dateFormat = data["date_format"];

                        chart.xAxis.tickFormat(function(d) {
                            return d3.time.format(dateFormat)(new Date(d))
                        });

                        var svg = d3.select('#d3Div' + target);

                        svg.attr('height', height)
                            .datum(s1)
                            .call(chart);

                        var events = data["events"];
                        if (typeof(events) != "undefined") {
                            var calcYMax = chart.yAxis.scale()(maxY);
                            var calcYMin = chart.yAxis.scale()(minY);
                            var targ = d3.select('#d3Div' + target + " .nv-linesWrap");
                            for (var eventIdx = 0; eventIdx < events.length; eventIdx++) {
                                var event = events[eventIdx];
                                var time = format.parse(event.date);
                                var calcX = chart.xAxis.scale()(time);
                                targ.append("g").append("rect").attr("height", (calcYMin - calcYMax)).attr("width", 3).style("fill", "#0000FF").attr("x", calcX).attr("y", 0);
                                targ.append("foreignObject").attr("width", 100).attr("height", 100).attr("y", (calcYMin - calcYMax) / 2).attr("x", calcX + 5).append("xhtml:body").attr("class", "report_annotation").style("font", "12px 'Helvetica Neue'").html("<p>"+event.label+"</p>");
                            }

                        }


                        Chart.canvasHeights(target, styleProps);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    getD3AreaCallback:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {

                        var height = Chart.chartHeight(target, styleProps);

                        var s1 = data["values"];

                        var format = d3.time.format("%m/%d/%Y");

                        for (var i = 0; i < s1.length; i++) {
                            var keyVals = s1[i];
                            for (var j = 0; j < keyVals.values.length; j++) {
                                var row = keyVals.values[j];
                                row.x = format.parse(row.x);
                            }
                        }

                        var chart = nv.models.stackedAreaChart()
                            //.width(width)
                            //.height(height)
                            .useInteractiveGuideline(true)  //We want nice looking tooltips and a guideline!
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .showLegend(true)       //Show the legend, allowing users to turn on/off line series.
                            .showYAxis(true)        //Show the y-axis
                            .showXAxis(true)        //Show the x-axis
                            .margin({top: 20, right: 40, bottom: 50, left: 76});

                        var customWidth = styleProps != null ? styleProps["preferredWidth"] : -1;
                        if (customWidth > -1) {
                            chart.width(customWidth);
                        }

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 40, -65);
                        Chart.assignAxisMinMaxValues(chart, data);

                        var dateFormat = data["date_format"];

                        chart.xAxis.tickFormat(function(d) {
                            return d3.time.format(dateFormat)(new Date(d))
                        });



                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target, styleProps);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    cleanup:function (target) {
        if (Chart.charts[target]) {
            var tt = $("#" + target);
            tt.unbind("jqplotDataClick");
            Chart.charts[target].destroy();
            delete Chart.charts[target];
            Chart.charts[target] = null;
        }
    }
};