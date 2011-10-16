package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: 10/6/11
 * Time: 1:00 PM
 */
public class FieldDecorationCalculationLogic extends CalculationLogic {
    private AnalysisItem analysisItem;
    private DataSet dataSet;

    public FieldDecorationCalculationLogic(AnalysisItem analysisItem, DataSet dataSet) {
        this.analysisItem = analysisItem;
        this.dataSet = dataSet;
    }

    protected void calculateResults(CalculationTreeNode calculationTreeNode, CalculationMetadata calculationMetadata) {
        for (IRow row : dataSet.getRows()) {
            ICalculationTreeVisitor rowVisitor = new EvaluationVisitor(row, analysisItem, calculationMetadata);
            calculationTreeNode.accept(rowVisitor);
        }
    }
}
