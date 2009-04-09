package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:33:21 PM
 */
public abstract class WSGraphicDefinition extends WSAnalysisDefinition {
    private long graphicDefinitionID;

    public long getGraphicDefinitionID() {
        return graphicDefinitionID;
    }

    public void setGraphicDefinitionID(long graphicDefinitionID) {
        this.graphicDefinitionID = graphicDefinitionID;
    }
}
