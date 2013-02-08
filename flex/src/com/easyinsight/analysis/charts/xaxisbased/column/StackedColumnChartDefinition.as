package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.CheckBoxReportFormItem;
import com.easyinsight.analysis.ColorReportFormItem;
import com.easyinsight.analysis.ComboBoxReportFormItem;
import com.easyinsight.analysis.FillProvider;
import com.easyinsight.analysis.MultiColor;
import com.easyinsight.analysis.Value;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.skin.ApplicationSkin;

import mx.collections.ArrayCollection;
import mx.controls.Alert;


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

    public function StackedColumnChartDefinition() {
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

            newObject[dimensionValue] = measureValue;
            if (!uniques.contains(dimensionValue)) {
                uniques.addItem(dimensionValue);
            }
        }
        return results;
    }
}
}