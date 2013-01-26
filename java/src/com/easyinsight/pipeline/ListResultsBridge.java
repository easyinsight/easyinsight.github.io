package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.dataset.DataSet;

import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 9:39:31 AM
 */
public class ListResultsBridge implements ResultsBridge {

    public DataResults toDataResults(DataSet dataSet, List<AnalysisItem> columns, Map<AnalysisItem, AnalysisItem> aliases, WSAnalysisDefinition report) {
        return dataSet.toListDataResults(columns, aliases, report);
    }
}
