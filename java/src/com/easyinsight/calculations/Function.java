package com.easyinsight.calculations;

import com.easyinsight.core.EmptyValue;
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

    public String minusQuotes(int i) {
        Value value = params.get(i);
        String string = value.toString();
        if (string.length() >= 2) {
            char first = string.charAt(0);
            char end = string.charAt(string.length() - 1);
            if (first == '"' && end == '"') {
                if (string.length() == 2) {
                    return "";
                }
                return string.substring(1, string.length() - 1);
            }
        }
        throw new FunctionException("Specify string parameters to functions by surrounding the parameter with quotation marks. For example, \"String\" to pass in String.");
    }

    public Value minusQuotes(Value value) {
        if (value.type() == Value.STRING) {
            String string = value.toString();
            if (string.length() >= 2) {
                char first = string.charAt(0);
                char end = string.charAt(string.length() - 1);
                if (first == '"' && end == '"') {
                    if (string.length() == 2) {
                        return EmptyValue.EMPTY_VALUE;
                    }
                    return new com.easyinsight.core.StringValue(string.substring(1, string.length() - 1));
                }
            }
        }
        return value;
    }

    public List<Value> getValueSet(int i) {
        return columns.get(i);
    }
}
