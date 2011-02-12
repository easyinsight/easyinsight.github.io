package com.easyinsight.example;

import com.easyinsight.rowutil.*;

import javax.xml.datatype.DatatypeConfigurationException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 11:04:36 AM
 */
public class APICompositeExample {

    public static final String API_KEY = "";
    public static final String API_SECRET_KEY = "";

    public static void main(String[] args) throws UnknownHostException, DatatypeConfigurationException {
        String operatingSystemDataSourceAPIKey = defineOSDataSource();
        String jvmMetricsDataSourceAPIKey = defineJVMDataSource();

        CompositeDataSourceFactory compositeDataSourceFactory = APIUtil.defineCompositeDataSource("Composite Data Source", API_KEY, API_SECRET_KEY);
        compositeDataSourceFactory.addDataSource(operatingSystemDataSourceAPIKey);
        compositeDataSourceFactory.addDataSource(jvmMetricsDataSourceAPIKey);
        compositeDataSourceFactory.joinDataSources(operatingSystemDataSourceAPIKey, jvmMetricsDataSourceAPIKey,
                "Host", "Host");
        compositeDataSourceFactory.defineCompositeSource();
    }

    private static String defineOSDataSource() throws UnknownHostException, DatatypeConfigurationException {

        String operatingSystem = ManagementFactory.getOperatingSystemMXBean().getName();
        String hostAddress = InetAddress.getLocalHost().getHostAddress();

        DataSourceFactory dataSourceFactory = APIUtil.defineDataSource("JVM OS Info", API_KEY, API_SECRET_KEY);

        dataSourceFactory.addGrouping("Host");
        dataSourceFactory.addGrouping("Operating System");

        DataSourceOperationFactory dataSourceOperationFactory = dataSourceFactory.defineDataSource();
        DataSourceTarget dataSourceTarget = dataSourceOperationFactory.addRowsOperation();
        DataRow dataRow = dataSourceTarget.newRow();

        dataRow.addValue("Host", hostAddress);
        dataRow.addValue("Operating System", operatingSystem);
        dataSourceTarget.flush();

        return dataSourceOperationFactory.getDataSourceKey();
    }

    private static String defineJVMDataSource() throws UnknownHostException, DatatypeConfigurationException {

        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        long currentMemory = Runtime.getRuntime().totalMemory();
        int threadCount = ManagementFactory.getThreadMXBean().getThreadCount();
        Date now = new Date();

        DataSourceFactory dataSourceFactory = APIUtil.defineDataSource("Sample JVM Data", API_KEY, API_SECRET_KEY);

        dataSourceFactory.addGrouping("Host");
        dataSourceFactory.addMeasure("Memory");
        dataSourceFactory.addMeasure("Threads");
        dataSourceFactory.addDate("Date");

        DataSourceOperationFactory dataSourceOperationFactory = dataSourceFactory.defineDataSource();
        DataSourceTarget dataSourceTarget = dataSourceOperationFactory.addRowsOperation();

        DataRow dataRow = dataSourceTarget.newRow();

        dataRow.addValue("Host", hostAddress);
        dataRow.addValue("Memory", currentMemory);
        dataRow.addValue("Threads", threadCount);
        dataRow.addValue("Date", now);
        dataSourceTarget.flush();

        return dataSourceOperationFactory.getDataSourceKey();
    }
}
