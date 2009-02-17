package com.easyinsight.benchmark;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Feb 14, 2009
 * Time: 10:29:35 PM
 */
public class BenchmarkManager {
    public static void recordBenchmark(String category, long time) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement benchmarkStmt = conn.prepareStatement("INSERT INTO BENCHMARK (CATEGORY, ELAPSED_TIME) VALUES (?, ?)");
            benchmarkStmt.setString(1, category);
            benchmarkStmt.setLong(2, time);
            benchmarkStmt.execute();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }
}
