package com.easyinsight.calculations.functions;


import com.easyinsight.analysis.*;
import com.easyinsight.calculations.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

import java.util.*;

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
        AnalysisItem instanceIDField = findDataSourceItem(0);
        AnalysisMeasure metricField = (AnalysisMeasure) findDataSourceItem(1);
        if (instanceIDField == null) {
            throw new FunctionException("Could not find the specified field " + instanceIDName);
        }
        if (metricField == null) {
            throw new FunctionException("Could not find the specified field " + metricName);
        }

        InsightRequestMetadata insightRequestMetadata = calculationMetadata.getInsightRequestMetadata();
        Collection<AnalysisItem> reportItems = insightRequestMetadata.getReportItems();

        List<AnalysisItem> additionalFields = new ArrayList<AnalysisItem>();
        for (AnalysisItem field : reportItems) {
            if (!field.qualifiedName().equals(instanceIDField.qualifiedName()) && !field.hasType(AnalysisItemTypes.MEASURE)) {
                additionalFields.add(field);
            }
        }
        String processName = getAnalysisItem().qualifiedName();



        RankingCalculationCache simpleCache = (RankingCalculationCache) calculationMetadata.getCache(new RankingCacheBuilder(instanceIDField, metricField, false,
                additionalFields), processName);
        Value instanceValue = getParameter(0);
        List<Value> additionalValues = new ArrayList<Value>(additionalFields.size());
        for (AnalysisItem item : additionalFields) {
            additionalValues.add(getRow().getValue(item));
        }
        Value rank = simpleCache.getRank(additionalValues, instanceValue);
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
        return -1;
    }
}