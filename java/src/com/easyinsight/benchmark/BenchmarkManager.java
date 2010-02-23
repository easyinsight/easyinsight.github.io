package com.easyinsight.benchmark;

import com.easyinsight.admin.HealthInfo;
import com.easyinsight.api.DatePair;
import com.easyinsight.api.NumberPair;
import com.easyinsight.api.Row;
import com.easyinsight.api.StringPair;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * User: James Boe
 * Date: Feb 14, 2009
 * Time: 10:29:35 PM
 */
public class BenchmarkManager {

    private static NumberPair createNumberPair(String name, Number value) {
        NumberPair numberPair = new NumberPair();
        numberPair.setKey(name);
        numberPair.setValue(value.doubleValue());
        return numberPair;
    }

    public static void internalMeasurement(HealthInfo healthInfo) {
        Row row = new Row();
        StringPair serverPair = new StringPair();
        serverPair.setKey("Server");
        serverPair.setValue(healthInfo.getServer());
        NumberPair currentMemoryPair = createNumberPair("Current Memory", healthInfo.getCurrentMemory());
        NumberPair freeMemoryPair = createNumberPair("Free Memory", healthInfo.getFreeMemory());
        NumberPair maxMemoryPair = createNumberPair("Max Memory", healthInfo.getMaxMemory());
        NumberPair threadCount = createNumberPair("Thread Count", healthInfo.getThreadCount());
        NumberPair activeDBConnections = createNumberPair("Active DB Connections", healthInfo.getActiveDBConnections());
        NumberPair idleDBConnections = createNumberPair("Idle DB Connections", healthInfo.getIdleDBConnections());
        NumberPair activeUsers = createNumberPair("Active Users", healthInfo.getActiveUsers());
        NumberPair majorCollectionCount = createNumberPair("Major Collection Count", healthInfo.getMajorCollectionCount());
        NumberPair majorCollectionTime = createNumberPair("Major Collection Time", healthInfo.getMajorCollectionTime());
        NumberPair minorCollectionCount = createNumberPair("Minor Collection Count", healthInfo.getMinorCollectionCount());
        NumberPair minorCollectionTime = createNumberPair("Minor Collection Time", healthInfo.getMinorCollectionTime());
        NumberPair clientCount = createNumberPair("Client Count", healthInfo.getClientCount());
        row.setStringPairs(new StringPair[] { serverPair});
        DatePair datePair = new DatePair();
        datePair.setKey("Date");
        datePair.setValue(new Date());
        row.setDatePairs(new DatePair[] { datePair} );
        NumberPair[] numberPairs = new NumberPair[] { currentMemoryPair, freeMemoryPair, maxMemoryPair, threadCount,
            activeDBConnections, idleDBConnections, activeUsers, majorCollectionCount, majorCollectionTime,
            minorCollectionCount, minorCollectionTime, clientCount};
        row.setNumberPairs(numberPairs);
        new InternalPublishService().addRow("Internal Monitoring", row);
    }

    public static void recordBenchmark(String category, long time) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement benchmarkStmt = conn.prepareStatement("INSERT INTO BENCHMARK (CATEGORY, ELAPSED_TIME, benchmark_date) VALUES (?, ?, ?)");
            benchmarkStmt.setString(1, category);
            benchmarkStmt.setLong(2, time);
            benchmarkStmt.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            benchmarkStmt.execute();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
