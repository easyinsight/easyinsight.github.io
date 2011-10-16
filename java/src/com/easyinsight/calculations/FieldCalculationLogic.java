package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.*;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: 10/6/11
 * Time: 1:00 PM
 */
public class FieldCalculationLogic extends CalculationLogic {
    private AnalysisItem analysisItem;
    private DataSet dataSet;

    public FieldCalculationLogic(AnalysisItem analysisItem, DataSet dataSet) {
        this.analysisItem = analysisItem;
        this.dataSet = dataSet;
    }

    protected void calculateResults(CalculationTreeNode calculationTreeNode, CalculationMetadata calculationMetadata) {
        for (IRow row : dataSet.getRows()) {
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(row, analysisItem, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
            Value value = new StringValue(rowVisitor.getResult().toString());
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                if (value.type() == Value.STRING || value.type() == Value.DATE) {
                    value = new NumericValue(value.toDouble());
                }
            }
            row.addValue(analysisItem.createAggregateKey(), value);
        }
    }
}
