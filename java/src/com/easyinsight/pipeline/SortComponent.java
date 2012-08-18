package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.AnalysisItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 4, 2009
 * Time: 11:10:39 AM
 */
public class SortComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        final List<AnalysisItem> sortItems = new ArrayList<AnalysisItem>(pipelineData.getReportItems());
        Collections.sort(sortItems, new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                int sortSequence = analysisItem.getSortSequence();
                if (sortSequence == 0) {
                    sortSequence = Integer.MAX_VALUE;
                }
                int sortSequence2 = analysisItem1.getSortSequence();
                if (sortSequence2 == 0) {
                    sortSequence2 = Integer.MAX_VALUE;
                }
                if (sortSequence < sortSequence2) {
                    return -1;
                }
                if (sortSequence > sortSequence2) {
                    return 1;
                }
                return 0;
            }
        });
        Collections.sort(dataSet.getRows(), new Comparator<IRow>() {

            public int compare(IRow row1, IRow row2) {
                int i = 0;
                int comparison = 0;
                while (comparison == 0 && i < sortItems.size()) {
                    AnalysisItem field = sortItems.get(i);
                    comparison = getComparison(field, row1, row2);
                    i++;
                }
                return comparison;
            }
        });
        /*for (AnalysisItem analysisItem : sortItems) {
            if (analysisItem.getSort() > 0) {
                if (analysisItem.getSort() == 1) {
                    dataSet.sort(analysisItem, false);
                } else if (analysisItem.getSort() == 2) {
                    dataSet.sort(analysisItem, true);
                }
            }
        }*/
        return dataSet;
    }

    private int getComparison(AnalysisItem field, IRow row1, IRow row2) {
        int comparison = 0;
        int ascending = field.getSort() == 2 ? -1 : 1;
        Value value1;

            value1 = row1.getValue(field);

        Value value2;

            value2 = row2.getValue(field);


        if (value1.type() == Value.NUMBER && value2.type() == Value.NUMBER) {
            comparison = value1.toDouble().compareTo(value2.toDouble()) * ascending;
        } else if (value1.type() == Value.DATE && value2.type() == Value.DATE) {
            DateValue date1 = (DateValue) value1;
            DateValue date2 = (DateValue) value2;
            comparison = date1.getDate().compareTo(date2.getDate()) * ascending;
        } else if (value1.type() == Value.DATE && value2.type() == Value.EMPTY) {
            comparison = ascending;
        } else if (value1.type() == Value.EMPTY && value2.type() == Value.DATE) {
            comparison = -ascending;
        } else if (value1.type() == Value.STRING && value2.type() == Value.STRING) {
            StringValue stringValue1 = (StringValue) value1;
            StringValue stringValue2 = (StringValue) value2;
            comparison = stringValue1.getValue().compareTo(stringValue2.getValue()) * ascending;
        } else if (value1.type() == Value.STRING && value2.type() == Value.EMPTY) {
            comparison = -ascending;
        } else if (value1.type() == Value.EMPTY && value2.type() == Value.STRING) {
            comparison = ascending;
        }
        return comparison;
    }

    public void decorate(DataResults listDataResults) {
        
    }
}
