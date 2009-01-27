package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.AnalysisBasedFeedDefinition;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.*;
import com.easyinsight.AnalysisDimension;
import com.easyinsight.AnalysisItem;
import com.easyinsight.DataService;
import com.easyinsight.AnalysisMeasure;
import com.easyinsight.core.NamedKey;
import com.easyinsight.webservice.google.ListDataResults;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: May 8, 2008
 * Time: 10:41:31 PM
 */
public class AnalysisBasedFeedTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
        new DataRetrieval();
    }

    public void testAnalysisBasedFeed() {

        long accountID = TestUtil.getTestUser();
        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = createDataFeed(accountID, userUploadService);
        long analysisID = createListDefinition(accountID, dataFeedID);
        AnalysisBasedFeedDefinition analysisBasedFeedDefinition = new AnalysisBasedFeedDefinition();
        analysisBasedFeedDefinition.setAnalysisDefinitionID(analysisID);
        analysisBasedFeedDefinition.setFeedName("Derived Feed");
        analysisBasedFeedDefinition.setGenre("Test");
        analysisBasedFeedDefinition.setUploadPolicy(new UploadPolicy());
        long analysisFeedID = userUploadService.createAnalysisBasedFeed(analysisBasedFeedDefinition);
        WSListDefinition listDefinition = createAnalysisList(accountID, analysisFeedID, dataFeedID);
        DataService dataService = new DataService();
        ListDataResults results = dataService.list(listDefinition, null);
        assertEquals(1, results.getRows().length);
    }

    private long createListDefinition(long accountID, long dataFeedID) {
        WSListDefinition listDefinition = new WSListDefinition();
        //ListDefinition listDefinition = new ListDefinition();
        listDefinition.setName("Test List");
        listDefinition.setDataFeedID(dataFeedID);
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        FilterValueDefinition filter = new FilterValueDefinition();
        AnalysisDimension analysisDimension = new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true);
        filter.setField(analysisDimension);
        filter.setInclusive(true);
        filter.setFilteredValues(Arrays.asList("WidgetX"));
        filters.add(filter);
        listDefinition.setFilterDefinitions(filters);
        AnalysisDimension myDimension = new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true);
        List<AnalysisItem> analysisFields = new ArrayList<AnalysisItem>();
        analysisFields.add(myDimension);
        listDefinition.setColumns(analysisFields);
        AnalysisService analysisService = new AnalysisService();
        return analysisService.saveAnalysisDefinition(listDefinition);
    }

    private WSListDefinition createAnalysisList(long accountID, long dataFeedID, long rootFeedID) {
        WSListDefinition listDefinition = new WSListDefinition();
        //ListDefinition listDefinition = new ListDefinition();
        listDefinition.setName("Composite List");
        listDefinition.setDataFeedID(dataFeedID);
        AnalysisDimension myDimension = new AnalysisDimension(TestUtil.createKey("Product", rootFeedID), true);
        List<AnalysisItem> analysisFields = new ArrayList<AnalysisItem>();
        analysisFields.add(myDimension);
        listDefinition.setColumns(analysisFields);
        return listDefinition;
    }

    private long createDataFeed(long accountID, UserUploadService userUploadService) {
        String csvText = "Customer,Product,Revenue\nAcme,WidgetX,500\nAcme,WidgetY,200";
        long uploadID = userUploadService.addRawUploadData(accountID, "test.csv", csvText.getBytes());
        System.out.println(uploadID);
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, new FlatFileUploadFormat(",", "\\,"));
        assertTrue(userUploadAnalysis.isSuccessful());
        assertTrue(userUploadAnalysis.getFields().size() == 3);
        return userUploadService.parsed(uploadID, new FlatFileUploadFormat(",", "\\,"), "Test Feed", "Testing",
                Arrays.asList(new AnalysisMeasure(new NamedKey("Revenue"), AggregationTypes.SUM), new AnalysisDimension(new NamedKey("Customer"), true),
                        new AnalysisDimension(new NamedKey("Product"), true)), new UploadPolicy(), new TagCloud());
    }
}
