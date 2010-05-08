package test.analysis;

import junit.framework.TestCase;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.Row;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.analysis.definitions.WSPlotChartDefinition;
import com.easyinsight.pipeline.CorrelationComponent;
import com.easyinsight.pipeline.PipelineData;

/**
 * User: jamesboe
 * Date: Oct 9, 2009
 * Time: 11:27:43 AM
 */
public class CorrelationTest extends TestCase {
    public void testCorrelation() {
        DataSet dataSet = new DataSet();
        IRow row1 = dataSet.createRow();
        row1.addValue("X", 1);
        row1.addValue("Y", 1);
        IRow row2 = dataSet.createRow();
        row2.addValue("X", 2);
        row2.addValue("Y", 2);
        IRow row3 = dataSet.createRow();
        row3.addValue("X", 3);
        row3.addValue("Y", 3);
        /*IRow row4 = dataSet.createRow();
        row4.addValue("X", 4);
        row4.addValue("Y", 4);
        IRow row5 = dataSet.createRow();
        row5.addValue("X", 5);
        row5.addValue("Y", 5);*/
        WSPlotChartDefinition plotChartDefinition = new WSPlotChartDefinition();
        AnalysisMeasure xMeasure = new AnalysisMeasure("X", AggregationTypes.SUM);
        AnalysisMeasure yMeasure = new AnalysisMeasure("Y", AggregationTypes.SUM);
        plotChartDefinition.setXaxisMeasure(xMeasure);
        plotChartDefinition.setYaxisMeasure(yMeasure);
        CorrelationComponent correlationComponent = new CorrelationComponent();
        PipelineData pipelineData = new PipelineData(plotChartDefinition, null, null, null, null, null);
        correlationComponent.apply(dataSet, pipelineData);
    }
}
