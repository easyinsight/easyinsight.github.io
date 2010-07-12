package com.easyinsight.etl;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jul 1, 2010
 * Time: 12:08:21 PM
 */
public class LookupTableUtil {
    public static List<Value> getValues(LookupTable lookupTable, List<FilterDefinition> filters) {
        List<Value> values = new ArrayList<Value>();
        WSListDefinition list = new WSListDefinition();
        list.setColumns(Arrays.asList(lookupTable.getSourceField()));
        list.setFilterDefinitions(filters);
        list.setDataFeedID(lookupTable.getDataSourceID());
        ListDataResults listDataResults = (ListDataResults) new DataService().list(list, new InsightRequestMetadata());
        for (ListRow listRow : listDataResults.getRows()) {
            values.add(listRow.getValues()[0]);
        }
        return values;
    }
}
