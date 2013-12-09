$.holdReady(true);
Modernizr.load({ complete: function () {
    $.holdReady(false);
}});

var stack;
var grid;
var textTemplate;
var reportTemplate;
var fullReportTemplate;
var test;

var gaugeTemplate;
var configurationDropdownTemplate;
var reportListTemplate;

var dashboard;
var email_modal;
var short_months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]

var currentReport;

var multi_value_results;
var multi_field_value_results;

var saveConfiguration;

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
    var embedComponent = typeof(userJSON.embedKey) != "undefined" ? ("&embedKey=" + userJSON.embedKey) : "";
    var embedded = typeof(userJSON.embedded) != "undefined" ? ("&embedded=true") : "";
    var dashboardComponent = dashboardID == -1 ? "" : ("&dashboardID=" + dashboardID);
    var drillthroughComponent = typeof(drillthroughID) != "undefined" ? ("&drillThroughKey=" + drillthroughID) : "";
    var postData = {
        url: obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset() + dashboardComponent + drillthroughComponent + embedComponent + embedded,
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


        $("#configuration-dropdown").html(configurationDropdownTemplate({"dashboard": dashboardJSON, "user": userJSON}))

        if (Modernizr.localstorage && dashboardJSON["local_storage"] && location.pathname.match(/^\/app\/html\/(report|dashboard)\/[a-zA-Z0-9]+$/)) {
            if (typeof(localStorage[dashboardKey]) != "undefined") {
                var cur;
                var vals = JSON.parse(localStorage[dashboardKey]);
                var filters = vals["filters"];
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

        } else {
            saveFilter = function (f, key) {
            };
            saveStack = function (k) {
            };
        }


        $("#base").append(dashboard(dashboardJSON));

        $(".dashboardStackNav").css("background-color", dashboardJSON["styles"]["alternative_stack_start"])
        $(".dashboard_base > .row > .col-md-12 > .tabbable > .nav-pills").css("background-color", dashboardJSON["styles"]["main_stack_start"])

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
                if ($(e.target).is(":checked")) {
                    $("input", $(e.target).parent().parent()).attr("checked", "checked");
                    for(i in selectionMap)
                        selectionMap[i] = true;
                } else {
                    $("input", $(e.target).parent().parent()).removeAttr("checked");
                    for(i in selectionMap)
                        selectionMap[i] = false;
                }
            }

            var choiceAllCheck = function (e) {
                var checked = $(e.target).is(":checked");
                selectionMap[$(".cb_filter_value", $(e.target).parent()).html()] = checked;
                if (checked && _.all(selectionMap, function(e, i, l) { return (i == "All") || e; })) {
                    $(".cb_all_choice", $(e.target).parent().parent()).attr("checked", "checked");
                    selectionMap["All"] = true;
                } else {
                    $(".cb_all_choice", $(e.target).parent().parent()).removeAttr("checked");
                    selectionMap["All"] = false;
                }
            };

            $('.multi_value_modal', target).on('show.bs.modal', function (e) {
                var f = filterMap[$(e.target).attr("id").replace(/_modal$/g, "")].filter;

                if(f.error) {
                    $(".loading-bar", e.target).show();
                    $.getJSON("/app/html/filterValue?filterID=" + f.id, function(d) {
                        $(".loading-bar", e.target).hide();
                        f.values = d.values;
                        var m = _.reduce(f.values, function(m, e) {
                            m[e == "" ? "[ No Value ]" : e] = f.selected["All"];
                            return m;
                        }, {});
                        selectionMap = $.extend({}, m, f.selected);
                        if(d.values.length > 100) {
                            d.error = "Too many values, please refine your search."
                        }
                        $(".multi-value-list", $(e.target)).html(multi_value_results({ data: { selected: selectionMap }, results: d }));
                        delete f.error;
                    })
                } else {
                    var m = _.reduce(f.values, function(m, e) {
                                    m[e == "" ? "[ No Value ]" : e] = f.selected["All"];
                                    return m;
                                }, {});
                    selectionMap = $.extend({}, m, f.selected);
                }
            });

            $('.multi_field_value_modal', target).on('show.bs.modal', function (e) {
                var f = filterMap[$(e.target).attr("id").replace(/_modal$/g, "")].filter;

                var m = _.reduce(f.values, function(m, e) {
                    m[e == "" ? "[ No Value ]" : e] = f.selected["All"];
                    return m;
                }, {});
                selectionMap = $.extend({}, m, f.selected);
            });

            $(".cb_all_choice", target).click(allCheck);

            $(".cb_filter_choice", target).click(choiceAllCheck);

            $(".multi_value_save", target).click(function (e) {
                var a = $(e.target).parent().parent().parent().parent();
                var k = a.attr("id").replace(/_modal$/g, "");
                var f = filterMap[k];
                f.filter.selected = selectionMap;
                var selectVals = toSelectedArray(selectionMap);
                var label;
                if(selectionMap["All"]) {
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
                $("#" + a.attr("id").replace(/_modal$/g, "")).html(short_months[f.filter.start] + " to " + short_months[f.filter.end]);
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


            $(".report-emailReportButton", target).click(function (e) {
                $("#emailReportWindow").modal(true, true, true);
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

