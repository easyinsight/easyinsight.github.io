package test.core;

import junit.framework.TestCase;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;
import com.easyinsight.analysis.ListDataResults;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: James Boe
 * Date: Jun 1, 2008
 * Time: 8:56:33 PM
 */
public class ComplexDataTest extends TestCase {
    public void testListData() {
        DataSet dataSet = createDataSet();
        WSListDefinition analysisDefinition = createListDefinition( new AnalysisItem[] { new AnalysisDimension(new NamedKey("Customer"), true),
                    new AnalysisList(new NamedKey("Market"), false, ","), new AnalysisList(new NamedKey("Silo"), false, ","),
                new AnalysisMeasure(new NamedKey("Revenue"), AggregationTypes.SUM)} );
        ListDataResults listDataResults = dataSet.toList(analysisDefinition, null, null);
        assertEquals(3, listDataResults.getRows().length);
        analysisDefinition = createListDefinition( new AnalysisItem[] { new AnalysisDimension(new NamedKey("Customer"), true),
                    new AnalysisList(new NamedKey("Market"), true, ","), new AnalysisList(new NamedKey("Silo"), false, ","), 
                            new AnalysisMeasure(new NamedKey("Revenue"), AggregationTypes.SUM)} );
        listDataResults = dataSet.toList(analysisDefinition, null, null);
        assertEquals(6, listDataResults.getRows().length);
        analysisDefinition = createListDefinition( new AnalysisItem[] { new AnalysisDimension(new NamedKey("Customer"), true),
                    new AnalysisList(new NamedKey("Market"), true, ","), new AnalysisList(new NamedKey("Silo"), true, ","),
                            new AnalysisMeasure(new NamedKey("Revenue"), AggregationTypes.SUM)} );
        listDataResults = dataSet.toList(analysisDefinition, null, null);
        assertEquals(12, listDataResults.getRows().length);
    }

     private WSListDefinition createListDefinition(AnalysisItem[] columns) {
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setColumns(Arrays.asList(columns));
        listDefinition.setFilterDefinitions(new ArrayList<FilterDefinition>());
        return listDefinition;
    }

    private DataSet createDataSet() {
        DataSet dataSet = new DataSet();
        IRow firstRow = dataSet.createRow();
        firstRow.addValue("Customer", "C1");
        firstRow.addValue("Market", "M1,M2");
        firstRow.addValue("Silo", "S1,S2");
        firstRow.addValue("Revenue", "500");
        IRow secondRow = dataSet.createRow();
        secondRow.addValue("Customer", "C2");
        secondRow.addValue("Market", "M2,M3,M4");
        secondRow.addValue("Silo", "S2,S3");
        secondRow.addValue("Revenue", "700");
        IRow thirdRow = dataSet.createRow();
        thirdRow.addValue("Customer", "C3");
        thirdRow.addValue("Market", "M3");
        thirdRow.addValue("Silo", "S3,S4");
        thirdRow.addValue("Revenue", "900");
        return dataSet;
    }
}
