package com.easyinsight.export;

import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
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

    @Override
    public int getProcessorType() {
        return SeleniumPostProcessor.EMAIL;
    }

    @Override
    public void process(byte[] bytes, EIConnection conn, long accountID) {
        
    }
}
