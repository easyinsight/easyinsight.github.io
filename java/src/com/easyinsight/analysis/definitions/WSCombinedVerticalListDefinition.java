package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 7/14/11
 * Time: 4:24 PM
 */
public class WSCombinedVerticalListDefinition extends WSAnalysisDefinition {

    private List<WSAnalysisDefinition> reports;

    private long combinedVerticalListDefinitionID;

    private int headerWidth;
    private int columnWidth;

    public int getHeaderWidth() {
        return headerWidth;
    }

    public void setHeaderWidth(int headerWidth) {
        this.headerWidth = headerWidth;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public long getCombinedVerticalListDefinitionID() {
        return combinedVerticalListDefinitionID;
    }

    public void setCombinedVerticalListDefinitionID(long combinedVerticalListDefinitionID) {
        this.combinedVerticalListDefinitionID = combinedVerticalListDefinitionID;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.COMBINED_VERTICAL_LIST;
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        return new HashSet<AnalysisItem>();
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
    }

    public List<WSAnalysisDefinition> getReports() {
        return reports;
    }

    public void setReports(List<WSAnalysisDefinition> reports) {
        this.reports = reports;
    }

     @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        headerWidth = (int) findNumberProperty(properties, "headerWidth", 140);
        columnWidth = (int) findNumberProperty(properties, "columnWidth", 73);
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("headerWidth", headerWidth));
        properties.add(new ReportNumericProperty("columnWidth", columnWidth));
        return properties;
    }
}
