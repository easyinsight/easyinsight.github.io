package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.PipelinePointInTime;
import com.easyinsight.calculations.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 6/19/14
 * Time: 10:20 AM
 */
public class Pipeline extends Function {
    @Override
    public Value evaluate() {
        String instanceIDName = minusBrackets(getParameterName(0));
        String stageName = minusBrackets(getParameterName(1));
        String stageDate = minusBrackets(getParameterName(2));
        List<String> lastStages = new ArrayList<>();
        for (int i = 3; i < paramCount(); i++) {
            lastStages.add(minusQuotes(getParameter(i)).toString());
        }

        AnalysisItem instanceIDField = findDataSourceItem(0);
        AnalysisItem stageNameField = findDataSourceItem(1);
        AnalysisItem stageDateField = findDataSourceItem(2);
        PipelinePointInTimeCache cache = (PipelinePointInTimeCache) calculationMetadata.getCache(new PipelinePointInTimeBuilder(instanceIDField, stageNameField,
                (AnalysisDateDimension) stageDateField, calculationMetadata, (AnalysisDateDimension) getAnalysisItem(), lastStages), "xyz");
        return new EmptyValue();
    }

    @Override
    public int getParameterCount() {
        return -1;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
