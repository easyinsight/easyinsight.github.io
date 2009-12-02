package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import com.easyinsight.pipeline.ResultsBridge;
import com.easyinsight.pipeline.TimelineResultsBridge;
import com.easyinsight.sequence.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Nov 25, 2009
 * Time: 2:18:35 PM
 */
public class WSTimeline extends WSAnalysisDefinition {
    private long timelineID;
    private WSAnalysisDefinition report;
    private Sequence sequence;

    public long getTimelineID() {
        return timelineID;
    }

    public void setTimelineID(long timelineID) {
        this.timelineID = timelineID;
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public String getDataFeedType() {
        return "Timeline";
    }

    @Override
    public List<FilterDefinition> retrieveFilterDefinitions() {
        List<FilterDefinition> filters = report.retrieveFilterDefinitions();
        for (FilterDefinition filter : filters) {
            if (filter.getField().getKey().equals(sequence.getAnalysisItem().getKey())) {
                filter.setField(sequence.toAnalysisItem());
            }
        }
        return filters;
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> items = report.getAllAnalysisItems();
        items.add(sequence.toAnalysisItem());
        return items;
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        //report.createReportStructure(structure);
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        //report.populateFromReportStructure(structure);
    }

    public boolean hasCustomResultsBridge() {
        return true;
    }

    public ResultsBridge getCustomResultsBridge() {
        List<AnalysisDimension> dimensions = new ArrayList<AnalysisDimension>();
        List<AnalysisMeasure> measures = new ArrayList<AnalysisMeasure>();
        for (AnalysisItem analysisItem : getAllAnalysisItems()) {
            if (analysisItem.getType() == AnalysisItemTypes.DIMENSION) {
                dimensions.add((AnalysisDimension) analysisItem);
            } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                measures.add((AnalysisMeasure) analysisItem);
            }
        }
        return new TimelineResultsBridge(this, dimensions, measures);
    }
}
