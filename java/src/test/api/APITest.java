package test.api;

import junit.framework.TestCase;
import com.easyinsight.api.*;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.analysis.*;
import com.easyinsight.security.SecurityUtil;
import test.util.TestUtil;

import javax.xml.rpc.ServiceException;
import java.util.Calendar;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.rmi.RemoteException;
import java.io.File;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 1:34:34 PM
 */
public class APITest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        Database.initialize();
        deleteDataSource();
        FeedRegistry.initialize();
    }

    private void deleteDataSource() throws SQLException {
        System.out.println("deleting");
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM DATA_FEED WHERE data_feed.feed_name = ?");
            deleteStmt.setString(1, "testds1");
            deleteStmt.executeUpdate();
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    private long getDataSource(String apiKey) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED WHERE API_KEY = ?");
            queryStmt.setString(1, apiKey);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            return rs.getLong(1);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    private WSListDefinition getDefinition(long dataSourceID) {
        WSListDefinition def = new WSListDefinition();
        def.setDataFeedID(dataSourceID);
        List<AnalysisItem> items = Arrays.asList(new AnalysisDimension(TestUtil.createKey("string", dataSourceID), true),
                new AnalysisMeasure(TestUtil.createKey("number", dataSourceID), AggregationTypes.SUM),
                new AnalysisDateDimension(TestUtil.createKey("date", dataSourceID), true, AnalysisDateDimension.DAY_LEVEL));
        def.setColumns(items);
        return def;
    }

    private void validateAddRowResults(String apiKey) throws SQLException {
        long dataSourceID = getDataSource(apiKey);
        DataService dataService = new DataService();
        ListDataResults listResults = dataService.list(getDefinition(dataSourceID), new InsightRequestMetadata());
        assertEquals(1, listResults.getRows().length);
        ListRow row = listResults.getRows()[0];
        assertEquals(3, row.getValues().length);
    }

    private void validateAddRowsResults(String apiKey) throws SQLException {
        long dataSourceID = getDataSource(apiKey);
        DataService dataService = new DataService();
        ListDataResults listResults = dataService.list(getDefinition(dataSourceID), new InsightRequestMetadata());
        assertEquals(listResults.getRows().length, 2);
        ListRow row = listResults.getRows()[0];
        assertEquals(3, row.getValues().length);
    }

    private void validateUpdateRowResults(String apiKey) throws SQLException {
        long dataSourceID = getDataSource(apiKey);
        DataService dataService = new DataService();
        ListDataResults listResults = dataService.list(getDefinition(dataSourceID), new InsightRequestMetadata());
    }

    public void testBasicUnchecked() throws ServiceException, RemoteException, SQLException {
        long userID = TestUtil.getIndividualTestUser();
        TestUncheckedPublish service = new TestUncheckedPublish(userID, SecurityUtil.getSecurityProvider().getUserPrincipal().getAccountID());
        com.easyinsight.api.Row row = new com.easyinsight.api.Row();
        StringPair stringPair = new StringPair();
        stringPair.setKey("string");
        stringPair.setValue("value");
        NumberPair numberPair = new NumberPair();
        numberPair.setKey("number");
        numberPair.setValue(5);
        DatePair datePair = new DatePair();
        datePair.setKey("date");
        datePair.setValue(new Date());
        row.setStringPairs(new StringPair[] { stringPair });
        row.setNumberPairs(new NumberPair[] { numberPair });
        row.setDatePairs(new DatePair[] { datePair });
        String apiKey = service.addRow("testds1", row);
        validateAddRowResults(apiKey);
        com.easyinsight.api.Row[] rows = new com.easyinsight.api.Row[] { row };
        stringPair.setValue("value2");
        service.addRows("testds1", rows);
        validateAddRowsResults(apiKey);
        com.easyinsight.api.Row updateRow = new com.easyinsight.api.Row();
        NumberPair newNumberPair = new NumberPair();
        newNumberPair.setKey("number");
        newNumberPair.setValue(10);
        updateRow.setNumberPairs(new NumberPair[] { newNumberPair });
        updateRow.setDatePairs(new DatePair[] { datePair });
        stringPair = new StringPair();
        stringPair.setKey("string");
        stringPair.setValue("value");
        updateRow.setStringPairs(new StringPair[] { stringPair });
        Where where = new Where();
        StringWhere stringWhere = new StringWhere();
        stringWhere.setKey("string");
        stringWhere.setValue("value");
        where.setStringWheres(new StringWhere[] { stringWhere });
        service.updateRow("testds1", updateRow, where);
        DayWhere dayWhere = new DayWhere();
        dayWhere.setKey("date");
        Calendar cal = Calendar.getInstance();
        dayWhere.setYear(cal.get(Calendar.YEAR));
        dayWhere.setDayOfYear(cal.get(Calendar.DAY_OF_YEAR));
        where = new Where();
        where.setDayWheres(new DayWhere[] { dayWhere} );
        service.updateRow("testds1", updateRow, where);
    }

    public void testDatesAdd() throws SQLException {
        long userID = TestUtil.getIndividualTestUser();
        TestUncheckedPublish service = new TestUncheckedPublish(userID, SecurityUtil.getSecurityProvider().getUserPrincipal().getAccountID());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 5);
        com.easyinsight.api.Row yesterdayRow = new com.easyinsight.api.Row();
        DatePair datePair = new DatePair();
        datePair.setKey("date");
        datePair.setValue(cal.getTime());
        NumberPair numberPair = new NumberPair();
        numberPair.setKey("number");
        numberPair.setValue(10);
        yesterdayRow.setNumberPairs(new NumberPair[] { numberPair });
        yesterdayRow.setDatePairs(new DatePair[] { datePair });
        DayWhere dayWhere = new DayWhere();
        dayWhere.setKey("date");
        dayWhere.setDayOfYear(cal.get(Calendar.DAY_OF_YEAR));
        dayWhere.setYear(cal.get(Calendar.YEAR));
        Where where = new Where();
        where.setDayWheres(new DayWhere[] { dayWhere });
        service.updateRow("testds4", yesterdayRow, where);
        cal.add(Calendar.MINUTE, 1);
        datePair.setValue(cal.getTime());
        service.updateRow("testds4", yesterdayRow, where);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        datePair.setValue(cal.getTime());
        dayWhere.setDayOfYear(cal.get(Calendar.DAY_OF_YEAR));
        dayWhere.setYear(cal.get(Calendar.YEAR));
        service.updateRow("testds4", yesterdayRow, where);
        cal.add(Calendar.MINUTE, 1);
        datePair.setValue(cal.getTime());
        String apiKey = service.updateRow("testds4", yesterdayRow, where);
        long id = getDataSource(apiKey);
        WSListDefinition listDef = new WSListDefinition();
        listDef.setDataFeedID(id);
        listDef.setColumns(Arrays.asList(new AnalysisMeasure(TestUtil.createKey("number", id), AggregationTypes.SUM),
                new AnalysisDateDimension(TestUtil.createKey("date", id), true, AnalysisDateDimension.DAY_LEVEL)));
        ListDataResults results = new DataService().list(listDef, new InsightRequestMetadata());
        assertEquals(results.getRows().length, 2);
    }

    public void testDynamicAPI() {
        long userID = TestUtil.getIndividualTestUser();
        long dataSourceID = TestUtil.createDefaultTestDataSource(userID);
        APIService apiService = new APIService();
        apiService.setApiManager(new TestAPIManager());
        apiService.deployService(dataSourceID);
    }

    public static void main(String[] args) {
        String classpath = System.getProperty("java.class.path");
        String[] elements = classpath.split(File.pathSeparator);
        for (String element : elements) {
            if (element.contains("production")) {
                System.out.println(element);
            }
        }
        System.out.println(System.getProperties());
    }
}
