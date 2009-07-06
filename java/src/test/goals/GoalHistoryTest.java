package test.goals;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.NamedKey;
import com.easyinsight.pipeline.StandardReportPipeline;

import java.util.*;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Jul 5, 2009
 * Time: 10:31:46 PM
 */
public class GoalHistoryTest extends TestCase {
    @Override
     protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testHistory() throws Exception {
        TestUtil.getIndividualTestUser();
        Calendar cal = Calendar.getInstance();
        DataSet dataSet = new DataSet();
        cal.add(Calendar.DAY_OF_YEAR, -30);
        Date startDate = cal.getTime();
        for (int i = 0; i < 30; i++) {
            IRow row = dataSet.createRow();
            row.addValue("Date", new DateValue(cal.getTime()));
            row.addValue("Value", new NumericValue(i));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        List<AnalysisItem> items = Arrays.asList(new AnalysisDateDimension("Date", true, AnalysisDateDimension.DAY_LEVEL),
                new AnalysisMeasure("Value", AggregationTypes.SUM));
        long id = TestUtil.createTestDataSource(dataSet, items);
        Set<AnalysisItem> itemSet = new HashSet<AnalysisItem>(Arrays.asList(TestUtil.getItem(id, "Date"), TestUtil.getItem(id, "Value")));
        Feed feed = FeedRegistry.instance().getFeed(id);
        dataSet = feed.getAggregateDataSet(itemSet, new ArrayList<FilterDefinition>(), new InsightRequestMetadata() , feed.getFields(), false, null);
        cal.setTime(startDate);
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        rollingFilterDefinition.setField(TestUtil.getItem(id, "Date"));
        rollingFilterDefinition.setApplyBeforeAggregation(true);
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.WEEK);
        AnalysisMeasure analysisMeasure = (AnalysisMeasure) TestUtil.getItem(id, "Value");
        for (int i = 0; i < 30; i++) {
            StandardReportPipeline pipeline = new StandardReportPipeline();
            WSListDefinition report = new WSListDefinition();
            report.setColumns(Arrays.asList((AnalysisItem) analysisMeasure));
            report.setDataFeedID(id);
            report.setFilterDefinitions(Arrays.asList((FilterDefinition) rollingFilterDefinition));
            InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
            insightRequestMetadata.setNow(cal.getTime());
            DataSet result = pipeline.setup(report, feed, insightRequestMetadata).toDataSet(dataSet);
            System.out.println("for " + cal.getTime());
            System.out.println("\t"  + result.getRows().size());
            if (result.getRows().size() > 0) {
                IRow row = result.getRow(0);
                System.out.println("\t" + row.getValue(analysisMeasure.createAggregateKey()));
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
    }
}
