package com.easyinsight.calculations;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 7/13/12
 * Time: 8:53 AM
 */
public class AssignFilterPipeline extends Function {
    public Value evaluate() {
        String field = minusQuotes(0);
        String pipeline = minusQuotes(1);
        calculationMetadata.getInsightRequestMetadata().assignFilterToPipeline(field, pipeline);
        return null;
    }

    public int getParameterCount() {
        return 2;
    }
}
