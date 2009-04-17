package com.easyinsight.analysis.charts.twoaxisbased {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSMultiMeasureDefinition")]
public class MultiMeasureDefinition extends ChartDefinition{

    public var xaxis:AnalysisItem;
    public var measures:ArrayCollection;

    public var normalized:Boolean;

    public function MultiMeasureDefinition() {
        super();
    }

    override public function populate(fields:ArrayCollection):void {
        this.measures = findItems(fields, AnalysisItemTypes.MEASURE);
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            xaxis = dimensions.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        var items:Array = [ xaxis ];
        for each (var item:AnalysisItem in measures) {
            items.push (item);
        }
        return new ArrayCollection(items);
    }
}
}