package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.users.Account;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 11/18/13
 * Time: 10:39 AM
 */
public class DistinctCachedSource extends ServerDataSourceDefinition {

    private long reportID;

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM distinct_cached_addon_report_source WHERE data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO distinct_cached_addon_report_source (data_source_id, report_id) values (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        if (reportID > 0) {
            insertStmt.setLong(2, reportID);
        } else {
            insertStmt.setNull(2, Types.BIGINT);
        }
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        DistinctCachedSourceFeed feed = new DistinctCachedSourceFeed();
        feed.setReportID(reportID);
        return feed;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.DISTINCT_CACHED_ADDON;
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT report_id FROM distinct_cached_addon_report_source WHERE data_source_id = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            reportID = rs.getLong(1);
            if (rs.wasNull()) {
                reportID = 0;
            }
        }
        getStmt.close();
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        Map<String, AnalysisItem> structure = report.createStructure();
        for (AnalysisItem item : structure.values()) {
            AnalysisItem clone;
            if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension baseDate = (AnalysisDateDimension) item;
                AnalysisDateDimension date = new AnalysisDateDimension();
                date.setDateLevel(baseDate.getDateLevel());
                date.setOutputDateFormat(baseDate.getOutputDateFormat());
                date.setDateOnlyField(baseDate.isDateOnlyField() || baseDate.hasType(AnalysisItemTypes.DERIVED_DATE));
                clone = date;
            } else if (item.hasType(AnalysisItemTypes.MEASURE)) {
                AnalysisMeasure baseMeasure = (AnalysisMeasure) item;
                AnalysisMeasure measure = new AnalysisMeasure();
                measure.setFormattingType(item.getFormattingType());
                measure.setAggregation(baseMeasure.getAggregation());
                measure.setPrecision(baseMeasure.getPrecision());
                measure.setMinPrecision(baseMeasure.getMinPrecision());
                clone = measure;
            } else {
                clone = new AnalysisDimension();
            }
            clone.setOriginalDisplayName(item.toDisplay());
            clone.setDisplayName(item.toDisplay());
            clone.setUnqualifiedDisplayName(item.toUnqualifiedDisplay());
            clone.setBasedOnReportField(item.getAnalysisItemID());
            Key key = keys.get(clone.toDisplay());
            if (key == null) {
                key = new NamedKey(clone.toDisplay());
            }
            clone.setKey(key);
            items.add(clone);
        }
        return items;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }
}
