package test.pipeline;

import junit.framework.TestCase;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.pipeline.*;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedRegistry;

import java.util.*;
import java.sql.SQLException;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 1:03:44 PM
 */
public class StepTest extends TestCase {

    protected void setUp() throws Exception {
        if (Database.instance() == null) {
            Database.initialize();
        }
        FeedRegistry.initialize();
    }

    public void testSteps() {

        AnalysisDateDimension startedDate = new AnalysisDateDimension("Started", true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisDateDimension endedDate = new AnalysisDateDimension("Completed", true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisDimension correlationDim = new AnalysisDimension("Correlation ID", true);
        AnalysisDimension otherDim = new AnalysisDimension("Other", true);
        AnalysisMeasure count = new AnalysisMeasure("Count", AggregationTypes.SUM);
        AnalysisStep analysisStep = new AnalysisStep();
        analysisStep.setCorrelationDimension(correlationDim);
        analysisStep.setStartDate(startedDate);
        analysisStep.setEndDate(endedDate);
        analysisStep.setGroup(true);
        analysisStep.setKey(new NamedKey("Step"));
        analysisStep.setDateLevel(AnalysisDateDimension.DAY_LEVEL);

        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setColumns(Arrays.asList(analysisStep, count));

        DataSet dataSet = new DataSet();
        IRow rowA = dataSet.createRow();
        rowA.addValue(startedDate.getKey(), getDate(Calendar.JANUARY, 5));
        rowA.addValue(endedDate.getKey(), getDate(Calendar.JANUARY, 9));
        rowA.addValue(count.getKey(), 1);
        rowA.addValue(otherDim.getKey(), "X");
        rowA.addValue(correlationDim.getKey(), "A");
        IRow rowB = dataSet.createRow();
        rowB.addValue(startedDate.getKey(), getDate(Calendar.JANUARY, 6));
        rowB.addValue(endedDate.getKey(), getDate(Calendar.JANUARY, 10));
        rowB.addValue(count.getKey(), 1);
        rowB.addValue(correlationDim.getKey(), "B");
        rowB.addValue(otherDim.getKey(), "X");

        PipelineData pipelineData = new PipelineData(listDefinition, Arrays.asList(startedDate, endedDate, correlationDim, otherDim, count, analysisStep), new InsightRequestMetadata(), null, new HashMap<String, String>(), new HashSet<AnalysisItem>());
        dataSet = new TypeTransformComponent().apply(dataSet, pipelineData);
        IComponent component = new StepCorrelationComponent(analysisStep);
        DataSet result = component.apply(dataSet, pipelineData);
        DataSet aggregateSet = new AggregationComponent().apply(result, pipelineData);
    }

    public void testFull() throws Exception {

        TestUtil.getIndividualTestUser();
        
        AnalysisDateDimension startedDate = new AnalysisDateDimension("Started", true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisDateDimension endedDate = new AnalysisDateDimension("Completed", true, AnalysisDateDimension.DAY_LEVEL);
        AnalysisDimension correlationDim = new AnalysisDimension("Correlation ID", true);
        AnalysisDimension otherDim = new AnalysisDimension("Other", true);
        AnalysisMeasure count = new AnalysisMeasure("Count", AggregationTypes.SUM);
        AnalysisStep analysisStep = new AnalysisStep();
        analysisStep.setCorrelationDimension(correlationDim);
        analysisStep.setStartDate(startedDate);
        analysisStep.setEndDate(endedDate);
        analysisStep.setGroup(true);
        analysisStep.setKey(new NamedKey("Step"));
        analysisStep.setDateLevel(AnalysisDateDimension.DAY_LEVEL);

        DataSet dataSet = new DataSet();
        IRow rowA = dataSet.createRow();
        rowA.addValue(startedDate.getKey(), getDate(Calendar.JANUARY, 5));
        rowA.addValue(endedDate.getKey(), getDate(Calendar.JANUARY, 9));
        rowA.addValue(count.getKey(), 1);
        rowA.addValue(otherDim.getKey(), "X");
        rowA.addValue(correlationDim.getKey(), "A");
        IRow rowB = dataSet.createRow();
        rowB.addValue(startedDate.getKey(), getDate(Calendar.JANUARY, 6));
        rowB.addValue(endedDate.getKey(), getDate(Calendar.JANUARY, 10));
        rowB.addValue(count.getKey(), 1);
        rowB.addValue(correlationDim.getKey(), "B");
        rowB.addValue(otherDim.getKey(), "X");

        long id = TestUtil.createTestDataSource(dataSet, Arrays.asList(startedDate, endedDate, correlationDim, otherDim, count, analysisStep));

        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setColumns(Arrays.asList(analysisStep, count));
        listDefinition.setDataFeedID(id);

        ListDataResults results = (ListDataResults) new DataService().list(listDefinition, new InsightRequestMetadata());

        System.out.println("blah");

        /*PipelineData pipelineData = new PipelineData(listDefinition, Arrays.asList(startedDate, endedDate, correlationDim, otherDim, count, analysisStep), new InsightRequestMetadata());
        dataSet = new TypeTransformComponent().apply(dataSet, pipelineData);
        IComponent component = new StepCorrelationComponent(analysisStep);
        DataSet result = component.apply(dataSet, pipelineData);
        DataSet aggregateSet = new AggregationComponent(new ArrayList<AnalysisItem>()).apply(result, pipelineData);*/
    }

    private static Date getDate(int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }
}
