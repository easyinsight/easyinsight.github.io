$(function () {
    jQuery.extend(jQuery.fn.dataTableExt.oSort, {
        "sortValue-pre": function (a) {
            var c = $("<td>" + a + "</td>").html();
            var s = $(c, ".sortData").html();

            if (isNaN(s))
                return s;
            else
                return +s;
        },

        "sortValue-asc": function (a, b) {
            return ((a < b) ? -1 : ((a > b) ? 1 : 0));
        },

        "sortValue-desc": function (a, b) {
            return ((a < b) ? 1 : ((a > b) ? -1 : 0));
        }
    });
})

TrendGrid = {

    crosstabCallback: function (targetDiv, properties, fullFilters, drillthroughKey, dashboardID) {
        return function(data) {
            Utils.noData(data, function() {
                var report = $('#' + targetDiv + ' .reportArea');
                report.html(data);
                $(".list_drillthrough").click(function (e) {
                    e.preventDefault();
                    var x = $(e.target);
                    var f = {"reportID": x.data("reportid"), "drillthroughID": x.data("drillthroughid"), "embedded": x.data("embedded"), "source": x.data("source"), "drillthroughKey": drillthroughKey, "filters": fullFilters,
                        "drillthrough_values": {}};
                    if (dashboardID != -1) {
                        f["dashboardID"] = dashboardID;
                    }
                    f["drillthrough_values"] = _.inject(x.data(), function(m, e, i, l) {

                            if(i.match(/^drillthrough/))
                                m[i.replace(/^drillthrough/, "")] = decodeURI(e);
                            return m; },
                        {});

                    drillThrough(f);
                })
            }, null, targetDiv);
        }
    },

    getCallback: function (targetDiv, properties, sorting, numColumns, fullFilters, drillthroughKey, dashboardID) {
        return function (data) {
            Utils.noData(data, function () {
                if (typeof(TrendGrid.availableDataTables[targetDiv]) != "undefined" && TrendGrid.availableDataTables[targetDiv] != null) {
                    TrendGrid.availableDataTables[targetDiv].fnDestroy();
                    TrendGrid.availableDataTables[targetDiv] = null;
                }
                var i = 1;
                var a = [];
                while (sorting[i.toString()]) {
                    a.push(sorting[i.toString()]);
                    i = i + 1;
                }

                var array = [];
                var j;
                var report = $('#' + targetDiv + ' .reportArea');
                report.html(data);
                var l = $("th", report).length;
                for (j = 0; j < l; j++) {
                    array.push({"sType": "sortValue"})
                }

                var paging = properties["generalSizeLimit"] > 0;

                var lockHeaders = properties["lockHeaders"];

                TrendGrid.createClasses(properties, targetDiv);
                var rowNumEnabled = properties["showLineNumbers"];
                TrendGrid.availableDataTables[targetDiv] = $('#' + targetDiv + ' .reportArea table').dataTable({bFilter: false, bPaginate: paging, sPaginationType: "full_numbers", bInfo: false, aaSorting: a, aoColumns: array,
                fnRowCallback: function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    if (rowNumEnabled) {
                        var index = iDisplayIndex +1;
                        $('td:eq(0)',nRow).html(index);
                        return nRow;
                    } else {
                        return nRow;
                    }
                }

                });

                //new $.fn.dataTable.FixedHeader(List.availableDataTables[targetDiv]);
                if (lockHeaders) {
                    $('#' + targetDiv + ' .reportArea table').floatThead();
                }

                //new $.fn.dataTable.FixedHeader(List.availableDataTables[targetDiv]);
                $(".list_drillthrough").click(function (e) {
                    e.preventDefault();
                    var x = $(e.target);
                    var f = {"reportID": x.data("reportid"), "drillthroughID": x.data("drillthroughid"), "embedded": x.data("embedded"), "source": x.data("source"), "drillthroughKey": drillthroughKey, "filters": fullFilters,
                        "drillthrough_values": {}};
                    if (dashboardID != -1) {
                        f["dashboardID"] = dashboardID;
                    }
                    f["drillthrough_values"] = _.inject(x.data(), function(m, e, i, l) {

                        if(i.match(/^drillthrough/))
                            m[i.replace(/^drillthrough/, "")] = decodeURI(e);
                        return m; },
                        {});

                    drillThrough(f);
                })
            }, null, targetDiv);
        }
    },
    createClasses: function (properties, target) {
        var curStyleSheet = TrendGrid.findDynamicStyleSheet();
        if (curStyleSheet == null) {
            var s = document.createElement("style");
            s.title = "customDataGridValues";
            $("head").append(s);
            curStyleSheet = TrendGrid.findDynamicStyleSheet();
        }

        curStyleSheet.insertRule("#" + target + " a.paginate_active {background-color:"+ Color.numToStr(properties["summaryRowBackgroundColor"]) + "; color: "+ Color.numToStr(properties["summaryRowTextColor"]) +";}", 0);

        curStyleSheet.insertRule("#" + target + " table.dataTable tr.even {background-color:"+ Color.numToStr(properties["rowColor2"]) + ";}", 0);

        // rowColor1
        curStyleSheet.insertRule("#" + target + " table.dataTable tr.odd {background-color:" + Color.numToStr(properties["rowColor1"]) + ";}", 0);
        // rowColor2
        curStyleSheet.insertRule("#" + target + " table.dataTable tr.even {background-color:" + Color.numToStr(properties["rowColor2"]) + ";}", 0);

        curStyleSheet.insertRule("#" + target + " table.dataTable tfoot td {background-color:" + Color.numToStr(properties["summaryRowBackgroundColor"]) + "; color: " + Color.numToStr(properties["summaryRowTextColor"]) + ";}", 0);
        var gradientString = "background-color: " + Color.numToStr(properties["headerColor1"]) + ";background-image: linear-gradient(bottom, " + Color.numToStr(properties["headerColor1"]) + " 30%, " + Color.numToStr(properties["headerColor2"]) + " 70%);" +
            "background-image: -o-linear-gradient(bottom, " + Color.numToStr(properties["headerColor1"]) + " 30%, " + Color.numToStr(properties["headerColor2"]) + " 70%);" +
            "background-image: -moz-linear-gradient(bottom, " + Color.numToStr(properties["headerColor1"]) + " 30%, " + Color.numToStr(properties["headerColor2"]) + " 70%);" +
            "background-image: -webkit-linear-gradient(bottom, " + Color.numToStr(properties["headerColor1"]) + " 30%, " + Color.numToStr(properties["headerColor2"]) + " 70%);" +
            "background-image: -ms-linear-gradient(bottom, " + Color.numToStr(properties["headerColor1"]) + " 30%, " + Color.numToStr(properties["headerColor2"]) + " 70%);" +
            "background-image: -webkit-gradient(linear,left bottom,left top,color-stop(0.3, " + Color.numToStr(properties["headerColor1"]) + "),color-stop(0.7, " + Color.numToStr(properties["headerColor2"]) + "));";
        curStyleSheet.insertRule("#" + target + " table.dataTable thead tr {" + gradientString + "color:" + Color.numToStr(properties["headerTextColor"]) + ";}", 0);
    },
    findDynamicStyleSheet: function () {
        var i = 0;
        var curStyleSheet = null;
        for (i = 0; i < document.styleSheets.length; i++) {
            if (document.styleSheets[i].title == "customDataGridValues") {
                curStyleSheet = document.styleSheets[i];
            }
        }
        return curStyleSheet;
    },
    availableDataTables: {}
}