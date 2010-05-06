package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 11:39:32 AM
 */
public interface ITemporalAggregation {
    AnalysisItem getSortItem();

    Key getAggregateKey();

    void addValue(Value value, Value timeValue);

    Value getValue(Value timeValue);

    Key getNewAggregateKey();

    void setSortItem(AnalysisDateDimension analysisDateDimension);
}
