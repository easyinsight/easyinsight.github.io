package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisTypes;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.HTMLReportMetadata;

import java.util.*;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 9:44 AM
 */
public class WSTrendDefinition extends WSKPIDefinition {

    private long trendReportID;

    public long getTrendReportID() {
        return trendReportID;
    }

    public void setTrendReportID(long trendReportID) {
        this.trendReportID = trendReportID;
    }

    @Override
    public String getDataFeedType() {
        return AnalysisTypes.TREND;
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {
        return super.toHTML(targetDiv, htmlReportMetadata);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
