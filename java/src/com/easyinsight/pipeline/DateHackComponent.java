package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * User: jamesboe
 * Date: 4/5/11
 * Time: 4:10 PM
 */
public class DateHackComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        Calendar cal = Calendar.getInstance();
        Calendar shiftedCal = Calendar.getInstance();
        //if (!pipelineData.getInsightRequestMetadata().isSuppressShifts()) {
            int time = pipelineData.getInsightRequestMetadata().getUtcOffset() / 60;
            String string;
            if (time > 0) {
                string = "GMT-"+Math.abs(time);
            } else if (time < 0) {
                string = "GMT+"+Math.abs(time);
            } else {
                string = "GMT";
            }
            TimeZone timeZone = TimeZone.getTimeZone(string);
            shiftedCal.setTimeZone(timeZone);
        //}

        for (IRow row : dataSet.getRows()) {
            for (AnalysisItem analysisItem : pipelineData.getReportItems()) {
                if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                    Value value = row.getValue(date);
                    if (value != null && value.type() == Value.DATE) {
                        DateValue dateValue = (DateValue) value;
                        dateValue.calculate(date.isTimeshift() ? shiftedCal : cal);
                    }
                }
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
