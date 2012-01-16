package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.MaterializedFilterPatternDefinition;
import com.easyinsight.analysis.ReaggregateAnalysisMeasure;
import com.easyinsight.analysis.definitions.WSVerticalListDefinition;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 1:14 PM
 */
public class CopyFields extends Function {
    public Value evaluate() {
        String fieldToReplace = minusQuotes(params.get(0)).toString();
        String copyString = minusQuotes(params.get(1)).toString();
        AnalysisItem template = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (analysisItem.toDisplay().toLowerCase().equals(fieldToReplace.toLowerCase())) {
                template = analysisItem;
            }
        }
        if (template == null) {
            throw new FunctionException("We couldn't find a field by the name of " + fieldToReplace + ".");
        }
        Collection<AnalysisItem> newFields = new ArrayList<AnalysisItem>();
        for (int i = 2; i < params.size(); i++) {
            String patternString = minusQuotes(params.get(i)).toString();
            Pattern pattern = Pattern.compile(MaterializedFilterPatternDefinition.createWildcardPattern(patternString.toLowerCase()));
            for (AnalysisItem field : calculationMetadata.getDataSourceFields()) {
                Matcher matcher = pattern.matcher(field.toDisplay().toLowerCase());
                if (matcher.matches()) {
                    AnalysisItem newField = fromTemplate(template, field, copyString);
                    newFields.add(newField);
                }
            }
        }
        calculationMetadata.getDataSourceFields().addAll(newFields);
        return new EmptyValue();
    }

    private AnalysisItem fromTemplate(AnalysisItem template, AnalysisItem target, String copyString) {
        try {
            AnalysisItem clone = template.clone();
            if (clone instanceof ReaggregateAnalysisMeasure) {
                ReaggregateAnalysisMeasure reaggregateAnalysisMeasure = (ReaggregateAnalysisMeasure) clone;
                reaggregateAnalysisMeasure.setWrappedMeasure(target);
            }
            String name = MessageFormat.format(copyString, target.toDisplay());
            //name = name.replaceAll("/", "t").replaceAll("-", "d");
            clone.setDisplayName(name);
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
