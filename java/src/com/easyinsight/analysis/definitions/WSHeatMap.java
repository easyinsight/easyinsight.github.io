package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.WSAnalysisDefinition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Dec 3, 2009
 * Time: 10:12:43 AM
 */
public class WSHeatMap extends WSAnalysisDefinition {

    private AnalysisItem latitudeItem;
    private AnalysisItem longitudeItem;
    private AnalysisItem measure;

    @Override
    public String getDataFeedType() {
        return "Heatmap";
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        return new HashSet<AnalysisItem>(Arrays.asList(latitudeItem, longitudeItem, measure));
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("latitudeItem", Arrays.asList(latitudeItem), structure);
        addItems("longitudeItem", Arrays.asList(longitudeItem), structure);
        addItems("measure", Arrays.asList(measure), structure);
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        latitudeItem = firstItem("latitudeItem", structure);
        longitudeItem = firstItem("longitudeItem", structure);
        measure = firstItem("measure", structure);
    }
}
