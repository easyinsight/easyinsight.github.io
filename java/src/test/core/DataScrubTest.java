package test.core;

import junit.framework.TestCase;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataService;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.database.Database;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.ListRow;

import java.util.List;
import java.util.ArrayList;

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
    }

    public void testScrubbing() {
        long userID = TestUtil.getIndividualTestUser();
        long dataFeedID = TestUtil.createDefaultTestDataSource(userID);
        WSListDefinition listDefinition = createListDefinition(userID, dataFeedID);
        DataService dataService = new DataService();
        ListDataResults results = (ListDataResults) dataService.list(listDefinition, null);
        ListRow listRow = results.getRows()[0];
        //String firstVal = (String) listRow.getValues()[0];
        //assertTrue(firstVal.equals("Widget X") || firstVal.equals("Widget Y"));
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
        long analysisID = analysisService.saveAnalysisDefinition(listDefinition).getAnalysisID();
        return (WSListDefinition) analysisService.openAnalysisDefinition(analysisID);
    }
}
