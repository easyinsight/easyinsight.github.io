package test.core;

import junit.framework.TestCase;

/**
 * User: James Boe
 * Date: Feb 8, 2008
 * Time: 7:09:01 PM
 */
public class DataSetOpsTest extends TestCase {

    public void testBlah() {
        
    }

    /*private WSListDefinition createListDefinition(AnalysisItem[] columns) {
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setColumns(Arrays.asList(columns));
        listDefinition.setFilterDefinitions(new ArrayList<FilterDefinition>());
        return listDefinition;
    }

    private WSCrosstabDefinition createCrosstabDefinition(AnalysisItem[] columns, AnalysisItem[] rows, AnalysisItem[] measures) {
        WSCrosstabDefinition crosstabDefinition = new WSCrosstabDefinition();
        crosstabDefinition.setColumns(Arrays.asList(columns));
        crosstabDefinition.setRows(Arrays.asList(rows));
        crosstabDefinition.setMeasures(Arrays.asList(measures));
        crosstabDefinition.setFilterDefinitions(new ArrayList<FilterDefinition>());
        return crosstabDefinition;
    }

    public void testCrosstabFunctionality() {
        TestDataSetCreator testDataSetCreator = new TestDataSetCreator();
        DataSet dataSet = testDataSetCreator.createDataSet();
        WSCrosstabDefinition crosstabDefinition = createCrosstabDefinition(new AnalysisItem[] { new AnalysisDimension("Customer", true) },
            new AnalysisItem[] { new AnalysisDateDimension("OrderDate", true, AnalysisItemTypes.YEAR_LEVEL) },
            new AnalysisItem[] { new AnalysisMeasure("Revenue", AggregationTypes.SUM) });
        Crosstab crosstab = dataSet.toCrosstab(crosstabDefinition);
        Collection<Map<String, Value>> data = crosstab.outputForm();
        for (Map<String, Value> cell : data) {
            assertTrue(cell.keySet().size() == 3);
        }
    }

    public void testListFunctionality() {
        TestDataSetCreator testDataSetCreator = new TestDataSetCreator();
        DataSet dataSet = testDataSetCreator.createDataSet();
        WSListDefinition analysisDefinition = createListDefinition( new AnalysisItem[] { new AnalysisDimension("Customer", true)} );
        ListDataResults listDataResults = dataSet.toList(analysisDefinition);
        assertEquals(10, listDataResults.getRows().length);
        assertEquals(1, listDataResults.getHeaders().length);
        analysisDefinition = createListDefinition( new AnalysisItem[] { new AnalysisDimension("Customer", true),
                    new AnalysisDimension("Product", true)} );
        listDataResults = dataSet.toList(analysisDefinition);
        assertEquals(20, listDataResults.getRows().length);
        assertEquals(2, listDataResults.getHeaders().length);
        noAggregation(dataSet);
        customer(dataSet, AggregationTypes.SUM, 20);
        fullRollup(dataSet, AggregationTypes.SUM, 200);
        customer(dataSet, AggregationTypes.AVERAGE, 10);
        fullRollup(dataSet, AggregationTypes.AVERAGE, 10);
        customer(dataSet, AggregationTypes.MIN, 10);
        fullRollup(dataSet, AggregationTypes.MIN, 10);
        customer(dataSet, AggregationTypes.MAX, 10);
        fullRollup(dataSet, AggregationTypes.MAX, 10);
        customer(dataSet, AggregationTypes.COUNT, 2);
        fullRollup(dataSet, AggregationTypes.COUNT, 20);
        analysisDefinition = createListDefinition( new AnalysisItem[] { new AnalysisDimension("Customer", true),
                    new AnalysisMeasure("Revenue", AggregationTypes.AVERAGE),
                    new AnalysisMeasure("Revenue", AggregationTypes.COUNT)} );
        listDataResults = dataSet.toList(analysisDefinition);
        assertEquals(10, listDataResults.getRows().length);
        assertEquals(3, listDataResults.getHeaders().length);
        for (ListRow listRow : listDataResults.getRows()) {
            NumericValue avgValue = (NumericValue) listRow.getValues()[1];
            assertEquals(10, avgValue.toDouble(), .001);
            NumericValue countValue = (NumericValue) listRow.getValues()[2];
            assertEquals(2, countValue.toDouble(), .001);
        }
        analysisDefinition = createListDefinition( new AnalysisItem[] { new AnalysisDimension("Customer", true),
                    new AnalysisMeasure("Revenue", AggregationTypes.SUM),
                    new AnalysisMeasure("Revenue", AggregationTypes.SUM)} );
        listDataResults = dataSet.toList(analysisDefinition);
        assertEquals(10, listDataResults.getRows().length);
        assertEquals(3, listDataResults.getHeaders().length);
        for (ListRow listRow : listDataResults.getRows()) {
            NumericValue avgValue = (NumericValue) listRow.getValues()[1];
            assertEquals(20, avgValue.toDouble(), .001);
            NumericValue countValue = (NumericValue) listRow.getValues()[2];
            assertEquals(20, countValue.toDouble(), .001);
        }
        analysisDefinition = createListDefinition(  new AnalysisItem[] { new AnalysisDateDimension("OrderDate", true, AnalysisItemTypes.YEAR_LEVEL),
            new AnalysisMeasure("Revenue", AggregationTypes.SUM)} );
        listDataResults = dataSet.toList(analysisDefinition);
        for (ListRow listRow : listDataResults.getRows()) {
            Double year = (Double) listRow.getValues()[0];
            assertEquals(2006, year, .001);
        }
        analysisDefinition = createListDefinition(  new AnalysisItem[] { new AnalysisDateDimension("OrderDate", true, AnalysisItemTypes.MONTH_LEVEL),
            new AnalysisMeasure("Revenue", AggregationTypes.SUM)} );
        listDataResults = dataSet.toList(analysisDefinition);
        assertEquals(10, listDataResults.getRows().length);
        analysisDefinition = createListDefinition(  new AnalysisItem[] { new AnalysisDateDimension("OrderDate", true, AnalysisItemTypes.DAY_LEVEL),
            new AnalysisMeasure("Revenue", AggregationTypes.SUM)} );
        listDataResults = dataSet.toList(analysisDefinition);
        assertEquals(20, listDataResults.getRows().length);        
    }

    private void fullRollup(DataSet dataSet, int aggregation, double expectedValue) {
        ListDataResults listDataResults;
        WSListDefinition analysisDefinition = createListDefinition( new AnalysisItem[] { new AnalysisMeasure("Revenue", aggregation)} );
        listDataResults = dataSet.toList(analysisDefinition);
        assertEquals(1, listDataResults.getRows().length);
        assertEquals(1, listDataResults.getHeaders().length);
        for (ListRow listRow : listDataResults.getRows()) {
            Double value = (Double) listRow.getValues()[0];
            assertEquals(expectedValue, value, .001);
        }
    }

    private void customer(DataSet dataSet, int aggregation, double expectedValue) {
        ListDataResults listDataResults;
        WSListDefinition analysisDefinition = createListDefinition(  new AnalysisItem[] { new AnalysisDimension("Customer", true),
                    new AnalysisMeasure("Revenue", aggregation)} );
        listDataResults = dataSet.toList(analysisDefinition);
        assertEquals(10, listDataResults.getRows().length);
        assertEquals(2, listDataResults.getHeaders().length);
        for (ListRow listRow : listDataResults.getRows()) {
            Double value = (Double) listRow.getValues()[1];
            assertEquals(expectedValue, value, .001);
        }
    }

    private void noAggregation(DataSet dataSet) {
        ListDataResults listDataResults;
        WSListDefinition analysisDefinition = createListDefinition(  new AnalysisItem[] { new AnalysisDimension("Customer", true),
                    new AnalysisDimension("Product", true),
                    new AnalysisMeasure("Revenue", AggregationTypes.SUM)} );
        listDataResults = dataSet.toList(analysisDefinition);
        assertEquals(20, listDataResults.getRows().length);
        assertEquals(3, listDataResults.getHeaders().length);
        for (ListRow listRow : listDataResults.getRows()) {
            Double value = (Double) listRow.getValues()[2];
            assertEquals(10., value, .001);
        }
    } */
}
