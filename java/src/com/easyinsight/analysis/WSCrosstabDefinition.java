package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 7:33:07 PM
 */
public class WSCrosstabDefinition extends WSAnalysisDefinition {

    private Collection<AnalysisItem> columns;
    private Collection<AnalysisItem> rows;
    private Collection<AnalysisItem> measures;
    private Long crosstabDefinitionID;

    public Long getCrosstabDefinitionID() {
        return crosstabDefinitionID;
    }

    public void setCrosstabDefinitionID(Long crosstabDefinitionID) {
        this.crosstabDefinitionID = crosstabDefinitionID;
    }

    public Collection<AnalysisItem> getColumns() {
        return columns;
    }

    public void setColumns(Collection<AnalysisItem> columns) {
        this.columns = columns;
    }

    public Collection<AnalysisItem> getRows() {
        return rows;
    }

    public void setRows(Collection<AnalysisItem> rows) {
        this.rows = rows;
    }

    public Collection<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(Collection<AnalysisItem> measures) {
        this.measures = measures;
    }

    public String getDataFeedType() {
        return "Crosstab";
    }

    public List<AnalysisItem> getAllAnalysisItems() {
        List<AnalysisItem> AnalysisItems = new ArrayList<AnalysisItem>();
        AnalysisItems.addAll(columns);
        AnalysisItems.addAll(rows);
        AnalysisItems.addAll(measures);
        return AnalysisItems;
    }
}
