package com.easyinsight.analysis {

public class TrendDropArea extends DropArea {

    public function TrendDropArea() {
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
            analysisItem.reportFieldExtension = new TrendReportFieldExtension();
            editEvent(null, AnalysisItemEditWindow.EXTENSION);
        }
    }
}
}