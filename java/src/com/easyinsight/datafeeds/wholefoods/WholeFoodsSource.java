package com.easyinsight.datafeeds.wholefoods;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Jun 15, 2010
 * Time: 2:12:02 PM
 */
public class WholeFoodsSource extends ServerDataSourceDefinition {

    public static final String STORE = "Store";
    public static final String QUANTITY = "Quantity";
    public static final String QUANTITY_UOM = "Quantity UOM";
    public static final String DATE = "Date";
    public static final String REGION = "Region";
    public static final String DOLLAR_SALES = "$ Sales";
    public static final String STORE_ID = "Store ID";
    public static final String LAST_YEAR_SALES = "$ Sales - Last Year";
    public static final String UNITS = "Unit Sales";
    public static final String LAST_YEAR_UNITS_SOLD = "Unit Sales - Last Year";
    public static final String ARP = "Average Retail Price";
    public static final String ITEM = "Item Name";
    public static final String ITEM_SKU = "Item SKU";

    private String wfUserName;
    private String wfPassword;
    private boolean initialized;
    private byte[] startFile;

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList( STORE, DATE, REGION, DOLLAR_SALES, STORE_ID, LAST_YEAR_SALES, UNITS,
                LAST_YEAR_UNITS_SOLD, ITEM, ITEM_SKU, ARP, QUANTITY, QUANTITY_UOM );
    }

    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public boolean isConfigured() {
        return wfUserName != null;
    }

    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        rollingFilterDefinition.setField(findAnalysisItem(WholeFoodsSource.DATE));
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.LAST_FULL_MONTH);
        kpis.add(KPIUtil.createKPIForDateFilter("Total Sales in the Last 30 Days", "symbol_dollar.png", (AnalysisMeasure) findAnalysisItemByDisplayName(DOLLAR_SALES),
                (AnalysisDateDimension) findAnalysisItem(DATE), MaterializedRollingFilterDefinition.MONTH,
                new ArrayList<FilterDefinition>(), KPI.GOOD, 30));
        return kpis;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        
        items.add(new AnalysisDimension(keys.get(STORE), true));
        items.add(new AnalysisDimension(keys.get(REGION), true));
        items.add(new AnalysisDimension(keys.get(ITEM), true));
        items.add(new AnalysisDimension(keys.get(ITEM_SKU), true));
        items.add(new AnalysisDimension(keys.get(STORE_ID), true));
        items.add(new AnalysisDimension(keys.get(QUANTITY), true));
        items.add(new AnalysisDimension(keys.get(QUANTITY_UOM), true));

        items.add(new AnalysisMeasure(keys.get(DOLLAR_SALES), DOLLAR_SALES, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisMeasure(keys.get(LAST_YEAR_SALES), LAST_YEAR_SALES, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisMeasure(keys.get(ARP), ARP, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisMeasure(keys.get(UNITS), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(LAST_YEAR_UNITS_SOLD), AggregationTypes.SUM));

        items.add(new AnalysisDateDimension(keys.get(DATE), true, AnalysisDateDimension.WEEK_LEVEL));

        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.WHOLE_FOODS;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PLUS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callID, Date lastRefreshDate) {
        DataSet dataSet = new DataSet();
        boolean firstRun = !initialized;
        try {
            if (!initialized) {
                PreparedStatement stmt = conn.prepareStatement("SELECT UPLOAD_FILE FROM whole_foods_source WHERE data_source_id = ?");
                stmt.setLong(1, getDataFeedID());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                byte[] file = rs.getBytes(1);
                dataSet = new WFCSVParse().createData(dataStorage, keys, file);
                startFile = null;
                initialized = true;
            } else {
                MessageQueue msgQueue = SQSUtils.connectToQueue("EIWFReport", "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
                PreparedStatement queryStmt = conn.prepareStatement("SELECT USER_KEY, USER_SECRET_KEY FROM USER WHERE USER_ID = ?");
                queryStmt.setLong(1, SecurityUtil.getUserID());
                ResultSet rs = queryStmt.executeQuery();
                rs.next();
                String userKey = rs.getString(1);
                String userSecretKey = rs.getString(2);
                String message = wfUserName + "," + wfPassword + "," + userKey + "," + userSecretKey + "," + getFeedName() + ",current";
                msgQueue.sendMessage(message);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return firstRun ? dataSet : null;
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        WholeFoodsSource source = (WholeFoodsSource) super.clone(conn);
        source.setWfUserName(null);
        source.setWfPassword(null);
        source.setInitialized(false);
        source.setStartFile(null);
        return source;
    }

    public byte[] getStartFile() {
        return startFile;
    }

    public void setStartFile(byte[] startFile) {
        this.startFile = startFile;
    }

    protected boolean clearsData() {
        return false;
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT username, wf_password, initialized FROM whole_foods_source WHERE data_source_id = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        rs.next();
        wfUserName = rs.getString(1);
        String string = rs.getString(2);
        if (string != null) {
            wfPassword = PasswordStorage.decryptString(string);
        }
        initialized = rs.getBoolean(3);
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM whole_foods_source WHERE data_source_id = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO whole_foods_source (data_source_id, username, wf_password, initialized, upload_file) values (?, ?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, wfUserName);
        insertStmt.setString(3, wfPassword != null ? PasswordStorage.encryptString(wfPassword) : null);
        insertStmt.setBoolean(4, initialized);
        if (startFile == null) {
            insertStmt.setNull(5, Types.BLOB);
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(startFile);
            BufferedInputStream bis = new BufferedInputStream(bais, 1024);
            insertStmt.setBinaryStream(5, bis, startFile.length);
        }
        insertStmt.execute();
    }

    public String validateCredentials() {
        return null;
    }

    public String getWfUserName() {
        return wfUserName;
    }

    public void setWfUserName(String wfUserName) {
        this.wfUserName = wfUserName;
    }

    public String getWfPassword() {
        return wfPassword;
    }

    public void setWfPassword(String wfPassword) {
        this.wfPassword = wfPassword;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
