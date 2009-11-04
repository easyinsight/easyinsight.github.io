package com.easyinsight.example;

import com.easyinsight.rowutil.RowMethod;
import com.easyinsight.rowutil.RowUtil;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 11:04:36 AM
 */
public class RowUtilExample {

    public static void main(String[] args) {
        try {
            // plug in your API key/secret key and data source name here
            String myAPIKey = "";
            String myAPISecretKey = "";
            String dataSourceName = "";

            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            long currentMemory = Runtime.getRuntime().totalMemory();
            int threadCount = ManagementFactory.getThreadMXBean().getThreadCount();
            Date now = new Date();

            RowUtil addData = new RowUtil(RowMethod.ADD, myAPIKey, myAPISecretKey, dataSourceName,
                    "Host", "Memory", "Threads", "Date");
            addData.newRow(hostAddress, currentMemory, threadCount, now);
            addData.flush();

            currentMemory = Runtime.getRuntime().maxMemory();

            RowUtil replaceData = new RowUtil(RowMethod.REPLACE, myAPIKey, myAPISecretKey, dataSourceName,
                    "Host", "Memory", "Threads", "Date");
            replaceData.newRow(hostAddress, currentMemory, threadCount, now);
            replaceData.flush();

            RowUtil whereData = new RowUtil(RowMethod.UPDATE, myAPIKey, myAPISecretKey, dataSourceName,
                    "Host", "Memory", "Threads", "Date");
            whereData.newRow(hostAddress, currentMemory, threadCount, now);
            whereData.where().and("Host", hostAddress);
            whereData.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
