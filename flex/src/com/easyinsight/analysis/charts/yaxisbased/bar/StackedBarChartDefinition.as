package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.Value;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.AnalysisDefinition;

import com.easyinsight.analysis.charts.yaxisbased.YAxisDefinition;

import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSStackedBarChartDefinition")]
public class StackedBarChartDefinition extends YAxisDefinition{

    public var chartColor:uint;
    public var useChartColor:Boolean;
    public var columnSort:String = ChartDefinition.SORT_UNSORTED;
    public var stackItem:AnalysisItem;
    public var labelPosition:String = "none";
    public var labelInsideFontColor:int = 0x222222;
    public var useInsideLabelFontColor:Boolean = true;
    public var labelFontSize:int = 12;
    public var labelFontWeight:String = "none";

    public function StackedBarChartDefinition() {
        super();
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
            if (!uniques.contains(dimensionValue)) {
                uniques.addItem(dimensionValue);
            }
        }
        return results;
    }
}
}