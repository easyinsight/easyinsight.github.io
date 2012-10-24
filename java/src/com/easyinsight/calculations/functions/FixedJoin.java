package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.CompositeCalculationMetadata;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;
import com.easyinsight.datafeeds.CompositeFeedCompositeConnection;
import com.easyinsight.datafeeds.CompositePair;
import com.easyinsight.datafeeds.FixedConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/24/12
 * Time: 3:42 PM
 */
public class FixedJoin extends Function {
    public Value evaluate() {
        // filter(
        String dataSource1 = minusQuotes(0);
        String dataSource2 = minusQuotes(1);
        String param1 = params.get(2).toString();
        String param2 = minusQuotes(3);
        FixedConnection fixedConnection = new FixedConnection(dataSource1, dataSource2, param1, param2);
        CompositeCalculationMetadata compositeCalculationMetadata = (CompositeCalculationMetadata) calculationMetadata;
        compositeCalculationMetadata.getJoins().add(fixedConnection);
        return null;
    }

    public int getParameterCount() {
        return -1;
    }
}
