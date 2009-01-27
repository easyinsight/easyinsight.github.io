package com.easyinsight.analysis;

import com.easyinsight.webservice.ShortAnalysisDefinition;
import com.easyinsight.AnalysisTypes;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:16 PM
 */
public class WSYahooMapDefinition extends WSGraphicDefinition {

    private long yahooMapDefinitionID;

    public long getYahooMapDefinitionID() {
        return yahooMapDefinitionID;
    }

    public void setYahooMapDefinitionID(long yahooMapDefinitionID) {
        this.yahooMapDefinitionID = yahooMapDefinitionID;
    }

    public String getDataFeedType() {
        return AnalysisTypes.YAHOO_MAP;
    }

    public ShortAnalysisDefinition createShortAnalysisDefinition() {
        throw new UnsupportedOperationException();
    }
}
