package com.easyinsight.analysis.charts.xaxisbased {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.ComboBoxReportFormItem;
import com.easyinsight.analysis.FillProvider;

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSXAxisChartDefinition")]
public class XAxisDefinition extends ChartDefinition{

    public var measure:AnalysisItem;
    public var xaxis:AnalysisItem;

    public var yAxisMin:Number;
    public var yAxisMax:Number;

    public var colorScheme:String = FillProvider.defaultFill;

    public function XAxisDefinition() {
        super();
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure = measures.getItemAt(0) as AnalysisItem;
        }
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            xaxis = dimensions.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ xaxis, measure]);
    }

    override public function createFormItems():ArrayCollection {
        var items:ArrayCollection = super.createFormItems();
        items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", colorScheme,
                    this, FillProvider.fillOptions));
        return items;
    }
}
}