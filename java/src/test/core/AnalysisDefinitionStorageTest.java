package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSColumnChartDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Key;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Connection;
import java.sql.SQLException;

import test.util.TestUtil;
import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Apr 12, 2008
 * Time: 5:01:29 PM
 */
public class AnalysisDefinitionStorageTest extends TestCase {

    public void setUp() {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testHierarchies() throws Exception {
        long userID = TestUtil.getIndividualTestUser();
        long dataSourceID = TestUtil.createDefaultTestDataSource(userID);
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
        HierarchyLevel topLevel = new HierarchyLevel();
        topLevel.setPosition(0);
        topLevel.setAnalysisItem(findAnalysisItem("Product", feedDefinition));
        HierarchyLevel bottomLevel = new HierarchyLevel();
        bottomLevel.setPosition(1);
        bottomLevel.setAnalysisItem(findAnalysisItem("Customer", feedDefinition));
        AnalysisHierarchyItem analysisHierarchyItem = new AnalysisHierarchyItem();
        analysisHierarchyItem.setHierarchyLevels(Arrays.asList(topLevel, bottomLevel));
        analysisHierarchyItem.setHierarchyLevel(topLevel);
        Key key = new NamedKey("Blah");
        analysisHierarchyItem.setKey(key);
        /*Connection conn = Database.instance().getConnection();
        conn.setAutoCommit(false);
        Session session = Database.instance().createSession(conn);
        session.save(key);
        session.save(analysisHierarchyItem);
        session.flush();
        conn.commit();*/
        feedDefinition.getFields().add(analysisHierarchyItem);
        new FeedStorage().updateDataFeedConfiguration(feedDefinition);
        feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID);
        AnalysisHierarchyItem loadedItem = (AnalysisHierarchyItem) findAnalysisItem("Blah", feedDefinition);
        assertEquals(2, loadedItem.getHierarchyLevels().size());
    }

    private AnalysisItem findAnalysisItem(String name, FeedDefinition feedDefinition) {
        for (AnalysisItem analysisItem : feedDefinition.getFields()) {
            if (analysisItem.getKey().toDisplayName().equals(name)) {
                return analysisItem;
            }
        }
        throw new RuntimeException("Could not find " + name + " in " + feedDefinition.getFeedName());
    }

    public void testListDefinitions() {
        System.out.println("connections = " + Database.instance().getActiveConnections());
        long userID = TestUtil.getIndividualTestUser();
        long dataSourceID = TestUtil.createDefaultTestDataSource(userID);
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setName("Test List");
        listDefinition.setDataFeedID(dataSourceID);
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        FilterValueDefinition filter = new FilterValueDefinition();
        AnalysisDimension analysisDimension = (AnalysisDimension) TestUtil.getItem(dataSourceID, "Product");
        filter.setField(analysisDimension);
        filter.setInclusive(true);
        filter.setFilteredValues(TestUtil.objectList("WidgetX"));
        filters.add(filter);
        listDefinition.setFilterDefinitions(filters);
        AnalysisDimension myDimension = (AnalysisDimension) TestUtil.getItem(dataSourceID, "Customer");
        List<AnalysisItem> analysisFields = new ArrayList<AnalysisItem>();
        analysisFields.add(myDimension);
        listDefinition.setColumns(analysisFields);
        AnalysisService analysisService = new AnalysisService();
        long analysisID = analysisService.saveAnalysisDefinition(listDefinition).getAnalysisID();
        System.out.println("analysis ID = " + analysisID);
        WSListDefinition retrievedDefinition = (WSListDefinition) analysisService.openAnalysisDefinition(analysisID);
        assertNotNull(retrievedDefinition.getFilterDefinitions());
        assertEquals(1, retrievedDefinition.getFilterDefinitions().size());
        assertEquals(1, retrievedDefinition.getAllAnalysisItems().size());
        analysisService.saveAnalysisDefinition(retrievedDefinition);
    }

    public void testCrosstabDefinitions() {
        System.out.println("connections = " + Database.instance().getActiveConnections());
        long userID = TestUtil.getIndividualTestUser();
        long dataFeedID = TestUtil.createDefaultTestDataSource(userID);
        WSCrosstabDefinition crosstabDefinition = new WSCrosstabDefinition();
        crosstabDefinition.setName("Crosstab Test");
        crosstabDefinition.setDataFeedID(dataFeedID);
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        FilterValueDefinition filter = new FilterValueDefinition();
        AnalysisDimension analysisDimension = (AnalysisDimension) TestUtil.getItem(dataFeedID, "Product");
        filter.setField(analysisDimension);
        filter.setInclusive(true);
        filter.setFilteredValues(TestUtil.objectList("WidgetX"));
        filters.add(filter);
        crosstabDefinition.setFilterDefinitions(filters);
        AnalysisItem myDimension = TestUtil.getItem(dataFeedID, "Product");
        crosstabDefinition.setRows(Arrays.asList(myDimension));
        AnalysisItem columnDimension = TestUtil.getItem(dataFeedID, "Customer");
        crosstabDefinition.setColumns(Arrays.asList(columnDimension));
        AnalysisItem measure = TestUtil.getItem(dataFeedID, "Revenue");
        crosstabDefinition.setMeasures(Arrays.asList(measure));
        AnalysisService analysisService = new AnalysisService();
        //WSAnalysisDefinition wsAnalysisDefinition = analysisService.openAnalysisDefinition(crosstabDefinition.getAnalysisID());

        long crosstabID = analysisService.saveAnalysisDefinition(crosstabDefinition).getAnalysisID();
        WSCrosstabDefinition retrievedDefinition = (WSCrosstabDefinition) analysisService.openAnalysisDefinition(crosstabID);
        assertEquals(1, retrievedDefinition.getFilterDefinitions().size());
        assertEquals(3, retrievedDefinition.getAllAnalysisItems().size());
    }

    public void testChartDefinitions() {
        System.out.println("connections = " + Database.instance().getActiveConnections());
        long userID = TestUtil.getIndividualTestUser();
        long dataFeedID = TestUtil.createDefaultTestDataSource(userID);
        WSColumnChartDefinition chartDefinition = new WSColumnChartDefinition();
        chartDefinition.setName("Chart Test");
        chartDefinition.setDataFeedID(dataFeedID);
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        FilterValueDefinition filter = new FilterValueDefinition();
        AnalysisDimension analysisDimension = new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true);
        filter.setField(analysisDimension);
        filter.setInclusive(true);
        filter.setFilteredValues(TestUtil.objectList("WidgetX"));
        filters.add(filter);
        chartDefinition.setFilterDefinitions(filters);
        AnalysisDimension myDimension = new AnalysisDimension(TestUtil.createKey("Customer", dataFeedID), true);
        AnalysisMeasure measure = new AnalysisMeasure(TestUtil.createKey("Revenue", dataFeedID), AggregationTypes.SUM);
        //chartDefinition.setMeasure(measure);
        chartDefinition.setXaxis(myDimension);
        AnalysisService analysisService = new AnalysisService();
        long analysisID = analysisService.saveAnalysisDefinition(chartDefinition).getAnalysisID();
        WSAnalysisDefinition wsAnalysisDefinition = analysisService.openAnalysisDefinition(analysisID);
        long chartID = analysisService.saveAnalysisDefinition(wsAnalysisDefinition).getAnalysisID();
        WSChartDefinition retrievedDefinition = (WSChartDefinition) analysisService.openAnalysisDefinition(chartID);
        assertEquals(1, retrievedDefinition.getFilterDefinitions().size());
        assertEquals(2, retrievedDefinition.getAllAnalysisItems().size());
    }    
}
