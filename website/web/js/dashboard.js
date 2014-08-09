$.holdReady(true);
Modernizr.load({ complete: function () {
    $.holdReady(false);
}});

var stack;
var grid;
var textTemplate;
var reportTemplate;
var fullReportTemplate;

var gaugeTemplate;
var d3Template;
var configurationDropdownTemplate;
var reportListTemplate;

var dashboard;
var email_modal;

var currentReport;

var multi_value_results;
var multi_field_value_results;

var saveConfiguration;
var startFullRenderPDF;

var busyIndicator;
busyIndicator = busyIndicator || (function () {

    return {
        showPleaseWait: function() {
            $("#pleaseWaitProcessingMessage").html("Generating PDF...");
            $("#pleaseWaitDialog").modal(true, true, true);
        },
        hidePleaseWait: function () {
            $("#pleaseWaitDialog").modal('hide');
            $("#pleaseWaitProcessingMessage").html("Processing...");
        }

    };
})();

function captureAndReturn(o) {
    var obj = o.report.report;
    if (obj.metadata.type == "list" || obj.metadata.type == "crosstab" || obj.metadata.type == "trend_grid" || obj.metadata.type == "tree" || obj.metadata.type == "form" ||
        obj.metadata.type == "ytd_definition" || obj.metadata.type == "compare_years" || obj.metadata.type == "trend") {
        return null;
    } else {
        var id = o.report.id;
        var svg = $("#d3Div" + id);
        var h = svg.height();
        var w = svg.width();
        var imageData = {};
        imageData.height = h;
        imageData.width = w;
        return imageData;
    }

}

function capture(id) {
    var svg = $("#d3Div" + id);
    var h = svg.height();
    var w = svg.width();
    var imageData = {};
    imageData.height = h;
    imageData.width = w;
    return imageData;
}

function drillThrough(params) {
    if(typeof(userJSON.embedKey) != "undefined")
        params["embedKey"] = userJSON.embedKey;
    if(typeof(userJSON.embedded) != "undefined")
        params["embedded"] = userJSON.embedded;
    $.ajax( {
        dataType: "json",
        url: '/app/drillThrough',
        data: JSON.stringify(params),
        success: function(data) {
            var url = data["url"];
            window.location.href = url;
        },
        error: function(a, b, c) {
            window.location.href = "/app/serverError.jsp"
        },
        contentType: "application/json; charset=UTF-8",
        type: "POST"
});
}

function drillThroughParameterized(params, val) {
    var first = true;
    var s = "";
    var p = function(p, param) {
        if(params[param] != null) {
            s = s + (first ? "" : "&") + p + "=" + params[param];
            first = false;
        }
    }
    p("embedded", "embedded");
    p("drillthroughID", "id");
    p("reportID", "reportID");
    p("sourceField", "source");
    if(val != null) {
        if(!first) {
            s = s + "&";
            first = false;
        }
        s = s + "f" + params["xaxis"] + "=" + encodeURI(val);
    }
    drillThrough(s);
}

var millisecondFormatter = function(format, val) {
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
}

var dashboardComponent = function (obj) {
    if (obj.type == "stack")
        return stack(obj);
    else if (obj.type == "grid") {
        return grid(obj);
    } else if (obj.type == "text") {
        return textTemplate(obj);
    } else if (obj.type == "report") {
        return reportTemplate({data: obj });
    }
}

function toSelectedArray(selection) {
    var m = _.map(selection, function(e, i, l) { return { n: i, s: e }; })
       var n = _.select(m, function(e, i, l) { return e.s });
        var o = _.map(n, function(e, i, l) { return e.n });

    return o;
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
        again(callDataID, null);
    });
}

function again(callDataID, message) {
    if (message != null) {
        $("#messageDiv").html(message);
    }
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
        var message = data["statusMessage"];
        again(callDataID, message);
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
    var c = v.children();
    for (var i = 0; i < c.length; i++) {
        if (c[i] == s[0]) {
            return i;
        }
    }
    return -1;
}

var toFilterString = function (f, store) {
    var c = {id: f["id"], enabled: f.enabled}
    if (!f.enabled || (!store && f.override)) {
        $.extend(c, {enabled: false});
        if (!store)
            return c;
    }
    if (f.type == "single")
        return $.extend(c, {selected: f["selected"]})
    else if (f.type == "multiple")
        return $.extend(c, {selected: f["selected"] });
    else if (f.type == "rolling") {
        if (f.interval_type == "18")
            return $.extend(c, {interval_type: f.interval_type, direction: f.direction, value: f.value, interval: f.interval });
        else
            return $.extend(c, {interval_type: f.interval_type});
    } else if (f.type == "date_range") {
        return $.extend(c, {start: f.start, end: f.end});
    } else if (f.type == "flat_date_month" || f.type == "flat_date_year") {
        return $.extend(c, {selected: f.selected});
    } else if (f.type == "multi_date") {
        return $.extend(c, {start: f.start, end: f.end});
    } else if (f.type == "field_filter") {
        return $.extend(c, {selected: f.selected});
    } else if (f.type == "multi_field_filter") {
        return $.extend(c, {selected: f.selected});
    } else if (f.type == "pattern_filter") {
        return $.extend(c, {pattern: f.pattern});
    } else
        return c;
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

var toPDF = function (o, dashboardID, drillthroughID) {
    var obj = o.report.report;
    var id = o.report.id;
    if (obj.metadata.type == "list" || obj.metadata.type == "crosstab" || obj.metadata.type == "trend_grid" || obj.metadata.type == "tree" || obj.metadata.type == "form" ||
        obj.metadata.type == "compare_years" || obj.metadata.type == "ytd_definition") {
        var i;
        if ($("#" + id + " :visible").size() == 0) {
            o.rendered = false;
            return;
        }
        var curFilters = $.map(o.filters, function (e, i) {
            return toFilterString(e, false);
        });
        var fullFilters = {};
        for (i = 0; i < curFilters.length; i++) {
            fullFilters[curFilters[i].id] = curFilters[i];
        }
        //beforeRefresh($("#" + id + " .loading"))();
        $.ajax({
            url: "/app/htmlPDF" + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset(),
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(fullFilters),
            error: function() {
                alert("Something went wrong in trying to export to PDF.");
            },
            success: function(data) {
                window.location.href = "/app/pdf?urlKey="+data["urlKey"];
            },
            type: "POST"
        });
    } else {
        capture(id);
    }
}



var toExcel = function (o, dashboardID, drillthroughID) {
    var obj = o.report.report;
    var id = o.report.id;
    var i;
    if ($("#" + id + " :visible").size() == 0) {
        o.rendered = false;
        return;
    }
    var curFilters = $.map(o.filters, function (e, i) {
        return toFilterString(e, false);
    });
    var fullFilters = {};
    for (i = 0; i < curFilters.length; i++) {
        fullFilters[curFilters[i].id] = curFilters[i];
    }
    //beforeRefresh($("#" + id + " .loading"))();
    $.ajax({
        url: "/app/htmlExcel" + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset(),
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(fullFilters),
        error: function() {
            alert("Something went wrong in trying to export to Excel.");
        },
        success: function(data) {
            window.location.href = "/app/excel?urlKey="+data["urlKey"];
        },
        type: "POST"
    });
}

var handleFullFilters = function (fullFilters) {

    return function () {
        return fullFilters;
    }
}

var renderReport = function (o, dashboardID, drillthroughID, reload) {
    var obj = o.report.report;
    var id = o.report.id;
    var filterStrings = [];
    var i;
    if (!reload && o.rendered) {
        return;
    }
    if( $("#" + id).offset().top <= 0 ) {
        o.rendered = false;
        return;
    }
    var curFilters = $.map(o.filters, function (e, i) {
        return toFilterString(e, false);
    });
    var fullFilters = {};
    for (i = 0; i < curFilters.length; i++) {
        fullFilters[curFilters[i].id] = curFilters[i];
    }
    fullFilters = handleFullFilters(fullFilters)();
    beforeRefresh($("#" + id + " .loading"))();
    var embedComponent = typeof(userJSON.embedKey) != "undefined" ? ("&embedKey=" + userJSON.embedKey) : "";
    var embedded = typeof(userJSON.embedded) != "undefined" ? ("&embedded=true") : "";
    var iframeKey = typeof(userJSON.iframeKey) != "undefined" ? ("&iframeKey=" + userJSON.iframeKey) : "";
    var dashboardComponent = dashboardID == -1 ? "" : ("&dashboardID=" + dashboardID);
    var drillthroughComponent = typeof(drillthroughID) != "undefined" ? ("&drillThroughKey=" + drillthroughID) : "";
    var postData = {
        url: obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + dashboardComponent + drillthroughComponent + embedComponent + embedded + iframeKey,
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(fullFilters),
        error: function() {
            $("#" + id + " .loading").hide();
            $("#" + id + " .reportError").show();
        },
        type: "POST"
    }

    if (obj.metadata.type == "pie") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getD3PieChartCallback(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID))}));
    } else if (obj.metadata.type == "serverList") {
        var p = postData.url.substr(obj.metadata.url.length);
        AsyncList.createTable(id, obj.metadata.properties, obj.metadata.columnData, JSON.stringify(fullFilters), obj.metadata.tableHTML, p, obj.metadata.uid, fullFilters, drillthroughID);
    } else if (obj.metadata.type == "diagram") {
        $.ajax($.extend(postData, {success: confirmRender(o, function (data) {
            window.drawDiagram(data, $("#" + id + " .reportArea"), obj.id, typeof(userJSON.embedded) != "undefined" ? userJSON.embedded : false, afterRefresh($("#" + id + " .loading"), fullFilters, drillthroughID));
        }) }));
    }
    else if (obj.metadata.type == "list" || obj.metadata.type == "trend_grid") {
        $.ajax($.extend(postData, {
            dataType: "text",
            success: confirmRender(o, List.getCallback(id, obj.metadata.properties, obj.metadata.sorting, obj.metadata.columns, fullFilters, drillthroughID))
        }));
    } else if (obj.metadata.type == "bar") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getD3BarChartCallback(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID))}));
    } else if (obj.metadata.type == "column") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getD3ColumnChartCallback(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID))}));
    } else if (obj.metadata.type == "plot" || obj.metadata.type == "bubble") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getD3ScatterCallback(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID))}));
    } else if (obj.metadata.type == "line") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getD3LineCallback(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID))}));
    } else if (obj.metadata.type == "area") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getD3AreaCallback(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID))}));
    } else if (obj.metadata.type == "topomap") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Map.getMap(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID, iframeKey != ""))}));
    } else if (obj.metadata.type == "stacked_bar") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getD3StackedBarChart(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID))}));
    } else if (obj.metadata.type == "stacked_column") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getD3StackedColumnChart(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID))}));
    } else if (obj.metadata.type == "bullet") {
        $("#" + id + " .reportArea").html(d3Template({id: id}));
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getBulletChartCallback(id, obj.metadata.parameters, true, obj.metadata.styles, fullFilters, drillthroughID))}));
    } else if (obj.metadata.type == "gauge") {
        $("#" + id + " .reportArea").html(gaugeTemplate({id: id, benchmark: obj.metadata.benchmark }));
        $.ajax($.extend(postData, {success: confirmRender(o, Gauge.getCallback(id + "ReportArea", id))}));
    } else {
        $.ajax($.extend(postData, {success: confirmRender(o, function (data) {
            Utils.noData(data, function () {
                $('#' + id + " .reportArea").html(data);
            }, null, id);
            $('#' + id + " .reportArea .list_drillthrough").click(function(e) {
                e.preventDefault();
                var x = $(e.target);
                var f = {"reportID": x.data("reportid"), "drillthroughID": x.data("drillthroughid"), "embedded": x.data("embedded"), "source": x.data("source"), "drillthroughKey": drillthroughID, "filters": fullFilters,
                    "drillthrough_values": {}};
                f["drillthrough_values"] = _.inject(x.data(), function(m, e, i, l) {

                    if(i.match(/^drillthrough/))
                        m[i.replace(/^drillthrough/, "")] = decodeURI(e);
                    return m; },
                    {});

                drillThrough(f);
            })
        })}));

    }
    o.rendered = true;
}

function flattenFilters(filters) {
    return _.flatten(_.map(filters, function (e, i, l) {
        if (e.type == "or_filter") {
            return e.filters;
        } else {
            return e;
        }
    }))
}

var buildReportGraph = function (obj, filterStack, filterMap, stackMap, reportMap) {
    if (obj.type == "report") {
        var ff1 = flattenFilters(obj.report.metadata.filters);
        var fs3 = filterStack.concat(ff1);

        var w = {"type": "report", "filters": fs3, "report": obj, "rendered": false, "id": obj.id, parent_filters: filterStack.concat() };

        reportMap[obj.id] = w;
        var t = _.reduce(ff1, function (m, i) {
            m[w.id + "_report_filter_" + i.id] = {"filter": i, "parent": w };
            return m;
        }, {});
        w["base_map"] = t;
        $.extend(filterMap, t);
        return w;
    } else if (obj.type == "grid") {
        var children = [];
        var ff2 = flattenFilters(obj.filters);
        var fs1 = filterStack.concat(ff2);
        for (var i = 0; i < obj.grid.length; i++) {
            for (var j = 0; j < obj.grid[i].length; j++) {
                children = _.flatten(children.concat(buildReportGraph(obj.grid[i][j], fs1, filterMap, stackMap, reportMap)));
            }
        }
        var x = {"type": "grid", "children": children };
        var u = _.reduce(ff2, function (m, i) {
            m[x.id + "_grid_filter_" + i.id] = {"filter": i, "parent": x };
            return m;
        }, {});
        $.extend(filterMap, u)
        return x;
    } else if (obj.type == "stack") {
        var ff3 = flattenFilters(obj.filters);
        var fs2 = filterStack.concat(ff3);
        var ch = [];
        for (var k = 0; k < obj.stack_items.length; k++) {
            ch = _.flatten(ch.concat(buildReportGraph(obj.stack_items[k].item, fs2, filterMap, stackMap, reportMap)))
        }
        var y = {"type": "stack", "children": ch, "id": obj.id, "data": obj };
        stackMap[obj.id] = y;
        var v = _.reduce(ff3, function (m, i) {
            m[y.id + "_stack_filter_" + i.id] = {"filter": i, "parent": y };
            return m;
        }, {});
        $.extend(filterMap, v)
        return y;
    } else {
        return {"type": obj.type};
    }
}

var hideFilter = function (id, filterMap) {
    $("._dashboard_filter_" + id).addClass("hideFilter");
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

var renderReports = function (obj, dashboardID, drillthroughID, force) {
    if (obj.type == "report") {
        if (!obj.report.report.metadata.adhoc_execution)
            renderReport(obj, dashboardID, drillthroughID, force);
    } else if (obj.type != "text") {
        for (var i = 0; i < obj.children.length; i++) {
            renderReports(obj.children[i], dashboardID, drillthroughID, force);
        }
    }
}

var fullRenderPDF = function (obj, dashboardID, drillthroughID, pdfData) {

    if (obj.type == "report") {
        pdfData[obj.id] = captureAndReturn(obj);
    } else if (obj.type != "text") {
        for (var i = 0; i < obj.children.length; i++) {
            fullRenderPDF(obj.children[i], dashboardID, drillthroughID, pdfData);
        }
    }

}

var renderExcel = function (obj, dashboardID, drillthroughID, force) {
    if (obj.type == "report") {
        toExcel(obj, dashboardID, drillthroughID);
    }
}

var renderPDF = function (obj, dashboardID, drillthroughID, force) {
    if (obj.type == "report") {
        toPDF(obj, dashboardID, drillthroughID);
    }
}

$(function () {
    Date.firstDayOfWeek = 0;
    Date.format = 'yyyy/mm/dd';
    $.get("/js/template.html", function (data) {
        var s = $(data);
        Filter = {
            create: function (obj, parent_id, type) {
                var c;
                if (!obj) return;
                if (obj.type == "single")
                    c = Filter.single;
                else if (obj.type == "multiple")
                    c = Filter.multiple;
                else if (obj.type == "rolling")
                    c = Filter.rolling;
                else if (obj.type == "date_range")
                    c = Filter.date_range;
                else if (obj.type == "flat_date_month")
                    c = Filter.flat_date_month;
                else if (obj.type == "flat_date_year")
                    c = Filter.flat_date_year;
                else if (obj.type == "multi_date")
                    c = Filter.multi_flat_date_month;
                else if (obj.type == "field_filter")
                    c = Filter.field_filter;
                else if (obj.type == "multi_field_filter")
                    c = Filter.multiple;
                else if (obj.type == "pattern_filter")
                    c = Filter.pattern_filter;
                else if (obj.type == "or_filter")
                    c = Filter.or_filter;
                else
                    c = Filter.generic_filter;
                return c({data: obj, parent_id: parent_id, type: type});
            },
            label: function(data, parent_id, type) {
                return ((typeof(parent_id) == 'undefined') ? '' : parent_id) + "_" + type + "_filter_" + data.id;
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
            pattern_filter: _.template($("#pattern_filter_template", s).html()),
            or_filter: _.template($("#or_filter_template", s).html()),
            generic_filter: _.template($("#generic_filter", s).html()),
            filters: _.template($("#filters_template", s).html())

        };
        dashboard = _.template($("#dashboard_template", s).html());
        stack = _.template($("#stack", s).html());
        grid = _.template($("#grid", s).html());
        reportTemplate = _.template($("#report_template", s).html());
        fullReportTemplate = _.template($("#report_full_template", s).html());
        textTemplate = _.template($("#text_template", s).html());
        gaugeTemplate = _.template($("#gauge_template", s).html());
        d3Template = _.template($("#d3_template", s).html());
        email_modal = _.template($("#email_modal", s).html());
        multi_value_results = _.template($("#multi_value_results_template", s).html());
        multi_field_value_results = _.template($("#multi_value_results_template", s).html());
        configurationDropdownTemplate = _.template($("#configuration_dropdown_template", s).html());
        reportListTemplate = _.template($("#report_list_template", s).html());

        var filterMap = _.reduce(flattenFilters(dashboardJSON["filters"]), function (m, i) {
            m["_dashboard_filter_" + i.id] = {"filter": i, "parent": null };
            return m;
        }, {});
        var stackMap = {};
        var reportMap = {};
        var graph = buildReportGraph(dashboardJSON["base"], flattenFilters(dashboardJSON["filters"]), filterMap, stackMap, reportMap);
        var dashboardKey = (dashboardJSON["id"] != -1) ? ("dashboard " + dashboardJSON["id"]) : ("report " + dashboardJSON["base"]["id"]);
        var saveFilter;
        var saveStack;
        var saveReport;
        var filtersFromLocalStorage;

        var f;
        var filterNameMap = {};
        var createFilterNameKey = function(base, name) {
            var s = "";
            var c = base.split("_");
            var n;
            for(n in c) {
                if(c[n] == "filter")
                    break;
                s = s + c[n] + "_"
            }
            s = s + name;
            return s;
        }
        for(f in filterMap) {
            if(filterMap[f].filter.name) {
                var curName = createFilterNameKey(f, filterMap[f].filter.name);
                filterNameMap[curName] = filterMap[f];
            }
        }
        for(f in filterMap) {
            if(filterMap[f].filter.parents && filterMap[f].filter.parents.length > 0) {
                filterMap[f].parent_filters = [];
                var cur;
                for(cur = 0;cur < filterMap[f].filter.parents.length;cur++) {
                    var s = createFilterNameKey(f, filterMap[f].filter.parents[cur]);
                    filterMap[f].parent_filters = filterMap[f].parent_filters.concat(filterNameMap[s]);
                }
            }
        }

        if(typeof(userJSON) != "undefined")
            $("#configuration-dropdown").html(configurationDropdownTemplate({"dashboard": dashboardJSON, "user": userJSON}))

        if (Modernizr.localstorage && dashboardJSON["local_storage"] && location.pathname.match(/^\/app\/html\/(report|dashboard)\/[a-zA-Z0-9]+$/)) {
            if (typeof(localStorage[dashboardKey]) != "undefined") {
                var cur;
                var vals = JSON.parse(localStorage[dashboardKey]);
                var filters = vals["filters"];
                var reports = vals["reports"];
                for(cur in reports) {
                    loadReport(reports[cur], cur);
                }
                for (cur in filters) {
                    if (typeof(filterMap[cur]) != "undefined" && typeof(filters[cur]) != "undefined")
                        $.extend(filterMap[cur].filter, filters[cur]);
                }
                if (typeof(vals["stacks"]) != "undefined")
                    $.extend(true, stackMap, vals["stacks"])
            }
            saveFilter = function (f, key) {
                if (localStorage[dashboardKey] == null) {
                    localStorage[dashboardKey] = JSON.stringify({});
                }
                var report = JSON.parse(localStorage[dashboardKey]);
                if (typeof(report["filters"]) == "undefined") {
                    report["filters"] = {};
                }
                report["filters"][key] = toFilterString(f.filter, true);
                localStorage[dashboardKey] = JSON.stringify(report);
            }
            $(".restore_default_config").click(function (e) {
                delete localStorage[dashboardKey];
            })
            saveStack = function (k) {
                var i = selectedIndex(k);
                if (localStorage[dashboardKey] == null) {
                    localStorage[dashboardKey] = JSON.stringify({});
                }
                var report = JSON.parse(localStorage[dashboardKey]);
                if (typeof(report["stacks"]) == "undefined") {
                    report["stacks"] = {};
                }
                report["stacks"][k] = {"data": { "selected": i } };
                localStorage[dashboardKey] = JSON.stringify(report);
            }

            saveReport = function(dashboard_report_id, report_id) {
                if(localStorage[dashboardKey] == null) {
                    localStorage[dashboardKey] = JSON.stringify({});
                }
                var report = JSON.parse(localStorage[dashboardKey]);
                if(typeof(report["reports"]) == "undefined") {
                    report["reports"] = {};
                }
                report["reports"][report_id] = dashboard_report_id;
                localStorage[dashboardKey] = JSON.stringify(report);
            }

            filtersFromLocalStorage = function(a) {
                var vals = JSON.parse(localStorage[dashboardKey]);
                var filters = vals["filters"];
                if(typeof(filters) != "undefined") {
                    for (cur in a) {
                        if (typeof(filters[cur]) != "undefined" && typeof(a[cur]) != "undefined")
                            $.extend(a[cur].filter, filters[cur]);
                    }
                }
                return a;
            }

        } else {
            saveFilter = function (f, key) {
            };
            saveStack = function (k) {
            };
            saveReport = function(a, b) { }
            filtersFromLocalStorage = function(a) { return a; }
        }


        $("#base").append(dashboard(dashboardJSON));

        $(".dashboardReportHeader").css("background-color", dashboardJSON["styles"]["report_header_background"]);
        $(".dashboardReportHeader").css("color", dashboardJSON["styles"]["report_header_text"]);
        $(".dashboardStackNav").css("background-color", dashboardJSON["styles"]["alternative_stack_start"]);
        $(".dashboardStackNav").addClass("dashboardStackPill");
        $(".dashboard_base > .row > .col-md-12 > .tabbable > .nav-pills").css("background-color", dashboardJSON["styles"]["main_stack_start"]);

        hideFilters(graph, filterMap);
        renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], false);

        var selectionMap = {};
        function addFilterCallbacks(target) {
            $(".single_filter", target).change(function (e) {
                var k = $(e.target).attr("id");
                var f = filterMap[k];
                f.filter.selected = $(e.target).val();
                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".month_select, .year_select", target).change(function (e) {
                var k = $(e.target).attr("id");
                var f = filterMap[k];
                f.filter.selected = $(e.target).val();
                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            })

            var allCheck = function (e) {
                var a = $(e.target).parent().parent().parent().parent().parent().parent().parent();

                var k = a.attr("id").replace(/_modal$/g, "");
                var selMap = selectionMap[k];
                if ($(e.target).is(":checked")) {
                    selMap["All"] = true;
                    $(".cb_filter_choice", $(e.target).parent().parent()).prop('checked', true);
                    for(i in selMap)
                        selMap[i] = true;
                } else {
                    selMap["All"] = false;
                    $(".cb_filter_choice", $(e.target).parent().parent()).removeAttr("checked", "checked");
                    for(i in selMap)
                        selMap[i] = false;
                }
            }

            var choiceAllCheck = function (e) {
                var a = $(e.target).parent().parent().parent().parent().parent().parent().parent();
                var k = a.attr("id").replace(/_modal$/g, "");
                var selMap = selectionMap[k];

                var checked = $(e.target).is(":checked");
                selMap[$(".cb_filter_value", $(e.target).parent()).html()] = checked;
                /*if (checked) {
                    var allSelected = _.all(selectionMap, function(e, i, l) {
                        return (i == "All") || e;
                    });
                    if (allSelected) {

                    }
                }*/
                if (checked && _.all(selMap, function(e, i, l) {
                    return (i == "All") || e; })) {
                    $(".cb_all_choice", $(e.target).parent().parent()).prop('checked', true);
                    selMap["All"] = true;
                } else {
                    $(".cb_all_choice", $(e.target).parent().parent()).removeAttr("checked");
                    selMap["All"] = false;
                }
            };

            $('.multi_value_modal', target).on('show.bs.modal', function (e) {
                var target = $(e.target).attr("id").replace(/_modal$/g, "");
                var ff = filterMap[$(e.target).attr("id").replace(/_modal$/g, "")];
                var f = ff.filter;

                var curFilterInfo = {id: f.id, parents: _.map(ff.parent_filters ? ff.parent_filters : [], function(e, i, d) {
                    return toFilterString(e.filter, false);
                })};
                var postData = {
                    url: "/app/html/filterValue",
                    contentType: "application/json; charset=UTF-8",
                    data: JSON.stringify(curFilterInfo),
                    type: "POST"
                }
                if(f.error || (ff.parent_filters && ff.parent_filters.length > 0)) {
                    $(".loading-bar", e.target).show();
                    $.ajax($.extend(postData, { success: function(d) {
                        $(".loading-bar", e.target).hide();
                        f.values = d.values;
                        var m = _.reduce(f.values, function(m, e) {
                            m[e == "" ? "[ No Value ]" : e] = f.selected["All"];
                            return m;
                        }, {});
                        var selMap = {};
                        selectionMap[target] = selMap;
                        if (f.selected["All"]) {
                            selMap["All"] = true;
                        }
                        for (var mo in m) {
                            var selected = f.selected[mo];
                            if (selected) {
                                selMap[mo] = true;
                            } else {
                                selMap[mo] = false;
                            }
                        }
                        if(d.values.length > 100) {
                            d.error = "Too many values, please refine your search."
                        } else {
                            $(".multi-value-list", $(e.target)).html(multi_value_results({ data: { selected: selMap }, results: d }));
                            $(".cb_all_choice", $(e.target)).click(allCheck);
                            $(".cb_filter_choice", $(e.target)).click(choiceAllCheck);
                        }
                        delete f.error;
                    } }))
                } else {
                    var m = _.reduce(f.values, function(m, e) {
                                    m[e == "" ? "[ No Value ]" : e] = f.selected["All"];
                                    return m;
                                }, {});
                    var selMap = {};
                    selectionMap[target] = selMap;
                    for (var mo in m) {
                        var selected = f.selected[mo];
                        if (selected) {
                            selMap[mo] = true;
                        } else {
                            selMap[mo] = false;
                        }
                    }
                }
            });

            $('.multi_field_value_modal', target).on('show.bs.modal', function (e) {
                var target = $(e.target).attr("id").replace(/_modal$/g, "");
                var f = filterMap[$(e.target).attr("id").replace(/_modal$/g, "")].filter;

                var m = _.reduce(f.values, function(m, e) {
                    m[e == "" ? "[ No Value ]" : e] = f.selected["All"];
                    return m;
                }, {});
                selectionMap[f.id] = $.extend({}, m, f.selected);
            });

            $(".cb_all_choice", target).click(allCheck);

            $(".cb_filter_choice", target).click(choiceAllCheck);

            $(".multi_value_save", target).click(function (e) {
                var a = $(e.target).parent().parent().parent().parent();
                var k = a.attr("id").replace(/_modal$/g, "");
                var f = filterMap[k];
                var selMap = selectionMap[k];
                f.filter.selected = selMap;
                var selectVals = toSelectedArray(selMap);
                var label;
                if(selMap["All"]) {
                    label = "All";
                } else {
                    label = selectVals.length == 1 ? selectVals[0] : selectVals.length + " Items";
                }
                $(".multi_filter", $(a).parent()).html(label);

                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".multi_flat_month_save", target).click(function (e) {
                var a = $(e.target).parent().parent().parent().parent();
                var k = a.attr("id").replace(/_modal$/g, "");
                var f = filterMap[k];
                var min = $(".multi_flat_month_start", a).val();
                var max = $(".multi_flat_month_end", a).val();
                f.filter.start = parseInt(min);
                f.filter.end = parseInt(max);
                var startLabel = f.filter.lookup[min];
                var endLabel = f.filter.lookup[max];
                $("#" + a.attr("id").replace(/_modal$/g, "")).html(startLabel + " to " + endLabel);
                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".rolling_filter_type", target).change(function (e) {
                var t = $(".custom", $(e.target).parent());
                var k = $(e.target).attr("id").replace(/_type$/g, "");
                var f = filterMap[k];
                if ($(e.target).val() == "18") {
                    t.show();

                } else {
                    t.hide();
                }
                f.filter.interval_type = $(e.target).val();
                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".rolling_filter_direction", target).change(function (e) {
                var k = $(e.target).attr("id").replace(/_custom_direction$/g, "");
                var f = filterMap[k];
                f.filter.direction = $(e.target).val();

                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }

                saveFilter(f, k);
            });

            $(".rolling_filter_value", target).change(function (e) {
                var k = $(e.target).attr("id").replace(/_custom_value$/g, "")
                var f = filterMap[k];
                f.filter.value = parseInt($(e.target).val());

                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".rolling_filter_interval", target).change(function (e) {
                var k = $(e.target).attr("id").replace(/_custom_interval$/g, "")
                var f = filterMap[k];
                f.filter.interval = $(e.target).val();
                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".field_filter", target).change(function (e) {
                var k = $(e.target).attr("id")
                var f = filterMap[k];
                f.filter.selected = $(e.target).val();
                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".filter_enabled", target).change(function (e) {
                var k = $(e.target).attr("id").replace(/_enabled$/g, "");
                var f = filterMap[k];


                f.filter.enabled = $(e.target).is(":checked");

                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".start_date_filter", target).datePicker({clickInput: true, startDate: '1900/01/01'}).bind("dateSelected", function (e, selectedDate, td) {
                var z = $(e.target);
                var k = z.attr("id").replace(/_date_start$/, "");
                var f = filterMap[k];
                f.filter.start = z.val();
                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".end_date_filter", target).datePicker({clickInput: true, startDate: '1900/01/01'}).bind("dateSelected", function (e, selectedDate, td) {
                var z = $(e.target);
                var k = z.attr("id").replace(/_date_end$/, "");
                var f = filterMap[k];
                f.filter.end = z.val();
                if (f.parent == null) {
                    renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                } else {
                    renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                }
                saveFilter(f, k);
            });

            $(".adhoc", target).click(function (e) {
                var z = $(e.target);
                $(".adhoc_instructions", z.parent()).hide();
                renderReport(reportMap[$(z.parent()).attr("id")], dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            })

            $(".dashboardReportHeader", target).click(function (e) {
                var f = $(".reportMenu", $(e.target).parent());
                f.slideToggle({ complete: function () {
                    f.css("overflow", "visible")
                }});
            });

            function filterText(e) {
                var p = $(e.target).parent();
                while (!p.hasClass("input-group")) p = p.parent();
                var val = $(".value-search-input", p).val();
                var modalParent = p.parent().parent().parent().parent().parent();
                var f = filterMap[modalParent.attr("id").replace(/_modal$/g, "")];

                var d = {values: _.filter(f.filter.values, function(e, i, l) {
                    return e.toLowerCase().match(val.toLowerCase()); })}
                if(d.values.length > 100) {
                    d["error"] = "Too many values, please refine your search."
                }
    //            $.getJSON("/app/html/filterValue?filterID=" + f.filter.id + "&q=" + encodeURIComponent(val), function (d) {
                    $(".multi-value-list", modalParent).html(multi_value_results({ data: { selected: selectionMap }, results: d }));
                    $(".cb_all_choice", modalParent).click(allCheck);
                    $(".cb_filter_choice", modalParent).click(choiceAllCheck);
    //            });
            }

            $(".value-search-btn", target).click(filterText);
            $(".value-search-input", target).change(filterText);

            $(".pattern_input", target).blur(function (e) {
                var k = $(e.target).attr("id");
                var f = filterMap[k];
                var p = $(e.target).val();
                if (p != f.filter.pattern) {
                    f.filter.pattern = p;
                    if (f.parent == null) {
                        renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                    } else {
                        renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
                    }
                    saveFilter(f, k);
                }

            })

            $("#embedDashboardWidth").change(function(e) {
                var width = $("#embedDashboardWidth").val();
                var height = $("#embedDashboardHeight").val();

                $("#embedDashboardURL").text("<iframe width=\""+width+"\" height=\"500\" src=\"https://www.easy-insight.com/app/html/dashboard/" + dashboardJSON["key"] + "/embed\"></iframe>");
            });

            $("#embedDashboardHeight").change(function(e) {
                var width = $("#embedDashboardWidth").val();
                var height = $("#embedDashboardHeight").val();
                $("#embedDashboardURL").text("<iframe width=\""+width+"\" height=\"500\" src=\"https://www.easy-insight.com/app/html/dashboard/" + dashboardJSON["key"] + "/embed\"></iframe>");
            });

            $(".report-emailReportButton", target).click(function (e) {
                $("#emailReportWindow").modal(true, true, true);
                e.preventDefault();
            })

            $(".report-embedReportButton", target).click(function (e) {
                $("#embedReportWindow").modal(true, true, true);
                e.preventDefault();
            })

            $(".report-dropdown-btn", target).on("show.bs.dropdown", function(e) {
                var c = $(e.target);
                var a = c.parent();
                while(typeof(a.attr("data-report-target")) == "undefined") {
                    a = a.parent();
                }
                var cc = a.attr("data-report-target");
                var component = (typeof(reportMap[cc].report.tag) != "undefined") ? ("&tagName=" + reportMap[cc].report.tag) : "";
                $.getJSON("/app/html/reportsByTag?dataSource=" + dashboardJSON["data_source_id"] + component, function(data) {
                    $(".report-dropdown", c).html(reportListTemplate(data))
                    $(".report-target-link", c).click(function(e) {



                        var b = $(e.target).attr("data-report-target");
                        loadReport(b, cc);
                        saveReport(b, cc);
                        e.preventDefault();
                    })
                })
            })

        }

        addFilterCallbacks($(document));

        $(".save-as-config-btn").click(function(e) {
            var v = $("#save-as-config-name").val();
            if(v == "") {

            } else {
                saveConfiguration(v);
                $("#save-as-modal").modal('hide');
            }

        })

        $(".save-current-configuration").click(function(e) {
            saveConfiguration(dashboardJSON["configuration_name"], dashboardJSON["configuration_key"]);
            e.preventDefault();
        })



        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $(".filter").removeClass("hideFilter");
            var q = $(e.target).parent().parent();
            var k = q.attr("id");
            var s = stackMap[k];
            s.data.selected = selectedIndex(k);
            saveStack(k);
            for (var f in filterMap) {
                filterMap[f].filter.override = false;
            }
            hideFilters(graph, filterMap);

            renderReports(s, dashboardJSON["id"], dashboardJSON["drillthroughID"], false);
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

        $(".clickable_grid").click(function (e) {
            var g = $(e.target).parent()
            var f = $(".reportMenu", g);
            $(".gridReportMenu:visible", g).hide({effect: "slide", direction: "up"});
            f.slideToggle({ complete: function () {
                f.css("overflow", "visible");
            }});
        })

        $(".emailReportButton").click(function (e) {
            $("#emailReportWindow").modal(true, true, true);
            var f = $(e.target).attr("data-key");
            if (typeof(f) == "undefined")
                f = $(e.target).parent().attr("data-key");
            currentReport = f;
        })

        $(".embed_dashboard").click(function(e) {
            $("#embedDashboardURL").text("<iframe width=\"500\" height=\"500\" src=\"https://www.easy-insight.com/app/html/dashboard/" + dashboardJSON["key"] + "/embed\"></iframe>");
            $("#embedDashboardWindow").modal(true, true, true);
        })

        $(".embedReportButton").click(function (e) {
            $("#embedReportWindow").modal(true, true, true);
            var f = $(e.target).attr("data-key");
            if (typeof(f) == "undefined")
                f = $(e.target).parent().attr("data-key");
            currentReport = f;
        })

        $(".email-send").click(function (e) {
            var format = $('input:radio[name=emailGroup]:checked').val();
            var recipient = $('#input01').val();
            var subject = $('#input02').val();
            var body = $('#textarea').val();
            $.getJSON('/app/emailReport?reportID=' + currentReport + '&format=' + format + "&recipient=" + recipient + "&subject=" + subject + "&body=" + body, function (data) {
                alert('Email sent.');
            });
        })

        $(".grid_report_link").click(function (e) {
            var a = $(e.target);
            while(typeof(a.attr("data-ref")) == "undefined") a = a.parent();
            var f = a.attr("data-ref");
            $("#" + f).show({effect: "slide"});
            e.preventDefault();
        })

        $(".grid_back_button").click(function (e) {
            var f = $(e.target).parent()
            if (!f.hasClass("gridReportMenu"))
                f = f.parent();
            f.hide({effect: "slide", direction: "left"})
            e.preventDefault();
        })

        $(".delete-config").click(function(e) {
            e.preventDefault();
            window.location.href = $(e.target).parent().attr("href") + "/delete";
        })

        function loadReport(key, dashboardKey) {
            $.getJSON("/app/html/report/" + key + "/data.json", function(data) {
                var cc;
                for(cc in reportMap[dashboardKey].base_map) {
                    delete filterMap[cc];
                }
                var w = reportMap[dashboardKey];
                var ff1 = data.filters;
                var t = _.reduce(ff1, function (m, i) {
                    m[w.id + "_report_filter_" + i.id] = {"filter": i, "parent": w };
                    return m;
                }, {});
                t = filtersFromLocalStorage(t);
                $.extend(filterMap, t);
                reportMap[dashboardKey].filters = data.filters.concat(reportMap[dashboardKey].parent_filters);
                reportMap[dashboardKey].report.report = { metadata: data, id: data.id, name: data.name };
                reportMap[dashboardKey].rendered = false;
                reportMap[dashboardKey].base_map = t;
                var t = $("#" + dashboardKey);
                $("." + dashboardKey + "_name").html(data.name);
                t.html(fullReportTemplate(reportMap[dashboardKey].report));
                addFilterCallbacks(t);
                renderReport(reportMap[dashboardKey], dashboardJSON["id"], dashboardJSON["drillthroughID"], true);

            })
        }

        $(".full_refresh").click(function(e) {
            renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
        })

        $(".export_excel").click(function(e) {
            renderExcel(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
        })

        startFullRenderPDF = function (obj, dashboardID, drillthroughID) {

            var ac = _.reduce(reportMap, function(m, e, i) {
                m[i] = e.report.report.id;
                return m;
            }, {});
            var c = {"filters": _.reduce(filterMap, function (m, e, i) {
                m[i] = toFilterString(e.filter, true);
                return m;
            }, {}), "stacks": _.reduce(stackMap, function(m, e, i) {
                m[i] = e.data.selected;
                return m;
            }, {}), "reports": ac};
            var pdfData = {};
            fullRenderPDF(obj, dashboardID, drillthroughID, pdfData);
            var postData = {dashboardID: dashboardID, configuration : c, reportImages: pdfData};
            busyIndicator.showPleaseWait();
            $.ajax ( {
                url: '/app/htmlDashboardPDF?dashboardID=' + dashboardID + "&timezoneOffset=" + new Date().getTimezoneOffset(),
                data: JSON.stringify(postData),
                success: function(data) {
                    var url = data["urlKey"];
                    busyIndicator.hidePleaseWait();
                    window.location.href = "/app/pdf?urlKey="+url;
                },
                error: function(a, b, c) {
                    busyIndicator.hidePleaseWait();
                },
                contentType: "application/json; charset=UTF-8",
                type: "POST"
            });
        }

        $(".export_dashboard_pdf").click(function(e) {
            startFullRenderPDF(graph, dashboardJSON["key"], dashboardJSON["drillthroughID"]);
        })


        $(".export_pdf").click(function(e) {
            renderPDF(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
        })

        saveConfiguration = function (name, key) {
            var ac = _.reduce(reportMap, function(m, e, i) {
                m[i] = e.report.report.id;
                return m;
            }, {});
            var c = {"filters": _.reduce(filterMap, function (m, e, i) {
                            m[i] = toFilterString(e.filter, true);
                return m;
                        }, {}), "stacks": _.reduce(stackMap, function(m, e, i) {
                m[i] = e.data.selected;
                return m;
            }, {}), "reports": ac, "name": name, "key": key }
            $.ajax({ url: "/app/html/" + ((dashboardJSON["id"] != -1) ? "dashboard" : "report")  + "/" + dashboardJSON["key"] + "/config",
                contentType: "application/json; charset=UTF-8",
                data: JSON.stringify(c),
                type: "POST",
                dataType: "json",
                success: function(d) {
                    window.location = d["target"];
                }
            })
        }
    })

})

