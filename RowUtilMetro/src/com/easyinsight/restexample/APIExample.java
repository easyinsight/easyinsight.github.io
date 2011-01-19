package com.easyinsight.restexample;

import com.easyinsight.helper.*;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 11:04:36 AM
 */
public class APIExample {

    public static final String API_KEY = "jboe";
    public static final String API_SECRET_KEY = "St0rmrising";

    public static void main(String[] args) {
        try {
            // some sample data from the VM

            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            long currentMemory = Runtime.getRuntime().totalMemory();
            int threadCount = ManagementFactory.getThreadMXBean().getThreadCount();
            Date now = new Date();

            // First, we're going to define the data source metadata, giving the data source a name of Sample JVM Data

            DataSourceFactory dataSourceFactory = APIUtil.defineDataSource("Sample JVM REST Data", API_KEY, API_SECRET_KEY);

            // Next, we add some fields to the data source

            dataSourceFactory.addGrouping("Host");
            dataSourceFactory.addMeasure("Memory");
            dataSourceFactory.addMeasure("Threads");
            dataSourceFactory.addDate("Date");

            // Calling defineDataSource() will actually create the data source over at the Easy Insight side of things

            DataSourceOperationFactory dataSourceOperationFactory = dataSourceFactory.defineDataSource();

            // Now, we'll set ourselves up to actually pass some data in

            DataSourceTarget dataSourceTarget = dataSourceOperationFactory.addRowsOperation();

            // Create a new row of data

            DataRow dataRow = dataSourceTarget.newRow();

            // Populate each field on the row

            dataRow.addValue("Host", hostAddress);
            dataRow.addValue("Memory", currentMemory);
            dataRow.addValue("Threads", threadCount);
            dataRow.addValue("Date", now);

            // And finally, send the row of data to Easy Insight!

            dataSourceTarget.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
