var stack;
var grid;
var textTemplate;
var reportTemplate;
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

var renderReport = function(obj) {
    if(obj.metadata.type == "pie") {
        var v = JSON.stringify(obj.metadata.parameters).replace(/\"/g, "");
        eval("var w = " + v);
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset(), Chart.getPieChartCallback(obj.id, w, {}))
    }
    else if(obj.metadata.type == "diagram") {
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset(), function(data) { window.drawDiagram(data, $("#" + obj.id + " .reportArea"), obj.id)})
    }
    else if(obj.metadata.type == "list") {
        $.getJSON(obj.metadata.url + "?reportID=" + obj.id + "&timezoneOffset=" + new Date().getTimezoneOffset(), List.getCallback(obj.id + "ReportArea", obj.metadata.properties, obj.metadata.sorting, obj.metadata.columns))
    }
}

var findReports = function(obj) {
    if(obj.type == "report") {

        return obj.report;
    } else if(obj.type == "grid") {
        var v = [];
        for(var i = 0;i < obj.grid.length;i++) {
            for(var j = 0;j < obj.grid[i].length;j++) {
                v = _.flatten(v.concat(findReports(obj.grid[i][j])));
            }
        }
        return v;
    } else if(obj.type == "stack") {
        var w = [];
        for(var k = 0;k < obj.stack_items.length;k++) {
            w = _.flatten(w.concat(findReports(obj.stack_items[k].item)));
        }
        return w;
    } else {
        return [];
    }

}

$(function () {
    $.get("/template.html", function (data) {
        var s = $(data);


        Filter = {
            create: function (obj) {
                if (obj.type == "single")
                    return Filter.single(obj);
            },
            single: _.template($("#single_value_filter_template", s).html())
        };
        dashboard = _.template($("#dashboard_template", s).html());
        stack = _.template($("#stack", s).html());
        grid = _.template($("#grid", s).html());
        reportTemplate = _.template($("#report_template", s).html());
        textTemplate = _.template($("#text_template", s).html());
        $("#base").append(dashboard(dashboardJSON));
        var reports = findReports(dashboardJSON["base"]);
        for(var i = 0;i < reports.length;i++) {
            renderReport(reports[i]);
        }
    })
})