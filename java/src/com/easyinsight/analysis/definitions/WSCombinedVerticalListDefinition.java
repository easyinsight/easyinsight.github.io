package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisTypes;
import com.easyinsight.analysis.WSAnalysisDefinition;

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
}
