package com.easyinsight.analysis;

import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.StringValue;

import java.util.Comparator;

/**
 * User: James Boe
 * Date: Aug 26, 2008
 * Time: 12:32:36 PM
 */
public class RowComparator implements Comparator<IRow> {

    private Key field;
    private int ascending;

    public RowComparator(AnalysisItem field, boolean ascending) {
        this.field = field.createAggregateKey();
        this.ascending = ascending ? 1 : -1;
    }

    public int compare(IRow row1, IRow row2) {
        Value value1 = row1.getValue(field);
        Value value2 = row2.getValue(field);
        if (value1.type() == Value.NUMBER && value2.type() == Value.NUMBER) {
            return value1.toDouble().compareTo(value2.toDouble()) * ascending;
        } else if (value1.type() == Value.DATE && value2.type() == Value.DATE) {
            DateValue date1 = (DateValue) value1;
            DateValue date2 = (DateValue) value2;
            return date1.getDate().compareTo(date2.getDate()) * ascending;
        } else if (value1.type() == Value.DATE && value2.type() == Value.EMPTY) {
            return ascending;
        } else if (value1.type() == Value.EMPTY && value2.type() == Value.DATE) {
            return -ascending;
        } else if (value1.type() == Value.STRING && value2.type() == Value.STRING) {
            StringValue stringValue1 = (StringValue) value1;
            StringValue stringValue2 = (StringValue) value2;
            return stringValue1.getValue().compareTo(stringValue2.getValue()) * ascending;
        } else if (value1.type() == Value.STRING && value2.type() == Value.EMPTY) {
            return -ascending;
        } else if (value1.type() == Value.EMPTY && value2.type() == Value.STRING) {
            return ascending;
        }
        return -1;
    }
}
