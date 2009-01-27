package com.easyinsight.calculations;

import com.easyinsight.core.Value;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:15:51 PM
 */
public interface IFunction {

    public Value evaluate();
    public void setParameters(List<Value> parameters);
}
