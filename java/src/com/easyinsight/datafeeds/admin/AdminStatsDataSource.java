package com.easyinsight.datafeeds.admin;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Account;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.DateValue;
import com.easyinsight.analysis.*;
import com.easyinsight.admin.AdminService;
import com.easyinsight.admin.HealthInfo;
import com.easyinsight.storage.DataStorage;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

/**
 * User: James Boe
 * Date: Apr 8, 2009
 * Time: 10:18:48 AM
 */
public class AdminStatsDataSource extends ServerDataSourceDefinition {

    public static final String MAX_MEMORY = "Max Memory";
    public static final String TOTAL_MEMORY = "Allocated Memory";
    public static final String FREE_UNALLOCATED = "Free Unallocated Memory";
    public static final String FREE_MEMORY = "Free Memory";
    public static final String CURRENT_MEMORY = "Current Memory";
    public static final String THREAD_COUNT = "Thread Count";
    public static final String SYSTEM_LOAD = "System Load Average";
    public static final String COMPILATION_TIME = "Compilation Time";
    public static final String MINOR_COLLECTION_COUNT = "Minor Collection Count";
    public static final String MINOR_COLLECTION_TIME = "Minor Collection Time";
    public static final String MAJOR_COLLECTION_COUNT = "Major Collection Count";
    public static final String MAJOR_COLLECTION_TIME = "Major Collection Time";
    public static final String CLIENT_COUNT = "Client Count";
    public static final String SERVER = "Server";
    public static final String DATE = "Date";

    public int getRequiredAccountTier() {
        return Account.ADMINISTRATOR;
    }

    public FeedType getFeedType() {
        return FeedType.ADMIN_STATS;
    }

    public int getCredentialsDefinition() {
        return 0;
    }

    public String validateCredentials(Credentials credentials) {
        return null;
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        HealthInfo healthInfo = new AdminService().getHealthInfo();
        DataSet dataSet = new DataSet();
        IRow row = dataSet.createRow();
        row.addValue(keys.get(DATE), new DateValue(new Date()));
        row.addValue(keys.get(MAX_MEMORY), healthInfo.getMaxMemory());
        row.addValue(keys.get(TOTAL_MEMORY), healthInfo.getCurrentMemory() + healthInfo.getFreeMemory());
        row.addValue(keys.get(FREE_UNALLOCATED), healthInfo.getMaxMemory() - (healthInfo.getCurrentMemory() + healthInfo.getFreeMemory()));
        row.addValue(keys.get(FREE_MEMORY), healthInfo.getFreeMemory());
        row.addValue(keys.get(CURRENT_MEMORY), healthInfo.getCurrentMemory());
        row.addValue(keys.get(THREAD_COUNT), healthInfo.getThreadCount());
        row.addValue(keys.get(SYSTEM_LOAD), healthInfo.getSystemLoadAverage());
        row.addValue(keys.get(COMPILATION_TIME), healthInfo.getCompilationTime());
        row.addValue(keys.get(MINOR_COLLECTION_COUNT), healthInfo.getMinorCollectionCount());
        row.addValue(keys.get(MINOR_COLLECTION_TIME), healthInfo.getMinorCollectionTime());
        row.addValue(keys.get(MAJOR_COLLECTION_COUNT), healthInfo.getMajorCollectionCount());
        row.addValue(keys.get(MAJOR_COLLECTION_TIME), healthInfo.getMajorCollectionTime());
        row.addValue(keys.get(CLIENT_COUNT), healthInfo.getClientCount());
        row.addValue(keys.get(SERVER), healthInfo.getServer());
        return dataSet;
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(MAX_MEMORY, TOTAL_MEMORY, FREE_UNALLOCATED, FREE_MEMORY, CURRENT_MEMORY, THREAD_COUNT,
                SYSTEM_LOAD, COMPILATION_TIME, MINOR_COLLECTION_COUNT, MINOR_COLLECTION_TIME, MAJOR_COLLECTION_COUNT,
                MAJOR_COLLECTION_TIME, CLIENT_COUNT, SERVER, DATE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(SERVER), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(DATE), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(MAX_MEMORY, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(TOTAL_MEMORY, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(FREE_UNALLOCATED, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(FREE_MEMORY, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(CURRENT_MEMORY, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(THREAD_COUNT, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(SYSTEM_LOAD, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(COMPILATION_TIME, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(MINOR_COLLECTION_COUNT, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(MINOR_COLLECTION_TIME, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(MAJOR_COLLECTION_COUNT, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(MAJOR_COLLECTION_TIME, AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(CLIENT_COUNT, AggregationTypes.AVERAGE));
        return analysisItems;
    }

    public void customStorage(Connection conn) throws SQLException {
    }

    public void customLoad(Connection conn) throws SQLException {
    }

    protected void addData(DataStorage dataStorage, DataSet dataSet) throws SQLException {
        dataStorage.insertData(dataSet);
    }

    public boolean isConfigured() {
        return true;
    }
}
