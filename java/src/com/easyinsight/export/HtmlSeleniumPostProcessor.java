package com.easyinsight.export;

import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Sep 15, 2010
 * Time: 4:31:01 PM
 */
public class HtmlSeleniumPostProcessor extends SeleniumPostProcessor {
    private long reportID;

    public HtmlSeleniumPostProcessor(long reportID) {
        this.reportID = reportID;
    }

    public long save(EIConnection conn) throws SQLException {
        long id = super.save(conn);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO html_selenium_processor (report_id, " +
                "selenium_processor_id) values (?, ?)");
        insertStmt.setLong(1, reportID);
        insertStmt.setLong(2, id);
        insertStmt.execute();
        return id;
    }

    public static SeleniumPostProcessor load(long processorID, EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT report_id FROM HTML_SELENIUM_PROCESSOR WHERE " +
                "SELENIUM_PROCESSOR_ID = ?");
        queryStmt.setLong(1, processorID);
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        long reportID = rs.getLong(1);
        return new HtmlSeleniumPostProcessor(reportID);
    }

    @Override
    public int getProcessorType() {
        return SeleniumPostProcessor.HTML;
    }

    @Override
    public void process(byte[] bytes, EIConnection conn, long accountID, long requestID) {
        // have to pass the data around to the appropriate point...
        HtmlResultCache.getInstance().addResults(bytes, requestID);
    }
}
