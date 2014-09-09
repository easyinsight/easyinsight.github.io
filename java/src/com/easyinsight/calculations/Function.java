package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:59:18 PM
 */
public abstract class Function implements IFunction {
    protected List<Value> params;
    protected List<List<Value>> columns;

    protected Map<Integer, Value> paramMap = new HashMap<Integer, Value>();

    protected CalculationMetadata calculationMetadata;

    private FunctionNode functionNode;

    public boolean equals(Value value1, Value value2) {
        try {
            if (value1.type() == value2.type()) {
                return value1.equals(value2);
            }
            if (value1.type() == Value.STRING && value2.type() == Value.EMPTY) {
                return value1.toString().equals("") || value1.toString().equals("(Empty)");
            } else if (value2.type() == Value.STRING && value1.type() == Value.EMPTY) {
                return value2.toString().equals("") || value2.toString().equals("(Empty)");
            } else if (value1.type() == Value.STRING && value2.type() == Value.NUMBER) {
                Double double1 = Double.parseDouble(value1.toString());
                Double double2 = value2.toDouble();
                return ((double2 - double1) < .00001);
            } else if (value1.type() == Value.NUMBER && value2.type() == Value.STRING) {
                Double double1 = Double.parseDouble(value2.toString());
                Double double2 = value1.toDouble();
                return ((double2 - double1) < .00001);
            }
            return value1.equals(value2);
        } catch (Exception e) {
            return false;
        }
    }

    protected AnalysisItem findReportItem(int parameter, List<AnalysisItem> fields) {
        Value value = params.get(parameter);
        String string = value.toString();
        if (string.startsWith("[")) {
            string = minusBrackets(string);
        } else if (string.startsWith("\"")) {
            string = minusQuotes(value).toString();
        }
        for (AnalysisItem field : fields) {
            if (string.equals(field.toDisplay())) {
                return field;
            }
        }
        return null;
    }

    protected AnalysisItem findDataSourceItem(int parameter) {
        String string = getParameterName(parameter);
        if (string.startsWith("[")) {
            string = minusBrackets(string);
        } else if (string.startsWith("\"")) {
            string = minusQuotes(new StringValue(string)).toString();
        }
        for (AnalysisItem field : calculationMetadata.getDataSourceFields()) {
            if (string.equals(field.toDisplay())) {
                return field;
            }
        }
        return null;
    }

    public void clearParams() {
        if (params != null) {
            params.clear();
        }
        paramMap.clear();
    }

    public void setFunctionNode(FunctionNode functionNode) {
        this.functionNode = functionNode;
    }

    public boolean onDemand() {
        return false;
    }

    private IRow row;
    private AnalysisItem analysisItem;

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public IRow getRow() {
        return row;
    }

    public void setRow(IRow row) {
        this.row = row;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    protected Value getParameter(int position) {
        Value value = paramMap.get(position);
        if (value == null) {
            EvaluationVisitor subNode = new EvaluationVisitor(row, analysisItem, calculationMetadata);
            if (functionNode.getChild(position + 1) != null) {
                ((CalculationTreeNode) functionNode.getChild(position + 1)).accept(subNode);
                paramMap.put(position, subNode.getResult());
            } else {
                paramMap.put(position, new EmptyValue());
            }
        }
        return paramMap.get(position);
    }

    protected int paramCount() {
        return functionNode.getChildCount() - 1;
    }

    protected String getParameterName(int position) {
        return (functionNode.getChild(position + 1).getChild(0)).getText();
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

    public static Value minusQuotes(Value value) {
        if (value.type() == Value.STRING) {
            String string = value.toString();
            if (string.length() >= 2) {
                char first = string.charAt(0);
                char end = string.charAt(string.length() - 1);
                if (first == '"' && end == '"') {
                    if (string.length() == 2) {
                        return new EmptyValue();
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
