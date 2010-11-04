package com.easyinsight.calculations;

import com.easyinsight.analysis.FunctionExplanation;
import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:15:51 PM
 */
public interface IFunction {

    public Value evaluate();
    public void setParameters(List<Value> parameters);
    public int getParameterCount();
    public FunctionExplanation explain();
}
