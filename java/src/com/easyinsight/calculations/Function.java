package com.easyinsight.calculations;

import com.easyinsight.core.Value;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:59:18 PM
 */
public abstract class Function implements IFunction {
    List<Value> params;
    List<List<Value>> columns;

    public void setParameters(List<Value> parameters) {
        params = parameters;
    }

    public void setColumns(List<List<Value>> columns) {
        this.columns = columns;
    }

    public List<Value> getValueSet(int i) {
        return columns.get(i);
    }
}
