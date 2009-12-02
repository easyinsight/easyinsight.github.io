package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;

import java.util.Set;
import java.util.Map;

/**
 * User: James Boe
 * Date: Apr 17, 2009
 * Time: 9:24:04 AM
 */
public class WSPluggableDefinition extends WSAnalysisDefinition {

    

    public String getDataFeedType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
