package com.easyinsight.analysis.gantt {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSGanttChartDefinition")]
public class GanttReport extends AnalysisDefinition {

    public var startTime:AnalysisItem ;
    public var endTime:AnalysisItem ;
    public var grouping:AnalysisItem ;
    public var ganttDefinitionID:int;
    
    public function GanttReport() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.GANTT;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ startTime, endTime, grouping ]);
    }

    override public function populate(fields:ArrayCollection):void {
        /*var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure = measures.getItemAt(0) as AnalysisItem;
        }*/
    }

    /*override public function createFormItems():ArrayCollection {
        var items:ArrayCollection = super.createFormItems();
        items.addItem(new NumericReportFormItem("Precision", "precision", precision, this, 0, 3));
        return items;
    }*/
}
}