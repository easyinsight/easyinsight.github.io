package test.core;

import junit.framework.TestCase;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.database.Database;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.DateValue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Feb 27, 2009
 * Time: 1:43:10 PM
 */
public class DateTimeTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public static long createDefaultTestDataSource() throws SQLException {
        UserUploadService userUploadService = new UserUploadService();

        //IRow row3 = dataSet.createRow();

        //IRow row4 = dataSet.createRow();
        long dataSourceID = userUploadService.createNewDefaultFeed("test");
        FeedStorage feedStorage = new FeedStorage();
        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataSourceID);
        feedDefinition.setFields(Arrays.asList(new AnalysisDimension("c", true), new AnalysisMeasure("m", AggregationTypes.SUM),
                new AnalysisDateDimension("d", true, AnalysisDateDimension.YEAR_LEVEL)));
        new FeedService().updateFeedDefinition(feedDefinition);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        DataSet dataSet = new DataSet();
        // row 1 = c1, 1/1/2009, 50
        IRow row1 = dataSet.createRow();
        row1.addValue(TestUtil.createKey("c", dataSourceID), "c1");
        row1.addValue(TestUtil.createKey("m", dataSourceID), new NumericValue(100));
        row1.addValue(TestUtil.createKey("d", dataSourceID), new DateValue(cal.getTime()));
        // row2 = c1, 2/1/2009, 100
        IRow row2 = dataSet.createRow();
        row2.addValue(TestUtil.createKey("c", dataSourceID), "c1");
        row2.addValue(TestUtil.createKey("m", dataSourceID), new NumericValue(100));
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 3);
        cal.set(Calendar.SECOND, 37);
        row2.addValue(TestUtil.createKey("d", dataSourceID), new DateValue(cal.getTime()));
        // row 3 = c1, 2/1/2009, 8:3:37
        IRow row3 = dataSet.createRow();
        row3.addValue(TestUtil.createKey("c", dataSourceID), "c1");
        row3.addValue(TestUtil.createKey("m", dataSourceID), new NumericValue(50));
        cal.set(Calendar.DAY_OF_MONTH, 3);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 3);
        cal.set(Calendar.SECOND, 37);
        row3.addValue(TestUtil.createKey("d", dataSourceID), new DateValue(cal.getTime()));
        // row 4 = c1, 2/3/2009, 8:3:37
        IRow row4 = dataSet.createRow();
        row4.addValue(TestUtil.createKey("c", dataSourceID), "c1");
        row4.addValue(TestUtil.createKey("m", dataSourceID), new NumericValue(100));
        cal.set(Calendar.HOUR_OF_DAY, 15);
        cal.set(Calendar.MINUTE, 3);
        cal.set(Calendar.SECOND, 37);
        row4.addValue(TestUtil.createKey("d", dataSourceID), new DateValue(cal.getTime()));
        IRow row5 = dataSet.createRow();
        row5.addValue(TestUtil.createKey("c", dataSourceID), "c1");
        row5.addValue(TestUtil.createKey("m", dataSourceID), new NumericValue(100));
        cal.set(Calendar.HOUR_OF_DAY, 15);
        cal.set(Calendar.MINUTE, 33);
        cal.set(Calendar.SECOND, 37);
        row5.addValue(TestUtil.createKey("d", dataSourceID), new DateValue(cal.getTime()));

        Connection conn = Database.instance().getConnection();
        try {
            DataStorage dataStorage = DataStorage.writeConnection(feedDefinition, conn);
            dataStorage.insertData(dataSet);
            dataStorage.commit();
        } finally {
            Database.instance().closeConnection(conn);
        }
        return dataSourceID;
    }

    private List<AnalysisItem> getColumns(int dateLevel, long dataSourceID) {
        return Arrays.asList(new AnalysisDimension(TestUtil.createKey("c", dataSourceID), true),
                new AnalysisMeasure(TestUtil.createKey("m", dataSourceID), AggregationTypes.SUM),
                new AnalysisDateDimension(TestUtil.createKey("d", dataSourceID), true,dateLevel));
    }

    private void validateResults(ListDataResults listDataResults, int rowSize) {
        assertEquals(rowSize, listDataResults.getRows().length);
    }

    public void testTimeUnits() throws SQLException {
        TestUtil.getIndividualTestUser();
        long dataSourceID = createDefaultTestDataSource();
        DataService dataService = new DataService();
        WSListDefinition yearDefinition = new WSListDefinition();
        yearDefinition.setDataFeedID(dataSourceID);
        yearDefinition.setColumns(getColumns(AnalysisDateDimension.YEAR_LEVEL, dataSourceID));
        ListDataResults yearResults = (ListDataResults) dataService.list(yearDefinition, new InsightRequestMetadata());
        validateResults(yearResults, 1);

        WSListDefinition monthDefinition = new WSListDefinition();
        monthDefinition.setDataFeedID(dataSourceID);
        monthDefinition.setColumns(getColumns(AnalysisDateDimension.MONTH_LEVEL, dataSourceID));
        ListDataResults monthResults = (ListDataResults) dataService.list(monthDefinition, new InsightRequestMetadata());
        validateResults(monthResults, 2);

        WSListDefinition dayDefinition = new WSListDefinition();
        dayDefinition.setDataFeedID(dataSourceID);
        dayDefinition.setColumns(getColumns(AnalysisDateDimension.DAY_LEVEL, dataSourceID));
        ListDataResults dayResults = (ListDataResults) dataService.list(dayDefinition, new InsightRequestMetadata());
        validateResults(dayResults, 3);

        WSListDefinition hourDefinition = new WSListDefinition();
        hourDefinition.setDataFeedID(dataSourceID);
        hourDefinition.setColumns(getColumns(AnalysisDateDimension.HOUR_LEVEL, dataSourceID));
        ListDataResults hourResults = (ListDataResults) dataService.list(hourDefinition, new InsightRequestMetadata());
        validateResults(hourResults, 4);

        WSListDefinition minuteDefinition = new WSListDefinition();
        minuteDefinition.setDataFeedID(dataSourceID);
        minuteDefinition.setColumns(getColumns(AnalysisDateDimension.MINUTE_LEVEL, dataSourceID));
        ListDataResults minuteResults = (ListDataResults) dataService.list(minuteDefinition, new InsightRequestMetadata());
        validateResults(minuteResults, 5);
    }

    public void testRollingDate() throws SQLException {
        TestUtil.getIndividualTestUser();
        long dataSourceID = createDeltaTestDataSource();
        DataService dataService = new DataService();
        WSListDefinition noDimDefinition = new WSListDefinition();
        noDimDefinition.setDataFeedID(dataSourceID);
        AnalysisMeasure measure = new AnalysisMeasure(TestUtil.createKey("m", dataSourceID), AggregationTypes.SUM);
        noDimDefinition.setColumns(Arrays.asList((AnalysisItem) measure));
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        rollingFilterDefinition.setField(TestUtil.getItem(dataSourceID, "d"));
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.LAST_DAY);
        noDimDefinition.setFilterDefinitions(Arrays.asList((FilterDefinition) rollingFilterDefinition));
        ListDataResults yearResults = (ListDataResults) dataService.list(noDimDefinition, new InsightRequestMetadata());
        assertEquals(1, yearResults.getRows().length);
        ListRow row = yearResults.getRows()[0];
        for (int i = 0; i < yearResults.getHeaders().length; i++) {
            if (yearResults.getHeaders()[i] == measure) {
                assertEquals(225, row.getValues()[i].toDouble(), .1);
            }
        }
    }

    public void testTemporalAggregations() throws SQLException {
        TestUtil.getIndividualTestUser();
        long dataSourceID = createDefaultTestDataSource();
        DataService dataService = new DataService();
        AnalysisMeasure measure = new AnalysisMeasure(TestUtil.createKey("m", dataSourceID), AggregationTypes.SUM);
        LastValueFilter lastValueFilter = new LastValueFilter(new AnalysisDateDimension(TestUtil.createKey("d", dataSourceID), true, AnalysisDateDimension.DAY_LEVEL));
        //AnalysisDateDimension dim = new AnalysisDateDimension(TestUtil.createKey("d", dataSourceID), true, AnalysisDateDimension.DAY_LEVEL);

        measure.setAggregation(AggregationTypes.LAST_VALUE);
        measure.setKey(TestUtil.createKey("m", dataSourceID));
        WSListDefinition dateDimDefinition = new WSListDefinition();
        dateDimDefinition.setDataFeedID(dataSourceID);
        dateDimDefinition.setFilterDefinitions(Arrays.asList((FilterDefinition) lastValueFilter));
        dateDimDefinition.setColumns(Arrays.asList(new AnalysisDimension(TestUtil.createKey("c", dataSourceID), true), measure));
        DataSet dataSet = dataService.listDataSet(dateDimDefinition, new InsightRequestMetadata());
        assertEquals(1, dataSet.getRows().size());
        for (IRow irow : dataSet.getRows()) {
            NumericValue numericValue = (NumericValue) irow.getValue(measure.createAggregateKey());
            assertEquals(100.0, numericValue.toDouble(), .001);
        }

        //assertEquals(2, results.getRows().length);
        /*for (int j = 0; j < results.getRows().length; j++) {
            row = results.getRows()[j];
            for (int i = 0; i < results.getHeaders().length; i++) {
                if (results.getHeaders()[i] == measure) {
                    System.out.println(row.getValues()[i]);
                }
            }
        }*/
    }

    public void testMultiDateLastValue() throws Exception {
        TestUtil.getIndividualTestUser();
        long dataSourceID = createDefaultTestDataSource();
        DataService dataService = new DataService();
        //TemporalAnalysisMeasure measure = new TemporalAnalysisMeasure();
        AnalysisDateDimension dim = new AnalysisDateDimension(TestUtil.createKey("d", dataSourceID), true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisMeasure measure = new AnalysisMeasure(TestUtil.createKey("m", dataSourceID), AggregationTypes.SUM);
        LastValueFilter lastValueFilter = new LastValueFilter(new AnalysisDateDimension(TestUtil.createKey("d", dataSourceID), true, AnalysisDateDimension.DAY_LEVEL));
        measure.setAggregation(AggregationTypes.LAST_VALUE);
        measure.setKey(TestUtil.createKey("m", dataSourceID));
        WSListDefinition dateDimDefinition = new WSListDefinition();
        dateDimDefinition.setDataFeedID(dataSourceID);
        dateDimDefinition.setColumns(Arrays.asList(new AnalysisDimension(TestUtil.createKey("c", dataSourceID), true),
                new AnalysisDateDimension(TestUtil.createKey("d", dataSourceID), true, AnalysisDateDimension.DAY_LEVEL), measure));
        dateDimDefinition.setFilterDefinitions(Arrays.asList((FilterDefinition) lastValueFilter));
        DataSet dataSet = dataService.listDataSet(dateDimDefinition, new InsightRequestMetadata());
        assertEquals(3, dataSet.getRows().size());
        for (IRow irow : dataSet.getRows()) {
            NumericValue numericValue = (NumericValue) irow.getValue(measure.createAggregateKey());
            assertEquals(100.0, numericValue.toDouble(), .001);
        }
    }

    public static long createDeltaTestDataSource() throws SQLException {
        UserUploadService userUploadService = new UserUploadService();

        //IRow row3 = dataSet.createRow();

        //IRow row4 = dataSet.createRow();
        long dataSourceID = userUploadService.createNewDefaultFeed("test");
        FeedStorage feedStorage = new FeedStorage();
        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataSourceID);
        feedDefinition.setFields(Arrays.asList(new AnalysisDimension("c", true), new AnalysisMeasure("m", AggregationTypes.SUM),
                new AnalysisDateDimension("d", true, AnalysisDateDimension.YEAR_LEVEL)));
        new FeedService().updateFeedDefinition(feedDefinition);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        DataSet dataSet = new DataSet();
        IRow row1 = dataSet.createRow();
        row1.addValue(TestUtil.createKey("c", dataSourceID), "c1");
        row1.addValue(TestUtil.createKey("m", dataSourceID), new NumericValue(50));
        row1.addValue(TestUtil.createKey("d", dataSourceID), new DateValue(cal.getTime()));
        IRow row2 = dataSet.createRow();
        row2.addValue(TestUtil.createKey("c", dataSourceID), "c1");
        row2.addValue(TestUtil.createKey("m", dataSourceID), new NumericValue(100));
        cal.set(Calendar.HOUR_OF_DAY, 8);
        row2.addValue(TestUtil.createKey("d", dataSourceID), new DateValue(cal.getTime()));
        IRow row3 = dataSet.createRow();
        row3.addValue(TestUtil.createKey("c", dataSourceID), "c1");
        row3.addValue(TestUtil.createKey("m", dataSourceID), new NumericValue(75));
        cal.set(Calendar.DAY_OF_MONTH, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        row3.addValue(TestUtil.createKey("d", dataSourceID), new DateValue(cal.getTime()));
        IRow row4 = dataSet.createRow();
        row4.addValue(TestUtil.createKey("c", dataSourceID), "c1");
        row4.addValue(TestUtil.createKey("m", dataSourceID), new NumericValue(150));
        cal.set(Calendar.HOUR_OF_DAY, 8);
        row4.addValue(TestUtil.createKey("d", dataSourceID), new DateValue(cal.getTime()));

        Connection conn = Database.instance().getConnection();
        try {
            DataStorage dataStorage = DataStorage.writeConnection(feedDefinition, conn);
            dataStorage.insertData(dataSet);
            dataStorage.commit();
        } finally {
            Database.instance().closeConnection(conn);
        }
        return dataSourceID;
    }
}
