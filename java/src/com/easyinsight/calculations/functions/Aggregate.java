package com.easyinsight.calculations.functions;


import com.easyinsight.analysis.*;
import com.easyinsight.calculations.*;
import com.easyinsight.core.Value;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 9:31:22 PM
 */
public class Aggregate extends Function {
    public Value evaluate() {
        String metricName = minusBrackets(getParameterName(0));
        AnalysisMeasure metricField = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE) && (metricName.equals(analysisItem.toDisplay()) || metricName.equals(analysisItem.getKey().toKeyString()))) {
                metricField = (AnalysisMeasure) analysisItem;
            }
        }
        if (metricField == null) {
            throw new FunctionException("Could not find the specified field " + metricName);
        }
        Aggregation aggregation = new AggregationFactory(metricField, false).getAggregation();
        for (IRow row : calculationMetadata.getDataSet().getRows()) {
            aggregation.addValue(row.getValue(metricField));
        }
        return aggregation.getValue();
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 1;
    }
}