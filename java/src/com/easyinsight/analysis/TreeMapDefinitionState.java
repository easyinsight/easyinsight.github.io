package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Mar 31, 2009
 * Time: 10:06:02 AM
 */
@Entity
@Table(name="treemap_report")
public class TreeMapDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="treemap_report_id")
    private long treemapDefinitionID;

    public long getTreemapDefinitionID() {
        return treemapDefinitionID;
    }

    public void setTreemapDefinitionID(long treemapDefinitionID) {
        this.treemapDefinitionID = treemapDefinitionID;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSTreeMapDefinition wsTreeMapDefinition = new WSTreeMapDefinition();
        wsTreeMapDefinition.setTreeMapDefinitionID(treemapDefinitionID);
        return wsTreeMapDefinition;
    }
}