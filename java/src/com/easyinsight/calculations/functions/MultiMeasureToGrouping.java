package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.pipeline.MultiMeasureComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 2/27/13
 * Time: 8:08 AM
 */
public class MultiMeasureToGrouping extends Function {
    public Value evaluate() {
        String groupingName = minusBrackets(getParameterName(0));
        AnalysisItem grouping = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (groupingName.equals(analysisItem.toDisplay())) {
                grouping = analysisItem;
            }
        }
        List<AnalysisItem> measures = new ArrayList<AnalysisItem>();
        for (int i = 1; i < paramCount(); i++) {
            String measure = minusBrackets(getParameterName(i));
            for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
                if (measure.equals(analysisItem.toDisplay())) {
                    measures.add(analysisItem);
                    break;
                }
            }
        }

        MultiMeasureComponent multiMeasureComponent = new MultiMeasureComponent(groupingName, measures, grouping, getAnalysisItem());
        if (!calculationMetadata.getGeneratedComponents().contains(multiMeasureComponent)) {
            calculationMetadata.getGeneratedComponents().add(multiMeasureComponent);
        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return -1;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
