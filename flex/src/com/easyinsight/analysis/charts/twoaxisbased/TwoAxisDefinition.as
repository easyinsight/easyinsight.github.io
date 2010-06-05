package com.easyinsight.analysis.charts.twoaxisbased {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.ComboBoxReportFormItem;

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSTwoAxisDefinition")]
public class TwoAxisDefinition extends ChartDefinition{
    public var measure:AnalysisItem;
    public var xaxis:AnalysisItem;
    public var yaxis:AnalysisItem;

    public var form:String = "segment";
    public var baseAtZero:String = "true";
    public var interpolateValues:String = "false";

    public function TwoAxisDefinition() {
        super();
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure = measures.getItemAt(0) as AnalysisItem;
        }
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            yaxis = dimensions.getItemAt(0) as AnalysisItem;
            if (dimensions.length > 1) {
                xaxis = dimensions.getItemAt(1) as AnalysisItem;
            }
        }
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ yaxis, xaxis, measure]);
    }

    override public function createFormItems():ArrayCollection {
        var items:ArrayCollection = super.createFormItems();
        items.addItem(new ComboBoxReportFormItem("Form", "form", form,
                this, ["segment", "step", "reverseStep", "horizontal", "curve"]));
        items.addItem(new ComboBoxReportFormItem("Base Y Axis at Zero", "baseAtZero", baseAtZero,
                this, ["true", "false"]));
        items.addItem(new ComboBoxReportFormItem("Interpolate Values", "interpolateValues", interpolateValues,
                this, ["true", "false"]));
        return items;
    }
}
}