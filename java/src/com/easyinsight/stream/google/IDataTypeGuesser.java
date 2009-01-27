package com.easyinsight.stream.google;
import com.easyinsight.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: jboe
* Date: Jan 3, 2008
* Time: 1:44:35 PM
*/
public interface IDataTypeGuesser {
    public void addValue(Key tag, Value value);

    List<AnalysisItem> createFeedItems();
}
