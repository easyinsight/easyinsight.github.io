var stack;
var grid;
var textTemplate;
var reportTemplate;

var gaugeTemplate;

var dashboard;

var dashboardComponent = function (obj) {
    if (obj.type == "stack")
        return stack(obj);
    else if (obj.type == "grid") {
        return grid(obj);
    } else if (obj.type == "text") {
        return textTemplate(obj);
    } else if (obj.type == "report") {
        return reportTemplate(obj);
    }
}

var Filter;

var afterRefresh = function (selector) {
    return function () {
        selector.hide();
    }
}

var toFilterString = function (f) {
    if (!f.enabled)
        return "filter" + f["id"] + "enabled=false";
    else if (f.type == "single")
        return "filter" + f["id"] + "=" + encodeURIComponent(f["selected"]);
    else if (f.type == "multiple")
        return "filter" + f["id"] + "=" + $.map(f["selected"],function (e, i) {
            return encodeURIComponent(e);
        }).join(",");
    else if (f.type == "rolling") {
        var z = "filter" + f["id"] + "=" + f.interval_type;
        if (f.interval_type == "18")
            return [z, "filter" + f.id + "direction=" + f.direction, "filter" + f.id + "value=" + f.value, "filter" + f.id + "interval=" + f.interval ]
        else
            return z;
    } else if (f.type == "date_range") {
        return ["filter" + f.id + "start=" + f.start_date, "filter" + f.id + "end=" + f.end_date ]
    }
    else
        return ["filter" + f["id"] + "direction=0", "filter" + f["id"] + "value=1", "filter" + f["id"] + "interval=2"];
}

var renderReport = function (o, dashboardID, reload) {
    var obj = o.report.report;
    var filterStrings = [];
    var i;
    if (!reload && o.rendered) {
        return;
    }
    if ($("#" + obj.id + " :visible").size() == 0) {
        o.rendered = false;
        return;
    }
    for (i = 0; i < o.filters.length; i++) {
        filterStrings = filterStrings.concat(toFilterString(o.filters[i]))
    }
    if (obj.metadata.type == "pie") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), Chart.getPieChartCallback(obj.id, w, {}))
    }
    else if (obj.metadata.type == "diagram") {
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), function (data) {
            window.drawDiagram(data, $("#" + obj.id + " .reportArea"), obj.id, afterRefresh($("#" + obj.id + " .noData")));
        })
    }
    else if (obj.metadata.type == "list") {
        $.ajax({
            dataType: "text",
            url: obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"),
            success: List.getCallback(obj.id, obj.metadata.properties, obj.metadata.sorting, obj.metadata.columns)
        });
    } else if (obj.metadata.type == "bar") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), Chart.getBarChartCallback(obj.id, w, true, obj.metadata.styles));
    } else if (obj.metadata.type == "column") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), Chart.getColumnChartCallback(obj.id, w, obj.metadata.styles));
    }
    else if (obj.metadata.type == "area" || obj.metadata.type == "bubble" || obj.metadata.type == "plot" || obj.metadata.type == "line") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), Chart.getCallback(obj.id, w, true, obj.metadata.styles));
    } else if (obj.metadata.type == "stacked_bar") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), Chart.getStackedBarChart(obj.id, w, obj.metadata.styles));
    } else if (obj.metadata.type == "stacked_column") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), Chart.getStackedColumnChart(obj.id, w, obj.metadata.styles));
    } else if (obj.metadata.type == "gauge") {
        $("#" + obj.id + " .reportArea").html(gaugeTemplate({id: obj.id, benchmark: null }))
        var v = JSON.stringify(obj.metadata.properties).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), Gauge.getCallback(obj.id + "ReportArea", obj.id, w, obj.metadata.max))
    } else {
        $.get(obj.metadata.url + '?reportID=' + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), function (data) {
            Utils.noData(data, function () {
                $('#' + obj.id + " .reportArea").html(data);
            }, null, obj.id);
        });
    }
    o.rendered = true;
}

var buildReportGraph = function (obj, filterStack, filterMap, stackMap, reportMap) {
    if (obj.type == "report") {
        var fs3 = filterStack.concat(obj.report.metadata.filters);

        var w = {"type": "report", "filters": fs3, "report": obj, "rendered": false };
        reportMap[obj.id] = w;
        var t = _.reduce(obj.report.metadata.filters, function (m, i) {
            m["filter" + i.id] = {"filter": i, "parent": w };
            return m;
        }, {});
        $.extend(filterMap, t);
        return w;
    } else if (obj.type == "grid") {
        var children = [];

        var fs1 = filterStack.concat(obj.filters);
        for (var i = 0; i < obj.grid.length; i++) {
            for (var j = 0; j < obj.grid[i].length; j++) {
                children = _.flatten(children.concat(buildReportGraph(obj.grid[i][j], fs1, filterMap, stackMap, reportMap)));
            }
        }
        var x = {"type": "grid", "children": children };
        var u = _.reduce(obj.filters, function (m, i) {
            m["filter" + i.id] = {"filter": i, "parent": x };
            return m;
        }, {});
        $.extend(filterMap, u)
        return x;
    } else if (obj.type == "stack") {
        var fs2 = filterStack.concat(obj.filters);
        var ch = [];
        for (var k = 0; k < obj.stack_items.length; k++) {
            ch = _.flatten(ch.concat(buildReportGraph(obj.stack_items[k].item, fs2, filterMap, stackMap, reportMap)))
        }
        var y = {"type": "stack", "children": ch };
        stackMap[obj.id] = y;
        var v = _.reduce(obj.filters, function (m, i) {
            m["filter" + i.id] = {"filter": i, "parent": y };
            return m;
        }, {});
        $.extend(filterMap, v)
        return y;
    } else {
        return {"type": obj.type};
    }
}

var renderReports = function (obj, dashboardID, force) {
    if (obj.type == "report") {
        if(!obj.report.report.metadata.adhoc_execution)
            renderReport(obj, dashboardID, force);
    } else if (obj.type != "text") {
        for (var i = 0; i < obj.children.length; i++) {
            renderReports(obj.children[i], dashboardID, force);
        }
    }
}

$(function () {
    Date.firstDayOfWeek = 0;
    Date.format = 'yyyy/mm/dd';
    $.get("/js/template.html", function (data) {
        var s = $(data);


        Filter = {
            create: function (obj) {
                if (!obj) return;
                if (obj.type == "single")
                    return Filter.single(obj);
                else if (obj.type == "multiple")
                    return Filter.multiple(obj);
                else if (obj.type == "rolling")
                    return Filter.rolling(obj);
                else if (obj.type == "date_range")
                    return Filter.date_range(obj);
            },
            single: _.template($("#single_value_filter_template", s).html(), null, {variable: "data"}),
            multiple: _.template($("#multi_value_filter_template", s).html(), null, {variable: "data"}),
            rolling: _.template($("#rolling_date_filter_template", s).html(), null, {variable: "data"}),
            date_range: _.template($("#absolute_date_filter_template", s).html(), null, {variable: "data"}),
            base_filter: _.template($("#filter_base", s).html()),
            filters: _.template($("#filters_template", s).html(), null, {variable: "filter_values"})

        };
        dashboard = _.template($("#dashboard_template", s).html());
        stack = _.template($("#stack", s).html());
        grid = _.template($("#grid", s).html());
        reportTemplate = _.template($("#report_template", s).html());
        textTemplate = _.template($("#text_template", s).html());
        gaugeTemplate = _.template($("#gauge_template", s).html());

        $("#base").append(dashboard(dashboardJSON));

        $(".nav-pills").css("background-color", dashboardJSON["styles"]["alternative_stack_start"])
        $(".dashboard_base > .row-fluid > .span12 > .tabbable > .nav-pills").css("background-color", dashboardJSON["styles"]["main_stack_start"])

        var filterMap = _.reduce(dashboardJSON["filters"], function (m, i) {
            m["filter" + i.id] = {"filter": i, "parent": null };
            return m;
        }, {});
        var stackMap = {};
        var reportMap = {};
        var graph = buildReportGraph(dashboardJSON["base"], dashboardJSON["filters"], filterMap, stackMap, reportMap);


        renderReports(graph, dashboardJSON["id"], false);
        $(".single_filter").change(function (e) {
            var f = filterMap[$(e.target || e.srcElement).attr("id")]
            f.filter.selected = $(e.target || e.srcElement).val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        });

        $(".cb_all_choice").click(function (e) {
            if ($(e.target || e.srcElement).is(":checked")) {
                $("input", $(e.target || e.srcElement).parent().parent()).attr("checked", "checked");
            } else {
                $("input", $(e.target || e.srcElement).parent().parent()).removeAttr("checked");
            }
        });

        $(".cb_filter_choice").click(function (e) {
            if ($(e.target || e.srcElement).is(":checked")) {
                if ($(".cb_filter_choice:not(:checked)", $(e.target || e.srcElement).parent().parent()).size() == 0) {
                    $(".cb_all_choice", $(e.target || e.srcElement).parent().parent()).attr("checked", "checked");
                }
            } else {
                $(".cb_all_choice", $(e.target || e.srcElement).parent().parent()).removeAttr("checked");
            }
        });

        $(".multi_value_save").click(function (e) {
            var a = $(e.target || e.srcElement).parent().parent();
            var f = filterMap[a.attr("id").split("_")[0]];
            var selects = $("li input:checked", a);
            var selectVals = $.map(selects, function (e, i) {
                return $(".cb_filter_value", $(e).parent()).html();
            });
            if (selectVals.indexOf("All") != -1) {
                selectVals = ["All"];
            }
            f.filter.selected = selectVals;
            $(".multi_filter", $(a).parent()).html(selectVals.length == 1 ? selectVals[0] : selectVals.length + " Items");

            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        });

        $(".rolling_filter_type").change(function (e) {
            var t = $(".custom", $(e.target || e.srcElement).parent());
            var f = filterMap[$(e.target || e.srcElement).attr("id").split("_")[0]];
            if ($(e.target || e.srcElement).val() == "18") {
                t.show();

            } else {
                t.hide();
            }
            f.filter.interval_type = $(e.target || e.srcElement).val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        });

        $(".rolling_filter_direction").change(function (e) {
            var f = filterMap[$(e.target || e.srcElement).attr("id").split("_")[0]];
            f.filter.direction = $(e.target || e.srcElement).val();

            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        });

        $(".rolling_filter_value").change(function (e) {
            var f = filterMap[$(e.target || e.srcElement).attr("id").split("_")[0]];
            f.filter.value = parseInt($(e.target || e.srcElement).val());

            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        });

        $(".rolling_filter_interval").change(function (e) {
            var f = filterMap[$(e.target || e.srcElement).attr("id").split("_")[0]];
            f.filter.interval = $(e.target || e.srcElement).val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        });

        $(".filter_enabled").change(function (e) {
            var f = filterMap[$(e.target || e.srcElement).attr("id").split("_")[0]];

            f.filter.enabled = $(e.target || e.srcElement).is(":checked");

            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        });

        $(".start_date_filter").datePicker({clickInput: true, startDate: '1900/01/01'}).bind("dateSelected", function (e, selectedDate, td) {
            var z = $(e.target);
            var f = filterMap[z.attr("id").split("_")[0]];
            f.filter.start_date = z.val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        });

        $(".end_date_filter").datePicker({clickInput: true, startDate: '1900/01/01'}).bind("dateSelected", function (e, selectedDate, td) {
            var z = $(e.target);
            var f = filterMap[z.attr("id").split("_")[0]];
            f.filter.end_date = z.val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        });

        $(".adhoc").click(function(e) {
            var z = $(e.target);
            renderReport(reportMap[$(z.parent()).attr("id")], dashboardJSON["id"], true);
        })

        $('a[data-toggle="tab"]').on('shown', function (e) {
            renderReports(stackMap[$(e.target).parent().parent().attr("id")], dashboardJSON["id"], false);
        });
    })
})

