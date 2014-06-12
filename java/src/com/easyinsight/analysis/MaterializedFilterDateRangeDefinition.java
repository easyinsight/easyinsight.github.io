package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:59:25 PM
 */
public class MaterializedFilterDateRangeDefinition extends MaterializedFilterDefinition {

    private Date lowValue;
    private Date highValue;

    public MaterializedFilterDateRangeDefinition(AnalysisItem key, Date lowValue, Date highValue, boolean sliding) {
        super(key);
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    public boolean allows(Value value) {
        boolean allowed = false;        
        if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            allowed = dateValue.getDate().compareTo(lowValue) >= 0 && dateValue.getDate().compareTo(highValue) <= 0;
        }
        return allowed;
    }

    @Override
    public void log(InsightRequestMetadata insightRequestMetadata, FilterDefinition filterDefinition) {
        insightRequestMetadata.addAudit(filterDefinition, "Start date on processing in memory is " + (((AnalysisDateDimension) filterDefinition.getField()).isTimeshift(insightRequestMetadata) ? " time shifted " : " not time shifted ") + " at query to " +  lowValue);
        insightRequestMetadata.addAudit(filterDefinition, "End date on processing in memory is " + (((AnalysisDateDimension) filterDefinition.getField()).isTimeshift(insightRequestMetadata) ? " time shifted " : " not time shifted ") + " at query to " +  highValue);
    }
}
