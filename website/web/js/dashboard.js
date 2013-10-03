var stack;
var grid;
var textTemplate;
var reportTemplate;

var gaugeTemplate;

var dashboard;
var short_months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]

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

var beforeRefresh = function (selector) {
    return function () {
        $(".reportArea", selector.parent()).hide();
        $(selector).show();
    }
}

function refreshDataSource(dataSourceID) {
    $("#refreshDiv").show();
    $("#problemHTML").hide();
    $.getJSON('/app/refreshDataSource?urlKey=' + dataSourceID, function (data) {
        var callDataID = data["callDataID"];
        again(callDataID);
    });
}

function again(callDataID) {
    setTimeout(function () {
        $.getJSON('/app/refreshStatus?callDataID=' + callDataID, function (data) {
            onDataSourceResult(data, callDataID);
        });
    }, 5000);
}
function onDataSourceResult(data, callDataID) {
    var status = data["status"];
    if (status == 1) {
        // running
        again(callDataID);
    } else if (status == 2) {
        $("#refreshDiv").hide();
        refreshReport();
    } else {
        $("#refreshDiv").hide();
        $("#problemHTML").show();
        $("#problemHTML").html(data["problemHTML"]);
    }
}

var selectedIndex = function (id) {
    var v = $("#" + id);
    var s = $("#" + id + " > .active");
    for (var i = 0; i < v.children().length; i++) {
        if (v.children()[i] == s[0]) {
            return i;
        }
    }
    return -1;
}

var toFilterString = function (f) {
    var c = {id: f["id"], enabled: f.enabled}
    if (!f.enabled || f.override)
        return $.extend(c, {enabled: false});
    else if (f.type == "single")
        return $.extend(c, {selected: encodeURIComponent(f["selected"])})
    else if (f.type == "multiple")
        return $.extend(c, {selected: $.map(f["selected"],function (e, i) {
            return encodeURIComponent(e);
        }) });
    else if (f.type == "rolling") {
        if (f.interval_type == "18")
            return $.extend(c, {interval_type: f.interval_type, direction: f.direction, value: f.value, interval: f.interval });
        else
            return $.extend(c, {interval_type: f.interval_type});
    } else if (f.type == "date_range") {
        return $.extend(c, {start: f.start_date, end: f.end_date});
    } else if (f.type == "flat_date_month" || f.type == "flat_date_year") {
        return $.extend(c, {selected: f.selected});
    } else if (f.type == "multi_date") {
        return $.extend(c, {start: f.min, end: f.max});
    } else if (f.type == "field_filter") {
        return $.extend(c, {selected: f.selected});
    } else
        return $.extend(c, {enabled: false})
}

var confirmRender = function (o, f) {
    return function (data) {
        var id = o.report.id;
        if ($("#" + id + " :visible").size() == 0) {
            o.rendered = false;
            return;
        } else {
            f(data);
            o.rendered = true;
        }
    }

}

var renderReport = function (o, dashboardID, reload) {
    var obj = o.report.report;
    var id = o.report.id;
    var filterStrings = [];
    var i;
    if (!reload && o.rendered) {
        return;
    }
    if ($("#" + id + " :visible").size() == 0) {
        o.rendered = false;
        return;
    }
    var curFilters = $.map(o.filters, function(e, i) { return toFilterString(e); });
    var fullFilters = {};
    for (i = 0; i < curFilters.length; i++) {
        fullFilters[curFilters[i].id] = curFilters[i];
    }
    beforeRefresh($("#" + id + " .loading"))();
    var postData = {
        url: obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID,
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(fullFilters),
        type: "POST"
    }
    if (obj.metadata.type == "pie") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getPieChartCallback(id, w, {})) }));
//        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), confirmRender(o, Chart.getPieChartCallback(id, w, {})))
    }
    else if (obj.metadata.type == "diagram") {
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), confirmRender(o, function (data) {
            window.drawDiagram(data, $("#" + id + " .reportArea"), obj.id, afterRefresh($("#" + id + " .loading")));
        }))
    }
    else if (obj.metadata.type == "list") {
        $.ajax({
            dataType: "text",
            url: obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"),
            success: confirmRender(o, List.getCallback(id, obj.metadata.properties, obj.metadata.sorting, obj.metadata.columns))
        });
    } else if (obj.metadata.type == "bar") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), confirmRender(o, Chart.getBarChartCallback(id, w, true, obj.metadata.styles)));
    } else if (obj.metadata.type == "column") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), confirmRender(o, Chart.getColumnChartCallback(id, w, obj.metadata.styles)));
    }
    else if (obj.metadata.type == "area" || obj.metadata.type == "bubble" || obj.metadata.type == "plot" || obj.metadata.type == "line") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), confirmRender(o, Chart.getCallback(id, w, true, obj.metadata.styles)));
    } else if (obj.metadata.type == "stacked_bar") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), confirmRender(o, Chart.getStackedBarChart(id, w, obj.metadata.styles)));
    } else if (obj.metadata.type == "stacked_column") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), confirmRender(o, Chart.getStackedColumnChart(id, w, obj.metadata.styles)));
    } else if (obj.metadata.type == "gauge") {
        $("#" + id + " .reportArea").html(gaugeTemplate({id: id, benchmark: null }))
        var v = JSON.stringify(obj.metadata.properties).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), confirmRender(o, Gauge.getCallback(id + "ReportArea", id, w, obj.metadata.max)))
    } else {
        $.get(obj.metadata.url + '?reportID=' + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + "&dashboardID=" + dashboardID + "&" + filterStrings.join("&"), confirmRender(o, function (data) {
            Utils.noData(data, function () {
                $('#' + id + " .reportArea").html(data);
            }, null, id);
        }));
    }
    o.rendered = true;
}

var buildReportGraph = function (obj, filterStack, filterMap, stackMap, reportMap) {
    if (obj.type == "report") {
        var fs3 = filterStack.concat(obj.report.metadata.filters);

        var w = {"type": "report", "filters": fs3, "report": obj, "rendered": false, "id": obj.id };
        reportMap[obj.id] = w;
        var t = _.reduce(obj.report.metadata.filters, function (m, i) {
            m[w.id + "filter" + i.id] = {"filter": i, "parent": w };
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
            m[x.id + "filter" + i.id] = {"filter": i, "parent": x };
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
        var y = {"type": "stack", "children": ch, "id": obj.id };
        stackMap[obj.id] = y;
        var v = _.reduce(obj.filters, function (m, i) {
            m[y.id + "filter" + i.id] = {"filter": i, "parent": y };
            return m;
        }, {});
        $.extend(filterMap, v)
        return y;
    } else {
        return {"type": obj.type};
    }
}

var hideFilter = function (id, filterMap) {
    $(".filter" + id).addClass("hideFilter");
    for (var f in filterMap) {
        if (filterMap[f].filter.id == id) {
            filterMap[f].filter.override = true;
        }
    }
}

var hideFilters = function (obj, filterMap) {
    if (typeof(obj.overrides) != "undefined") {
        for (var i = 0; i < obj.overrides.length; i++)
            hideFilter(obj.overrides[i], filterMap);
    } else if (typeof(obj.report) != "undefined" && typeof(obj.report.overrides) != "undefined") {
        for (var i = 0; i < obj.report.overrides.length; i++)
            hideFilter(obj.report.overrides[i], filterMap);
    }
    if (obj.type == "stack") {
        var ss;
        if (obj.children.length == 1) {
            ss = 0;
        } else {
            ss = selectedIndex(obj.id);
        }
        hideFilters(obj.children[ss], filterMap);
    } else if (obj.type != "report" && obj.type != "text") {
        for (var i = 0; i < obj.children.length; i++) {
            hideFilters(obj.children[i], filterMap);
        }
    }
}

var renderReports = function (obj, dashboardID, force) {
    if (obj.type == "report") {
        if (!obj.report.report.metadata.adhoc_execution)
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
            create: function (obj, parent_id) {
                if (!obj) return;
                if (obj.type == "single")
                    return Filter.single({data: obj, parent_id: parent_id});
                else if (obj.type == "multiple")
                    return Filter.multiple({data: obj, parent_id: parent_id});
                else if (obj.type == "rolling")
                    return Filter.rolling({data: obj, parent_id: parent_id});
                else if (obj.type == "date_range")
                    return Filter.date_range({data: obj, parent_id: parent_id});
                else if (obj.type == "flat_date_month")
                    return Filter.flat_date_month({data: obj, parent_id: parent_id});
                else if (obj.type == "flat_date_year")
                    return Filter.flat_date_year({data: obj, parent_id: parent_id});
                else if (obj.type == "multi_date")
                    return Filter.multi_flat_date_month({data: obj, parent_id: parent_id})
                else if (obj.type == "field_filter")
                    return Filter.field_filter({data: obj, parent_id: parent_id});
            },
            multi_flat_date_month: _.template($("#multi_flat_date_filter", s).html()),
            flat_date_year: _.template($("#flat_date_year_filter", s).html()),
            flat_date_month: _.template($("#flat_date_month_filter", s).html()),
            single: _.template($("#single_value_filter_template", s).html()),
            multiple: _.template($("#multi_value_filter_template", s).html()),
            rolling: _.template($("#rolling_date_filter_template", s).html()),
            date_range: _.template($("#absolute_date_filter_template", s).html()),
            base_filter: _.template($("#filter_base", s).html()),
            field_filter: _.template($("#field_filter_template", s).html()),
            filters: _.template($("#filters_template", s).html())

        };
        dashboard = _.template($("#dashboard_template", s).html());
        stack = _.template($("#stack", s).html());
        grid = _.template($("#grid", s).html());
        reportTemplate = _.template($("#report_template", s).html());
        textTemplate = _.template($("#text_template", s).html());
        gaugeTemplate = _.template($("#gauge_template", s).html());

        $("#base").append(dashboard(dashboardJSON));

        $(".dashboardStackNav").css("background-color", dashboardJSON["styles"]["alternative_stack_start"])
        $(".dashboard_base > .row > .col-md-12 > .tabbable > .nav-pills").css("background-color", dashboardJSON["styles"]["main_stack_start"])

        var filterMap = _.reduce(dashboardJSON["filters"], function (m, i) {
            m["filter" + i.id] = {"filter": i, "parent": null };
            return m;
        }, {});
        var stackMap = {};
        var reportMap = {};
        var graph = buildReportGraph(dashboardJSON["base"], dashboardJSON["filters"], filterMap, stackMap, reportMap);

        hideFilters(graph, filterMap);
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

        $(".month_select, .year_select").change(function (e) {
            var f = filterMap[$(e.target).attr("id")];
            f.filter.selected = $(e.target).val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        })

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
            var a = $(e.target || e.srcElement).parent().parent().parent().parent();
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

        $(".multi_flat_month_save").click(function (e) {
            var a = $(e.target).parent().parent().parent().parent();
            var f = filterMap[a.attr("id").split("_")[0]];
            var min = $(".multi_flat_month_start", a).val();
            var max = $(".multi_flat_month_end", a).val();
            f.filter.min = parseInt(min);
            f.filter.max = parseInt(max);
            $("#" + a.attr("id").split("_")[0]).html(short_months[f.filter.min] + " to " + short_months[f.filter.max]);
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], true);
            }
        })

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

        $(".field_filter").change(function (e) {
            var f = filterMap[$(e.target).attr("id").split("_")[0]];
            f.filter.selected = $(e.target).val();
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

        $(".adhoc").click(function (e) {
            var z = $(e.target);
            $(".adhoc_instructions", z.parent()).hide();
            renderReport(reportMap[$(z.parent()).attr("id")], dashboardJSON["id"], true);
        })

        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {

            $(".filter").removeClass("hideFilter");
            var q = $(e.target).parent().parent();
            var s = stackMap[q.attr("id")];
            var t = q.children();
            var selected = -1;
            for (var f in filterMap) {
                filterMap[f].filter.override = false;
            }

            hideFilters(graph, filterMap);

            renderReports(s, dashboardJSON["id"], false);
        });
        var showFilters = true;
        $('.toggle-filters').click(function (e) {
            if (showFilters) {
                $(".filters").hide();
            } else {
                $(".filters").show();
            }
            showFilters = !showFilters;
        })
    })
})

