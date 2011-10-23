package com.easyinsight.analysis {
import com.easyinsight.analysis.diagram.DiagramReportFieldExtension;

public class DiagramDropArea extends DropArea {

    public function DiagramDropArea() {
        super();
    }

    override public function getDropAreaType():String {
        return "Measure";
    }

    override protected function getNoDataLabel():String {
        return "Drop Measure Here";
    }

    override public function set analysisItem(analysisItem:AnalysisItem):void {

        var newField:Boolean = false;
        if (this.analysisItem == null && analysisItem != null) {
            newField = true;
        }
        if (analysisItem != null) {
            if (!analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                var analysisMeasure:AnalysisMeasure = new AnalysisMeasure();
                analysisMeasure.key = analysisItem.key;
                analysisMeasure.displayName = analysisItem.displayName;
                analysisMeasure.aggregation = AggregationTypes.SUM;
                analysisItem = analysisMeasure;
            }
        }

        super.analysisItem = analysisItem;
        if (analysisItem != null && analysisItem.reportFieldExtension == null) {
            analysisItem.reportFieldExtension = new DiagramReportFieldExtension();
            editEvent(null, AnalysisItemEditWindow.EXTENSION);
        }
    }
}
}