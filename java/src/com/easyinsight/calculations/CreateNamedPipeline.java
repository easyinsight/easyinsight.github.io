package com.easyinsight.calculations;

import com.easyinsight.core.Value;
import com.easyinsight.pipeline.NamedPipeline;

/**
 * User: jamesboe
 * Date: 7/13/12
 * Time: 8:35 AM
 */
public class CreateNamedPipeline extends Function {

    public Value evaluate() {
        String name = minusQuotes(0);
        // where?
        String where = minusQuotes(1);
        String param = minusQuotes(2);
        if ("onJoins".equals(where)) {
            NamedPipeline pipeline = new NamedPipeline(name);
            pipeline.setPreassigned(calculationMetadata.getFeed());
            pipeline.setAdditionalItems(calculationMetadata.getReport().getAddedItems());
            calculationMetadata.getInsightRequestMetadata().putPipeline(param, pipeline);
        }
        return null;
    }

    public int getParameterCount() {
        return 3;
    }
}
