package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * User: jamesboe
 * Date: Apr 11, 2010
 * Time: 9:19:41 PM
 */
public class DateRangePluginComponent implements IComponent {

    private FilterDateRangeDefinition filterDateRangeDefinition;
    private Date startDate;
    private Date endDate;

    public DateRangePluginComponent(FilterDateRangeDefinition filterDateRangeDefinition) {
        this.filterDateRangeDefinition = filterDateRangeDefinition;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (filterDateRangeDefinition.getStartDateDimension() != null) {
            Date date = null;
            Key key = filterDateRangeDefinition.getStartDateDimension().createAggregateKey();
            for (IRow row : dataSet.getRows()) {
                Value value = row.getValue(key);
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    if (dateValue.getDate() != null) {
                        date = dateValue.getDate();
                        break;
                    }
                }
            }
            if (date != null) {
                filterDateRangeDefinition.setStartDate(date);
                this.startDate = date;
            }
            pipelineData.getReportItems().remove(filterDateRangeDefinition.getStartDateDimension());
        }
        if (filterDateRangeDefinition.getEndDateDimension() != null) {
            Date date = null;
            Key key = filterDateRangeDefinition.getEndDateDimension().createAggregateKey();
            for (IRow row : dataSet.getRows()) {
                Value value = row.getValue(key);
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    if (dateValue.getDate() != null) {
                        date = dateValue.getDate();
                        break;
                    }
                }
            }
            if (date != null) {
                filterDateRangeDefinition.setEndDate(date);
                this.endDate = date;
            }
            pipelineData.getReportItems().remove(filterDateRangeDefinition.getEndDateDimension());
        }
        return dataSet;
    }

    private boolean findItem(AnalysisItem field, List<AnalysisItem> allRequestedAnalysisItems, List<AnalysisItem> allFields) {
        int found = 0;
        for (AnalysisItem item : allRequestedAnalysisItems) {
            List<AnalysisItem> items = item.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), false, false, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null));
            found += items.contains(field) ? 1 : 0;
        }
        return found > 0;
    }

    public void decorate(DataResults listDataResults) {
        listDataResults.getAdditionalProperties().put("xAxisMinimum", startDate);
        listDataResults.getAdditionalProperties().put("xAxisMaximum", endDate);
    }
}
