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

AsyncMultiSummary = {
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

            if (j == 0) {
                columnDef["bSortable"] = false;
                columnDef["mData"] = AsyncMultiSummary.createButton(j);
            } else {
                columnDef["mData"] = AsyncMultiSummary.createCallback(j);
            }
            array.push(columnDef);
        }

        //var paging = properties["generalSizeLimit"] > 0;
        var paging = true;
        var lockHeaders = properties["lockHeaders"];
        AsyncMultiSummary.createClasses(properties, targetDiv, columnData.classes);
        $("#" + targetDiv + " .reportArea").show();
        $("#" + targetDiv + " .noData").hide();
        if (typeof(afterRefresh) != "undefined") {
            if (afterRefresh.length > 0) {
                afterRefresh($("#" + targetDiv + " .loading"))();
            } else {
                afterRefresh();
            }
        }
        List.availableDataTables[targetDiv] = $('#' + targetDiv + ' .reportArea table').dataTable({bFilter: false, bServerSide: true, sAjaxSource: "/app/asyncMultiSummary" + params,
            fnServerParams: function (aoData) {
                aoData.push({ "name": "filters", "value": postData }, {"name": "uid", "value": uid})
            }, sServerMethod: "POST", bPaginate: paging, sPaginationType: "full_numbers", bInfo: false, aaSorting: a, aoColumns: array,
            fnDrawCallback: function( oSettings ) {
                var nTrs = $('tbody tr');
                for ( var i=0 ; i<nTrs.length ; i++ )
                {
                    var data = oSettings.aoData[i];
                    var nested = data._aData["nestedData"];
                    var tr = nTrs[i];
                    var x = $(tr);
                    $(nested).insertAfter(x);
                }
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
                        nCells[i + 1].innerHTML = summaries[i];
                    }
                }
            },
            sAjaxDataProp: "rowData.rows",
            oLanguage: {
                sLoadingRecords: "Loading the report...",
                sProcessing: "Loading the report..."
            },
            bProcessing: true,
            fnRowCallback: AsyncMultiSummary.createStyleCallback(l)
        });
        if (lockHeaders) {
            $('#' + targetDiv + ' .reportArea table').floatThead();
        }
    },
    createStyleCallback:function (colCount) {
        return function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            for (var i = 0; i < colCount; i++) {
                if (i == 0) {
                    $('td:eq('+ i +')', nRow).width(50);
                }
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
            /*var parent = $(nRow).parent();
            parent.insertAfter($(nRow), "<tr><td colspan='3'>MEESE</td></tr>");*/
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
    createButton:function (dataField) {
        return function (source, type, val) {
            var hasNested = source["hasNested"];
            if (hasNested) {
                var rowID = source["rowID"];
                return "<button type=\"button\" style=\"padding: 2px 4px;font-size:10px\" class=\"btn btn-info\" data-toggle=\"collapse\" data-target=\"#collapse" + rowID + "\">Details</button>"
            } else {
                return "";
            }
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

        //curStyleSheet.insertRule("#" + target + " .dataTables_wrapper .paginate_button.current {background-color:" + Color.numToStr(properties["summaryRowBackgroundColor"]) + "; color: " + Color.numToStr(properties["summaryRowTextColor"]) + ";}", 0);

        /*curStyleSheet.insertRule("#" + target + " table.dataTable tr.even {background-color:#FFFFFF;}", 0);

         // rowColor1
         curStyleSheet.insertRule("#" + target + " table.dataTable tr.odd {background-color:#FFFFFF;}", 0);
         // rowColor2
         curStyleSheet.insertRule("#" + target + " table.dataTable tr.even {background-color:#FFFFFF;}", 0);

         curStyleSheet.insertRule("#" + target + " table.dataTable tfoot td {background-color:" + Color.numToStr(properties["summaryRowBackgroundColor"]) + "; color: " + Color.numToStr(properties["summaryRowTextColor"]) + ";}", 0);*/
        curStyleSheet.insertRule("#" + target + " table.dataTable tfoot td {background-color:" + Color.numToStr(properties["summaryBackgroundColor"]) + "; color: " + Color.numToStr(properties["summaryTextColor"]) + ";}", 0);
        var gradientString = "background-color: " + Color.numToStr(properties["headerColor1"]);
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