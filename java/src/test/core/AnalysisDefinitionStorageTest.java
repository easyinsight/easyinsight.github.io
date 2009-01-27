package test.core;

import junit.framework.TestCase;
import com.easyinsight.database.Database;
import com.easyinsight.analysis.*;
import com.easyinsight.*;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.UserUploadAnalysis;
import com.easyinsight.userupload.FlatFileUploadFormat;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.PersistableValue;
import com.easyinsight.core.PersistableStringValue;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Apr 12, 2008
 * Time: 5:01:29 PM
 */
public class AnalysisDefinitionStorageTest extends TestCase {

    public void setUp() {
        Database.initialize();
        new DataRetrieval();
    }

    public void testListDefinitions() {
        long userID = TestUtil.getTestUser();
        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = createFeed(userID, userUploadService);
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
        AnalysisDimension myDimension = new AnalysisDimension(TestUtil.createKey("Customer", dataFeedID), true);
        List<AnalysisItem> analysisFields = new ArrayList<AnalysisItem>();
        analysisFields.add(myDimension);
        listDefinition.setColumns(analysisFields);
        AnalysisService analysisService = new AnalysisService();
        long analysisID = analysisService.saveAnalysisDefinition(listDefinition);
        System.out.println("analysis ID = " + analysisID);
        WSListDefinition retrievedDefinition = (WSListDefinition) analysisService.openAnalysisDefinition(analysisID);
        assertNotNull(retrievedDefinition.getFilterDefinitions());
        assertEquals(1, retrievedDefinition.getFilterDefinitions().size());
        assertEquals(1, retrievedDefinition.getAllAnalysisItems().size());
        analysisService.getAllDefinitions();
        analysisService.saveAnalysisDefinition(retrievedDefinition);
    }

    private long createFeed(long userID, UserUploadService userUploadService) {
        String csvText = "Customer,Product,Revenue\nAcme,WidgetX,500\nAcme,WidgetY,200";
        long uploadID = userUploadService.addRawUploadData(userID, "test.csv", csvText.getBytes());
        UserUploadAnalysis userUploadAnalysis = userUploadService.attemptParse(uploadID, new FlatFileUploadFormat(",", "\\,"));
        assertTrue(userUploadAnalysis.isSuccessful());
        return userUploadService.parsed(uploadID, new FlatFileUploadFormat(",", "\\,"), "Test Feed", "Testing",
                Arrays.asList(new AnalysisMeasure(new NamedKey("Revenue"), AggregationTypes.SUM), new AnalysisDimension(new NamedKey("Customer"), true),
                        new AnalysisDimension(new NamedKey("Product"), true)),
                new UploadPolicy(userID), new TagCloud());
    }

    public void testCrosstabDefinitions() {
        long userID = TestUtil.getTestUser();
        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = createFeed(userID, userUploadService);
        CrosstabDefinition crosstabDefinition = new CrosstabDefinition();
        crosstabDefinition.setTitle("Crosstab Test");
        crosstabDefinition.setDataFeedID(dataFeedID);
        List<PersistableFilterDefinition> filters = new ArrayList<PersistableFilterDefinition>();
        PersistableValueFilterDefinition filter = new PersistableValueFilterDefinition();
        AnalysisDimension analysisDimension = new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true);
        filter.setField(analysisDimension);
        filter.setInclusive(true);
        filter.setFilterValues(new HashSet<PersistableValue>(Arrays.asList(new PersistableStringValue("WidgetX"))));
        filters.add(filter);
        crosstabDefinition.setFilterDefinitions(filters);
        AnalysisDimension myDimension = new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true);
        List<CrosstabRowField> rows = new ArrayList<CrosstabRowField>();
        CrosstabRowField rowField = new CrosstabRowField();
        rowField.setPosition(1);
        rowField.setAnalysisItem(myDimension);
        rows.add(rowField);
        crosstabDefinition.setRows(rows);
        AnalysisDimension columnDimension = new AnalysisDimension(TestUtil.createKey("Customer", dataFeedID), true);
        List<CrosstabColumnField> columns = new ArrayList<CrosstabColumnField>();
        CrosstabColumnField columnField = new CrosstabColumnField();
        columnField.setPosition(1);
        columnField.setAnalysisItem(columnDimension);
        columns.add(columnField);
        crosstabDefinition.setColumns(columns);
        AnalysisMeasure measure = new AnalysisMeasure(TestUtil.createKey("Revenue", dataFeedID), AggregationTypes.SUM);
        List<CrosstabMeasureField> measures = new ArrayList<CrosstabMeasureField>();
        CrosstabMeasureField measureField = new CrosstabMeasureField();
        measureField.setAnalysisItem(measure);
        measureField.setPosition(1);
        measures.add(measureField);
        crosstabDefinition.setMeasures(measures);
        AnalysisStorage analysisStorage = new AnalysisStorage();
        analysisStorage.saveAnalysis(crosstabDefinition);
        AnalysisService analysisService = new AnalysisService();
        WSAnalysisDefinition wsAnalysisDefinition = analysisService.openAnalysisDefinition(crosstabDefinition.getAnalysisID());
        long crosstabID = analysisService.saveAnalysisDefinition(wsAnalysisDefinition);
        WSCrosstabDefinition retrievedDefinition = (WSCrosstabDefinition) analysisService.openAnalysisDefinition(crosstabID);
        assertEquals(1, retrievedDefinition.getFilterDefinitions().size());
        assertEquals(3, retrievedDefinition.getAllAnalysisItems().size());
    }

    public void testChartDefinitions() {
        long userID = TestUtil.getTestUser();
        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = createFeed(userID, userUploadService);
        WSChartDefinition chartDefinition = new WSChartDefinition();
        chartDefinition.setName("Chart Test");
        chartDefinition.setDataFeedID(dataFeedID);
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        FilterValueDefinition filter = new FilterValueDefinition();
        AnalysisDimension analysisDimension = new AnalysisDimension(TestUtil.createKey("Product", dataFeedID), true);
        filter.setField(analysisDimension);
        filter.setInclusive(true);
        filter.setFilteredValues(Arrays.asList("WidgetX"));
        filters.add(filter);
        chartDefinition.setFilterDefinitions(filters);
        AnalysisDimension myDimension = new AnalysisDimension(TestUtil.createKey("Customer", dataFeedID), true);
        List<AnalysisItem> rows = new ArrayList<AnalysisItem>();
        rows.add(myDimension);
        chartDefinition.setDimensions(rows);        
        AnalysisMeasure measure = new AnalysisMeasure(TestUtil.createKey("Revenue", dataFeedID), AggregationTypes.SUM);
        List<AnalysisItem> measures = new ArrayList<AnalysisItem>();
        measures.add(measure);
        chartDefinition.setMeasures(measures);
        AnalysisService analysisService = new AnalysisService();
        long analysisID = analysisService.saveAnalysisDefinition(chartDefinition);
        WSAnalysisDefinition wsAnalysisDefinition = analysisService.openAnalysisDefinition(analysisID);
        long chartID = analysisService.saveAnalysisDefinition(wsAnalysisDefinition);
        WSChartDefinition retrievedDefinition = (WSChartDefinition) analysisService.openAnalysisDefinition(chartID);
        assertEquals(1, retrievedDefinition.getFilterDefinitions().size());
        assertEquals(2, retrievedDefinition.getAllAnalysisItems().size());
    }    
}
