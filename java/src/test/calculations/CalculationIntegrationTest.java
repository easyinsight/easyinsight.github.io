package test.calculations;

import junit.framework.TestCase;
import test.util.TestUtil;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.database.Database;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.NumericValue;

import java.util.*;
import java.sql.SQLException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

/**
 * User: James Boe
 * Date: Jul 11, 2008
 * Time: 4:54:32 PM
 */
public class CalculationIntegrationTest extends TestCase {

    protected void setUp() throws Exception {
        Database.initialize();
        FeedRegistry.initialize();
    }

    public void testIntegration() throws SQLException {
        FeedDefinition feedDefinition = setupStuff();
        long feedID = feedDefinition.getDataFeedID();
        Resolver resolver = new Resolver(feedDefinition);
        // The data set provided at this point has three numeric variables--Revenue, Cost Number, and Units.
        // TODO: use this resolver to populate your object tree with Keys for each variable
        // TODO: our target calculation will be ((Revenue + 100) - (Cost Number)) / Units * 12
        //CalculationTreeNode tree = evalString("((Revenue + 100) - (Cost Number)) / Units * 12", resolver);
        CalculationTreeNode tree = evalString("Revenue / Cost Number", resolver);

        // Once the object is populated, you can test by doing the following...

        Feed feed = FeedRegistry.instance().getFeed(feedDefinition.getDataFeedID());
        /*DataSet dataSet = feed.getAggregateDataSet(new HashSet<AnalysisItem>(Arrays.asList(TestUtil.createKey("Cost Number", feedID), TestUtil.createKey("Revenue", feedID),
                TestUtil.createKey("Units", feedID))), null, new InsightRequestMetadata(), feedDefinition.getFields(), false, null);*/
        DataSet dataSet = new DataSet();

        for (IRow row : dataSet.getRows()) {
            // TODO: apply your visitor here
            // TODO: ((Revenue * Cost Number) / Units) * 12 = should give us 240

            ICalculationTreeVisitor visitor = new EvaluationVisitor(row);
            tree.accept(visitor);
            assertEquals(300.0, visitor.getResult().toDouble(), .1);
            //assertEquals(240.0, visitor.getResult().toDouble());


            // TODO: produce your double here and I'll hook up to the next piece
        }
    }

    public void testExclusiveFilterScenario() throws SQLException {
        TestUtil.getIndividualTestUser();
        AnalysisDimension otherDim = new AnalysisDimension("Other", true);
        AnalysisMeasure count = new AnalysisMeasure("Count", AggregationTypes.SUM);
        AnalysisMeasure value = new AnalysisMeasure("Value", AggregationTypes.SUM);
        DataSet dataSet = new DataSet();
        IRow rowA = dataSet.createRow();
        rowA.addValue(count.getKey(), 5);
        rowA.addValue(value.getKey(), 10);
        rowA.addValue(otherDim.getKey(), "X");
        IRow rowB = dataSet.createRow();
        rowB.addValue(count.getKey(), 5);
        rowB.addValue(value.getKey(), 10);
        rowB.addValue(otherDim.getKey(), "Y");
        IRow rowZ = dataSet.createRow();
        rowZ.addValue(count.getKey(), 7);
        rowZ.addValue(value.getKey(), 18);
        rowZ.addValue(otherDim.getKey(), "Z");
        long id = TestUtil.createTestDataSource(dataSet, Arrays.asList(value, otherDim, count));
        AnalysisCalculation calculation = new AnalysisCalculation();
        calculation.setKey(new NamedKey("Calculation"));
        calculation.setCalculationString("Count + Value");
        WSListDefinition listDefinition = new WSListDefinition();
        FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
        filterValueDefinition.setField(TestUtil.getItem(id, "Other"));
        filterValueDefinition.setInclusive(false);
        filterValueDefinition.setFilteredValues(Arrays.asList((Object) "Z"));
        filterValueDefinition.setApplyBeforeAggregation(true);
        listDefinition.setFilterDefinitions(Arrays.asList((FilterDefinition) filterValueDefinition));
        listDefinition.setColumns(Arrays.asList((AnalysisItem) calculation));
        listDefinition.setDataFeedID(id);
        ListDataResults results = new DataService().list(listDefinition, new InsightRequestMetadata());
        assertEquals(1, results.getRows().length);
        NumericValue doubleValue = (NumericValue) results.getRows()[0].getValues()[0];
        assertEquals(30., doubleValue.toDouble(), .1);
    }

    public void testVariableSet() throws SQLException {

        FeedDefinition feedDefinition = setupStuff();
        long feedID = feedDefinition.getDataFeedID();
        Resolver resolver = new Resolver(feedDefinition);
        // The data set provided at this point has three numeric variables--Revenue, Cost Number, and Units.
        // TODO: use this resolver to populate your object tree with Keys for each variable
        // TODO: our target calculation will be ((Revenue + 100) - (Cost Number)) / Units * 12
        CalculationTreeNode tree = evalString("((Revenue + 100) - (Cost Number)) / Units * 12 + Units * Revenue", resolver);

        VariableListVisitor visitor = new VariableListVisitor();
        tree.accept(visitor);
        //assertEquals(3, visitor.getVariableList().size());
        
    }

    private CalculationTreeNode evalString(String s, Resolver r) {
        CalculationTreeNode c = null;
        ICalculationTreeVisitor visitor = new EvaluationVisitor();
        CalculationsParser.expr_return ret;
        CalculationsLexer lexer = new CalculationsLexer(new ANTLRStringStream(s));
        CommonTokenStream tokes = new CommonTokenStream();
        tokes.setTokenSource(lexer);
        CalculationsParser parser = new CalculationsParser(tokes);
        parser.setTreeAdaptor(new NodeFactory());
        try {
            ret = parser.expr();
            c = (CalculationTreeNode) ret.getTree();
            visitor = new ResolverVisitor(r, new FunctionFactory());
            c.accept(visitor);
        } catch (RecognitionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return c;
    }

    private FeedDefinition setupStuff() throws SQLException {
        long userID = TestUtil.getIndividualTestUser();
        UserUploadService userUploadService = new UserUploadService();
        long dataFeedID = createDataFeed(userID, userUploadService);
        FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataFeedID);
        Set<String> foundKeys = new HashSet<String>();
        for (AnalysisItem field : feedDefinition.getFields()) {
            foundKeys.add(field.getKey().toKeyString());
        }
        Set<String> expectedKeys = new HashSet<String>(Arrays.asList("Customer", "Revenue", "Cost Number", "Units"));
        assertEquals("All of the expected keys were not present.", expectedKeys, foundKeys);
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setDataFeedID(dataFeedID);
        listDefinition.setColumns(Arrays.asList((new AnalysisMeasure(TestUtil.createKey("Revenue", dataFeedID), AggregationTypes.SUM)),
                new AnalysisMeasure(TestUtil.createKey("Cost Number", dataFeedID), AggregationTypes.SUM),
                        new AnalysisMeasure(TestUtil.createKey("Units", dataFeedID), AggregationTypes.SUM),
                new AnalysisDimension(TestUtil.createKey("Customer", dataFeedID), true)));
        ListDataResults results = new DataService().list(listDefinition, null);
        assertEquals("The number of results is incorrect.", 1, results.getRows().length);
        return feedDefinition;
    }

    private long createDataFeed(long accountID, UserUploadService userUploadService) {
        String csvText = "Customer,Revenue,Cost Number,Units\nAcme,400,100,20";
        long uploadID = userUploadService.addRawUploadData(accountID, "test.csv", csvText.getBytes());
        return userUploadService.create(uploadID, "Test Feed").getFeedID();
    }
}
