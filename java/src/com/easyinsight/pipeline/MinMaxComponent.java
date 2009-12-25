package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSTimeline;
import com.easyinsight.analysis.definitions.WSXAxisDefinition;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Dec 22, 2009
 * Time: 2:46:48 PM
 */
public class MinMaxComponent implements IComponent {

    private List<MinMax> minMaxList = new ArrayList<MinMax>();

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        WSAnalysisDefinition baseReport = pipelineData.getReport();
        List<AnalysisMeasure> measures = getMeasures(baseReport);

        for (AnalysisMeasure measure : measures) {
            double minValue = Double.MAX_VALUE;
            double maxValue = Double.MIN_VALUE;
            for (IRow row : dataSet.getRows()) {
                Value value = row.getValue(measure.createAggregateKey());
                if (value.type() == Value.NUMBER) {
                    NumericValue numericValue = (NumericValue) value;
                    if (numericValue.toDouble() != null) {
                        minValue = Math.min(minValue, numericValue.toDouble());
                        maxValue = Math.max(maxValue, numericValue.toDouble());
                    }
                }
            }

            minMaxList.add(new MinMax(measure, minValue, maxValue));
        }
        return dataSet;

    }

    private static class MinMax {
        private AnalysisMeasure analysisMeasure;
        private double min;
        private double max;

        private MinMax(AnalysisMeasure analysisMeasure, double min, double max) {
            this.analysisMeasure = analysisMeasure;
            this.min = min;
            this.max = max;
        }
    }

    private List<AnalysisMeasure> getMeasures(WSAnalysisDefinition baseReport) {
        WSAnalysisDefinition coreReport;
        if (baseReport instanceof WSTimeline) {
            WSTimeline timeline = (WSTimeline) baseReport;
            coreReport = timeline.getReport();
        } else {
            coreReport = baseReport;
        }
        Map<String, AnalysisItem> items = new HashMap<String, AnalysisItem>();
        coreReport.createReportStructure(items);
        List<AnalysisMeasure> measures = new ArrayList<AnalysisMeasure>();
        for (AnalysisItem item : items.values()) {
            if (item.hasType(AnalysisItemTypes.MEASURE)) {
                measures.add((AnalysisMeasure) item);
            }
        }
        return measures;
    }

    public void decorate(DataResults listDataResults) {
        for (MinMax minMax : minMaxList) {
            listDataResults.getAdditionalProperties().put(minMax.analysisMeasure.qualifiedName() + "_min", minMax.min);
            listDataResults.getAdditionalProperties().put(minMax.analysisMeasure.qualifiedName() + "_max", minMax.max);
        }
    }
}
