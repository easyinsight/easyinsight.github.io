package com.easyinsight.calculations;

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
public class ReplaceFields extends Function {
    public Value evaluate() {
        WSVerticalListDefinition verticalListDefinition = (WSVerticalListDefinition) calculationMetadata.getReport();
        String fieldToReplace = minusQuotes(params.get(0)).toString();
        AnalysisItem template = null;
        for (AnalysisItem analysisItem : verticalListDefinition.getMeasures()) {
            if (analysisItem.toDisplay().toLowerCase().equals(fieldToReplace.toLowerCase())) {
                template = analysisItem;
            }
        }
        int index = verticalListDefinition.getMeasures().indexOf(template);
        verticalListDefinition.getMeasures().remove(template);
        for (int i = 1; i < params.size(); i++) {
            String patternString = minusQuotes(params.get(i)).toString();
            Pattern pattern = Pattern.compile(MaterializedFilterPatternDefinition.createWildcardPattern(patternString.toLowerCase()));
            for (AnalysisItem field : calculationMetadata.getDataSourceFields()) {
                Matcher matcher = pattern.matcher(field.toDisplay().toLowerCase());
                if (matcher.matches()) {
                    verticalListDefinition.getMeasures().add(index, fromTemplate(template, field));
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
