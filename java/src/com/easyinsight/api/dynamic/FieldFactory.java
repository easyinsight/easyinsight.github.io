package com.easyinsight.api.dynamic;

import com.easyinsight.AnalysisItem;
import com.easyinsight.AnalysisItemTypes;
import com.easyinsight.AnalysisMeasure;
import com.easyinsight.core.NamedKey;

import java.text.MessageFormat;

/**
 * User: James Boe
 * Date: Sep 7, 2008
 * Time: 11:12:07 PM
 */
public class FieldFactory {

    private static String fieldDeclarationString = "private {0} {1};\r\n\r\n" +
                                                   "public {0} get{2}() '{'\r\n"+
                                                   "\treturn {1};\r\n"+
                                                   "'}'\r\n\r\n"+
                                                   "public void set{2}({0} {1}) '{'\r\n"+
                                                   "\tthis.{1} = {1};\r\n"+
                                                   "'}'\r\n\r\n";

    private AnalysisItem analysisItem;

    public FieldFactory(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public String toCode() {
        String fieldName = getParameterName(analysisItem);
        String fieldClass = getParameterClass(analysisItem);
        String propertyName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return MessageFormat.format(fieldDeclarationString, fieldClass, fieldName, propertyName);
    }

    public static void main(String[] args) {
        AnalysisMeasure analysisMeasure = new AnalysisMeasure(new NamedKey("Order Number"), 1);
        String code = new FieldFactory(analysisMeasure).toCode();
        System.out.println(code);
    }

    protected String getParameterClass(AnalysisItem analysisItem) {
        String paramClass;
        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            paramClass = "Date";
        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            paramClass = "double";
        } else {
            paramClass = "String";
        }
        return paramClass;
    }

    protected String getParameterName(AnalysisItem analysisItem) {
        String rawString = analysisItem.getKey().toKeyString();
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = rawString.toCharArray();
        for (char character : chars) {
            if (Character.isLetterOrDigit(character)) {
                stringBuilder.append(character);
            }
        }
        return stringBuilder.toString();
    }
}
