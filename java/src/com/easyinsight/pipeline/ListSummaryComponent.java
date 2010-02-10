package com.easyinsight.pipeline;

import cern.colt.list.DoubleArrayList;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSPlotChartDefinition;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 9, 2009
 * Time: 11:20:42 AM
 */
public class ListSummaryComponent implements IComponent {

    private Map<AnalysisMeasure, Value> aggregationMap = new HashMap<AnalysisMeasure, Value>();

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {

        for (AnalysisItem reportItem : pipelineData.getReportItems()) {
            if (reportItem.hasType(AnalysisItemTypes.MEASURE)) {
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) reportItem;
                AggregationFactory aggregationFactory = new AggregationFactory(analysisMeasure);
                Aggregation aggregation = aggregationFactory.getAggregation();
                for (IRow row : dataSet.getRows()) {
                    Value value = row.getValue(analysisMeasure.createAggregateKey());
                    aggregation.addValue(value);
                }
                Value aggregateValue = aggregation.getValue();
                aggregationMap.put(analysisMeasure, aggregateValue);
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
        for (Map.Entry<AnalysisMeasure, Value> entry : aggregationMap.entrySet()) {
            listDataResults.getAdditionalProperties().put("summary" + entry.getKey().qualifiedName(), entry.getValue());
        }
    }
}