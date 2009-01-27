package test.core;

import junit.framework.TestCase;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.UserUploadAnalysis;
import com.easyinsight.userupload.FlatFileUploadFormat;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.*;
import com.easyinsight.DataService;
import com.easyinsight.AnalysisDimension;
import com.easyinsight.AnalysisMeasure;
import com.easyinsight.AnalysisItem;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.database.Database;
import com.easyinsight.webservice.google.ListDataResults;

import java.util.Arrays;
import java.util.ArrayList;
import java.sql.SQLException;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Jun 1, 2008
 * Time: 12:01:17 AM
 */
public class CompositeFeedTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
        new DataRetrieval();
    }
    
    public void testCompositeFeed() throws SQLException {
        long accountID = TestUtil.getTestUser();
        UserUploadService userUploadService = new UserUploadService();
        /*long dataFeedID = createFirstDataFeed(accountID, userUploadService);
        System.out.println(dataFeedID);
        long secondID = createSecondFeed(accountID, userUploadService);
        long thirdID = createThirdFeed(accountID, userUploadService);
        CompositeFeedNode targetNode = new CompositeFeedNode(secondID);
        CompositeFeedNode rootNode = new CompositeFeedNode(dataFeedID);
        CompositeFeedNode scoreNode = new CompositeFeedNode(thirdID);
        CompositeFeedConnection join = new CompositeFeedConnection();
        join.setSourceNode(targetNode);
        join.setSourceJoin(TestUtil.createKey("Customer", dataFeedID));
        join.setTargetJoin(TestUtil.createKey("Cust", secondID));
        CompositeFeedConnection managerToScoreJoin = new CompositeFeedConnection();
        managerToScoreJoin.setSourceNode(scoreNode);
        managerToScoreJoin.setSourceJoin(TestUtil.createKey("Manager", secondID));
        managerToScoreJoin.setTargetJoin(TestUtil.createKey("Manager", thirdID));
        rootNode.setCompositeFeedConnections(Arrays.asList( join ));
        targetNode.setCompositeFeedConnections(Arrays.asList( managerToScoreJoin ));
        CompositeFeedDefinition compositeFeed = new CompositeFeedDefinition();
        compositeFeed.setFeedName("Composite Feed");
        compositeFeed.setCompositeFeedNode(rootNode);
        FeedService feedService = new FeedService();
        long feedID = feedService.createCompositeFeed(rootNode, "Composite Test Feed");
        DataService dataService = new DataService();
        dataService.getFeedMetadata(feedID, null);
        WSListDefinition listDefinition = new WSListDefinition();

        listDefinition.setColumns(Arrays.asList(new AnalysisMeasure(findKey(rootNode, "Revenue"), AggregationTypes.AVERAGE),
                new AnalysisDimension(findKey(rootNode, "Manager"), true), new AnalysisDimension(findKey(rootNode, "Customer"), true)));
        listDefinition.setFilterDefinitions(new ArrayList<FilterDefinition>());
        ListDataResults listDataResults = dataService.list(listDefinition);
        assertEquals(2, listDataResults.getRows().length);
        listDefinition.setColumns(Arrays.asList(new AnalysisMeasure(findKey(rootNode, "Revenue"), AggregationTypes.AVERAGE),
                new AnalysisDimension(findKey(rootNode, "Manager"), true)));
        listDefinition.setFilterDefinitions(new ArrayList<FilterDefinition>());
        listDataResults = dataService.list(listDefinition);
        assertEquals(2, listDataResults.getRows().length);
        listDefinition.setColumns(Arrays.asList(new AnalysisMeasure(findKey(rootNode, "Score"), AggregationTypes.AVERAGE),
                new AnalysisDimension(findKey(rootNode, "Manager"), true)));
        listDefinition.setFilterDefinitions(new ArrayList<FilterDefinition>());
        listDataResults = dataService.list(listDefinition);
        assertEquals(2, listDataResults.getRows().length);*/
    }

   /* private Key findKey(CompositeFeedNode rootNode, String keyName) throws SQLException {
        CompositeKeyLocator locator = new CompositeKeyLocator(keyName);
        locator.visit(rootNode);
        Key key = locator.getFoundKey();
        if (key == null) {
            throw new RuntimeException("Could not find key " + keyName);
        }
        return key;
    }*/

    private static class CompositeKeyLocator extends CompositeFeedNodeVisitor {

        private FeedStorage feedStorage = new FeedStorage();
        private String keyName;
        private Key foundKey;

        private CompositeKeyLocator(String keyName) {
            this.keyName = keyName;
        }

        public Key getFoundKey() {
            return foundKey;
        }

        protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(compositeFeedNode.getDataFeedID());
            for (AnalysisItem analysisItem : feedDefinition.getFields()) {
                if (keyName.equals(analysisItem.getKey().toKeyString())) {
                    DerivedKey derivedKey = new DerivedKey();
                    derivedKey.setParentKey(analysisItem.getKey());
                    derivedKey.setFeedID(compositeFeedNode.getDataFeedID());
                    foundKey = derivedKey;
                }
            }
        }
    }

    private long createFirstDataFeed(long accountID, UserUploadService userUploadService) {
        String csvText = "Customer,Product,Revenue\nAcme,WidgetX,500\nCompanyX,WidgetY,200";
        long uploadID = userUploadService.addRawUploadData(accountID, "test1.csv", csvText.getBytes());
        System.out.println(uploadID);
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, new FlatFileUploadFormat(",", "\\,"));
        assertTrue(userUploadAnalysis.isSuccessful());
        assertTrue(userUploadAnalysis.getFields().size() == 3);
        long dataFeedID = userUploadService.parsed(uploadID, new FlatFileUploadFormat(",", "\\,"), "Test Feed", "Testing",
                Arrays.asList(new AnalysisMeasure(new NamedKey("Revenue"), AggregationTypes.SUM), new AnalysisDimension(new NamedKey("Customer"), true),
                        new AnalysisDimension(new NamedKey("Product"), true)), new UploadPolicy(accountID), new TagCloud());
        return dataFeedID;
    }

    private long createSecondFeed(long accountID, UserUploadService userUploadService) {
        String csvText = "Cust,Manager\nAcme,John\nCompanyX,Bob";
        long uploadID = userUploadService.addRawUploadData(accountID, "stocks1.csv", csvText.getBytes());
        System.out.println(uploadID);
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, new FlatFileUploadFormat(",", "\\,"));
        assertTrue(userUploadAnalysis.isSuccessful());
        long dataFeedID = userUploadService.parsed(uploadID, new FlatFileUploadFormat(",", "\\,"), "Commercial Feed", "Financial",
                Arrays.asList(new AnalysisItem[] { new AnalysisDimension(new NamedKey("Cust"), true), new AnalysisDimension(new NamedKey("Manager"), true) } ),
                new UploadPolicy(accountID), new TagCloud());
        return dataFeedID;
    }

    private long createThirdFeed(long accountID, UserUploadService userUploadService) {
        String csvText = "Manager,Score\nJohn,7\nBob,3";
        long uploadID = userUploadService.addRawUploadData(accountID, "stocks2.csv", csvText.getBytes());
        System.out.println(uploadID);
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, new FlatFileUploadFormat(",", "\\,"));
        assertTrue(userUploadAnalysis.isSuccessful());
        long dataFeedID = userUploadService.parsed(uploadID, new FlatFileUploadFormat(",", "\\,"), "Score Feed", "Financial",
                Arrays.asList(new AnalysisMeasure(new NamedKey("Score"), AggregationTypes.SUM), new AnalysisDimension(new NamedKey("Manager"), true)),
                new UploadPolicy(accountID), new TagCloud());
        return dataFeedID;
    }
}
