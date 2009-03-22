package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;

import java.util.*;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:33:21 PM
 */
public abstract class WSGraphicDefinition extends WSAnalysisDefinition {
    private Long graphicDefinitionID;

    public Long getGraphicDefinitionID() {
        return graphicDefinitionID;
    }

    public void setGraphicDefinitionID(Long graphicDefinitionID) {
        this.graphicDefinitionID = graphicDefinitionID;
    }
}
