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
            System.out.println("comparing " + dateValue.getDate() + " to " + lowValue + " and " + highValue);
            allowed = dateValue.getDate().compareTo(lowValue) >= 0 && dateValue.getDate().compareTo(highValue) <= 0;
        }
        return allowed;
    }
}
