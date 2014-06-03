package com.easyinsight.analysis;

import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * User: James Boe
 * Date: Aug 26, 2008
 * Time: 12:32:36 PM
 */
public class RowComparator implements Comparator<IRow> {

    private List<SortKey> sortKeys;

    public RowComparator(AnalysisItem field, boolean ascending) {
        sortKeys = new ArrayList<>();
        sortKeys.add(new SortKey(field.createAggregateKey(), ascending ? 1 : -1));
    }

    public RowComparator() {
        sortKeys = new ArrayList<>();
    }

    public void addSortKey(AnalysisItem field, boolean ascending) {
        sortKeys.add(new SortKey(field.createAggregateKey(), ascending ? 1 : -1));
    }

    private static final Value NO_NUM = new NumericValue(Double.MIN_VALUE);
    private static final Value NO_STR = new StringValue("");
    private static final Value NO_DATE = new DateValue(new Date(0));

    public int compare(IRow row1, IRow row2) {
        int comparison = 0;
        for (SortKey sortKey : sortKeys) {
            Value value1 = row1.getValue(sortKey.key).toSortValue();
            Value value2 = row2.getValue(sortKey.key).toSortValue();
            if (value1.type() == Value.EMPTY) {
                if (value2.type() == Value.NUMBER) {
                    value1 = NO_NUM;
                } else if (value2.type() == Value.STRING) {
                    value1 = NO_STR;
                } else if (value2.type() == Value.DATE) {
                    value1 = NO_DATE;
                }
            }
            if (value2.type() == Value.EMPTY) {
                if (value1.type() == Value.NUMBER) {
                    value2 = NO_NUM;
                } else if (value1.type() == Value.STRING) {
                    value2 = NO_STR;
                } else if (value1.type() == Value.DATE) {
                    value2 = NO_DATE;
                }
            }
            if (value1.type() == Value.NUMBER && value2.type() == Value.NUMBER) {
                comparison = value1.toDouble().compareTo(value2.toDouble()) * sortKey.ascending;
            } else if (value1.type() == Value.DATE && value2.type() == Value.DATE) {
                DateValue date1 = (DateValue) value1;
                DateValue date2 = (DateValue) value2;
                comparison = date1.getDate().compareTo(date2.getDate()) * sortKey.ascending;
            } else if (value1.type() == Value.STRING && value2.type() == Value.STRING) {
                StringValue stringValue1 = (StringValue) value1;
                StringValue stringValue2 = (StringValue) value2;
                comparison = stringValue1.getValue().compareTo(stringValue2.getValue()) * sortKey.ascending;
            }
            if (comparison != 0) {
                break;
            }
        }
        return comparison;
    }

    private static class SortKey {
        private Key key;
        private int ascending;

        private SortKey(Key key, int ascending) {
            this.key = key;
            this.ascending = ascending;
        }
    }
}
