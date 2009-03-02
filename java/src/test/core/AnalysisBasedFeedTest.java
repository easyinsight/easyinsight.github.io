package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.AnalysisBasedFeedDefinition;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataService;
import com.easyinsight.analysis.ListDataResults;

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
    }

    public void testAnalysisBasedFeed() {
        long userID = TestUtil.getIndividualTestUser();
        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = TestUtil.createDefaultTestDataSource(userID);
        long analysisID = createListDefinition(dataFeedID);
        AnalysisBasedFeedDefinition analysisBasedFeedDefinition = new AnalysisBasedFeedDefinition();
        analysisBasedFeedDefinition.setAnalysisDefinitionID(analysisID);
        analysisBasedFeedDefinition.setFeedName("Derived Feed");
        analysisBasedFeedDefinition.setGenre("Test");
        analysisBasedFeedDefinition.setUploadPolicy(new UploadPolicy());
        long analysisFeedID = userUploadService.createAnalysisBasedFeed(analysisBasedFeedDefinition);
        WSListDefinition listDefinition = createAnalysisList(analysisFeedID, dataFeedID);
        DataService dataService = new DataService();
        ListDataResults results = dataService.list(listDefinition, null);
        assertEquals(1, results.getRows().length);
    }

    private long createListDefinition(long dataFeedID) {
        WSListDefinition listDefinition = new WSListDefinition();
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

    private WSListDefinition createAnalysisList(long dataFeedID, long rootFeedID) {
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setName("Composite List");
        listDefinition.setDataFeedID(dataFeedID);
        AnalysisDimension myDimension = new AnalysisDimension(TestUtil.createKey("Product", rootFeedID), true);
        List<AnalysisItem> analysisFields = new ArrayList<AnalysisItem>();
        analysisFields.add(myDimension);
        listDefinition.setColumns(analysisFields);
        return listDefinition;
    }
}
