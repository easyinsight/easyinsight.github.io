package com.easyinsight.analysis {

public class TrendDropArea extends MeasureDropArea {

    public function TrendDropArea() {
        super();
    }

    override public function getDropAreaType():String {
        return "Measure";
    }

    override protected function getNoDataLabel():String {
        return "Drag Metric Here";
    }

    override public function set analysisItem(analysisItem:AnalysisItem):void {

        if (analysisItem != null) {
            if (!analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                var analysisMeasure:AnalysisMeasure = new AnalysisMeasure();
                analysisMeasure.key = analysisItem.key;
                analysisMeasure.displayName = analysisItem.displayName;
                analysisMeasure.aggregation = AggregationTypes.COUNT_DISTINCT;
                analysisMeasure.concrete = analysisItem.concrete;
                analysisMeasure.filters = analysisItem.filters;
                analysisItem = analysisMeasure;
            }
        }

        if (analysisItem != null && analysisItem.reportFieldExtension == null) {
            var ext:TrendReportFieldExtension = new TrendReportFieldExtension();
            var dsID:int = 0;
            if (analysisItem.key is DerivedKey) {
                dsID = DerivedKey(analysisItem.key).feedID;
            } else {

            }
            for each (var f:AnalysisItemWrapper in analysisItems) {
                if (f.analysisItem.hasType(AnalysisItemTypes.DATE)) {
                    if (dsID > 0) {
                        if (f.analysisItem.key is DerivedKey && DerivedKey(f.analysisItem.key).feedID == dsID) {
                            ext.date = AnalysisDateDimension(f.analysisItem);
                            break;
                        }
                    } else {
                        ext.date = AnalysisDateDimension(f.analysisItem);
                        break;
                    }
                }
            }
            analysisItem.reportFieldExtension = ext;
        }

        super.analysisItem = analysisItem;
    }
}
}