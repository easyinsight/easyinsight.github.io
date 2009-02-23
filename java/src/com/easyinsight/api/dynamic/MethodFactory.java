package com.easyinsight.api.dynamic;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 4:46:19 PM
 */
public abstract class MethodFactory {

    public abstract String toDeclaration();
    public abstract String toBody();

    protected String createValueDefString(AnalysisItem analysisItem) {
        String parameter = getParameterName(analysisItem);
        String valueDef;
        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            valueDef = "new DateValue(" + parameter + ")";
        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            valueDef = "new NumericValue(" + parameter + ")";
        } else {
            valueDef = "new StringValue(" + parameter + ")";
        }
        return valueDef;
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
}
