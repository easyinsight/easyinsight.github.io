package com.easyinsight.calculations;

import com.easyinsight.core.Value;
import com.easyinsight.datafeeds.AltFallthroughConnection;
import com.easyinsight.datafeeds.CompositeFeedCompositeConnection;
import com.easyinsight.datafeeds.CompositePair;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/24/12
 * Time: 3:42 PM
 */
public class FallThroughJoin extends Function {
    public Value evaluate() {

        // fallthroughjoin([Deal ID], [Task Deal ID], [Company ID], [Task Company ID], [Contact ID], [Task Contact ID])

        String dataSource1 = minusQuotes(0);
        String dataSource2 = minusQuotes(1);
        List<CompositePair> pairs = new ArrayList<CompositePair>();
        for (int i = 2; i < params.size(); i += 2) {
            String field1 = minusQuotes(i);
            String field2 = minusQuotes(i + 1);
            pairs.add(new CompositePair(field1, field2));
        }
        CompositeCalculationMetadata compositeCalculationMetadata = (CompositeCalculationMetadata) calculationMetadata;
        compositeCalculationMetadata.getJoins().add(new AltFallthroughConnection(pairs, dataSource1, dataSource2));
        return null;
    }

    public int getParameterCount() {
        return -1;
    }
}
