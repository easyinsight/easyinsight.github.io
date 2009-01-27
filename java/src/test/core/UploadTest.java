package test.core;

import junit.framework.TestCase;
import com.easyinsight.userupload.*;
import com.easyinsight.database.Database;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.*;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.core.NamedKey;
import com.easyinsight.webservice.google.ListDataResults;

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
        new DataRetrieval();
    }

    public void testUpload() {
        long accountID = TestUtil.getTestUser();
        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = createFirstDataFeed(accountID, userUploadService);
        System.out.println(dataFeedID);
        createCommercialDataFeed(accountID, userUploadService);
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
        FeedMetadata feedMetadata = dataService.getFeedMetadata(dataFeedID, null);

        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setDataFeedID(dataFeedID);
        listDefinition.setColumns(Arrays.asList(new AnalysisDimension(TestUtil.createKey("Customer", dataFeedID), true),
                new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true), new AnalysisMeasure(TestUtil.createKey("Revenue", dataFeedID), AggregationTypes.SUM)));
        listDefinition.setName("Sample Analysis");
        List filterDefinitions = Arrays.asList(new FilterValueDefinition(new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true), true,
                Arrays.asList("WidgetX")));
        listDefinition.setFilterDefinitions(filterDefinitions);
        ListDataResults dataResults = dataService.list(listDefinition, null);
        assertEquals(3, dataResults.getHeaders().length);
        assertEquals(1, dataResults.getRows().length);

        FeedDefinition update = userUploadService.getDataFeedConfiguration(dataFeedID);
        update.setGenre("MoreTesting");
        update.setFeedName("Renamed Feed");
        userUploadService.updateFeedDefinition(update);
    }

    private long createFirstDataFeed(long accountID, UserUploadService userUploadService) {
        String csvText = "Customer,Product,Revenue\nAcme,WidgetX,500\nAcme,WidgetY,200";
        long uploadID = userUploadService.addRawUploadData(accountID, "test.csv", csvText.getBytes());
        System.out.println(uploadID);
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, new FlatFileUploadFormat(",", "\\,"));
        assertTrue(userUploadAnalysis.isSuccessful());
        long dataFeedID = userUploadService.parsed(uploadID, new FlatFileUploadFormat(",", "\\,"), "Test Feed", "Testing",
                Arrays.asList(new AnalysisMeasure(new NamedKey("Revenue"), AggregationTypes.SUM), new AnalysisDimension(new NamedKey("Customer"), true),
                        new AnalysisDimension(new NamedKey("Product"), true)),
                new UploadPolicy(), new TagCloud());
        return dataFeedID;
    }

    private long createCommercialDataFeed(long accountID, UserUploadService userUploadService) {
        String csvText = "Customer,Product,Revenue\nAcme,WidgetX,500\nAcme,WidgetY,200";
        long uploadID = userUploadService.addRawUploadData(accountID, "stocks.csv", csvText.getBytes());
        System.out.println(uploadID);
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, new FlatFileUploadFormat(",", "\\,"));
        assertTrue(userUploadAnalysis.isSuccessful());
        long dataFeedID = userUploadService.parsed(uploadID, new FlatFileUploadFormat(",", "\\,"), "Commercial Feed", "Financial",
                Arrays.asList(new AnalysisMeasure(new NamedKey("Revenue"), AggregationTypes.SUM), new AnalysisDimension(new NamedKey("Customer"), true),
                        new AnalysisDimension(new NamedKey("Product"), true)), new UploadPolicy(), new TagCloud());
        return dataFeedID;
    }
}
