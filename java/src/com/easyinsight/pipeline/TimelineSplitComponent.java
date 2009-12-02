package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 9:11:27 AM
 */
public class TimelineSplitComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void decorate(DataResults dataResults) {
        // split the data into the appropriate subset
        ListDataResults listDataResults = (ListDataResults) dataResults;

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
