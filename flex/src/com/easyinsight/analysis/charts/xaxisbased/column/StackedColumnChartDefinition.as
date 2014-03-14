package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.Value;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSStackedColumnChartDefinition")]
public class StackedColumnChartDefinition extends XAxisDefinition{


    public var columnSort:String = ChartDefinition.SORT_UNSORTED;
    public var stackItem:AnalysisItem;
    public var labelPosition:String = "none";
    public var labelInsideFontColor:int = 0x222222;
    public var useInsideLabelFontColor:Boolean = true;
    public var labelFontSize:int = 12;
    public var labelFontWeight:String = "none";
    public var legendMaxWidth:int = 200;
    public var multiColors:ArrayCollection = new ArrayCollection();
    public var stackSort:String = ChartDefinition.SORT_UNSORTED;
    public var stackLimit:int = 10;

    public function StackedColumnChartDefinition() {
        super();
    }

    override public function supportsEmbeddedFonts():Boolean {
        return true;
    }

    override public function get type():int {
        return AnalysisDefinition.STACKED_COLUMN;
    }

    override public function getChartType():int {
        return ChartTypes.COLUMN_2D_STACKED;
    }

    override public function getChartFamily():int {
        return ChartTypes.COLUMN_FAMILY;
    }

    override public function populate(fields:ArrayCollection):void {
        super.populate(fields);
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 1) {
            stackItem = dimensions.getItemAt(1) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        var fields:ArrayCollection = super.getFields();
        if (stackItem != null) {
            fields.addItem(stackItem);
        }
        return fields;
    }

    public function populateGroupings(dataSet:ArrayCollection, uniques:ArrayCollection):ArrayCollection {
        var map:Object = new Object();
        var results:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < dataSet.length; i++) {
            var object:Object = dataSet.getItemAt(i);
            var xValVal:Value = object[xaxis.qualifiedName()];
            var xVal:String = xValVal.toString();
            if (xVal == null ||
                    xVal == "") {
                xVal = "(No Value)";
            }

            var measureValue:Value = object[measures.getItemAt(0).qualifiedName()];

            if (measureValue.type() == Value.EMPTY || measureValue.toNumber() == 0) {
                continue;
            }

            var newObject:Object = map[xVal];
            if (newObject == null) {
                newObject = new Object();
                map[xVal] = newObject;
                results.addItem(newObject);
            }
            var stackVal:Value = object[stackItem.qualifiedName()];
            var dimensionValue:String = stackVal.toString();
            if (dimensionValue == null || dimensionValue == "") {
                dimensionValue = "(No Value)";
            }
            newObject[xaxis.qualifiedName()] = xVal;
            if (stackVal.links != null) {
                for (var linkKey:String in stackVal.links) {
                    newObject[dimensionValue + linkKey + "_link"] = stackVal.links[linkKey];
                }
            }
            newObject[stackItem.qualifiedName()] = dimensionValue;
            newObject[stackItem.qualifiedName() + "_ORIGINAL"] = stackVal;
            newObject[xaxis.qualifiedName() + "_ORIGINAL"] = xValVal;
            if (xValVal.sortValue != null) {
                newObject[xaxis.qualifiedName() + "_SORT"] = xValVal.sortValue.getValue();
            } else {
                newObject[xaxis.qualifiedName() + "_SORT"] = xVal;
            }

            newObject[dimensionValue] = measureValue;
            if (!uniques.contains(dimensionValue)) {
                uniques.addItem(dimensionValue);
            }
        }

        // get the total sum for each bar

        if (columnSort == ChartDefinition.SORT_Y_ASCENDING || columnSort == ChartDefinition.SORT_Y_DESCENDING) {
            sortByYAxis(results, uniques);
        } else if (columnSort == ChartDefinition.SORT_X_ASCENDING || columnSort == ChartDefinition.SORT_X_DESCENDING) {
            sortByXAxis(results, uniques);
        }


        return results;
    }

    private function sortByXAxis(results:ArrayCollection, uniques:ArrayCollection):void {
        var sort:Sort = new Sort();
        sort.fields = [ new SortField(xaxis.qualifiedName() + "_SORT", true, columnSort != ChartDefinition.SORT_X_ASCENDING)];
        results.sort = sort;
        results.refresh();
    }

    private function sortByYAxis(results:ArrayCollection, uniques:ArrayCollection):void {
        for each (var o:Object in results) {
            var sum:Number = 0;
            for (var dimValue:String in o) {
                if (uniques.contains(dimValue)) {
                    var mValue:Value = o[dimValue];
                    sum += Number(mValue.getValue());
                }
            }
            o["sortSumValueXYZ"] = sum;
        }
        var sort:Sort = new Sort();
        sort.fields = [ new SortField("sortSumValueXYZ", false, columnSort != ChartDefinition.SORT_Y_ASCENDING, true)];
        results.sort = sort;
        results.refresh();

        for each (var o1:Object in results) {
            delete o1["sortSumValueXYZ"];
        }
    }
}
}