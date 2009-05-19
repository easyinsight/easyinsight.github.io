package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.ListDataResults;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 1:42:06 PM
 */
public interface IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData);
    public void decorate(ListDataResults listDataResults);
}
