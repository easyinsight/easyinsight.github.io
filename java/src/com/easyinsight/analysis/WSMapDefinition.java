package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisTypes;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:24 PM
 */
public class WSMapDefinition extends WSGraphicDefinition {

    private long mapDefinitionID;
    private int mapType;

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public long getMapDefinitionID() {
        return mapDefinitionID;
    }

    public void setMapDefinitionID(long mapDefinitionID) {
        this.mapDefinitionID = mapDefinitionID;
    }

    public String getDataFeedType() {
        return AnalysisTypes.MAP;
    }
}
