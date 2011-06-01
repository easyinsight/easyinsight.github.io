package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisTypes;
import com.easyinsight.analysis.WSAnalysisDefinition;

import java.util.*;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:51:51 PM
 */
public class WSVerticalListDefinition extends WSAnalysisDefinition {

    private List<AnalysisItem> measures;
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
        if (measures == null) {
            measures = new ArrayList<AnalysisItem>();
        }
        items.addAll(measures);
        return items;
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("measures", measures, structure);
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        measures = items("measures", structure);
    }

}
