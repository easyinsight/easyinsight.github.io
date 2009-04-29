package test.solutions;

import junit.framework.TestCase;
import com.easyinsight.solutions.Solution;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.solutions.SolutionInstallInfo;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.basecamp.BaseCampDataSource;
import com.easyinsight.api.ValidatingPublishService;
import com.easyinsight.api.Row;
import com.easyinsight.api.StringPair;
import com.easyinsight.api.NumberPair;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSColumnChartDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Credentials;
import test.util.TestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Date;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 5:10:23 PM
 */
public class SolutionTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testSolutions() {
        TestUtil.getIndividualAdminUser();
        UserUploadService uploadService = new UserUploadService();
        long dataSourceID = uploadService.createNewDefaultFeed("Default Data Source");
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
        AnalysisDimension customer = new AnalysisDimension("customer", true);
        AnalysisMeasure amount = new AnalysisMeasure("amount", AggregationTypes.SUM);
        feedDefinition.setFields(Arrays.asList(customer, amount));
        new FeedService().updateFeedDefinition(feedDefinition, null, null);
        ValidatingPublishService validatingPublishService = new ValidatingPublishService() {

            protected String getUserName() {
                return SecurityUtil.getSecurityProvider().getUserPrincipal().getUserName();
            }

            protected long getAccountID() {
                return SecurityUtil.getAccountID();
            }

            protected long getUserID() {
                return SecurityUtil.getUserID();
            }
        };
        Row row1 = createRow();
        Row row2 = createRow();
        validatingPublishService.addRows(feedDefinition.getApiKey(), new Row[] { row1, row2 });
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setName("List");
        listDefinition.setColumns(Arrays.asList(getItem("customer", feedDefinition), getItem("amount", feedDefinition)));
        listDefinition.setDataFeedID(dataSourceID);
        long listID = new AnalysisService().saveAnalysisDefinition(listDefinition);
        WSColumnChartDefinition chartDefinition = new WSColumnChartDefinition();
        chartDefinition.setName("ColumnChart");
        chartDefinition.setDataFeedID(dataSourceID);
        chartDefinition.setMeasure(getItem("amount", feedDefinition));
        chartDefinition.setXaxis(getItem("customer", feedDefinition));
        long chartID = new AnalysisService().saveAnalysisDefinition(chartDefinition);
        Solution solution = new Solution();
        solution.setName("Blah");
        solution.setCopyData(true);
        SolutionService solutionService = new SolutionService();
        long solutionID = solutionService.addSolution(solution, Arrays.asList( (int) dataSourceID ));
        TestUtil.getIndividualTestUser();
        List<SolutionInstallInfo> infos = solutionService.installSolution(solutionID);
        for (SolutionInstallInfo info : infos) {
            if (info.getType() == SolutionInstallInfo.DATA_SOURCE) {
                FeedDefinition newDataSource = new FeedStorage().getFeedDefinitionData(info.getNewID());
                WSListDefinition defaultQuery = new WSListDefinition();
                defaultQuery.setDataFeedID(info.getNewID());
                defaultQuery.setColumns(Arrays.asList(getItem("customer", newDataSource), getItem("amount", newDataSource)));
                ListDataResults results = new DataService().list(defaultQuery, new InsightRequestMetadata());
                assertEquals(1, results.getRows().length);
            } else if (info.getType() == SolutionInstallInfo.INSIGHT) {
                WSAnalysisDefinition def = new AnalysisService().openAnalysisDefinition(info.getNewID());
                ListDataResults results = new DataService().list(def, new InsightRequestMetadata());
                assertEquals(1, results.getRows().length);
            }
        }
    }

    public void testDynamicSources() {
        TestUtil.getIndividualAdminUser();
        UserUploadService uploadService = new UserUploadService();
        BaseCampDataSource ds = new BaseCampDataSource();
        ds.setUrl("easyinsight.basecamphq.com");
        Credentials c = new Credentials();
        c.setUserName("apiuser");
        c.setPassword("@p!user");
        long sourceId = uploadService.newExternalDataSource(ds, c);
        Solution solution = new Solution();
        solution.setName("Blah");
        solution.setCopyData(false);
        SolutionService solutionService = new SolutionService();
        long solutionID = solutionService.addSolution(solution, Arrays.asList( (int) sourceId ));
        TestUtil.getIndividualTestUser();
        long newSourceID = 0;
        List<SolutionInstallInfo> infos = solutionService.installSolution(solutionID);
        for (SolutionInstallInfo info : infos) {
            if (info.getType() == SolutionInstallInfo.DATA_SOURCE) {
                newSourceID = info.getNewID();
                FeedDefinition newDataSource = new FeedStorage().getFeedDefinitionData(newSourceID);
                WSListDefinition defaultQuery = new WSListDefinition();
                defaultQuery.setDataFeedID(info.getNewID());
                defaultQuery.setColumns(Arrays.asList(getItem(BaseCampDataSource.CREATORNAME, newDataSource)));
                ListDataResults results = new DataService().list(defaultQuery, new InsightRequestMetadata());
                assertEquals(0, results.getRows().length);
            }
        }
        ds = (BaseCampDataSource) new FeedService().getFeedDefinition(newSourceID);
        ds.setUrl("easyinsight.basecamphq.com");
        ds.refreshData(c, SecurityUtil.getAccountID(), new Date());
        WSListDefinition defaultQuery = new WSListDefinition();
        defaultQuery.setDataFeedID(newSourceID);
        defaultQuery.setColumns(Arrays.asList(getItem(BaseCampDataSource.CREATORNAME, ds)));
        ListDataResults results = new DataService().list(defaultQuery, new InsightRequestMetadata());
        assertTrue(results.getRows().length > 0);
    }

    private AnalysisItem getItem(String name, FeedDefinition feedDefinition) {
        for (AnalysisItem item : feedDefinition.getFields()) {
            if (item.getKey().toKeyString().equals(name)) {
                return item;
            }
        }
        throw new RuntimeException();
    }

    private Row createRow() {
        Row row = new Row();
        StringPair stringPair = new StringPair();
        stringPair.setKey("customer");
        stringPair.setValue("c1");
        NumberPair numberPair = new NumberPair();
        numberPair.setKey("amount");
        numberPair.setValue(500);
        row.setStringPairs(new StringPair[] { stringPair });
        row.setNumberPairs(new NumberPair[] { numberPair });
        return row;
    }
}
