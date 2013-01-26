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
 * Time: 9:32:05 AM
 */
public interface ResultsBridge {
    public DataResults toDataResults(DataSet dataSet, List<AnalysisItem> columns, Map<AnalysisItem, AnalysisItem> aliases, WSAnalysisDefinition report);
}
