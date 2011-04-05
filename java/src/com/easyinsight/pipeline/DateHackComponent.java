package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 4/5/11
 * Time: 4:10 PM
 */
public class DateHackComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        Calendar cal = Calendar.getInstance();
        for (IRow row : dataSet.getRows()) {
            for (Value value : row.getValues().values()) {
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    dateValue.calculate(cal);
                }
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
