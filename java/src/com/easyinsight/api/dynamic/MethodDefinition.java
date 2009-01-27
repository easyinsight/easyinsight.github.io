package com.easyinsight.api.dynamic;

import com.easyinsight.AnalysisItem;

import java.util.List;
import java.util.Iterator;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 2:45:48 PM
 */
public class MethodDefinition extends MethodFactory {

    private long feedID;
    private String methodName;
    private List<AnalysisItem> analysisItems;

    public MethodDefinition(long feedID, String methodName, List<AnalysisItem> analysisItems) {
        this.feedID = feedID;
        this.methodName = methodName;
        this.analysisItems = analysisItems;
    }

    public String toDeclaration() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("public void ");
        stringBuilder.append(methodName);
        stringBuilder.append("( ");
        Iterator<AnalysisItem> iter = analysisItems.iterator();
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
        Iterator<AnalysisItem> iter = analysisItems.iterator();
        while (iter.hasNext()) {
            AnalysisItem analysisItem = iter.next();
            String parameterClass = getParameterClass(analysisItem);
            stringBuilder.append(parameterClass);
            stringBuilder.append(" ");
            String parameterName = getParameterName(analysisItem);
            stringBuilder.append(parameterName);
            if (iter.hasNext()) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(") {\r\n");
        stringBuilder.append("\t\tMap<String, Value> data = new HashMap<String, Value>();\r\n");
        for (AnalysisItem analysisItem : analysisItems) {
            String valueDefString = createValueDefString(analysisItem);
            stringBuilder.append("\t\tdata.put(\"");
            stringBuilder.append(analysisItem.getKey().toKeyString());
            stringBuilder.append("\", ");
            stringBuilder.append(valueDefString);
            stringBuilder.append(");\r\n");
        }
        stringBuilder.append("\t\tInboundData.addInboundData(");
        stringBuilder.append(feedID);
        stringBuilder.append(", data);\r\n");
        stringBuilder.append("\t}\r\n");
        return stringBuilder.toString();
    }


}
