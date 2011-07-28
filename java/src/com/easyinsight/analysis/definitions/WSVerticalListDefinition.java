package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:51:51 PM
 */
public class WSVerticalListDefinition extends WSAnalysisDefinition {

    private List<AnalysisItem> measures;
    private AnalysisItem column;

    private long verticalListID;

    public long getVerticalListID() {
        return verticalListID;
    }

    public void setVerticalListID(long verticalListID) {
        this.verticalListID = verticalListID;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.VERTICAL_LIST;
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> items = new HashSet<AnalysisItem>();
        if (measures != null) {
            items.addAll(measures);
        }
        if (column != null) {
            items.add(column);
        }
        return items;
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("measures", measures, structure);
        if (column != null) {
            addItems("grouping", Arrays.asList(column), structure);
        }
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        measures = items("measures", structure);
        List<AnalysisItem> columns = items("grouping", structure);
        if (columns.size() > 0) {
            column = columns.get(0);
        }
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public AnalysisItem getColumn() {
        return column;
    }

    public void setColumn(AnalysisItem column) {
        this.column = column;
    }
}
