package test.core;

import junit.framework.TestCase;
import com.easyinsight.analysis.*;
import com.easyinsight.AnalysisDimension;
import com.easyinsight.AnalysisItem;
import com.easyinsight.DataService;
import com.easyinsight.AnalysisMeasure;
import com.easyinsight.core.NamedKey;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.database.Database;
import com.easyinsight.webservice.google.ListDataResults;
import com.easyinsight.webservice.google.ListRow;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.UserUploadAnalysis;
import com.easyinsight.userupload.FlatFileUploadFormat;
import com.easyinsight.userupload.UploadPolicy;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: May 9, 2008
 * Time: 9:52:52 PM
 */
public class DataScrubTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
        new DataRetrieval();
    }

    public void testScrubbing() {
        long accountID = TestUtil.getTestUser();
        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = createDataFeed(accountID, userUploadService);
        WSListDefinition listDefinition = createListDefinition(accountID, dataFeedID);
        DataService dataService = new DataService();
        ListDataResults results = dataService.list(listDefinition, null);
        ListRow listRow = results.getRows()[0];
        //String firstVal = (String) listRow.getValues()[0];
        //assertTrue(firstVal.equals("Widget X") || firstVal.equals("Widget Y"));
    }

    private long createDataFeed(long accountID, UserUploadService userUploadService) {
        String csvText = "Customer,Product,Revenue\nAcme,WidgetX,500\nAcme,WidgetY,200";
        long uploadID = userUploadService.addRawUploadData(accountID, "test.csv", csvText.getBytes());
        System.out.println(uploadID);
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, new FlatFileUploadFormat(",", "\\,"));
        assertTrue(userUploadAnalysis.isSuccessful());
        return userUploadService.parsed(uploadID, new FlatFileUploadFormat(",", "\\,"), "Test Feed", "Testing",
                Arrays.asList(new AnalysisMeasure(new NamedKey("Revenue"), AggregationTypes.SUM), new AnalysisDimension(new NamedKey("Customer"), true),
                        new AnalysisDimension(new NamedKey("Product"), true)), new UploadPolicy(accountID), new TagCloud());
    }

    private WSListDefinition createListDefinition(long accountID, long dataFeedID) {
        WSListDefinition listDefinition = new WSListDefinition();
        //ListDefinition listDefinition = new ListDefinition();
        listDefinition.setName("Test List");
        listDefinition.setDataFeedID(dataFeedID);
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();        
        listDefinition.setFilterDefinitions(filters);
        AnalysisDimension myDimension = new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true);
        List<AnalysisItem> analysisFields = new ArrayList<AnalysisItem>();
        analysisFields.add(myDimension);
        listDefinition.setColumns(analysisFields);
        // TODO: update
        //List<DataScrub> dataScrubs = Arrays.asList(new DataScrub(TestUtil.createKey("Product", dataFeedID), "WidgetX", "Widget X"),
         //       new DataScrub(TestUtil.createKey("Product", dataFeedID), "WidgetY", "Widget Y"));
        //listDefinition.setDataScrubs(dataScrubs);
        AnalysisService analysisService = new AnalysisService();
        long analysisID = analysisService.saveAnalysisDefinition(listDefinition);
        return (WSListDefinition) analysisService.openAnalysisDefinition(analysisID);
    }
}
