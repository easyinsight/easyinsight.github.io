$(function() {
    jQuery.extend( jQuery.fn.dataTableExt.oSort, {
        "sortValue-pre": function ( a ) {
            var c = $("<td>" + a + "</td>").html();
            var s = $(c, ".sortData").html();

            if(isNaN(s))
                return s;
            else
                return +s;
        },

        "sortValue-asc": function ( a, b ) {
            return ((a < b) ? -1 : ((a > b) ? 1 : 0));
        },

        "sortValue-desc": function ( a, b ) {
            return ((a < b) ? 1 : ((a > b) ? -1 : 0));
        }
    } );
})

List = {
    getCallback:function (targetDiv, properties, sorting, numColumns) {
        return function (data) {
            Utils.noData(data, function () {
                var i = 1;
                var a = [];
                while(sorting[i.toString()]) {
                    a.push(sorting[i.toString()]);
                    i = i + 1;
                }

                var array = [];
                var j;
                for(j = 0;j < numColumns;j++) {
                    array.push({"sType": "sortValue"})
                }

                List.createClasses(properties, targetDiv);
                $('#' + targetDiv + ' .reportArea').html(data);
                $('#' + targetDiv + ' .reportArea table').dataTable({bFilter:false, bPaginate:false, bInfo:false, aaSorting: a, aoColumns: array })
            }, null, targetDiv);
        }
    },
    createClasses:function (properties, target) {
        var curStyleSheet = List.findDynamicStyleSheet();
        if(curStyleSheet == null) {
            var s = document.createElement("style");
            s.title = "customDataGridValues";
            $("head").append(s);
            curStyleSheet = List.findDynamicStyleSheet();
        }

        // rowColor1
        curStyleSheet.insertRule("#" + target + " table.dataTable tr.odd {background-color:"+ Color.numToStr(properties["rowColor1"]) + ";}", 0);
        // rowColor2
        curStyleSheet.insertRule("#" + target + " table.dataTable tr.even {background-color:"+ Color.numToStr(properties["rowColor2"]) + ";}", 0);

        curStyleSheet.insertRule("#" + target + " table.dataTable tfoot td {background-color:"+ Color.numToStr(properties["summaryRowBackgroundColor"]) + "; color: "+ Color.numToStr(properties["summaryRowTextColor"]) +";}", 0);
        var gradientString = "background-color: " + Color.numToStr(properties["headerColor1"]) + ";background-image: linear-gradient(bottom, " +Color.numToStr(properties["headerColor1"]) + " 30%, "+ Color.numToStr(properties["headerColor2"]) + " 70%);" +
        "background-image: -o-linear-gradient(bottom, " +Color.numToStr(properties["headerColor1"]) + " 30%, "+ Color.numToStr(properties["headerColor2"]) + " 70%);" +
        "background-image: -moz-linear-gradient(bottom, " +Color.numToStr(properties["headerColor1"]) + " 30%, "+ Color.numToStr(properties["headerColor2"]) + " 70%);" +
        "background-image: -webkit-linear-gradient(bottom, " +Color.numToStr(properties["headerColor1"]) + " 30%, "+ Color.numToStr(properties["headerColor2"]) + " 70%);" +
        "background-image: -ms-linear-gradient(bottom, " +Color.numToStr(properties["headerColor1"]) + " 30%, "+ Color.numToStr(properties["headerColor2"]) + " 70%);" +
        "background-image: -webkit-gradient(linear,left bottom,left top,color-stop(0.3, " +Color.numToStr(properties["headerColor1"]) + "),color-stop(0.7, "+ Color.numToStr(properties["headerColor2"]) + "));";
        curStyleSheet.insertRule("#" + target + " table.dataTable thead tr {" + gradientString + "color:" + Color.numToStr(properties["headerTextColor"]) + ";}", 0);
    },
    findDynamicStyleSheet:function () {
        var i = 0;
        var curStyleSheet = null;
        for (i = 0; i < document.styleSheets.length; i++) {
            if (document.styleSheets[i].title == "customDataGridValues") {
                curStyleSheet = document.styleSheets[i];
            }
        }
        return curStyleSheet;
    }
}