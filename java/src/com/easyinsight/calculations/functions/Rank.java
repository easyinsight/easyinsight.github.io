package com.easyinsight.calculations.functions;


import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.IRow;
import com.easyinsight.calculations.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 9:31:22 PM
 */
public class Rank extends Function {
    public Value evaluate() {
        String instanceIDName = minusBrackets(getParameterName(0));
        String metricName = minusBrackets(getParameterName(1));
        AnalysisItem instanceIDField = null;
        AnalysisMeasure metricField = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (instanceIDName.equals(analysisItem.toDisplay()) || instanceIDName.equals(analysisItem.getKey().toKeyString())) {
                instanceIDField = analysisItem;
            }
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE) && (metricName.equals(analysisItem.toDisplay()) || metricName.equals(analysisItem.getKey().toKeyString()))) {
                metricField = (AnalysisMeasure) analysisItem;
            }
        }
        if (instanceIDField == null) {
            throw new FunctionException("Could not find the specified field " + instanceIDName);
        }
        if (metricField == null) {
            throw new FunctionException("Could not find the specified field " + metricName);
        }
        String processName = getAnalysisItem().qualifiedName();
        RankingCalculationCache simpleCache = (RankingCalculationCache) calculationMetadata.getCache(new RankingCacheBuilder(instanceIDField, metricField, false), processName);
        Value instanceValue = getParameter(0);
        Value rank = simpleCache.getRank(instanceValue);
        if (rank == null) {
            return new EmptyValue();
        } else {
            return rank;
        }
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 2;
    }
}