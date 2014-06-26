Chart = {



    getD3StackedColumnChart:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {
                        var height = Chart.chartHeight(target, styleProps);

                        var chart = nv.models.multiBarChart()
                            //.width(width)
                            .height(height)
                            .reduceXTicks(false)
                            .showControls(false)
                            .stacked(true)
                            .staggerLabels(true)
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .showYAxis(true)        //Show the y-axis
                            .showXAxis(true)        //Show the x-axis
                            .margin({top: 20, right: 40, bottom: 60, left: 80});

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

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 50, 12);
                        Chart.assignAxisMinMaxValues(chart, data, true);

                        var s1 = data["values"];

                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target);

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

                        var chart = nv.models.multiBarHorizontalChart()
                            //.width(width)
                            .height(height)
                            .showControls(false)
                            .stacked(true)
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .margin({top: 20, right: 40, bottom: 60, left: 120});

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

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, -20, 30);
                        Chart.assignAxisMinMaxValues(chart, data, true);

                        var s1 = data["values"];

                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target);

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

                        //var height = Chart.chartHeight(target, styleProps);

                        var s1 = data["values"][0];

                        var chart = nv.models.bulletChart()
                            //.width(width)
                            //.height(height)
                            .margin({top: 20, right: 20, bottom: 20, left: 40});




                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            //.attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target);

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

                        var chart = nv.models.pieChart()
                            //.width(width)
                            .x(function(d) { return d.label })
                            .y(function(d) { return d.value })
                            .showLabels(true)
                            .color(colors)
                            .height(height)
                            .labelThreshold(0.05)
                            .labelType("value")
                            /*.donut(true)          //Turn on Donut mode. Makes pie chart look tasty!
                            .donutRatio(0.35)*/
                            .margin({top: 20, right: 20, bottom: 20, left: 20});

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

                        Chart.canvasHeights(target);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    getD3ColumnChartCallback:function (target, params, showLabels, styleProps, filters, extras, drillthroughKey) {
        return function (data) {
            Utils.noDataD3(data["values"], function () {
                nv.addGraph({
                    generate: function() {

                        var height = Chart.chartHeight(target, styleProps);
                        var chart;

                        var s1 = data["values"];

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
                                .staggerLabels(true)
                                .transitionDuration(350)  //how fast do you want the lines to transition?
                                .tooltipContent(function(key, x, y, e, graph) {
                                    return '<b>' + x + '</b>' +
                                        '<p>' +  y + '</p>'
                                })
                                .showYAxis(true)        //Show the y-axis
                                .showXAxis(true)        //Show the x-axis
                                .margin({top: 20, right: 40, bottom: 80, left: 80});
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
                            .staggerLabels(true)
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .showYAxis(true)        //Show the y-axis
                            .showXAxis(true)        //Show the x-axis
                            .margin({top: 20, right: 40, bottom: 80, left: 80});
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
                            Chart.assignAxisMinMaxValues(chart, data, true);
                        }




                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 50, 12);




                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target);

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

                        var height = Chart.chartHeight(target, styleProps);

                        var chart = nv.models.multiBarHorizontalChart()
                            //.width(width)
                            .height(height)
                            .showControls(false)
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .margin({top: 20, right: 30, bottom: 60, left: 130});
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

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, -20, 40);
                        Chart.assignAxisMinMaxValues(chart, data, true);

                        var s1 = data["values"];

                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    canvasHeights:function (target) {
        var h = $("#d3Div"+ target).height();
        var w = $("#d3Div"+ target).width();
        $("#d3Canvas"+target).attr('height', h);
        $("#d3Canvas"+target).attr('width', w);
    },

    chartHeightWithIFrame:function (target, styleProps, iframedInUI) {
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
            var raHeight = $('#'+target+'ReportArea').height();
            if (typeof(raHeight) != "undefined" && raHeight > 150) {
                height = raHeight;
            } else {
                height = nv.utils.windowSize().height - $('#filterRow').height() - $('#reportHeader').height() - 250;
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

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 50, 12);
                        Chart.assignAxisMinMaxValues(chart, data, true);

                        var s1 = data["values"];

                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target);

                        nv.utils.windowResize(function() { chart.update() });
                        return chart;
                    }
                });

            }, Chart.cleanup, target);
        };
    },

    assignAxisLabels:function (xAxis, yAxis, data, xLabelDistance, yLabelDistance) {
        xAxis.axisLabel(data["xTitle"]);
        if (typeof(data["xFormat"]) != "undefined") {
            xAxis.tickFormat(Chart.createFormat(data["xFormat"]));
        }
        xAxis.axisLabelDistance(xLabelDistance);
        yAxis.axisLabel(data["yTitle"]);
        if (typeof(data["yFormat"]) != "undefined") {
            yAxis.tickFormat(Chart.createFormat(data["yFormat"]));
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

    createFormat:function (formatInfo) {
        return function(d) {
            if (formatInfo.type == "measure") {
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
            }
            return d;
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

                        //chart.xAxis.tickFormat(f);


                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 40, 12);
                        Chart.assignAxisMinMaxValues(chart, data);

                        chart.xAxis.tickFormat(function(d) {
                            return d3.time.format('%x')(new Date(d))
                        });

                        var s1 = data["values"];

                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target);

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

                        var chart = nv.models.stackedAreaChart()
                            //.width(width)
                            //.height(height)
                            .useInteractiveGuideline(true)  //We want nice looking tooltips and a guideline!
                            .transitionDuration(350)  //how fast do you want the lines to transition?
                            .showLegend(true)       //Show the legend, allowing users to turn on/off line series.
                            .showYAxis(true)        //Show the y-axis
                            .showXAxis(true)        //Show the x-axis
                            .margin({top: 20, right: 40, bottom: 50, left: 70});

                        Chart.assignAxisLabels(chart.xAxis, chart.yAxis, data, 40, 12);
                        Chart.assignAxisMinMaxValues(chart, data);

                        chart.xAxis.tickFormat(function(d) {
                            return d3.time.format('%x')(new Date(d))
                        });

                        var s1 = data["values"];

                        d3.select('#d3Div' + target)
                            //.attr('width', width)
                            .attr('height', height)
                            .datum(s1)
                            .call(chart);

                        Chart.canvasHeights(target);

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