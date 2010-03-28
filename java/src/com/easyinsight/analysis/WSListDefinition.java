package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.ListSummaryComponent;
import com.easyinsight.pipeline.MinMaxComponent;

import java.util.*;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 10:19:11 AM
 */
public class WSListDefinition extends WSAnalysisDefinition {
    private List<AnalysisItem> columns;
    private boolean showLineNumbers = false;
    private ListLimitsMetadata listLimitsMetadata;
    private boolean summaryTotal;
    private long listDefinitionID;

    public boolean isSummaryTotal() {
        return summaryTotal;
    }

    public void setSummaryTotal(boolean summaryTotal) {
        this.summaryTotal = summaryTotal;
    }

    public long getListDefinitionID() {
        return listDefinitionID;
    }

    public void setListDefinitionID(long listDefinitionID) {
        this.listDefinitionID = listDefinitionID;
    }

    public ListLimitsMetadata getListLimitsMetadata() {
        return listLimitsMetadata;
    }

    public void setListLimitsMetadata(ListLimitsMetadata listLimitsMetadata) {
        this.listLimitsMetadata = listLimitsMetadata;
    }

    public List<AnalysisItem> getColumns() {
        return columns;
    }

    public void setColumns(List<AnalysisItem> columns) {
        this.columns = columns;
    }

    public boolean isShowLineNumbers() {
        return showLineNumbers;
    }

    public void setShowLineNumbers(boolean showLineNumbers) {
        this.showLineNumbers = showLineNumbers;
    }

    public String getDataFeedType() {
        return "List";
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        for (AnalysisItem item : columns) {
            columnList.add(item);
        }
        columnList.addAll(getLimitFields());
        return columnList;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("", getColumns(), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        setColumns(items("", structure));
    }

    public LimitsResults applyLimits(DataSet dataSet) {
        LimitsResults limitsResults;
        if (listLimitsMetadata != null) {
            int count = dataSet.getRows().size();
            limitsResults = new LimitsResults(count >= listLimitsMetadata.getNumber(), count, listLimitsMetadata.getNumber());
            if (listLimitsMetadata.getAnalysisItem() != null) {
                dataSet.sort(listLimitsMetadata.getAnalysisItem(), listLimitsMetadata.isTop());    
            }
            dataSet.subset(listLimitsMetadata.getNumber());
        } else {
            limitsResults = super.applyLimits(dataSet);
        }
        return limitsResults;
    }

    public List<AnalysisItem> getLimitFields() {
        if (listLimitsMetadata != null && listLimitsMetadata.getAnalysisItem() != null) {
            return Arrays.asList(listLimitsMetadata.getAnalysisItem());
        } else {
            return super.getLimitFields();
        }
    }

    @Override
    public List<IComponent> createComponents() {
        List<IComponent> components = super.createComponents();
        if (summaryTotal) {
            components.add(new ListSummaryComponent());
        }
        return components;
    }
}
