package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.definitions.WSPlotChartDefinition;
import cern.colt.list.DoubleArrayList;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

/**
 * User: jamesboe
 * Date: Oct 9, 2009
 * Time: 11:20:42 AM
 */
public class CorrelationComponent implements IComponent {

    private Double correlation;

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (dataSet.getRows().size() > 0) {
            WSAnalysisDefinition report = pipelineData.getReport();
            WSPlotChartDefinition plotDef = (WSPlotChartDefinition) report;
            DoubleArrayList xAxisList = new DoubleArrayList();
            DoubleArrayList yAxisList = new DoubleArrayList();
            double[] xVals = new double[dataSet.getRows().size()];
            double[] yVals = new double[dataSet.getRows().size()];
            int i = 0;
            for (IRow row : dataSet.getRows()) {
                xAxisList.add(row.getValue(plotDef.getXaxisMeasure()).toDouble());
                yAxisList.add(row.getValue(plotDef.getYaxisMeasure()).toDouble());
                xVals[i] = row.getValue(plotDef.getXaxisMeasure()).toDouble();
                yVals[i] = row.getValue(plotDef.getYaxisMeasure()).toDouble();
                i++;
            }
            correlation = new PearsonsCorrelation().correlation(xVals, yVals);
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
        if (correlation != null) {
            listDataResults.getAdditionalProperties().put("correlation", correlation);
        }
    }
}
