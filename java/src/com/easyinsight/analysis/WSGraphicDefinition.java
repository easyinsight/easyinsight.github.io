package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:33:21 PM
 */
public abstract class WSGraphicDefinition extends WSAnalysisDefinition {
    private List<AnalysisItem> measures;
    private List<AnalysisItem> dimensions;
    private Long graphicDefinitionID;

    public Long getGraphicDefinitionID() {
        return graphicDefinitionID;
    }

    public void setGraphicDefinitionID(Long graphicDefinitionID) {
        this.graphicDefinitionID = graphicDefinitionID;
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public List<AnalysisItem> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<AnalysisItem> dimensions) {
        this.dimensions = dimensions;
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        for (AnalysisItem item : measures) {
            columnList.add(item);
        }
        columnList.addAll(dimensions);
        return columnList;
    }
}
