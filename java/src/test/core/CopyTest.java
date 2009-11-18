package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.AnalysisBasedFeedDefinition;
import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.userupload.UserUploadService;
import test.util.TestUtil;

import java.util.Calendar;
import java.util.Arrays;
import java.util.Date;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: May 27, 2009
 * Time: 4:42:03 PM
 */
public class CopyTest extends TestCase {

    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
        System.out.println("Setup - Active Connections: " + Database.instance().getActiveConnections());
    }

    protected void tearDown() throws Exception {
        System.out.println("TearDown - Active Connections: " + Database.instance().getActiveConnections());
    }

    public void testDataSourceCopy() throws SQLException {
        System.out.println("Test - Active Connections: " + Database.instance().getActiveConnections());
        TestUtil.getIndividualTestUser();
        AnalysisDateDimension startedDate = new AnalysisDateDimension("Started", true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisDateDimension endedDate = new AnalysisDateDimension("Completed", true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisDimension correlationDim = new AnalysisDimension("Correlation ID", true);
        AnalysisDimension otherDim = new AnalysisDimension("Other", true);
        AnalysisMeasure count = new AnalysisMeasure("Count", AggregationTypes.SUM);
        AnalysisStep analysisStep = new AnalysisStep();
        analysisStep.setCorrelationDimension(correlationDim);
        analysisStep.setStartDate(startedDate);
        analysisStep.setEndDate(endedDate);
        analysisStep.setGroup(true);
        analysisStep.setKey(new NamedKey("Step"));
        analysisStep.setDateLevel(AnalysisDateDimension.DAY_LEVEL);

        DataSet dataSet = new DataSet();
        IRow rowA = dataSet.createRow();
        rowA.addValue(startedDate.getKey(), getDate(Calendar.JANUARY, 5));
        rowA.addValue(endedDate.getKey(), getDate(Calendar.JANUARY, 9));
        rowA.addValue(count.getKey(), 1);
        rowA.addValue(otherDim.getKey(), "X");
        rowA.addValue(correlationDim.getKey(), "A");
        IRow rowB = dataSet.createRow();
        rowB.addValue(startedDate.getKey(), getDate(Calendar.JANUARY, 6));
        rowB.addValue(endedDate.getKey(), getDate(Calendar.JANUARY, 10));
        rowB.addValue(count.getKey(), 1);
        rowB.addValue(correlationDim.getKey(), "B");
        rowB.addValue(otherDim.getKey(), "X");

        long id = TestUtil.createTestDataSource(dataSet, Arrays.asList(startedDate, endedDate, correlationDim, otherDim, count, analysisStep));

        WSListDefinition derivedDefinition = new WSListDefinition();
        derivedDefinition.setColumns(Arrays.asList(TestUtil.getItem(id, "Count"), TestUtil.getItem(id, "Step")));
        FilterValueDefinition filterValueDefinition = new FilterValueDefinition(TestUtil.getItem(id, "Other"), true, Arrays.asList((Object) "Blah"));
        derivedDefinition.setFilterDefinitions(Arrays.asList((FilterDefinition) filterValueDefinition));
        derivedDefinition.setDataFeedID(id);

        long reportID = new AnalysisService().saveAnalysisDefinition(derivedDefinition).getAnalysisID();

        UserUploadService uploadService = new UserUploadService();

        AnalysisBasedFeedDefinition abfd = new AnalysisBasedFeedDefinition();
        abfd.setAnalysisDefinitionID(reportID);
        abfd.setFeedName("Derived Data Source");
        uploadService.createAnalysisBasedFeed(abfd);

        WSListDefinition derivedReport = new WSListDefinition();
        derivedReport.setColumns(Arrays.asList(TestUtil.getItem(id, "Count"), TestUtil.getItem(id, "Step")));
        derivedReport.setDataFeedID(id);
        new AnalysisService().saveAnalysisDefinition(derivedReport);

        assertEquals(1, uploadService.copyDataSource(id, "New Data Source", false, false).size());
        assertEquals(3, uploadService.copyDataSource(id, null, false, true).size());
        assertEquals(3, uploadService.copyDataSource(id, null, true, true).size());
        assertEquals(1, uploadService.copyDataSource(id, null, true, false).size());
    }

    private static Date getDate(int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }
}
