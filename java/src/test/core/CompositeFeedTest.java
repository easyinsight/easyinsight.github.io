package test.core;

import junit.framework.TestCase;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.database.Database;

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
    }
    
    public void testCompositeFeed() throws SQLException {
        long accountID = TestUtil.getIndividualTestUser();
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

}
