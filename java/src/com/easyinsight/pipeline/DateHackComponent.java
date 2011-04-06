package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
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
            for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                    Value value = row.getValue(date);
                    if (value != null && value.type() == Value.DATE) {
                        DateValue dateValue = (DateValue) value;
                        dateValue.calculate(cal);
                        dateValue.setDateTime(date.isTimeshift());
                    }
                }
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
