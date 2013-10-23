$.holdReady(true);
Modernizr.load({ complete: function() { $.holdReady(false); }});
var stack;
var grid;
var textTemplate;
var reportTemplate;

var gaugeTemplate;

var dashboard;
var email_modal;
var short_months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]

var currentReport;

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
        if(!store)
            return cur;
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

var renderReport = function (o, dashboardID, drillthroughID, reload) {
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
    var curFilters = $.map(o.filters, function (e, i) {
        return toFilterString(e, false);
    });
    var fullFilters = {};
    for (i = 0; i < curFilters.length; i++) {
        fullFilters[curFilters[i].id] = curFilters[i];
    }
    beforeRefresh($("#" + id + " .loading"))();
    var dashboardComponent = dashboardID == -1 ? "" : ("&dashboardID=" + dashboardID);
    var drillthroughComponent = typeof(drillthroughID) != "undefined" ? ("&drillThroughKey=" + drillthroughID) : "";
    var postData = {
        url: obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + dashboardComponent + drillthroughComponent,
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(fullFilters),
        type: "POST"
    }
    if (obj.metadata.type == "pie") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getPieChartCallback(id, w, {})) }));
    }
    else if (obj.metadata.type == "diagram") {
        $.ajax($.extend(postData, {success: confirmRender(o, function (data) {
            window.drawDiagram(data, $("#" + id + " .reportArea"), obj.id, afterRefresh($("#" + id + " .loading")));
        }) }));
    }
    else if (obj.metadata.type == "list") {
        $.ajax($.extend(postData, {
            dataType: "text",

            success: confirmRender(o, List.getCallback(id, obj.metadata.properties, obj.metadata.sorting, obj.metadata.columns))
        }));
    } else if (obj.metadata.type == "bar") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getBarChartCallback(id, w, true, obj.metadata.styles))}));
    } else if (obj.metadata.type == "column") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getColumnChartCallback(id, w, obj.metadata.styles)) }));
    }
    else if (obj.metadata.type == "area" || obj.metadata.type == "bubble" || obj.metadata.type == "plot" || obj.metadata.type == "line") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getCallback(id, w, true, obj.metadata.styles))}));
    } else if (obj.metadata.type == "stacked_bar") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getStackedBarChart(id, w, obj.metadata.styles))}));
    } else if (obj.metadata.type == "stacked_column") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.ajax($.extend(postData, {success: confirmRender(o, Chart.getStackedColumnChart(id, w, obj.metadata.styles)) }));
        ;
    } else if (obj.metadata.type == "gauge") {
        $("#" + id + " .reportArea").html(gaugeTemplate({id: id, benchmark: null }))
        var v = JSON.stringify(obj.metadata.properties).replace(/\"/g, "");
        eval("var w = " + v);
        $.ajax($.extend(postData, {success: confirmRender(o, Gauge.getCallback(id + "ReportArea", id, w, obj.metadata.max))}));
    } else {
        $.ajax($.extend(postData, {success: confirmRender(o, function (data) {
            Utils.noData(data, function () {
                $('#' + id + " .reportArea").html(data);
            }, null, id);
        })}));
        ;
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
        var y = {"type": "stack", "children": ch, "id": obj.id, "selected": 0, "data": obj };
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
        email_modal = _.template($("#email_modal", s).html());

        var filterMap = _.reduce(dashboardJSON["filters"], function (m, i) {
            m["filter" + i.id] = {"filter": i, "parent": null };
            return m;
        }, {});
        var stackMap = {};
        var reportMap = {};
        var graph = buildReportGraph(dashboardJSON["base"], dashboardJSON["filters"], filterMap, stackMap, reportMap);
        var key = (dashboardJSON["id"] != -1) ? ("dashboard " + dashboardJSON["id"]) : "";
        var saveFilter;
        if(Modernizr.localstorage) {
            if(typeof(localStorage[key]) != "undefined") {
                var cur;
                var vals = JSON.parse(localStorage[key]);
                var filters = vals["filters"];
                for(cur in filters) {
                    if(typeof(filterMap[cur]) != "undefined" && typeof(filters[cur]) != "undefined")
                        $.extend(filterMap[cur].filter, filters[cur]);
                }
                if(typeof(vals["stacks"]) != "undefined")
                    $.extend(true, stackMap, vals["stacks"])
            }
            saveFilter = function(f, key, reportKey) {
                if(localStorage[reportKey] == null) {
                    localStorage[reportKey] = JSON.stringify({});
                }
                var report = JSON.parse(localStorage[reportKey]);
                if(typeof(report["filters"]) == "undefined") {
                    report["filters"] = {};
                }
                report["filters"][key] = toFilterString(f.filter, true);
                localStorage[reportKey] = JSON.stringify(report);
            }
        } else {
            savefilter = function(f, key, reportKey){ };
        }
        $("#base").append(dashboard(dashboardJSON));

        $(".dashboardStackNav").css("background-color", dashboardJSON["styles"]["alternative_stack_start"])
        $(".dashboard_base > .row > .col-md-12 > .tabbable > .nav-pills").css("background-color", dashboardJSON["styles"]["main_stack_start"])



        hideFilters(graph, filterMap);
        renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], false);
        $(".single_filter").change(function (e) {
            var k = $(e.target).attr("id");
            var f = filterMap[k];
            f.filter.selected = $(e.target).val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }
            saveFilter(f, k, key);
        });

        $(".month_select, .year_select").change(function (e) {
            var k = $(e.target).attr("id");
            var f = filterMap[k];
            f.filter.selected = $(e.target).val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }
            saveFilter(f, k, key);
        })

        $(".cb_all_choice").click(function (e) {
            if ($(e.target).is(":checked")) {
                $("input", $(e.target).parent().parent()).attr("checked", "checked");
            } else {
                $("input", $(e.target).parent().parent()).removeAttr("checked");
            }
        });

        $(".cb_filter_choice").click(function (e) {
            if ($(e.target).is(":checked")) {
                if ($(".cb_filter_choice:not(:checked)", $(e.target).parent().parent()).size() == 0) {
                    $(".cb_all_choice", $(e.target).parent().parent()).attr("checked", "checked");
                }
            } else {
                $(".cb_all_choice", $(e.target).parent().parent()).removeAttr("checked");
            }
        });

        $(".multi_value_save").click(function (e) {
            var a = $(e.target).parent().parent().parent().parent();
            var k = a.attr("id").replace(/_modal$/g, "");
            var f = filterMap[k];
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
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }

            saveFilter(f, k, key);
        });

        $(".multi_flat_month_save").click(function (e) {
            var a = $(e.target).parent().parent().parent().parent();
            var k = a.attr("id").replace(/_modal$/g, "");
            var f = filterMap[k];
            var min = $(".multi_flat_month_start", a).val();
            var max = $(".multi_flat_month_end", a).val();
            f.filter.start = parseInt(min);
            f.filter.end = parseInt(max);
            $("#" + a.attr("id").replace(/_modal$/g, "")).html(short_months[f.filter.start] + " to " + short_months[f.filter.end]);
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }
            saveFilter(f, k, key);
        })

        $(".rolling_filter_type").change(function (e) {
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
            saveFilter(f, k, key);
        });

        $(".rolling_filter_direction").change(function (e) {
            var k = $(e.target).attr("id").replace(/_custom_direction$/g, "");
            var f = filterMap[k];
            f.filter.direction = $(e.target).val();

            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }

            saveFilter(f, k, key);
        });

        $(".rolling_filter_value").change(function (e) {
            var k = $(e.target).attr("id").replace(/_custom_value$/g, "")
            var f = filterMap[k];
            f.filter.value = parseInt($(e.target).val());

            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }
            saveFilter(f, k, key);
        });

        $(".rolling_filter_interval").change(function (e) {
            var k = $(e.target).attr("id").replace(/_custom_interval$/g, "")
            var f = filterMap[k];
            f.filter.interval = $(e.target).val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }
            saveFilter(f, k, key);
        });

        $(".field_filter").change(function (e) {
            var k = $(e.target).attr("id")
            var f = filterMap[k];
            f.filter.selected = $(e.target).val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }
            saveFilter(f, k, key);
        });

        $(".filter_enabled").change(function (e) {
            var k = $(e.target).attr("id").replace(/_enabled$/g, "");
            var f = filterMap[k];

            f.filter.enabled = $(e.target).is(":checked");

            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }
            saveFilter(f, k, key);
        });

        $(".start_date_filter").datePicker({clickInput: true, startDate: '1900/01/01'}).bind("dateSelected", function (e, selectedDate, td) {
            var z = $(e.target);
            var k = z.attr("id").replace(/_date_start$/, "");
            var f = filterMap[k];
            f.filter.start = z.val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
            }
            saveFilter(f, k, key);
        });

        $(".end_date_filter").datePicker({clickInput: true, startDate: '1900/01/01'}).bind("dateSelected", function (e, selectedDate, td) {
            var z = $(e.target);
            var k = z.attr("id").replace(/_date_end$/, "");
            var f = filterMap[k];
            f.filter.end = z.val();
            if (f.parent == null) {
                renderReports(graph, dashboardJSON["id"], dashboardJSON["drillthroughID"], dashboardJSON["drillthroughID"], true);
            } else {
                renderReports(f.parent, dashboardJSON["id"], dashboardJSON["drillthroughID"], dashboardJSON["drillthroughID"], true);
            }
            saveFilter(f, k, key);
        });

        $(".adhoc").click(function (e) {
            var z = $(e.target);
            $(".adhoc_instructions", z.parent()).hide();
            renderReport(reportMap[$(z.parent()).attr("id")], dashboardJSON["id"], dashboardJSON["drillthroughID"], true);
        })

        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $(".filter").removeClass("hideFilter");
            var q = $(e.target).parent().parent();
            var k = q.attr("id");
            var s = stackMap[k];

            if(Modernizr.localstorage) {
                var i = selectedIndex(k);
                if(localStorage[key] == null) {
                    localStorage[key] = JSON.stringify({});
                }
                var report = JSON.parse(localStorage[key]);
                if(typeof(report["stacks"]) == "undefined") {
                    report["stacks"] = {};
                }
                report["stacks"][k] = {"data": { "selected": i } };
                localStorage[key] = JSON.stringify(report);
            }
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

        $(".dashboardReportHeader").click(function (e) {
            var f = $(".reportMenu", $(e.target).parent());
            f.slideToggle({ complete: function () {
                f.css("overflow", "visible")
            }});
        });

        $(".clickable_grid").click(function (e) {
            var g = $(e.target).parent()
            var f = $(".reportMenu", g);
            $(".gridReportMenu", g).hide({effect: "slide", direction: "up"});
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

        $(".email-send").click(function (e) {
            var format = $('input:radio[name=emailGroup]:checked').val();
            var recipient = $('#input01').val();
            var subject = $('#input02').val();
            var body = $('#textarea').val();
            $.getJSON('/app/emailReport?reportID='  + currentReport + '&format=' + format + "&recipient=" + recipient + "&subject=" + subject + "&body=" + body, function (data) {
                alert('Email sent.');
            });

        })

        $(".grid_report_link").click(function(e) {
            var f = $(e.target).attr("data-ref");
            $("#" + f).show({effect: "slide"});
            e.preventDefault();
        })

        $(".grid_back_button").click(function(e) {
            var f = $(e.target).parent()
            if(!f.hasClass("gridReportMenu"))
                f = f.parent();
            f.hide({effect: "slide", direction: "left"})
        })

    })
})

