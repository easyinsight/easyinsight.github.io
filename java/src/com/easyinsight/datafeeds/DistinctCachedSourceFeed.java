package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 11/18/13
 * Time: 1:03 PM
 */
public class DistinctCachedSourceFeed extends Feed {

    private long reportID;

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT data_source_id FROM cached_addon_report_source WHERE report_id = ?");
            stmt.setLong(1, reportID);
            ResultSet rs = stmt.executeQuery();

            WSAnalysisDefinition report;
            try {
                report = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            } catch (Exception e) {
                LogClass.error(e);
                return new DataSet();
            }
            Feed feed;
            if (rs.next()) {
                CachedAnalysisBasedFeed cachedFeed = new CachedAnalysisBasedFeed();
                cachedFeed.setAnalysisDefinition(report);
                cachedFeed.setReportFilters(insightRequestMetadata.getReportFilters());
                feed = cachedFeed;
            } else {
                AnalysisBasedFeed cachedFeed = new AnalysisBasedFeed();
                cachedFeed.setAnalysisDefinition(report);
                feed = cachedFeed;
            }
            stmt.close();
            return feed.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
