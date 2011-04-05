package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 4:34:40 PM
 */
public class AnalysisDateDimensionResultMetadata extends AnalysisItemResultMetadata {
    private Date earliestDate;
    private Date latestDate;

    public Date getEarliestDate() {
        return earliestDate;
    }

    public void setEarliestDate(Date earliestDate) {
        this.earliestDate = earliestDate;
    }

    public Date getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(Date latestDate) {
        this.latestDate = latestDate;
    }

    public void addValue(AnalysisItem analysisItem, Value value, InsightRequestMetadata insightRequestMetadata) {
        AnalysisDateDimension dateDim = (AnalysisDateDimension) analysisItem;
        dateDim.setDateLevel(AnalysisDateDimension.DAY_LEVEL);
        value = analysisItem.transformValue(value, insightRequestMetadata, false);
        if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            Date date = dateValue.getDate();
            if (earliestDate == null) {

                // It's the first Value we've received.

                earliestDate = date;
                latestDate = date;
            } else {
                if (date.compareTo(earliestDate) < 0) {
                    earliestDate = new Date(date.getTime() + insightRequestMetadata.getUtcOffset() * 60 * 1000);
                }
                if (date.compareTo(latestDate) > 0) {
                    latestDate = new Date(date.getTime() + insightRequestMetadata.getUtcOffset() * 60 * 1000);
                }
            }
        }
    }
}
