package test;

import com.easyinsight.database.Database;
import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;
import com.easyinsight.dataset.DataSet;
import test.util.TestUtil;

import java.util.Calendar;
import java.util.Arrays;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: May 21, 2009
 * Time: 3:08:20 PM
 */
public class ReportLoader {
    public static void main(String[] args) throws Exception {
        Database.initialize();
        TestUtil.getIndividualTestUser();
        for (int i = 0; i < 5000; i++) {
            System.out.println(i);
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


            long id = TestUtil.createTestDataSource(new DataSet(), Arrays.asList(startedDate, endedDate, correlationDim, otherDim, count, analysisStep));

            for (int j = 0; j < 10; j++) {
                WSListDefinition listDefinition = new WSListDefinition();
                listDefinition.setColumns(Arrays.asList(analysisStep, count, startedDate, endedDate, correlationDim));
                listDefinition.setDataFeedID(id);
                listDefinition.setName("Test");
                new AnalysisService().saveAnalysisDefinition(listDefinition);
            }
        }
    }
}
