package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisCoordinate;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.text.NumberFormat;

/**
 * User: jamesboe
 * Date: Jul 21, 2010
 * Time: 8:06:19 PM
 */
public class CoordinatePrecisionComponent implements IComponent {

    private AnalysisCoordinate analysisCoordinate;

    public CoordinatePrecisionComponent(AnalysisCoordinate analysisCoordinate) {
        this.analysisCoordinate = analysisCoordinate;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        int precision = 3;
        if (pipelineData.getReport() instanceof WSHeatMap) {
            WSHeatMap map = (WSHeatMap) pipelineData.getReport();
            precision = map.getPrecision();
        }
        for (IRow row : dataSet.getRows()) {
            Value value = row.getValue(analysisCoordinate.createAggregateKey());
            if (value != null && value.type() != Value.EMPTY) {
                String transformed = adjust(value, precision);
                row.addValue(analysisCoordinate.createAggregateKey(), transformed);
            }
        }
        return dataSet;
    }

    private String adjust(Value value, int precision) {
        double number = value.toDouble();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(precision);
        nf.setMaximumFractionDigits(precision);
        return nf.format(number);
    }

    public void decorate(DataResults listDataResults) {

    }
}
