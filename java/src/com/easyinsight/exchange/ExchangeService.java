package com.easyinsight.exchange;

import com.easyinsight.database.Database;

import com.easyinsight.logging.LogClass;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * User: jamesboe
 * Date: Sep 14, 2009
 * Time: 7:26:38 PM
 */
public class ExchangeService {
    public List<ReportExchangeItem> getReports(String tag) {
        List<ReportExchangeItem> exchangeItems = new ArrayList<ReportExchangeItem>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement analysisQueryStmt;
            PreparedStatement getTagsStmt = conn.prepareStatement("SELECT TAG FROM ANALYSIS_TO_TAG, ANALYSIS_TAGS WHERE " +
                    "ANALYSIS_TO_TAG.analysis_tags_id = ANALYSIS_TAGS.analysis_tags_id AND analysis_to_tag.analysis_id = ?");
            if (tag == null) {
                analysisQueryStmt = conn.prepareStatement("SELECT DISTINCT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE, avg(USER_REPORT_RATING.rating)," +
                        "analysis.create_date, data_feed.attribution, ANALYSIS.DESCRIPTION, DATA_FEED.FEED_NAME, ANALYSIS.AUTHOR_NAME," +
                        "DATA_FEED.MARKETPLACE_VISIBLE FROM DATA_FEED, ANALYSIS " +
                        " LEFT JOIN USER_REPORT_RATING ON USER_REPORT_RATING.report_id = ANALYSIS.ANALYSIS_ID WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                        "((DATA_FEED.MARKETPLACE_VISIBLE = ? AND ANALYSIS.FEED_VISIBILITY = ?) OR ANALYSIS.MARKETPLACE_VISIBLE = ?) AND " +
                        "ANALYSIS.ROOT_DEFINITION = ? GROUP BY ANALYSIS.ANALYSIS_ID");
                analysisQueryStmt.setBoolean(1, true);
                analysisQueryStmt.setBoolean(2, true);
                analysisQueryStmt.setBoolean(3, true);
                analysisQueryStmt.setBoolean(4, false);
            } else {
                analysisQueryStmt = conn.prepareStatement("SELECT DISTINCT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE FROM ANALYSIS, DATA_FEED, " +
                        "FEED_TO_TAG, ANALYSIS_TAGS, ANALYSIS_TO_TAG WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                        "((DATA_FEED.MARKETPLACE_VISIBLE = ? AND ANALYSIS.FEED_VISIBILITY = ?) OR ANALYSIS.MARKETPLACE_VISIBLE = ?) AND " +
                        "((FEED_TO_TAG.FEED_ID = DATA_FEED.DATA_FEED_ID AND FEED_TO_TAG.ANALYSIS_TAGS_ID = ANALYSIS_TAGS.ANALYSIS_TAGS_ID AND ANALYSIS_TAGS.TAG = ?) OR " +
                        "(ANALYSIS_TO_TAG.ANALYSIS_ID = ANALYSIS.ANALYSIS_ID AND ANALYSIS_TO_TAG.ANALYSIS_TAGS_ID = ANALYSIS_TAGS.ANALYSIS_TAGS_ID AND ANALYSIS_TAGS.TAG = ?))" +
                        "AND ANALYSIS.ROOT_DEFINITION = ? ORDER BY ANALYSIS.VIEWS DESC");
                analysisQueryStmt.setBoolean(1, true);
                analysisQueryStmt.setBoolean(2, true);
                analysisQueryStmt.setBoolean(3, true);
                analysisQueryStmt.setString(4, tag);
                analysisQueryStmt.setString(5, tag);
                analysisQueryStmt.setBoolean(6, false);
            }
            ResultSet analysisRS = analysisQueryStmt.executeQuery();
            while (analysisRS.next()) {
                long analysisID = analysisRS.getLong(1);
                if (analysisID == 0) {
                    continue;
                }
                String title = analysisRS.getString(2);
                long dataSourceID = analysisRS.getLong(3);
                int reportType = analysisRS.getInt(4);
                double ratingAverage = analysisRS.getDouble(5);

                Date created = null;
                java.sql.Date date = analysisRS.getDate(6);
                if (date != null) {
                    created = new Date(date.getTime());
                }
                
                String attribution = analysisRS.getString(7);
                String description = analysisRS.getString(8);
                String dataSourceName = analysisRS.getString(9);
                String authorName = analysisRS.getString(10);
                boolean accessible = analysisRS.getBoolean(11);
                getTagsStmt.setLong(1, analysisID);
                ReportExchangeItem item = new ReportExchangeItem(title, analysisID, reportType, dataSourceID, attribution, ratingAverage, 0, created, description, authorName, dataSourceName,
                        accessible);
                exchangeItems.add(item);
                ResultSet tagRS = getTagsStmt.executeQuery();
                List<String> tags = new ArrayList<String>();
                while (tagRS.next()) {
                    tags.add(tagRS.getString(1));
                }
                item.setTags(tags);                
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return exchangeItems;
    }
}
