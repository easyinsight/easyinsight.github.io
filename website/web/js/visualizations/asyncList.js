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

AsyncList = {
    createTable: function (targetDiv, properties, columnData, postData, tableHTML, params, uid, fullFilters, drillthroughKey) {


        if (typeof(List.availableDataTables[targetDiv]) != "undefined" && List.availableDataTables[targetDiv] != null) {
            List.availableDataTables[targetDiv].fnDestroy();
            List.availableDataTables[targetDiv] = null;
        }
        var i = 1;
        var a = [];
        var sorting = columnData.sorting;
        while (sorting[i.toString()]) {
            a.push(sorting[i.toString()]);
            i = i + 1;
        }

        var array = [];
        var j;
        var report = $('#' + targetDiv + ' .reportArea');
        report.html(tableHTML);
        //report.html(data);
        var l = $("th", report).length;

        for (j = 0; j < l; j++) {
            var columnDef = {"sType": "sortValue", "sClass": targetDiv + j};

            columnDef["mData"] = AsyncList.createCallback(j);
            array.push(columnDef);
        }

        var paging = properties["generalSizeLimit"] > 0;
        AsyncList.createClasses(properties, targetDiv, columnData.classes);
        $("#" + targetDiv + " .reportArea").show();
        $("#" + targetDiv + " .noData").hide();
        if (typeof(afterRefresh) != "undefined") {
            if (afterRefresh.length > 0) {
                afterRefresh($("#" + targetDiv + " .loading"))();
            } else {
                afterRefresh();
            }
        }
        List.availableDataTables[targetDiv] = $('#' + targetDiv + ' .reportArea table').dataTable({bFilter: false, bServerSide: true, sAjaxSource: "/app/asyncList" + params,
            fnServerParams: function (aoData) {
                aoData.push({ "name": "filters", "value": postData }, {"name": "uid", "value": uid})
            }, sServerMethod: "POST", bPaginate: paging, sPaginationType: "full_numbers", bInfo: false, aaSorting: a, aoColumns: array,
            fnDrawCallback: function( oSettings ) {
                $(".list_drillthrough").click(function (e) {

                    e.preventDefault();
                    var x = $(e.target);
                    var f = {"reportID": x.data("reportid"), "drillthroughID": x.data("drillthroughid"), "embedded": x.data("embedded"), "source": x.data("source"), "drillthroughKey": drillthroughKey, "filters": fullFilters,
                        "drillthrough_values": {}};
                    f["drillthrough_values"] = _.inject(x.data(), function(m, e, i, l) {

                            if(i.match(/^drillthrough/))
                                m[i.replace(/^drillthrough/, "")] = decodeURI(e);
                            return m; },
                        {});
                    drillThrough(f);
                })
            },
            fnFooterCallback: function(nFoot, aData, iStart, iEnd, aiDisplay) {
                if (typeof(nFoot) != "undefined") {
                    //var json = this.ajax.json();
                    var api = this.api();
                    var json = api.ajax.json();
                    var rowData = json["rowData"];
                    var summaries = rowData["summaries"];
                    var nCells = nFoot.getElementsByTagName('td');
                    var colLength = rowData["columnLength"];
                    for (var i = 0; i < colLength; i++) {
                        nCells[i].innerHTML = summaries[i];
                    }
                }
            },
            sAjaxDataProp: "rowData.rows",
            oLanguage: {
                sLoadingRecords: "Loading the report...",
                sProcessing: "Loading the report..."
            },
            bProcessing: true,
            fnRowCallback: AsyncList.createStyleCallback(l)
        });

    },
    createStyleCallback:function (colCount) {
        return function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            for (var i = 0; i < colCount; i++) {
                var bold = aData[i + "bold"];
                if (typeof(bold) != "undefined") {
                    $('td:eq('+i+')', nRow).css('font-weight', 'bold');
                }
                var bg = aData[i+"bg"];
                if (typeof(bg) != "undefined") {
                    $('td:eq('+i+')', nRow).css('background-color', bg);
                }
                var fg = aData[i+"fg"];
                if (typeof(bg) != "undefined") {
                    $('td:eq('+i+')', nRow).css('color', fg);
                }
            }
        }
    },
    createCallback:function (dataField) {
        return function (source, type, val) {
            if (type == "display") {
                var url = source[dataField + "url"];
                var str = source[dataField];
                if (typeof(url) != "undefined") {
                    str = "<a href=\"" + url + "\">" + source[dataField] + "</a>";
                } else {
                    var dt = source[dataField + "dt"];
                    if (typeof(dt) != "undefined") {
                        str = dt;
                    }
                }
                return str;
            }
            return source[dataField];
        }
    },
    createClasses: function (properties, target, columnClasses) {
        var curStyleSheet = List.findDynamicStyleSheet();
        if (curStyleSheet == null) {
            var s = document.createElement("style");
            s.title = "customDataGridValues";
            $("head").append(s);
            curStyleSheet = List.findDynamicStyleSheet();
        }

        for (var colClass in columnClasses) {
            var colStyleDetail = columnClasses[colClass];
            var align = colStyleDetail.align;
            var classContents = "." + target + colClass + "{";
            if (align == "left") {
                classContents += "text-align: left";
            } else if (align == "center") {
                classContents += "text-align: center";
            } else if (align == "right") {
                classContents += "text-align: right";
            }
            classContents += "}";
            curStyleSheet.insertRule(classContents, 0);
        }

        curStyleSheet.insertRule("." + target + "left { text-align:left }", 0);

        //curStyleSheet.insertRule("#" + target + " .dataTables_wrapper .paginate_button.current {background-color:" + Color.numToStr(properties["summaryRowBackgroundColor"]) + "; color: " + Color.numToStr(properties["summaryRowTextColor"]) + ";}", 0);

        curStyleSheet.insertRule("#" + target + " table.dataTable tr.even {background-color:" + Color.numToStr(properties["rowColor2"]) + ";}", 0);

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