package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:15:51 PM
 */
public interface IFunction {

    public Value evaluate();
    void setFunctionNode(FunctionNode functionNode);
    boolean onDemand();
    public void setParameters(List<Value> parameters);
    public int getParameterCount();
    void setCalculationMetadata(CalculationMetadata calculationMetadata);

    void setRow(IRow row);

    void setAnalysisItem(AnalysisItem analysisItem);
    public void clearParams();
}
