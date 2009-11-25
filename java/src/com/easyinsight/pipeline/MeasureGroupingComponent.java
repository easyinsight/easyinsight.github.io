package com.easyinsight.pipeline;

import com.easyinsight.core.NamedKey;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.AnalysisMeasureGrouping;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;

/**
 * User: jamesboe
 * Date: Sep 27, 2009
 * Time: 5:04:57 PM
 */
public class MeasureGroupingComponent implements IComponent {

    private AnalysisMeasureGrouping measureGrouping;

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {
            for (AnalysisMeasure analysisMeasure : measureGrouping.getMeasures()) {
                Value val = row.getValue(analysisMeasure.getKey());
                StringValue stringValue = new StringValue(analysisMeasure.getKey().toKeyString());
                row.addValue(measureGrouping.getKey(), stringValue);
                row.addValue(new NamedKey("Measure"), val);
                // y-axis, x-axis, measure
                // this is where you basically need, uh...
                // need to add the measure as well
            }    
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void decorate(ListDataResults listDataResults) {


    }
}
