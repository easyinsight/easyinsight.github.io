package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.MultiColor;
import com.easyinsight.analysis.Value;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.yaxisbased.YAxisDefinition;
import com.easyinsight.skin.ApplicationSkin;

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSStackedBarChartDefinition")]
public class StackedBarChartDefinition extends YAxisDefinition{

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

    public function StackedBarChartDefinition() {
        super();
    }

    override public function initialConfig():void {
        super.initialConfig();
        if  (ApplicationSkin.instance().multiColors != null && ApplicationSkin.instance().multiColors.length > 0 &&
                MultiColor(ApplicationSkin.instance().multiColors.getItemAt(0)).color1StartEnabled) {
            multiColors = ApplicationSkin.instance().multiColors;
        }
    }

    override public function supportsEmbeddedFonts():Boolean {
        return true;
    }

    override public function get type():int {
        return AnalysisDefinition.STACKED_BAR;
    }

    override public function getChartType():int {
        return ChartTypes.BAR_2D_STACKED;
    }

    override public function getChartFamily():int {
        return ChartTypes.BAR_FAMILY;
    }

    override public function getFields():ArrayCollection {
        var fields:ArrayCollection = super.getFields();
        if (stackItem != null) {
            fields.addItem(stackItem);
        }
        return fields;
    }

    override public function populate(fields:ArrayCollection):void {
        super.populate(fields);
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 1) {
            stackItem = dimensions.getItemAt(1) as AnalysisItem;
        }
    }

    public function populateGroupings(dataSet:ArrayCollection, uniques:ArrayCollection):ArrayCollection {
        var map:Object = new Object();
        var results:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < dataSet.length; i++) {
            var object:Object = dataSet.getItemAt(i);
            var xValVal:Value = object[yaxis.qualifiedName()];
            var xVal:String = xValVal.toString();
            if (xVal == null ||
                    xVal == "") {
                xVal = "(No Value)";
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
            if (stackVal.links != null) {
                for (var linkKey:String in stackVal.links) {
                    newObject[dimensionValue + linkKey + "_link"] = stackVal.links[linkKey];
                }
            }
            newObject[yaxis.qualifiedName()] = xVal;
            newObject[dimensionValue] = object[measures.getItemAt(0).qualifiedName()];
            newObject[stackItem.qualifiedName() + "_ORIGINAL"] = stackVal;
            newObject[yaxis.qualifiedName() + "_ORIGINAL"] = xValVal;
            if (xValVal.sortValue != null) {
                newObject[yaxis.qualifiedName() + "_SORT"] = xValVal.sortValue.getValue();
            } else {
                newObject[yaxis.qualifiedName() + "_SORT"] = xVal;
            }
            if (!uniques.contains(dimensionValue)) {
                uniques.addItem(dimensionValue);
            }
        }
        if (columnSort == ChartDefinition.SORT_Y_ASCENDING || columnSort == ChartDefinition.SORT_Y_DESCENDING) {
            sortByYAxis(results, uniques);
        } else if (columnSort == ChartDefinition.SORT_X_ASCENDING || columnSort == ChartDefinition.SORT_X_DESCENDING) {
            sortByXAxis(results, uniques);
        }


        return results;
    }

    private function sortByYAxis(results:ArrayCollection, uniques:ArrayCollection):void {
        var sort:Sort = new Sort();
        sort.fields = [ new SortField(yaxis.qualifiedName() + "_SORT", true, columnSort == ChartDefinition.SORT_Y_ASCENDING)];
        results.sort = sort;
        results.refresh();
    }

    private function sortByXAxis(results:ArrayCollection, uniques:ArrayCollection):void {
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
        sort.fields = [ new SortField("sortSumValueXYZ", false, columnSort == ChartDefinition.SORT_X_ASCENDING, true)];
        results.sort = sort;
        results.refresh();

        for each (var o1:Object in results) {
            delete o1["sortSumValueXYZ"];
        }
    }
}
}