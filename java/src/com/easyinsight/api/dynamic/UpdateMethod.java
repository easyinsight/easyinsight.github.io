package com.easyinsight.api.dynamic;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Nov 11, 2008
 * Time: 2:44:48 PM
 */
public class UpdateMethod extends MethodFactory {
    private List<AnalysisItem> updates;
    private long feedID;
    private String methodName;
    private List<AnalysisItem> analysisItems;
    private String paramClassName;
    private String paramName;
    private String paramSingleName;

    public UpdateMethod(long feedID, String methodName, List<AnalysisItem> analysisItems, String paramClassName, String paramName, String paramSingleName, List<AnalysisItem> updates) {
        this.feedID = feedID;
        this.methodName = methodName;
        this.analysisItems = analysisItems;
        this.paramClassName = paramClassName;
        this.paramName = paramName;
        this.paramSingleName = paramSingleName;
        this.updates = updates;
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
        stringBuilder.append(",");
        Iterator<AnalysisItem> iter = updates.iterator();
        while (iter.hasNext()) {
            AnalysisItem analysisItem = iter.next();
            String parameterClass = getParameterClass(analysisItem);
            String parameterName = getParameterName(analysisItem);
            stringBuilder.append("@WebParam(name=\"");
            stringBuilder.append(parameterName);
            stringBuilder.append("\") ");
            stringBuilder.append(parameterClass);
            stringBuilder.append(" ");
            stringBuilder.append(parameterName);
            if (iter.hasNext()) {
                stringBuilder.append(", ");
            }
        }
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
        stringBuilder.append(",");
        Iterator<AnalysisItem> iter = updates.iterator();
        while (iter.hasNext()) {
            AnalysisItem analysisItem = iter.next();
            String parameterClass = getParameterClass(analysisItem);
            String parameterName = getParameterName(analysisItem);
            stringBuilder.append(parameterClass);
            stringBuilder.append(" ");
            stringBuilder.append(parameterName);
            if (iter.hasNext()) {
                stringBuilder.append(", ");
            }
        }
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
            stringBuilder.append("\t\t\tdataList.add(data);\r\n");
        }

        stringBuilder.append("\t\t}\r\n");
        stringBuilder.append("\t\tMap<String, Value> updates = new HashMap<String, Value>();\r\n");
        for (AnalysisItem analysisItem : updates) {
            String valueDefString = createValueDefString(analysisItem);
            stringBuilder.append("\t\tupdates.put(\"");
            stringBuilder.append(analysisItem.getKey().toKeyString());
            stringBuilder.append("\", ");
            stringBuilder.append(valueDefString);
            stringBuilder.append(");\r\n");
        }
        stringBuilder.append("\t\tInboundData.updateInboundData(");
        stringBuilder.append(feedID);
        stringBuilder.append(", dataList, updates);\r\n");
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
        List<AnalysisItem> updateItems = new ArrayList<AnalysisItem>();
        updateItems.add(new AnalysisDimension(new NamedKey("Region"), true));
        MethodFactory methodFactory = new UpdateMethod(1, "addOrders", analysisItems, "Order", "orders", "order", updateItems);
        System.out.println(methodFactory.toDeclaration());
        System.out.println(methodFactory.toBody());
    }
}
