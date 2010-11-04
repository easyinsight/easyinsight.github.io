package test.core;

import junit.framework.TestCase;
import test.util.TestUtil;

import java.util.Arrays;
import java.sql.SQLException;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.database.Database;

/**
 * User: James Boe
 * Date: May 23, 2009
 * Time: 2:37:23 PM
 */
public class ReportItemTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testReportItems() throws Exception {
        TestUtil.getIndividualTestUser();
        AnalysisDimension blah = new AnalysisDimension("blah", true);
        AnalysisMeasure count = new AnalysisMeasure("count", AggregationTypes.SUM);
        long id = TestUtil.createTestDataSource(new DataSet(), Arrays.asList(blah, count));
        WSListDefinition listDefinition = new WSListDefinition();
        blah = (AnalysisDimension) TestUtil.getItem(id, "blah");
        count = (AnalysisMeasure) TestUtil.getItem(id, "count");
        blah.setDisplayName("bad");
        count.setDisplayName("bad");
        listDefinition.setColumns(Arrays.asList(blah, count));
        listDefinition.setDataFeedID(id);
        new AnalysisService().saveAnalysisDefinition(listDefinition);
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(id);
        for (AnalysisItem item : feedDefinition.getFields()) {
            assertFalse("Item has the wrong display name.", "bad".equals(item.getDisplayName()));
        }
    }
}
