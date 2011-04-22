package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.CheckBoxReportFormItem;
import com.easyinsight.analysis.ColorReportFormItem;
import com.easyinsight.analysis.ComboBoxReportFormItem;
import com.easyinsight.analysis.FillProvider;
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

    public function StackedBarChartDefinition() {
        super();
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

    override public function createFormItems():ArrayCollection {
        var items:ArrayCollection = super.createFormItems();
        items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", colorScheme,
                    this, [FillProvider.ocean, FillProvider.linearGradients, FillProvider.highContrast]));
        items.addItem(new CheckBoxReportFormItem("Use Custom Chart Color", "useChartColor", useChartColor, this));
        items.addItem(new ColorReportFormItem("Custom Chart Color", "chartColor", chartColor, this));
        items.addItem(new ComboBoxReportFormItem("Chart Sort", "columnSort", columnSort, this,
                [ChartDefinition.SORT_UNSORTED, ChartDefinition.SORT_X_ASCENDING, ChartDefinition.SORT_X_DESCENDING,
                ChartDefinition.SORT_Y_ASCENDING, ChartDefinition.SORT_Y_DESCENDING]));
        return items;
    }

    public function populateGroupings(dataSet:ArrayCollection, uniques:ArrayCollection):ArrayCollection {
        var map:Object = new Object();
        var results:ArrayCollection = new ArrayCollection();
        for (var i:int = 0; i < dataSet.length; i++) {
            var object:Object = dataSet.getItemAt(i);
            var xVal:String = object[yaxis.qualifiedName()];
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
            var dimensionValue:String = object[stackItem.qualifiedName()];
            if (dimensionValue == null || dimensionValue == "") {
                dimensionValue = "(No Value)";
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