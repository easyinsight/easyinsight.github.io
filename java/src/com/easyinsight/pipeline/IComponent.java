package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.ListDataResults;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 1:42:06 PM
 */
public interface IComponent {

    public static final int COORDINATE = 1;
    public static final int LOOKUP_TABLE = 2;
    public static final int DATA_SCRUB = 3;
    public static final int TAGS = 4;
    public static final int TYPE_TRANSFORM = 5;
    public static final int ROW_FILTER = 6;
    public static final int MEASURE_FILTER = 7;
    public static final int STEP_CORRELATION = 8;
    public static final int STEP_TRANSFORM = 9;
    public static final int LAST_VALUE = 10;
    public static final int PRE_AGGREGATION = 11;
    public static final int CALCULATION = 12;
    public static final int POST_AGGREGATION = 12;
    public static final int SUMMARY_FILTER = 13;
    public static final int LIMITS = 14;
    public static final int SORT = 15;

    public DataSet apply(DataSet dataSet, PipelineData pipelineData);
    public void decorate(DataResults listDataResults);
}
