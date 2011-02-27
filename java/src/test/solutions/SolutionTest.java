package test.solutions;

import com.easyinsight.datafeeds.basecamp.BaseCampTodoSource;
import junit.framework.TestCase;
import com.easyinsight.solutions.Solution;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.solutions.SolutionInstallInfo;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedRegistry;

import com.easyinsight.datafeeds.basecamp.BaseCampCompositeSource;
import com.easyinsight.api.ValidatingPublishService;
import com.easyinsight.api.Row;
import com.easyinsight.api.StringPair;
import com.easyinsight.api.NumberPair;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSColumnChartDefinition;
import com.easyinsight.analysis.definitions.WSLineChartDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Credentials;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.EIDescriptor;
import test.util.TestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.sql.SQLException;

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

    public void testSolutions() throws SQLException {
        TestUtil.getIndividualAdminUser();
        UserUploadService uploadService = new UserUploadService();
        long dataSourceID = uploadService.createNewDefaultFeed("Default Data Source");
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
        AnalysisDimension customer = new AnalysisDimension("customer", true);
        AnalysisDimension region = new AnalysisDimension("region", true);
        AnalysisMeasure amount = new AnalysisMeasure("amount", AggregationTypes.SUM);
        AnalysisRangeDimension range = new AnalysisRangeDimension(new NamedKey("Range"), true);
        AnalysisList list = new AnalysisList(new NamedKey("List"), true, ",");
        AnalysisDateDimension startDate = new AnalysisDateDimension("Start", true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisDateDimension endDate = new AnalysisDateDimension("End", true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisStep step = new AnalysisStep(new NamedKey("Step"), true, AnalysisDateDimension.DAY_LEVEL, startDate, endDate, customer);
        AnalysisHierarchyItem hierarchyItem = new AnalysisHierarchyItem();
        HierarchyLevel regionLevel = new HierarchyLevel();
        regionLevel.setAnalysisItem(region);
        HierarchyLevel customerLevel = new HierarchyLevel();
        customerLevel.setAnalysisItem(customer);
        hierarchyItem.setHierarchyLevel(customerLevel);
        hierarchyItem.setHierarchyLevels(Arrays.asList(regionLevel, customerLevel));
        hierarchyItem.setKey(new NamedKey("Hierarchy"));
        feedDefinition.setFields(Arrays.asList(customer, amount, region, range, list, startDate, endDate, step));
        new FeedService().updateFeedDefinition(feedDefinition);
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
        long listID = new AnalysisService().saveAnalysisDefinition(listDefinition).getAnalysisID();
        WSColumnChartDefinition chartDefinition = new WSColumnChartDefinition();
        chartDefinition.setName("ColumnChart");
        chartDefinition.setDataFeedID(dataSourceID);
        chartDefinition.setMeasure(getItem("amount", feedDefinition));
        chartDefinition.setXaxis(getItem("customer", feedDefinition));
        long chartID = new AnalysisService().saveAnalysisDefinition(chartDefinition).getAnalysisID();
        Solution solution = new Solution();
        solution.setName("Blah");
        solution.setCopyData(true);
        SolutionService solutionService = new SolutionService();
        long solutionID = solutionService.addSolution(solution, Arrays.asList( (int) dataSourceID ));
        TestUtil.getIndividualTestUser();
        List<SolutionInstallInfo> infos = solutionService.installSolution(solutionID);
        for (SolutionInstallInfo info : infos) {
            if (info.getDescriptor().getType() == EIDescriptor.DATA_SOURCE) {
                FeedDefinition newDataSource = new FeedStorage().getFeedDefinitionData(info.getDescriptor().getId());
                WSListDefinition defaultQuery = new WSListDefinition();
                defaultQuery.setDataFeedID(info.getDescriptor().getId());
                defaultQuery.setColumns(Arrays.asList(getItem("customer", newDataSource), getItem("amount", newDataSource)));
                ListDataResults results = (ListDataResults) new DataService().list(defaultQuery, new InsightRequestMetadata());
                assertEquals(1, results.getRows().length);
            } else if (info.getDescriptor().getType() == EIDescriptor.REPORT) {
                WSAnalysisDefinition def = new AnalysisService().openAnalysisDefinition(info.getDescriptor().getId());
                ListDataResults results = (ListDataResults) new DataService().list(def, new InsightRequestMetadata());
                assertEquals(1, results.getRows().length);
            }
        }
        List<Solution> solutions = solutionService.getSolutions();
    }

    public void testDynamicSources() throws SQLException {
        TestUtil.getIndividualAdminUser();
        UserUploadService uploadService = new UserUploadService();
        BaseCampCompositeSource ds = new BaseCampCompositeSource();
        ds.setUrl("easyinsight.basecamphq.com");
        Credentials c = new Credentials();
        c.setUserName("apiuser");
        c.setPassword("@p!user");
        long sourceId = uploadService.newExternalDataSource(ds);
        AnalysisHierarchyItem responsibility = new AnalysisHierarchyItem();
        responsibility.setKey(new NamedKey("Responsibility"));
        HierarchyLevel respPartyLevel = new HierarchyLevel();
        respPartyLevel.setAnalysisItem(TestUtil.getActualItem(sourceId, BaseCampTodoSource.RESPONSIBLEPARTYNAME));
        respPartyLevel.setPosition(0);
        HierarchyLevel todoLevel = new HierarchyLevel();
        todoLevel.setAnalysisItem(TestUtil.getActualItem(sourceId, BaseCampTodoSource.TODOLISTNAME));
        todoLevel.setPosition(1);
        responsibility.setHierarchyLevel(respPartyLevel);
        responsibility.setHierarchyLevels(Arrays.asList(respPartyLevel, todoLevel));
        ds = (BaseCampCompositeSource) new FeedService().getFeedDefinition(sourceId);
        ds.getFields().add(responsibility);
        new FeedService().updateFeedDefinition(ds);
        ds = (BaseCampCompositeSource) new FeedService().getFeedDefinition(sourceId);
        WSTreeMapDefinition treeMap = new WSTreeMapDefinition();
        treeMap.setDataFeedID(sourceId);
        treeMap.setHierarchy(TestUtil.getItem(sourceId, "Responsibility"));
        treeMap.setMeasure1(TestUtil.getItem(sourceId, BaseCampTodoSource.COUNT));
        treeMap.setMeasure2(TestUtil.getItem(sourceId, BaseCampTodoSource.COUNT));
        treeMap.setName("Responsibility Tree Map");
        FilterValueDefinition filterValueDefinition = new FilterValueDefinition(TestUtil.getItem(sourceId, BaseCampTodoSource.COMPLETED),
                true, Arrays.asList((Object) "false"));
        treeMap.setFilterDefinitions(Arrays.asList((FilterDefinition) filterValueDefinition));
        new AnalysisService().saveAnalysisDefinition(treeMap);
        WSLineChartDefinition lineChart = new WSLineChartDefinition();
        lineChart.setDataFeedID(sourceId);
        lineChart.setName("Line Chart");
        lineChart.setMeasure(TestUtil.getItem(sourceId, BaseCampTodoSource.COUNT));
        lineChart.setYaxis(TestUtil.getItem(sourceId, BaseCampTodoSource.RESPONSIBLEPARTYNAME));
        lineChart.setXaxis(TestUtil.getItem(sourceId, BaseCampTodoSource.ITEMCYCLE));
        new AnalysisService().saveAnalysisDefinition(lineChart);
        Solution solution = new Solution();
        solution.setName("Blah");
        solution.setCopyData(false);
        SolutionService solutionService = new SolutionService();
        long solutionID = solutionService.addSolution(solution, Arrays.asList( (int) sourceId ));
        TestUtil.getIndividualTestUser();
        long newSourceID = 0;
        List<SolutionInstallInfo> infos = solutionService.installSolution(solutionID);
        for (SolutionInstallInfo info : infos) {
            if (info.getDescriptor().getType() == EIDescriptor.DATA_SOURCE) {
                newSourceID = info.getDescriptor().getId();
                FeedDefinition newDataSource = new FeedStorage().getFeedDefinitionData(newSourceID);
                new DataService().getFeedMetadata(newSourceID);
                WSListDefinition defaultQuery = new WSListDefinition();
                defaultQuery.setDataFeedID(info.getDescriptor().getId());
                defaultQuery.setColumns(Arrays.asList(getItem(BaseCampTodoSource.CREATORNAME, newDataSource)));
                ListDataResults results = (ListDataResults) new DataService().list(defaultQuery, new InsightRequestMetadata());
                assertEquals(0, results.getRows().length);
            }
        }
        ds = (BaseCampCompositeSource) new FeedService().getFeedDefinition(newSourceID);
        ds.setUrl("easyinsight.basecamphq.com");
        ds.refreshData(SecurityUtil.getAccountID(), new Date(), null, null, null);
        WSListDefinition defaultQuery = new WSListDefinition();
        defaultQuery.setDataFeedID(newSourceID);
        defaultQuery.setColumns(Arrays.asList(getItem(BaseCampTodoSource.CREATORNAME, ds)));
        ListDataResults results = (ListDataResults) new DataService().list(defaultQuery, new InsightRequestMetadata());
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
