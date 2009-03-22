package com.easyinsight.analysis {
import mx.collections.ArrayCollection;
public class ReportTypeConversion {
    public function ReportTypeConversion() {
    }

    public function convert(analysisDefinition:AnalysisDefinition, newDefinition:AnalysisDefinition):void {
        var fields:ArrayCollection = analysisDefinition.getFields();
        newDefinition.populate(fields);
    }
}
}