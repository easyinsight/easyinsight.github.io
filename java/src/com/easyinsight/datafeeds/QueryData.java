package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.dataset.DataSet;

import java.util.HashSet;
import java.util.Set;

/**
* User: jamesboe
* Date: 6/27/12
* Time: 12:22 PM
*/
public class QueryData {
    public DataSet dataSet;
    public Set<AnalysisItem> neededItems = new HashSet<AnalysisItem>();
    public Set<Long> ids = new HashSet<Long>();

    QueryData(long id) {
        ids.add(id);
    }
}
