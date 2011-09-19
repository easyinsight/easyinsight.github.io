package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.MaterializedFilterPatternDefinition;
import com.easyinsight.analysis.ReaggregateAnalysisMeasure;
import com.easyinsight.analysis.definitions.WSVerticalListDefinition;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 1:14 PM
 */
public class ReplaceCalculation extends Function {
    public Value evaluate() {
        WSVerticalListDefinition verticalListDefinition = (WSVerticalListDefinition) calculationMetadata.getReport();
        String fieldToReplace = minusQuotes(params.get(0)).toString();
        AnalysisCalculation template = null;
        for (AnalysisItem analysisItem : verticalListDefinition.getMeasures()) {
            if (analysisItem.toDisplay().toLowerCase().equals(fieldToReplace.toLowerCase())) {
                template = (AnalysisCalculation) analysisItem;
            }
        }
        String baseString = template.getCalculationString();
        verticalListDefinition.getMeasures().remove(template);
        for (int i = 1; i < params.size(); i++) {
            String patternString = minusQuotes(params.get(i)).toString();
            Pattern pattern = Pattern.compile(MaterializedFilterPatternDefinition.createWildcardPattern(patternString.toLowerCase()));
            for (AnalysisItem field : calculationMetadata.getDataSourceFields()) {
                Matcher matcher = pattern.matcher(field.toDisplay().toLowerCase());
                if (matcher.matches()) {
                    AnalysisCalculation copyCalc = (AnalysisCalculation) fromTemplate(template, field);
                    //String hackName = copyCalc.toDisplay().replaceAll("/", "t").replaceAll("-", "d");
                    String hackName = copyCalc.toDisplay();
                    String newCalculationString = baseString.replaceAll(template.toDisplay(), hackName);
                    System.out.println(newCalculationString);
                    copyCalc.setCalculationString(newCalculationString);
                    verticalListDefinition.getMeasures().add(copyCalc);
                }
            }
        }
        return new EmptyValue();
    }

    private AnalysisItem fromTemplate(AnalysisItem template, AnalysisItem target) {
        try {
            AnalysisItem clone = template.clone();
            if (clone instanceof ReaggregateAnalysisMeasure) {
                ReaggregateAnalysisMeasure reaggregateAnalysisMeasure = (ReaggregateAnalysisMeasure) clone;
                reaggregateAnalysisMeasure.setWrappedMeasure(target);
            }
            clone.setDisplayName(target.getDisplayName());
            clone.setKey(target.getKey());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
