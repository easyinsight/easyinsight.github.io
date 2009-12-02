package test.core;

import junit.framework.TestCase;
import com.easyinsight.userupload.*;
import com.easyinsight.database.Database;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.analysis.ListDataResults;

import java.util.Arrays;
import java.util.List;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Apr 29, 2008
 * Time: 12:47:27 AM
 */
public class UploadTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testUpload() {
        long userID = TestUtil.getIndividualTestUser();
        long dataFeedID = TestUtil.createDefaultTestDataSource(userID);
        System.out.println(dataFeedID);
        FeedService dataProviderService = new FeedService();

        System.out.println(dataProviderService.searchForAvailableFeeds(null, null).size());
        dataProviderService.searchForSubscribedFeeds();
        dataProviderService.getMostPopularFeeds(null, 10);

        System.out.println(dataProviderService.searchForAvailableFeeds("Test", "Testing").size());
        dataProviderService.getMostPopularFeeds("Testing", 10);
        // next up, test the retrieval...

        System.out.println(dataProviderService.searchForAvailableFeeds(null, "Financial").size());
        System.out.println(dataProviderService.searchForAvailableFeeds("Com", null).size());
        dataProviderService.getMostPopularFeeds("Financial", 10);

        // all right, next test...

        System.gc();

        DataService dataService = new DataService();
        FeedMetadata feedMetadata = dataService.getFeedMetadata(dataFeedID);

        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setDataFeedID(dataFeedID);
        listDefinition.setColumns(Arrays.asList(new AnalysisDimension(TestUtil.createKey("Customer", dataFeedID), true),
                new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true), new AnalysisMeasure(TestUtil.createKey("Revenue", dataFeedID), AggregationTypes.SUM)));
        listDefinition.setName("Sample Analysis");
        List filterDefinitions = Arrays.asList(new FilterValueDefinition(new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true), true,
                TestUtil.objectList("WidgetX")));
        listDefinition.setFilterDefinitions(filterDefinitions);
        ListDataResults dataResults = (ListDataResults) dataService.list(listDefinition, null);
        assertEquals(3, dataResults.getHeaders().length);
        assertEquals(1, dataResults.getRows().length);

        UserUploadService userUploadService = new UserUploadService();
        FeedDefinition update = userUploadService.getDataFeedConfiguration(dataFeedID);
        update.setGenre("MoreTesting");
        update.setFeedName("Renamed Feed");
        userUploadService.updateFeedDefinition(update);
    }
}
