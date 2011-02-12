package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: jamesboe
 * Date: Sep 15, 2010
 * Time: 4:30:07 PM
 */
public abstract class SeleniumPostProcessor {

    public static final int EMAIL = 1;
    public static final int HTML = 2;

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long save(EIConnection conn) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO selenium_processor (processor_type) values (?)",
                Statement.RETURN_GENERATED_KEYS);
        insertStmt.setInt(1, getProcessorType());
        insertStmt.execute();
        return Database.instance().getAutoGenKey(insertStmt);
    }

    public abstract int getProcessorType();

    public static SeleniumPostProcessor loadProcessor(long processorID, EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT PROCESSOR_TYPE FROM SELENIUM_PROCESSOR WHERE " +
                "SELENIUM_PROCESSOR_ID = ?");
        queryStmt.setLong(1, processorID);
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        int type = rs.getInt(1);
        SeleniumPostProcessor processor;
        if (type == EMAIL) {
            processor = EmailSeleniumPostProcessor.load(processorID, conn);
        } else if (type == HTML) {
            processor = HtmlSeleniumPostProcessor.load(processorID, conn);
        } else {
            throw new RuntimeException();
        }
        processor.setId(processorID);
        return processor;
    }

    public abstract void process(byte[] bytes, EIConnection conn, long accountID);
}
