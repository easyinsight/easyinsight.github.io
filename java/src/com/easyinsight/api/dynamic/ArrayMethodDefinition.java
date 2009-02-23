package com.easyinsight.api.dynamic;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;

import java.util.List;
import java.util.Arrays;

/**
 * User: James Boe
 * Date: Sep 7, 2008
 * Time: 11:25:08 PM
 */
public class ArrayMethodDefinition extends MethodFactory {

    private long feedID;
    private String methodName;
    private List<AnalysisItem> analysisItems;
    private String paramClassName;
    private String paramName;
    private String paramSingleName;
    private boolean append;

    public ArrayMethodDefinition(long feedID, String methodName, List<AnalysisItem> analysisItems, String paramClassName, String paramName, String paramSingleName, boolean append) {
        this.feedID = feedID;
        this.methodName = methodName;
        this.analysisItems = analysisItems;
        this.paramClassName = paramClassName;
        this.paramName = paramName;
        this.paramSingleName = paramSingleName;
        this.append = append;
    }

    public String toDeclaration() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("public void ");
        stringBuilder.append(methodName);
        stringBuilder.append("( ");
        stringBuilder.append("@WebParam(name=\"");
        stringBuilder.append(paramName);
        stringBuilder.append("\") ");
        stringBuilder.append(paramClassName);
        stringBuilder.append("[] ");
        stringBuilder.append(paramName);
        stringBuilder.append(");\r\n");
        return stringBuilder.toString();
    }

    public String toBody() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\tpublic void ");
        stringBuilder.append(methodName);
        stringBuilder.append("( ");
        stringBuilder.append(paramClassName);
        stringBuilder.append("[] ");
        stringBuilder.append(paramName);
        stringBuilder.append(") {\r\n");
        stringBuilder.append("\t\tList<Map<String, Value>> dataList = new ArrayList<Map<String, Value>>();\r\n");
        stringBuilder.append("\t\tfor (");
        stringBuilder.append(paramClassName);
        stringBuilder.append(" ");
        stringBuilder.append(paramSingleName);
        stringBuilder.append(" : ");
        stringBuilder.append(paramName);
        stringBuilder.append(") {\r\n");
        stringBuilder.append("\t\t\tMap<String, Value> data = new HashMap<String, Value>();\r\n");
        for (AnalysisItem analysisItem : analysisItems) {
            stringBuilder.append("\t\t\tdata.put(\"");
            stringBuilder.append(analysisItem.getKey().toKeyString());
            stringBuilder.append("\", ");
            stringBuilder.append(createGetter(analysisItem));
            stringBuilder.append(");\r\n");
        }
        stringBuilder.append("\t\t\tdataList.add(data);\r\n");
        stringBuilder.append("\t\t}\r\n");
        if (append)
            stringBuilder.append("\t\tInboundData.addInboundData(");
        else
            stringBuilder.append("\t\tInboundData.replaceInboundData(");
        stringBuilder.append(feedID);
        stringBuilder.append(", dataList);\r\n");
        stringBuilder.append("\t}\r\n");
        return stringBuilder.toString();
    }

    private String createGetter(AnalysisItem analysisItem) {
        String parameter = getParameterName(analysisItem);
        String getter = paramSingleName + ".get" + Character.toUpperCase(parameter.charAt(0)) + parameter.substring(1) + "()";
        String valueDef;
        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            valueDef = "new DateValue(" + getter + ")";
        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            valueDef = "new NumericValue(" + getter + ")";
        } else {
            valueDef = "new StringValue(" + getter + ")";
        }
        return valueDef;
    }

    public static void main(String[] args) {
        List<AnalysisItem> analysisItems = Arrays.asList(new AnalysisDimension(new NamedKey("Order Number"), true),
                new AnalysisMeasure(new NamedKey("Revenue"), 1), new AnalysisDateDimension(new NamedKey("Order Date"), true, 1));
        MethodFactory methodFactory = new ArrayMethodDefinition(1, "addOrders", analysisItems, "Order", "orders", "order", true);
        System.out.println(methodFactory.toDeclaration());
        System.out.println(methodFactory.toBody());
    }
}
