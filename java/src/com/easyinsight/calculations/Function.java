package com.easyinsight.calculations;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:59:18 PM
 */
public abstract class Function implements IFunction {
    List<Value> params;
    List<List<Value>> columns;

    Map<Integer, Value> paramMap = new HashMap<Integer, Value>();

    CalculationMetadata calculationMetadata;

    private FunctionNode functionNode;

    public void setFunctionNode(FunctionNode functionNode) {
        this.functionNode = functionNode;
    }

    public boolean onDemand() {
        return false;
    }

    protected Value getParameter(int position) {
        Value value = paramMap.get(position);
        if (value == null) {
            EvaluationVisitor subNode = new EvaluationVisitor(null, null, calculationMetadata);
            ((CalculationTreeNode) functionNode.getChild(position + 1)).accept(subNode);
            paramMap.put(position, subNode.getResult());
        }
        return paramMap.get(position);
    }

    protected String getParameterName(int position) {
        return ((CalculationTreeNode) functionNode.getChild(position + 1)).getText();
    }

    public void setCalculationMetadata(CalculationMetadata calculationMetadata) {
        this.calculationMetadata = calculationMetadata;
    }

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

    public String minusBrackets(String string) {
        char first = string.charAt(0);
        char end = string.charAt(string.length() - 1);
        if (first == '[' && end == ']') {
            if (string.length() == 2) {
                return "";
            }
            return string.substring(1, string.length() - 1);
        }
        return "";
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
